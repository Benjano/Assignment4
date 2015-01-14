package protocol_http;


public abstract class HttpProtocol {

	public void copy(HttpProtocol protocol) {
		protocol.copy(this);
	}
	
	public abstract void copy(HttpRequest protocol);

}
