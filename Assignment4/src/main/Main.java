package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import constants.HttpResultCode;
import protocol.HttpRequest;
import protocol.HttpResponse;

public class Main {

	public static void main(String[] args) {
		HttpRequest request = new HttpRequest("POST /login.jsp HTTP/1.1\nAccept: text/html");
		System.out.println(request);
		
		System.out.println(request.getHeader(""));
		
//		POST /login.jsp HTTP/1.1
//		Accept: text/html
//		Name = aviv
//		Phone = 0543821321
//		$
		
//		HTTP/1.1 200
//		Set-Cookie: aviv@1232123
//		UserAuth = aviv@1232123
		
		
		HttpResponse response = new HttpResponse(HttpResultCode.RESULT_OK);
		response.addHeader("Set-Cookie", "user_auth=1231232");
		response.addValue("aviv", "asido");
		System.out.println(response);
		
		int c = 98;
		System.out.println(c);
		System.out.println((char) c);
	}

}
