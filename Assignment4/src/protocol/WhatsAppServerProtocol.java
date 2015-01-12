package protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import tokenizer.Message;
import tokenizer.MessageImpl;
import whatsapp.WhatsAppManagment;
import constants.ErrorMessage;
import constants.HttpResultCode;
import constants.RequestType;
import constants.WhatsAppRequestType;

public class WhatsAppServerProtocol implements ServerProtocol<Message<String>> {

	public static final String UTF_8 = "UTF-8";
	public static final String LOGIN = "/login.jsp";
	public static final String LOGOUT = "/logout.jsp";
	public static final String LIST = "/list.jsp";
	public static final String CREATE_GROUP = "/create_group.jsp";
	public static final String SEND = "/send.jsp";
	public static final String ADD_USER = "/add_user.jsp";
	public static final String REMOVE_USER = "/remove_user.jsp";
	public static final String QUEUE = "/queue.jsp";

	private WhatsAppManagment _managment;

	public WhatsAppServerProtocol(WhatsAppManagment managment) {
		_managment = managment;
	}

	@Override
	public Message<String> processMessage(Message<String> msg) {
		HttpRequest request = new HttpRequest(msg.getMessage());
		HttpResponse response = new HttpResponse();
		response.setResultCode(HttpResultCode.RESULT_OK);

		if (!validateRequest(request)) {
			response.setResultCode(HttpResultCode.RESULT_BAD_REQUEST);
			return response.getMessage();
		}

		WhatsAppRequestType requestType = checkRequestType(request);
		if (requestType == WhatsAppRequestType.BAD_REQUEST) {
			response.setResultCode(HttpResultCode.RESULT_NOT_FOUND);
			return response.getMessage();
		}

		if (!validateRequestType(requestType, request, response)) {
			return response.getMessage();
		}

		proccessRequest(requestType, request, response);

		return response.getMessage();
	}

	@Override
	public boolean isEnd(Message<String> msg) {
		// TODO Auto-generated method stub
		return false;
	}

	private WhatsAppRequestType checkRequestType(HttpRequest request) {
		if (request.getReqeustType().equals(LOGIN)) {
			return WhatsAppRequestType.LOGIN;
		}
		if (request.getReqeustType().equals(LOGOUT)) {
			return WhatsAppRequestType.LOGOUT;
		}
		if (request.getReqeustType().equals(LIST)) {
			return WhatsAppRequestType.LIST;
		}
		if (request.getReqeustType().equals(CREATE_GROUP)) {
			return WhatsAppRequestType.CREATE_GROUP;
		}
		if (request.getReqeustType().equals(SEND)) {
			return WhatsAppRequestType.SEND;
		}
		if (request.getReqeustType().equals(ADD_USER)) {
			return WhatsAppRequestType.ADD_USER;
		}
		if (request.getReqeustType().equals(REMOVE_USER)) {
			return WhatsAppRequestType.REMOVE_USER;
		}
		if (request.getReqeustType().equals(QUEUE)) {
			return WhatsAppRequestType.QUEUE;
		}

		return WhatsAppRequestType.BAD_REQUEST;
	}

	private boolean validateRequest(HttpRequest request) {
		if (!request.getHttpVersion().equals("HTTP/1.1")
				|| request.getReqeustType() == RequestType.BAD_REQUEST)
			return false;
		return true;
	}

	private boolean validateRequestType(WhatsAppRequestType requestType,
			HttpRequest request, HttpResponse response) {

		String headerChecker;
		String headerChecker2;
		boolean isOk = true;

		switch (requestType) {
		case LOGIN:
			isOk = validateHeader(request.getHeader("UserName"))
					& validateHeader(request.getHeader("Phone"));
			if (!isOk) {
				response.addHeader("ERROR 765", ErrorMessage.ERROR_765);
			}
			break;

		case LOGOUT:
				break;

		case LIST:
		
			break;

		case CREATE_GROUP:
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
			break;

		case SEND:
			
			break;

		case ADD_USER:
			

			break;

		case REMOVE_USER: // TODO
			
			break;

		case QUEUE:
			break;

		default:
			response.setResultCode(HttpResultCode.RESULT_NOT_FOUND);
			isOk = false;
			break;
		}

		return isOk;
	}

	private boolean proccessRequest(WhatsAppRequestType requestType,
			HttpRequest request, HttpResponse response) {

		String headerChecker;
		boolean isOk = true;

		switch (requestType) {
		case LOGIN:
			isOk = validateHeader(request.getHeader("UserName"))
					& validateHeader(request.getHeader("Phone"));
			if (!isOk) {
				response.addHeader("ERROR 765", ErrorMessage.ERROR_765);
			}

			break;

		case LOGOUT:
			break;

		case LIST:
			headerChecker = request.getHeader("List");
			isOk = validateHeader(headerChecker)
					&& (headerChecker.equals("Users")
							| headerChecker.equals("Group") | headerChecker
								.equals("Groups"));
			if (!isOk) {
				response.addHeader("ERROR 273", ErrorMessage.ERROR_273);
			}
			break;

		case CREATE_GROUP:
			headerChecker = request.getHeader("GroupName");
			isOk = validateHeader(headerChecker);

			if (isOk) {
				if (!_managment.validateUserInGroup(headerChecker)) {
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

			break;

		case SEND:
			headerChecker = request.getHeader("Type");
			isOk = validateHeader(headerChecker);
			if (isOk) {
				if (headerChecker.equals("Group")) {
					headerChecker = request.getHeader("Target");
					isOk = validateHeader(headerChecker);
					if (isOk) {
						isOk = _managment.validateGroup(headerChecker);
					}
				} else if (headerChecker.equals("Direct")) {
					headerChecker = request.getHeader("Target");
					isOk = validateHeader(headerChecker);
					if (isOk) {
						isOk = _managment.validatePhoneNumber(headerChecker);
					}
				} else
					isOk = false;
			}
			break;

		case ADD_USER:
			headerChecker = request.getHeader("Target");
			isOk = validateHeader(headerChecker);
			if (isOk) {
				isOk = _managment.validateGroup(headerChecker);
				headerChecker = request.getHeader("User");
				isOk = validateHeader(headerChecker);
				if (isOk) {
					isOk = _managment.validatePhoneNumber(headerChecker);
				}
			}
			break;

		case REMOVE_USER:
			headerChecker = request.getHeader("Target");
			isOk = validateHeader(headerChecker);
			if (isOk) {
				isOk = _managment.validateGroup(headerChecker);
				headerChecker = request.getHeader("User");
				isOk = validateHeader(headerChecker);
				if (isOk) {
					isOk = _managment.validatePhoneNumber(headerChecker);
					if (isOk) {
						isOk = _managment.validateUserInGroup(headerChecker);
					}
				}
			}
			break;

		case QUEUE:
			break;

		default:
			response.setResultCode(HttpResultCode.RESULT_NOT_FOUND);
			isOk = false;
			break;
		}

		return isOk;

	}

	private boolean validateHeader(String header) {
		return header != null && !header.equals("");
	}

}
