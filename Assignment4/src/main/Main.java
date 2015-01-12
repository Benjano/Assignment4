package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import constants.HttpResultCode;
import protocol.HttpRequest;
import protocol.HttpResponse;
import tokenizer.Message;
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
		User aviv = new User("aviv", "0526310736");

		whatsapp.Message message = new whatsapp.Message(nir, aviv,
				"May I tell you something?");

		whatsapp.Message message2 = new whatsapp.Message(aviv, nir, "No.");
		System.out.println(message);
		System.out.println(message2);
	}

}
