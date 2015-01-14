package protocol_http;

public class MessageString implements Message<String> {

	private String _Message;

	public MessageString(String msg) {
		_Message = msg;
	}

	@Override
	public String toString() {
		return _Message;
	}

}
