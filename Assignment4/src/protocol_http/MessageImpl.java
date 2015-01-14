package protocol_http;

public class MessageImpl<T> implements Message<T> {

	private T _Value;
	private String _RawMessage;

	public MessageImpl(String rawMessage, T value) {
		_RawMessage = rawMessage;
		_Value = value;
	}

	public MessageImpl(String rawMessage) {
		_RawMessage = rawMessage;
		_Value = null;
	}

	@Override
	public String toString() {
		return _RawMessage;
	}

	@Override
	public T getValue() {
		return _Value;
	}

}
