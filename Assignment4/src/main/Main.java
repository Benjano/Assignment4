package main;

import constants.RequestType;
import protocol_http.HttpRequest;
import protocol_http.MessageImpl;
import protocol_whatsapp.WhatsAppHttpReqeust;
import protocol_whatsapp.WhatsAppServerProtocol;
import whatsapp.Group;
import whatsapp.User;

public class Main {

	public static void main(String[] args) {

		WhatsAppServerProtocol serverProtocol = new WhatsAppServerProtocol();

		HttpRequest requestLogin = new HttpRequest(RequestType.POST,
				"/login.jsp", "Http/1.1");
		WhatsAppHttpReqeust whatsAppLogin = new WhatsAppHttpReqeust(
				requestLogin);
		whatsAppLogin.addValue("UserName", "Aviv");
		whatsAppLogin.addValue("Phone", "0543610736");

		System.out.println(serverProtocol
				.processMessage(new MessageImpl<WhatsAppProtocol>(whatsAppLogin
						.toString()s, whatsAppLogin)));

		// protocol_http.Message msgLogin = new MessageImpl(
		// "POST /login.jsp HTTP/1.1\n UserName=Aviv&Phone=0546310736");
		// System.out.println(serverProtocol.processMessage(msgLogin));
		//
		// protocol_http.Message msgLogin2 = new MessageImpl(
		// "POST /login.jsp HTTP/1.1\n UserName=Nir&Phone=0545750068");
		// System.out.println(serverProtocol.processMessage(msgLogin2));
		//
		// protocol_http.Message msgLogin3 = new MessageImpl(
		// "POST /login.jsp HTTP/1.1\n UserName=hen&Phone=0547539991");
		// System.out.println(serverProtocol.processMessage(msgLogin3));
		//
		// protocol_http.Message msgCreateGroup = new MessageImpl(
		// "POST /create_group.jsp HTTP/1.1\nCookie: 1\n GroupName=Aviv and Nir&Users=0545750068");
		// System.out.println(serverProtocol.processMessage(msgCreateGroup));
		//
		// protocol_http.Message msgCreateGroup2 = new MessageImpl(
		// "POST /create_group.jsp HTTP/1.1\nCookie: 1\n GroupName=Aviv and Hen&Users=0547539991");
		// System.out.println(serverProtocol.processMessage(msgCreateGroup2));
		//
		//
		// protocol_http.Message msgListUsers = new MessageImpl(
		// "POST /list.jsp HTTP/1.1\nCookie: 1\n List=Users");
		// System.out.println(serverProtocol.processMessage(msgListUsers));
		//
		//
		// protocol_http.Message msgListGroup = new MessageImpl(
		// "POST /list.jsp HTTP/1.1\nCookie: 1\n List=Group&Group=Aviv and Nir");
		// System.out.println(serverProtocol.processMessage(msgListGroup));
		//
		//
		// protocol_http.Message msgListGroups = new MessageImpl(
		// "POST /list.jsp HTTP/1.1\nCookie: 1\n List=Groups");
		// System.out.println(serverProtocol.processMessage(msgListGroups));
		//
		// protocol_http.Message msgSendDirect = new MessageImpl(
		// "POST /send.jsp HTTP/1.1\nCookie: 1\n Type=Direct&Target=0545750068&Contect=May I tell you something?");
		// System.out.println(serverProtocol.processMessage(msgSendDirect));
		//
		// protocol_http.Message msgAddUser = new MessageImpl(
		// "POST /add_user.jsp HTTP/1.1\nCookie: 1\n Target=Aviv and Nir&User=0547539991");
		// System.out.println(serverProtocol.processMessage(msgAddUser));
		//
		// protocol_http.Message msgRemoveUser = new MessageImpl(
		// "POST /remove_user.jsp HTTP/1.1\nCookie: 1\n Target=Aviv and Nir&User=0547539991");
		// System.out.println(serverProtocol.processMessage(msgRemoveUser));
		//
		// protocol_http.Message msgQueue = new MessageImpl(
		// "GET /queue.jsp HTTP/1.1\nCookie: 1");
		// System.out.println(serverProtocol.processMessage(msgQueue));
		//
		// protocol_http.Message msgSendToGroup = new MessageImpl(
		// "POST /send.jsp HTTP/1.1\nCookie: 1\n Type=Group&Target=Aviv and Nir&Contect=Nir, May I tell you something please?");
		// System.out.println(serverProtocol.processMessage(msgSendToGroup));
		//
		// System.out.println(serverProtocol.processMessage(msgQueue));
		//
		// System.out.println(serverProtocol.processMessage(msgQueue));

		// User nir = new User("nir", "0545750068");
		// User aviv = new User("aviv", "0546310736");
		// User hen = new User("hen", "0547539991");
		//
		// Group group = new Group("Nir and Aviv!", nir);
		// group.addUser(hen);
		//
		// Message message = new Message(aviv.getPhone(), nir.getPhone(),
		// "bdjhbv");
		//
		// System.out.println(group.getGroupManagerName());
		// System.out.println(group.getGroupName());
		// System.out.println(group.addUser(aviv));
		// System.out.println(group.addUser(aviv));
		// System.out.println(group.removeUser(aviv));
		// System.out.println(group.removeUser(aviv));
		// System.out.println(group.toString());
		// System.out.println(group.addUser(aviv));
		// System.out.println(group.toString());
		//
		// aviv.addMessage(message);
		//
		// System.out.println(aviv.getMessages(nir.getPhone()));
		//
		// String test = "HELLO MY NAME IS ALMO\n";
		// // String test2 = test.substring(0, test.length()-1);
		// System.out.println(test);
		// System.out.println("T");

		// whatsapp.Message message = new whatsapp.Message(nir,
		// aviv,"May I tell you something?");

		// whatsapp.Message message2 = new whatsapp.Message(aviv, nir, "No.");
		// System.out.println(message);
		// System.out.println(message2);

		// import protocol.ServerProtocolFactory;
		// import protocol_http.HttpProtocolFactory;
		// import protocol_whatsapp.WhatsAppProtocolFactory;
		// import tokenizer.TokenizerFactory;
		// import tokenizer_http.HttpTokenizerFactory;
		// import tokenizer_whatsaap.WhatsAppTokenizerFactory;
	}

}
