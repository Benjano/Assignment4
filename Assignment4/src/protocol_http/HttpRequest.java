package protocol_http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import constants.RequestType;

public class HttpRequest {

	private String _RawMessage;
	protected RequestType _ReqeustType;
	private String _Location, _HttpVersion;
	protected String _MessageBody;
	private Map<String, String> _Headers;

	public HttpRequest(String rawMessage) {
		_RawMessage = rawMessage;
		_Headers = new LinkedHashMap<String, String>();
		parse();
	}

	private void parse() {
		StringTokenizer tokenizer = new StringTokenizer(_RawMessage);
		if (tokenizer.countTokens() >= 3) {
			try {
				_ReqeustType = RequestType.valueOf(tokenizer.nextToken());
			} catch (Exception e) {
				_ReqeustType = RequestType.BAD_REQUEST;
				return;

			}
			_Location = tokenizer.nextToken();
			_HttpVersion = tokenizer.nextToken();

			int start = 0;
			for (int i = 0; i < _RawMessage.length(); i++) {
				if (_RawMessage.charAt(i) == '\n') {
					start++;
				} else
					break;
			}
			_RawMessage = _RawMessage.substring(start, _RawMessage.length());
			String[] lines = _RawMessage
					.substring(_RawMessage.indexOf('\n'), _RawMessage.length())
					.trim().split("\n");

			for (String line : lines) {
				line = line.trim();
				if (line.length() > 0) {
					// Check if line is a header
					if (line.contains(":") & _MessageBody == null) {
						line = line.trim();
						String[] header = line.split(":");
						if (header.length == 2) {
							_Headers.put(header[0].trim(), header[1].trim());
						} else {
							_ReqeustType = RequestType.BAD_REQUEST;
							return;
						}
					} else {
						if (_ReqeustType == RequestType.POST
								& _MessageBody == null) {
							_MessageBody = line;
						} else {
							_ReqeustType = RequestType.BAD_REQUEST;
							return;
						}
					}
				}
			}
		} else {
			_ReqeustType = RequestType.BAD_REQUEST;
		}
		return;
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

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_ReqeustType).append(" ").append(_Location).append(" ")
				.append(_HttpVersion).append("\n\n");

		for (Entry<String, String> header : _Headers.entrySet()) {
			builder.append(header.getKey() + ": " + header.getValue() + "\n");
		}

		builder.append("\n").append(_MessageBody);

		String result = builder.toString();
		result = result.substring(0, result.length() - 1);
		result += "\n$";
		return result;
	}
}
