package protocol_whatsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import constants.RequestType;
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
	
	public WhatsAppHttpReqeust(WhatsAppProtocol request) {
		super(request);
		request.copy(this);
	}
	
	public void copy(WhatsAppHttpReqeust request){
		request._IsLocked = _IsLocked;
		request._Values = new LinkedHashMap<String, String>();
		request._Values.putAll(_Values);
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

}
