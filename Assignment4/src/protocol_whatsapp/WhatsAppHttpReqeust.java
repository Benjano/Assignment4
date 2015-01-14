package protocol_whatsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import constants.RequestType;
import protocol_http.HttpRequest;

public class WhatsAppHttpReqeust extends HttpRequest {

	private Map<String, String> _Values;

	public WhatsAppHttpReqeust(String rawMessage) {
		super(rawMessage);
		_Values = new LinkedHashMap<String, String>();
		if (_ReqeustType.equals(RequestType.POST))
			parseBodyMessage();
	}

	private void parseBodyMessage() {
		String[] values = _MessageBody.split("&");
		for (String value : values) {
			String[] valueSplit = value.split("=");
			if (valueSplit.length == 2) {
				try {
					_Values.put(valueSplit[0].trim(),
							URLDecoder.decode(valueSplit[1], "UTF-8").trim());
				} catch (UnsupportedEncodingException e) {
					_ReqeustType = RequestType.BAD_REQUEST;
					return;
				}
			} else {
				_ReqeustType = RequestType.BAD_REQUEST;
				return;
			}
		}
	}

	public String getValue(String key) {
		return _Values.get(key);
	}

}
