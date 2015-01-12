package protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import tokenizer.Message;
import tokenizer.MessageImpl;

public class HttpResponse {

	private String _HttpVersion;
	private int _ResultCode;
	private Map<String, String> _Headers;
	private Map<String, String> _ResponseMessage;

	public HttpResponse(int resultCode) {
		_HttpVersion = "HTTP/1.1";
		_Headers = new LinkedHashMap<String, String>();
		_ResponseMessage = new LinkedHashMap<String, String>();
		_ResultCode = resultCode;
	}

	public void addHeader(String name, String value) {
		_Headers.put(name, value);
	}

	public void addValue(String name, String value) {
		try {
			_ResponseMessage.put(name, URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Message<String> getMessage() {
		return new MessageImpl(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_HttpVersion).append(" ").append(_ResultCode)
				.append("\n");
		for (Entry<String, String> header : _Headers.entrySet()) {
			builder.append(header.getKey() + ": " + header.getValue() + "\n");
		}
		for (Entry<String, String> header : _ResponseMessage.entrySet()) {
			builder.append(header.getKey() + " = " + header.getValue() + "\n");
		}
		return builder.toString();
	}

}
