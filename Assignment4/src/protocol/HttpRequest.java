package protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
		_ReqeustType = RequestType.valueOf(tokenizer.nextToken());
		_Location = tokenizer.nextToken();
		_HttpVersion = tokenizer.nextToken();

		String[] lines = _RawMessage.split("\r\n");
		for (int i = 1; i < lines.length; i++) {
			String[] keyVal = lines[i].split(":", 2);
			_Headers.put(keyVal[0], keyVal[1]);
		}
	}
	
	public String getHeader(String key){
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

	@Override
	public String toString() {
		return "[" + _RawMessage + "]";
	}

}
