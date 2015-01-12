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
	private Map<String, Group> _Group;

	private WhatsAppManagment() {
		_Users = new ConcurrentHashMap<String, User>();
		_Group = new ConcurrentHashMap<String, Group>();
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

	private User getUser(String phone) {
		return _Users.get(phone);
	}

	private boolean validateHeader(String header) {
		return header != null && !header.equals("");
	}

	// ************** URI FUNCTIONS **************
	public boolean handleLogin(HttpRequest request, HttpResponse response) {
		boolean result = true;
		String name = request.getHeader("UserName");
		String phone = request.getHeader("Phone");
		result = validateHeader(name) & validateHeader(phone);

		if (result) {
			User user = getUserCreateIfNotExists(name, phone);
			String auth = generateCookieCode(name, phone);
			synchronized (user) {
				_CurrentLoggedUsers.put(auth, user);
			}
			response.addValue("Set-Cookie", "user_auth" + "&" + auth);
		} else {
			response.addHeader("ERROR 765", ErrorMessage.ERROR_765);
		}
		return result;
	}

	public boolean handleLogOut(HttpRequest request, HttpResponse response) {
		// TODO
		response.addHeader("Goodbye", "logged out");

		return true;
	}

	public boolean handleList(HttpRequest request, HttpResponse response) {
		headerChecker = request.getHeader("List");
		isOk = validateHeader(headerChecker)
				&& (headerChecker.equals("Users")
						| headerChecker.equals("Group") | headerChecker
							.equals("Groups"));
		if (!isOk) {
			response.addHeader("ERROR 273", ErrorMessage.ERROR_273);
		}
		return true;
	}

	public boolean handleCreateGroup(HttpRequest request, HttpResponse response) {
		headerChecker = request.getHeader("GroupName");
		isOk = validateHeader(headerChecker);
		if (isOk) {
			if (!_managment.validateGroup(headerChecker)) {
				try {
					String decodedUsers = URLDecoder.decode(headerChecker,
							UTF_8);
					String[] usersName = decodedUsers.split(",");
					for (String userName : usersName) {
						if (!(isOk = _managment.validateUser(userName))) {
							response.addHeader("ERROR 929",
									ErrorMessage.ERROR_929 + " " + userName);
							break;
						}
					}
				} catch (UnsupportedEncodingException e) {
					isOk = false;
					response.addHeader("ERROR 675", ErrorMessage.ERROR_675);
				}
			} else {
				response.addHeader("ERROR 511", ErrorMessage.ERROR_511);
			}
		} else {
			response.addHeader("ERROR 675", ErrorMessage.ERROR_675);
		}
		return true;
	}

	public boolean handleSend(HttpRequest request, HttpResponse response) {
		headerChecker = request.getHeader("Type");
		isOk = validateHeader(headerChecker);
		if (isOk) {
			if (headerChecker.equals("Group")) {
				headerChecker = request.getHeader("Target");
				isOk = validateHeader(headerChecker);
				if (isOk) {
					isOk = _managment.validateGroup(headerChecker);
					if (!isOk) {
						response.addHeader("ERROR 771", ErrorMessage.ERROR_771);
					}
				} else {
					response.addHeader("ERROR 711", ErrorMessage.ERROR_711);
				}
			} else if (headerChecker.equals("Direct")) {
				headerChecker = request.getHeader("Target");
				isOk = validateHeader(headerChecker);
				if (isOk) {
					isOk = _managment.validatePhoneNumber(headerChecker);
					if (!isOk) {
						response.addHeader("ERROR 771", ErrorMessage.ERROR_771);
					}
				} else {
					response.addHeader("ERROR 711", ErrorMessage.ERROR_711);
				}
			} else {
				isOk = false;
				response.addHeader("ERROR 836", ErrorMessage.ERROR_836);
			}
		}
		return true;
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
		// TODO Auto-generated method stub
		return false;
	}

}
