package main;

import constants.HttpType;
import protocol_http.HttpProtocol;
import protocol_http.HttpRequest;
import protocol_http.MessageImpl;
import protocol_whatsapp.WhatsAppHttpReqeust;
import protocol_whatsapp.WhatsAppServerProtocol;

public class Main {

	public static void main(String[] args) {

		WhatsAppServerProtocol serverProtocol = new WhatsAppServerProtocol();
		HttpRequest requestLogin = new HttpRequest(HttpType.POST, "/login.jsp",
				"Http/1.1");
		WhatsAppHttpReqeust whatsAppLogin = new WhatsAppHttpReqeust(
				requestLogin);
		whatsAppLogin.addValue("UserName", "Aviv");
		whatsAppLogin.addValue("Phone", "0543610736");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppLogin
						.toString(), whatsAppLogin)));

		WhatsAppHttpReqeust whatsAppLogin2 = new WhatsAppHttpReqeust(
				requestLogin);
		whatsAppLogin2.addValue("UserName", "Nir");
		whatsAppLogin2.addValue("Phone", "0545750068");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppLogin
						.toString(), whatsAppLogin2)));

		WhatsAppHttpReqeust whatsAppLogin3 = new WhatsAppHttpReqeust(
				requestLogin);
		whatsAppLogin3.addValue("UserName", "hen");
		whatsAppLogin3.addValue("Phone", "0547539991");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppLogin
						.toString(), whatsAppLogin3)));

		HttpRequest requestCreateGroup = new HttpRequest(HttpType.POST,
				"/create_group.jsp", "Http/1.1");
		requestCreateGroup.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppCreateGroup = new WhatsAppHttpReqeust(
				requestCreateGroup);
		whatsAppCreateGroup.addValue("GroupName", "AvivAndNir");
		whatsAppCreateGroup.addValue("Users", "0545750068");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppCreateGroup.toString(), whatsAppCreateGroup)));

		HttpRequest requestList = new HttpRequest(HttpType.POST, "/list.jsp",
				"Http/1.1");
		requestList.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppUsersList = new WhatsAppHttpReqeust(
				requestList);
		whatsAppUsersList.addValue("List", "Users");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppUsersList
						.toString(), whatsAppUsersList)));

		WhatsAppHttpReqeust whatsAppUsersGroups = new WhatsAppHttpReqeust(
				requestList);
		whatsAppUsersGroups.addValue("List", "Groups");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppUsersGroups.toString(), whatsAppUsersGroups)));

		WhatsAppHttpReqeust whatsAppUsersGroup = new WhatsAppHttpReqeust(
				requestList);
		whatsAppUsersGroup.addValue("List", "Group");
		whatsAppUsersGroup.addValue("Group", "AvivAndNir");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppUsersGroup.toString(), whatsAppUsersGroup)));

		HttpRequest requestSend = new HttpRequest(HttpType.POST, "/send.jsp",
				"Http/1.1");
		requestSend.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppSendDirect = new WhatsAppHttpReqeust(
				requestSend);
		whatsAppSendDirect.addValue("Type", "Direct");
		whatsAppSendDirect.addValue("Target", "0545750068");
		whatsAppSendDirect.addValue("Content", "May I tell you something?");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppSendDirect.toString(), whatsAppSendDirect)));

		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppSendDirect.toString(), whatsAppSendDirect)));
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppSendDirect.toString(), whatsAppSendDirect)));

		HttpRequest requestAddUser = new HttpRequest(HttpType.POST,
				"/add_user.jsp", "Http/1.1");
		requestAddUser.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppAddUser = new WhatsAppHttpReqeust(
				requestAddUser);
		whatsAppAddUser.addValue("Target", "AvivAndNir");
		whatsAppAddUser.addValue("User", "0547539991");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppAddUser
						.toString(), whatsAppAddUser)));

		WhatsAppHttpReqeust whatsAppSendGroup = new WhatsAppHttpReqeust(
				requestSend);
		whatsAppSendGroup.addValue("Type", "Group");
		whatsAppSendGroup.addValue("Target", "AvivAndNir");
		whatsAppSendGroup.addValue("Contect", "NO!");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppSendGroup
						.toString(), whatsAppSendGroup)));

		HttpRequest requestRemoveUser2 = new HttpRequest(HttpType.POST,
				"/remove_user.jsp", "Http/1.1");
		requestRemoveUser2.addHeader("Cookie", "2");

		WhatsAppHttpReqeust whatsAppRemoveUser2 = new WhatsAppHttpReqeust(
				requestRemoveUser2);
		whatsAppRemoveUser2.addValue("Target", "AvivAndNir");
		whatsAppRemoveUser2.addValue("User", "0547539991");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppRemoveUser2.toString(), whatsAppRemoveUser2)));

		HttpRequest requestRemoveUser1 = new HttpRequest(HttpType.POST,
				"/remove_user.jsp", "Http/1.1");
		requestRemoveUser1.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppRemoveUser1 = new WhatsAppHttpReqeust(
				requestRemoveUser1);
		whatsAppRemoveUser1.addValue("Target", "AvivAndNir");
		whatsAppRemoveUser1.addValue("User", "0547539991");
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppRemoveUser1.toString(), whatsAppRemoveUser1)));

		HttpRequest requestQueue = new HttpRequest(HttpType.GET, "/queue.jsp",
				"Http/1.1");
		requestQueue.addHeader("Cookie", "1");

		WhatsAppHttpReqeust whatsAppQueue1 = new WhatsAppHttpReqeust(
				requestQueue);
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppQueue1
						.toString(), whatsAppQueue1)));

		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(
						whatsAppSendDirect.toString(), whatsAppSendDirect)));

		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppQueue1
						.toString(), whatsAppQueue1)));

		HttpRequest requestQueue2 = new HttpRequest(HttpType.GET, "/queue.jsp",
				"Http/1.1");
		requestQueue2.addHeader("Cookie", "2");

		WhatsAppHttpReqeust whatsAppQueue2 = new WhatsAppHttpReqeust(
				requestQueue2);
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppQueue2
						.toString(), whatsAppQueue2)));
		System.out.println(serverProtocol
				.processMessage(new MessageImpl<HttpProtocol>(whatsAppQueue2
						.toString(), whatsAppQueue2)));

	}
}
