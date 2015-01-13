package main;


import protocol_http.HttpRequest;
import whatsapp.Group;
import whatsapp.Message;
import whatsapp.User;

public class Main {

	public static void main(String[] args) {
		

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
		
		aviv.addMessage(message);
		
		System.out.println(aviv.getMessages(nir.getPhone()));

		
		

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
