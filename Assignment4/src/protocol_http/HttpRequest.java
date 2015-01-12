package protocol_http;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import constants.RequestType;

public class HttpRequest {

	private String _RawMessage;
	private RequestType _ReqeustType;
	private String _Location;
	private String _HttpVersion;
	private Map<String, String> _Headers;

	public HttpRequest(String rawMessage) {
		_RawMessage = rawMessage;
		_Headers = new HashMap<String, String>();
		parse();
	}

	private void parse() {
		StringTokenizer tokenizer = new StringTokenizer(_RawMessage);
		if (tokenizer.countTokens() >= 3) {
			try {
				_ReqeustType = RequestType.valueOf(tokenizer.nextToken());
			} catch (Exception e) {
				_ReqeustType = RequestType.BAD_REQUEST;

			}
			_Location = tokenizer.nextToken();
			_HttpVersion = tokenizer.nextToken();

			while (tokenizer.hasMoreTokens()) {
				String headerName = tokenizer.nextToken();
				headerName = headerName.replace(':', ' ');
				headerName = headerName.trim();
				if (tokenizer.hasMoreTokens()) {
					String headerValue = tokenizer.nextToken();
					_Headers.put(headerName, headerValue);
				}
			}
		}
	}

	public String getHeader(String key) {
		return _Headers.get(key);
	}

	public RequestType getReqeustType() {
		return _ReqeustType;
	}

	public String getLocation() {
		return _Location;
	}

	public String getHttpVersion() {
		return _HttpVersion;
	}

	public Message<String> getMessage() {
		return new MessageImpl(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_ReqeustType).append(" ").append(_Location).append(" ")
				.append(_HttpVersion).append("\n");

		for (Entry<String, String> header : _Headers.entrySet()) {
			builder.append(header.getKey() + ": " + header.getValue() + "\n");
		}
		return builder.toString();
	}
}
