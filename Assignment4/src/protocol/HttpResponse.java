package protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse {

	private String _Location;
	private String _HttpVersion;
	private int _StatusCode;
	private Map<String, String> _Headers;
	private Map<String, String> _ResponseMessage;

	public HttpResponse(int statusCode) {
		_HttpVersion = "HTTP/1.1";
		_Headers = new LinkedHashMap<String, String>();
		_ResponseMessage = new LinkedHashMap<String, String>();
		_StatusCode = statusCode;
	}
	
	public void addHeader(String name, String value) {
		try {
			_Headers.put(name, URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void addValue(String name, String value) {
		try {
			_ResponseMessage.put(name, URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	 @Override
	  public String toString() {
	    StringBuilder s = new StringBuilder();
	    s.append(_HttpVersion).append(" ").append(_StatusCode).append("\n");
	    for (Entry<String, String> header : _Headers.entrySet()) {
	      s.append(header.getKey() + " = " + header.getValue()+"\n");
	    }
	    for (Entry<String, String> header : _ResponseMessage.entrySet()) {
		      s.append(header.getKey() + " = " + header.getValue()+"\n");
		    }
	    return s.toString();
	  }

}
