package protocol_http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import constants.RequestType;

public class HttpRequest extends HttpProtocol {

	private RequestType _ReqeustType;
	private String _Location, _HttpVersion;
	protected String _BodyMessage;
	private Map<String, String> _Headers;
	private boolean _IsLocked;

	public HttpRequest(RequestType type, String location, String httpVersion) {
		_Headers = new LinkedHashMap<String, String>();
		_ReqeustType = type;
		_Location = location;
		_HttpVersion = httpVersion;
		_IsLocked = false;
		_BodyMessage = "";
	}

	/**
	 * Copy Constructor
	 * 
	 * @param request
	 */
//	private HttpRequest(HttpRequest request) {
//		 request._ReqeustType = _ReqeustType;
//		 request._Location = _Location;
//		 request._BodyMessage = _BodyMessage;
//		 request._Headers = new LinkedHashMap<String, String>();
//		 request._Headers.putAll(_Headers);
//		 request._IsLocked = _IsLocked ;
//	}
	
	public HttpRequest(HttpProtocol request) {
		request.copy(this);
	}
	
	
	@Override
	public void copy(HttpProtocol protocol) {
		protocol.copy(this);
	}

	public void copy(HttpRequest request) {
		 request._ReqeustType = _ReqeustType;
		 request._Location = _Location;
		 request._BodyMessage = _BodyMessage;
		 request._Headers = new LinkedHashMap<String, String>();
		 request._Headers.putAll(_Headers);
		 request._IsLocked = _IsLocked ;
	}
	
	
	
	

	// private void parse() {
	// StringTokenizer tokenizer = new StringTokenizer(_RawMessage);
	// if (tokenizer.countTokens() >= 3) {
	// try {
	// _ReqeustType = RequestType.valueOf(tokenizer.nextToken());
	// } catch (Exception e) {
	// _ReqeustType = RequestType.BAD_REQUEST;
	// return;
	//
	// }
	// _Location = tokenizer.nextToken();
	// _HttpVersion = tokenizer.nextToken();
	//
	// int start = 0;
	// for (int i = 0; i < _RawMessage.length(); i++) {
	// if (_RawMessage.charAt(i) == '\n') {
	// start++;
	// } else
	// break;
	// }
	// _RawMessage = _RawMessage.substring(start, _RawMessage.length());
	// String[] lines = _RawMessage
	// .substring(_RawMessage.indexOf('\n'), _RawMessage.length())
	// .trim().split("\n");
	//
	// for (String line : lines) {
	// line = line.trim();
	// if (line.length() > 0) {
	// // Check if line is a header
	// if (line.contains(":") & _MessageBody == null) {
	// line = line.trim();
	// String[] header = line.split(":");
	// if (header.length == 2) {
	// _Headers.put(header[0].trim(), header[1].trim());
	// } else {
	// _ReqeustType = RequestType.BAD_REQUEST;
	// return;
	// }
	// } else {
	// if (_ReqeustType == RequestType.POST
	// & _MessageBody == null) {
	// _MessageBody = line;
	// } else {
	// _ReqeustType = RequestType.BAD_REQUEST;
	// return;
	// }
	// }
	// }
	// }
	// } else {
	// _ReqeustType = RequestType.BAD_REQUEST;
	// }
	// return;
	// }

	public void addHeader(String key, String value) {
		if (!_IsLocked) {
			_Headers.put(key, value);
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

	public void setMessageBody(String bodyMessage) {
		if (!_IsLocked) {
			_BodyMessage = bodyMessage;
		}
	}

	public String getBodyMessage() {
		return _BodyMessage;
	}

	public void setBadType() {
		if (!_IsLocked) {
			_ReqeustType = RequestType.BAD_REQUEST;
		}
	}
	

	public void lockRequest() {
		_IsLocked = true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_ReqeustType).append(" ").append(_Location).append(" ")
				.append(_HttpVersion).append("\n\n");

		for (Entry<String, String> header : _Headers.entrySet()) {
			builder.append(header.getKey() + ": " + header.getValue() + "\n");
		}

		builder.append("\n").append(_BodyMessage);

		String result = builder.toString();
		result = result.substring(0, result.length() - 1);
		result += "\n$";
		return result;
	}
}
