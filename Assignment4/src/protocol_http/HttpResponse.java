package protocol_http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import constants.HttpResultCode;

public class HttpResponse {

	private String _HttpVersion;
	private int _ResultCode;
	private Map<String, String> _Headers;
	private String _ResponseMessage;

	public HttpResponse() {
		_HttpVersion = "HTTP/1.1";
		_Headers = new LinkedHashMap<String, String>();
		_ResponseMessage = "";
		_ResultCode = HttpResultCode.RESULT_I_AM_A_TEAPOT;
	}

	public void setResultCode(int resultCode) {
		_ResultCode = resultCode;
	}

	public void addHeader(String name, String value) {
		_Headers.put(name, value);
	}

	public void setMessage(String msg) {
		_ResponseMessage = msg;
	}

	public Message<String> getMessage() {
		return new MessageString(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_HttpVersion).append(" ").append(_ResultCode)
				.append("\n");
		for (Entry<String, String> header : _Headers.entrySet()) {
			builder.append(header.getKey() + ": " + header.getValue() + "\n");
		}
		builder.append("\n").append(_ResponseMessage).append("\n$");
		return builder.toString();
	}

}
