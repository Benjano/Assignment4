package whatsapp;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import constants.ErrorMessage;
import protocol_whatsapp.WhatsAppHttpReqeust;
import protocol_whatsapp.WhatsAppHttpResponse;

public class WhatsAppManagment {

	public static final String HEADER_COOKIE = "Cookie";

	// ********** Static Members **********
	// Is the main WhatsAppManagment created
	private static boolean _IsCreated = false;

	// The main WhatsAppManagment instance
	private static WhatsAppManagment _WhatsAppManagment;

	// The sum of active instances
	private static AtomicInteger _SumOfInstances;

	// <Cookie, user> used to monitor current logged users
	private static Map<String, User> _CurrentLoggedUsers;

	// ********** Members **********
	// The users of WhatsApp <phone, user>
	private Map<String, User> _Users;

	// The users of WhatsApp <name, group>
	private Map<String, Group> _Groups;

	private SecureRandom random = new SecureRandom();

	private WhatsAppManagment() {
		_Users = new ConcurrentHashMap<String, User>();
		_Groups = new ConcurrentHashMap<String, Group>();
		_CurrentLoggedUsers = new ConcurrentHashMap<String, User>();
		_SumOfInstances = new AtomicInteger(0);
	}

	/**
	 * Create WhatsAppManagment instance if not exists
	 * 
	 * @return WhatsAppManagment instance
	 */
	public static synchronized WhatsAppManagment create() {
		if (!_IsCreated) {
			_WhatsAppManagment = new WhatsAppManagment();
			_IsCreated = true;
		}
		_SumOfInstances.incrementAndGet();
		return _WhatsAppManagment;
	}

	/**
	 * Release the current instance of WhatsAppManagment. If no intances left
	 * then delete the WhatsAppManagment static instance
	 */
	public void release() {
		if (_SumOfInstances.decrementAndGet() == 0) {
			_IsCreated = false;
			_WhatsAppManagment = null;
		}
	}

	private String generateCookieCode(String name, String phone) {
		return new BigInteger(30, random).toString(32) + phone;
	}

	private User getUserCreateIfNotExists(String name, String phone) {
		if (_Users.containsKey(phone)) {
			return _Users.get(phone);
		}
		User user = new User(name, phone);
		_Users.put(phone, user);
		return user;
	}

	// ************** URI FUNCTIONS **************
	public boolean handleLogin(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String name = request.getValue("UserName");
		String phone = request.getValue("Phone");

		if (name != null & phone != null) {
			User user = getUserCreateIfNotExists(name, phone);
			String auth = generateCookieCode(name, phone);
			synchronized (user) {
				_CurrentLoggedUsers.put(auth, user);
			}
			response.addHeader("Set-Cookie", "user_auth" + "&" + auth);
			response.setMessage("Welcome " + user);
			return true;
		} else {
			response.setMessage("ERROR 765: " + ErrorMessage.ERROR_765);
		}
		return false;
	}

	/**
	 * Remove user from logged users and update response
	 * 
	 * @param request
	 * @param response
	 * @return true if user remove else false
	 */
	public boolean handleLogOut(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			response.setMessage("Goodbye");
			_CurrentLoggedUsers.remove(cookie);
			return true;
		}
		return false;
	}

	public boolean handleList(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		if (validateCookie(request.getHeader(HEADER_COOKIE))) {
			String value = request.getValue("List");
			if (value != null) {
				if (value.equals("Users")) {
					String users = "";
					for (Map.Entry<String, User> it : _Users.entrySet()) {
						users += it.getValue() + "\n";
					}
					// Remove the last \n
					users = users.substring(0, users.length() - 1);
					response.setMessage(users);
					return true;
				} else if (value.equals("Group")) {
					value = request.getValue("Group");
					if (value != null && _Groups.containsKey(value)) {
						response.setMessage(_Groups.get(value).toString());
						return true;
					}
				} else if (value.equals("Groups")) {
					String groups = "";
					for (Map.Entry<String, Group> it : _Groups.entrySet()) {
						groups += it.getKey() + ": " + it.getValue() + "\n";
					}
					// Remove the last \n
					groups = groups.substring(0, groups.length() - 1);
					response.setMessage(groups);
					return true;
				}
			}
			response.setMessage("ERROR 273: " + ErrorMessage.ERROR_273);
			return false;
		}
		response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
		return false;
	}

	public boolean handleCreateGroup(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			String groupName = request.getValue("GroupName");
			String usersRaw = request.getValue("Users");
			if (groupName != null && usersRaw != null) {
				if (!_Groups.containsKey(groupName)) {
					_Groups.put(groupName, new Group(groupName,
							_CurrentLoggedUsers.get(cookie)));
					Group group = _Groups.get(groupName);
					synchronized (group) {
						String[] users = usersRaw.split(",");
						if (users.length > 0) {
							for (String user : users) {
								if (!_Users.containsKey(user.trim())) {
									response.setMessage("ERROR 929: "
											+ ErrorMessage.ERROR_929 + " "
											+ user);
									return false;
								}
							}
							for (String user : users) {
								group.addUser(_Users.get(user));
							}

							response.setMessage("Group " + groupName
									+ " Created");
							return true;
						}
					}
				}
				response.setMessage("ERROR 511: " + ErrorMessage.ERROR_511);
				return false;
			}
			response.setMessage("ERROR 675: " + ErrorMessage.ERROR_675);
			return false;
		}
		response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
		return false;
	}

	public boolean handleSend(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			String source = _CurrentLoggedUsers.get(cookie).getPhone();
			String target = request.getValue("Target");
			String type = request.getValue("Type");
			String contect = request.getValue("Contect");
			if (target != null && type != null && contect != null) {
				if (type.equals("Direct")) {
					if (_Users.containsKey(target)) {
						Message message = new Message(source, target, contect);
						_Users.get(source).addMessageFromUser(message);
						_Users.get(target).addMessageFromUser(message);
						response.setMessage("Message Send");
						return true;
					}
					response.setMessage("ERROR 771: " + ErrorMessage.ERROR_771);
					return false;
				} else if (type.equals("Group")) {
					if (_Groups.containsKey(target)) {
						if (validateUserInGroup(target, source)) {
							Message message = new Message(source, target,
									contect);
							_Groups.get(target).addMessage(message);
							response.setMessage("Message Send");
							return true;
						}
					}
					response.setMessage("ERROR 771: " + ErrorMessage.ERROR_771);
					return false;
				}
				response.setMessage("ERROR 836: " + ErrorMessage.ERROR_836);
				return false;
			}
			response.setMessage("ERROR 711: " + ErrorMessage.ERROR_711);
			return false;
		}
		response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
		return false;
	}

	public boolean handleAddUser(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			String source = _CurrentLoggedUsers.get(cookie).getPhone();
			User sourceUser = _Users.get(source);
			String targetGroup = request.getValue("Target");
			String userPhone = request.getValue("User");

			if (targetGroup != null && userPhone != null) {
				User targetUser = _Users.get(userPhone);
				if (_Groups.containsKey(targetGroup)) {
					Group group = _Groups.get(targetGroup);
					if (group.isUserExistsInGroup(sourceUser)
							&& _Users.containsKey(userPhone)) {
						if (!group.isUserExistsInGroup(targetUser)) {
							group.addUser(targetUser);
							response.setMessage(userPhone + " added to "
									+ group.getGroupName());
							return true;
						}
						response.setMessage("ERROR 142: "
								+ ErrorMessage.ERROR_142);
						return false;
					}
					response.setMessage("ERROR 242: " + ErrorMessage.ERROR_242);
					return false;
				}
				response.setMessage("ERROR 770: " + ErrorMessage.ERROR_770);
				return false;
			}
			response.setMessage("ERROR 669: " + ErrorMessage.ERROR_669);
			return false;
		}
		response.setMessage("ERROR 669: " + ErrorMessage.ERROR_669);
		return false;
	}

	public boolean handleRemoveUser(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			String source = _CurrentLoggedUsers.get(cookie).getPhone();
			User sourceUser = _Users.get(source);
			String targetGroup = request.getValue("Target");
			String userPhone = request.getValue("User");

			if (targetGroup != null && userPhone != null) {
				User targetUser = _Users.get(userPhone);
				if (_Groups.containsKey(targetGroup)) {
					Group group = _Groups.get(targetGroup);
					if (group.isUserExistsInGroup(sourceUser)) {
						if (group.isUserExistsInGroup(targetUser)) {
							group.removeUser(targetUser);
							response.setMessage(userPhone + " removed from "
									+ group.getGroupName());
							return true;
						}
						response.setMessage("ERROR 769: "
								+ ErrorMessage.ERROR_769);
						return false;
					}
					response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
					return false;
				}
				response.setMessage("ERROR 769: " + ErrorMessage.ERROR_769);
				return false;
			}
			response.setMessage("ERROR 336: " + ErrorMessage.ERROR_336);
			return false;
		}
		response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
		return false;
	}

	public boolean handleQueue(WhatsAppHttpReqeust request,
			WhatsAppHttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			List<Message> messages = _CurrentLoggedUsers.get(cookie)
					.getNewMessages();
			String result = "";
			if (!messages.isEmpty()) {
				for (Message message : messages) {
					result += message + "\n";
				}
				result = result.substring(0, result.length() - 1);
				response.setMessage(result);
				return true;
			}
			response.setMessage("No new messages");
			return true;
		}
		response.setMessage("ERROR 668: " + ErrorMessage.ERROR_668);
		return false;
	}

	// ************** VALIDATE METHODS **************

	public boolean validateUserInGroup(String groupName, String userPhone) {
		if (_Groups.containsKey(groupName) && _Users.containsKey(userPhone)) {
			return _Groups.get(groupName).isUserExistsInGroup(
					_Users.get(userPhone));
		}
		return false;
	}

	public boolean validateGroupManager(String groupName, String userPhone) {
		if (_Groups.containsKey(groupName) && _Users.containsKey(userPhone)) {
			return _Groups.get(groupName).getGroupManagerPhone()
					.equals(userPhone);
		}
		return false;
	}

	public boolean validateCookie(String cookie) {
		if (cookie != null & validateHeader(cookie)) {
			return _CurrentLoggedUsers.containsKey(cookie);
		}
		return false;
	}

	private boolean validateHeader(String header) {
		return header != null && !header.equals("");
	}

}
