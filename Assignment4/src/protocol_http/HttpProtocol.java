package protocol_http;

import constants.HttpType;

public interface HttpProtocol {

	public String getBodyMessage();

	public String getHeader(String key);

	public HttpType getReqeustType();

	public String getLocation();

	public String getHttpVersion();
	
	public String[][] getAllHeaders();

}
