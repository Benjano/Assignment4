package protocol;

import protocol_http.HttpRequest;
import protocol_http.HttpResponse;
import protocol_http.Message;
import whatsapp.WhatsAppManagment;
import constants.HttpResultCode;
import constants.RequestType;

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

	public static final String SHUTDOWN = "shutdown";

	private WhatsAppManagment _managment;

	public WhatsAppServerProtocol() {
		_managment = WhatsAppManagment.create();
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

		processRequest(request, response);

		return response.getMessage();
	}

	@Override
	public boolean isEnd(Message<String> msg) {
		return msg.getMessage().toLowerCase().equals(SHUTDOWN);
	}

	// Process the request by it's URI
	private void processRequest(HttpRequest request, HttpResponse response) {
		if (request.getLocation().equals(LOGIN)) {
			_managment.handleLogin(request, response);
			return;
		} else {
			String cookie = request.getHeader("Cookie");
			if (_managment.validateCookie(cookie)) {
				if (request.getLocation().equals(LOGOUT)) {
					_managment.handleLogOut(request, response);
				} else if (request.getLocation().equals(LIST)) {
					_managment.handleList(request, response);
				} else if (request.getLocation().equals(CREATE_GROUP)) {
					_managment.handleCreateGroup(request, response);
				} else if (request.getLocation().equals(SEND)) {
					_managment.handleSend(request, response);
				} else if (request.getLocation().equals(ADD_USER)) {
					_managment.handleAddUser(request, response);
				} else if (request.getLocation().equals(REMOVE_USER)) {
					_managment.handleRemoveUser(request, response);
				} else if (request.getLocation().equals(QUEUE)) {
					_managment.handleQueue(request, response);
				} else {
					response.setResultCode(HttpResultCode.RESULT_NOT_FOUND);
				}
			} else {
				response.setResultCode(HttpResultCode.RESULT_METHOD_NOT_ALLOWED);
			}
		}
	}

	// Check if the request is written in HttpProtocol
	private boolean validateRequest(HttpRequest request) {
		if (request.getHttpVersion() != null
				&& !request.getHttpVersion().equals("HTTP/1.1")
				|| request.getReqeustType() == RequestType.BAD_REQUEST)
			return false;
		return true;
	}
}
