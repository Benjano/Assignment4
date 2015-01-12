package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import constants.HttpResultCode;
import protocol.HttpRequest;
import protocol.HttpResponse;

public class Main {

	public static void main(String[] args) {
		HttpRequest request = new HttpRequest("GET sendMessage HTTP/1.1 Accept: text/html");
		System.out.println(request);
		
		HttpResponse response = new HttpResponse(HttpResultCode.RESULT_OK);
		response.addHeader("Set-Cookie", "user_auth=1231232");
		response.addValue("aviv", "asido");
		System.out.println(response);
	}

}
