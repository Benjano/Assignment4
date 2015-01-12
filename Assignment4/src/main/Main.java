package main;


import protocol_http.HttpRequest;
import whatsapp.Group;
import whatsapp.Message;
import whatsapp.User;

public class Main {

	public static void main(String[] args) {
		HttpRequest request = new HttpRequest(
				"POST /login.jsp HTTP/1.1\nAccept: text/html");
		System.out.println(request);

		System.out.println(request.getHeader(""));

		// POST /login.jsp HTTP/1.1
		// Accept: text/html
		// Name = aviv
		// Phone = 0543821321
		// $

		// HTTP/1.1 200
		// Set-Cookie: aviv@1232123
		// UserAuth = aviv@1232123

		// HttpResponse response = new HttpResponse(HttpResultCode.RESULT_OK);
		// response.addHeader("Set-Cookie", "user_auth=1231232");
		// response.addValue("aviv", "asido");
		// System.out.println(response);
		//
		// int c = 98;
		// System.out.println(c);
		// System.out.println((char) c);

		User nir = new User("nir", "0545750068");
		User aviv = new User("aviv", "0546310736");
		User hen = new User("hen", "0547539991");
		
		Group group = new Group("Nir and Aviv!",nir);
		group.addUser(hen);
		
		Message message= new Message(aviv.getPhone(), nir.getPhone(), "bdjhbv");
		
		System.out.println(group.getGroupManagerName());
		System.out.println(group.getGroupName());
		System.out.println(group.addUser(aviv));
		System.out.println(group.addUser(aviv));
		System.out.println(group.removeUser(aviv));
		System.out.println(group.removeUser(aviv));
		System.out.println(group.toString());
		System.out.println(group.addUser(aviv));
		System.out.println(group.toString());

		
		

//		whatsapp.Message message = new whatsapp.Message(nir, aviv,"May I tell you something?");

//		whatsapp.Message message2 = new whatsapp.Message(aviv, nir, "No.");
//		System.out.println(message);
//		System.out.println(message2);
		
		
//		import protocol.ServerProtocolFactory;
//		import protocol_http.HttpProtocolFactory;
//		import protocol_whatsapp.WhatsAppProtocolFactory;
//		import tokenizer.TokenizerFactory;
//		import tokenizer_http.HttpTokenizerFactory;
//		import tokenizer_whatsaap.WhatsAppTokenizerFactory;
	}

}
