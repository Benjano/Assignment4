package protocol_whatsapp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import protocol_http.HttpRequest;
import protocol_http.HttpProtocol;

public class WhatsAppHttpReqeust extends HttpRequest {

	private Map<String, String> _Values;

	private boolean _IsLocked;

	public WhatsAppHttpReqeust(HttpProtocol request) {
		super(request);
		_Values = new LinkedHashMap<String, String>();
		_IsLocked = false;
	}

	public WhatsAppHttpReqeust(WhatsAppHttpReqeust request) {
		super(request);
		_Values = new LinkedHashMap<String, String>();
		_Values.putAll(request._Values);
		_IsLocked = false;
	}

	public void addValue(String key, String value) {
		if (!_IsLocked) {
			_Values.put(key, value);
		}
	}

	@Override
	public void lockRequest() {
		super.lockRequest();
		_IsLocked = true;
	}

	public String getValue(String key) {
		return _Values.get(key);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getReqeustType()).append(" ").append(getLocation())
				.append(" ").append(getHttpVersion()).append("\n\n");

		String[][] headers = getAllHeaders();
		for (String[] header : headers) {
			builder.append(header[0] + ": " + header[1] + "\n");
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
