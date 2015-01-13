package whatsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import constants.ErrorMessage;
import protocol_http.HttpRequest;
import protocol_http.HttpResponse;

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
		// TODO Auto-generated method stub
		return null;
	}

	private User getUserCreateIfNotExists(String name, String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean validateHeader(String header) {
		return header != null && !header.equals("");
	}

	// ************** URI FUNCTIONS **************
	public boolean handleLogin(HttpRequest request, HttpResponse response) {
		String name = request.getValue("UserName");
		String phone = request.getValue("Phone");

		if (name != null & phone != null) {
			User user = getUserCreateIfNotExists(name, phone);
			String auth = generateCookieCode(name, phone);
			synchronized (user) {
				_CurrentLoggedUsers.put(auth, user);
			}
			response.addHeader("Set-Cookie", "user_auth" + "&" + auth);
			response.setMessage("Welcome " + user.getName() + "@"
					+ user.getPhone());
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
	public boolean handleLogOut(HttpRequest request, HttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			response.setMessage("Goodbye");
			_CurrentLoggedUsers.remove(cookie);
			return true;
		}
		return false;
	}

	public boolean handleList(HttpRequest request, HttpResponse response) {
		if (validateCookie(request.getHeader(HEADER_COOKIE))) {
			String value = request.getValue("List");
			if (value != null) {
				if (value.equals("Users")) {
					String users = "";
					for (Map.Entry<String, User> it : _Users.entrySet()) {
						users += it.getValue() + "\n";
					}
					// Remove the last \n
					users.substring(0, users.length() - 1);
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
					groups.substring(0, groups.length() - 1);
					response.setMessage(groups);
					return true;
				}
			}
		}
		response.setMessage("ERROR 273: " + ErrorMessage.ERROR_273);
		return false;
	}

	public boolean handleCreateGroup(HttpRequest request, HttpResponse response) {
		String cookie = request.getHeader(HEADER_COOKIE);
		if (validateCookie(cookie)) {
			String groupName = request.getValue("GroupName");
			String usersRaw = request.getValue("Users");
			if (groupName != null && usersRaw != null) {
				if (!_Groups.containsKey(groupName)) {
					Group group = _Groups.put(groupName, new Group(groupName,
							_CurrentLoggedUsers.get(cookie)));
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

	public boolean handleSend(HttpRequest request, HttpResponse response) {
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
						_Users.get(source).addMessage(message);
						_Users.get(target).addMessage(message);
						return true;
					}
					response.setMessage("ERROR 771: " + ErrorMessage.ERROR_771);
					return false;
				} else if (type.equals("Group")) {
					if (_Groups.containsKey(target)) {
						Message message = new Message(source, target, contect);
						_Groups.get(target).addMessage(message);
						return true;
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

	public boolean handleAddUser(HttpRequest request, HttpResponse response) {
		headerChecker2 = request.getHeader("Target");

		if (isOk = validateHeader(headerChecker2)
				&& (isOk = _managment.validateGroup(headerChecker2))) {
			headerChecker = request.getHeader("User");
			if (isOk = validateHeader(headerChecker)) {
				if (isOk = _managment.validatePhoneNumber(headerChecker)) {
					if (isOk = _managment.validateGroupManager(headerChecker)) {
						if (!(isOk = !_managment.validateUserInGroup(
								headerChecker2, headerChecker))) {
							// User is already in group
							response.addHeader("ERROR 142",
									ErrorMessage.ERROR_142);
						}
					} else {
						// User is not the group manager TODO
						response.addHeader("ERROR 669", ErrorMessage.ERROR_669);
					}
				} else {
					// User does not exists
					response.addHeader("ERROR 242", ErrorMessage.ERROR_242);
				}
			} else {
				// Missing parameters
				response.addHeader("ERROR 242", ErrorMessage.ERROR_242);
			}
		} else {
			// Group does not exists
			response.addHeader("ERROR 770", ErrorMessage.ERROR_770);
		}
		return true;
	}

	public boolean handleRemoveUser(HttpRequest request, HttpResponse response) {
		headerChecker2 = request.getHeader("Target");
		isOk = validateHeader(headerChecker2);
		if (isOk = validateHeader(headerChecker2)
				&& _managment.validateGroup(headerChecker2)) {
			headerChecker = request.getHeader("User");
			if (isOk = validateHeader(headerChecker)
					&& _managment.validatePhoneNumber(headerChecker)) {
				if (isOk) {
					isOk = _managment.validateUserInGroup(headerChecker2,
							headerChecker);
				}
			} else {
				// User does not exists
				response.addHeader("ERROR 336", ErrorMessage.ERROR_336);
			}
		} else {
			// Group does not exists
			response.addHeader("ERROR 769", ErrorMessage.ERROR_769);
		}
		return true;
	}

	public boolean handleQueue(HttpRequest request, HttpResponse response) {

		return true;
	}

	// ************** VALIDATE METHODS **************
	public boolean validateUser(String userName) {
		return true;
	}

	public boolean validateGroup(String headerChecker) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validatePhoneNumber(String headerChecker) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validateUserInGroup(String groupName, String userPhone) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validateGroupManager(String headerChecker) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean validateCookie(String cookie) {
		if (validateHeader(cookie)) {
			return _CurrentLoggedUsers.containsKey(cookie);
		}
		return false;
	}

}
