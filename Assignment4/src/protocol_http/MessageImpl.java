package protocol_http;


public class MessageImpl implements Message<String> {

	private String _Message;

	public MessageImpl(String msg) {
		_Message = msg;
	}

	@Override
	public String getMessage() {
		return _Message;
	}

}
