package tokenizer;

public class MessageImpl<T> implements Message<T> {

	private String _messageRaw;
	
	public MessageImpl(){
		_messageRaw = "";
	}
	
	@Override
	public void addChar(char c) {
		_messageRaw+=c;
	}

	@Override
	public String getRawString() {
		return _messageRaw;
	}

}
