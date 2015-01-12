package protocol_http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import constants.RequestType;

public class HttpRequest {

	private String _RawMessage;
	private RequestType _ReqeustType;
	private String _Location, _HttpVersion;
	private Map<String, String> _Headers;
	private Map<String, String> _Values;

	public HttpRequest(String rawMessage) {
		_RawMessage = rawMessage;
		_Headers = new LinkedHashMap<String, String>();
		_Values = new LinkedHashMap<String, String>();
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

			String[] lines = _RawMessage.substring(_RawMessage.indexOf('\n'))
					.trim().split("\n");
			String[] values = null;

			for (String line : lines) {
				// Check if line is a header
				if (line.contains(":") & values == null) {
					line = line.trim();
					String[] header = line.split(":");
					if (header.length == 2) {
						_Headers.put(header[0].trim(), header[1].trim());
					} else {
						_ReqeustType = RequestType.BAD_REQUEST;
						return;
					}
				} else {
					if (_ReqeustType == RequestType.POST & values == null) {
						values = line.split("&");
						for (String value : values) {
							String[] valueSplit = value.split("=");
							if (valueSplit.length == 2) {
								try {
									_Values.put(
											valueSplit[0].trim(),
											URLDecoder.decode(valueSplit[1],
													"UTF-8").trim());
								} catch (UnsupportedEncodingException e) {
									_ReqeustType = RequestType.BAD_REQUEST;
									return;
								}
							} else {
								_ReqeustType = RequestType.BAD_REQUEST;
								return;
							}
						}
					} else {
						_ReqeustType = RequestType.BAD_REQUEST;
						return;
					}
				}
			}
		}
	}

	public String getHeader(String key) {
		return _Headers.get(key);
	}

	public String getValue(String key) {
		return _Values.get(key);
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

		for (Entry<String, String> value : _Values.entrySet()) {
			builder.append(value.getKey() + "=" + value.getValue() + "&");
		}
		String result = builder.toString();
		result = result.substring(0, result.length() - 1);
		result += "\n$";
		return result;
	}
}
