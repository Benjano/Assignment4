package protocol_http;

import constants.HttpType;

public interface HttpProtocol {

	/**
	 * Return the body of the message
	 */
	public String getBodyMessage();

	/**
	 * Return the request type
	 */
	public HttpType getReqeustType();

	/**
	 * Return the location
	 */
	public String getLocation();

	/**
	 * Return the version of http
	 */
	public String getHttpVersion();

	/**
	 * Return all headers [0] - key [1] - value
	 */
	public String[][] getAllHeaders();

}
