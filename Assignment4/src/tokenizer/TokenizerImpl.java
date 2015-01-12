package tokenizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class TokenizerImpl<T> implements Tokenizer<Message<T>> {

	private Vector<InputStreamReader> _Readers;
	private char _Delimeter;
	private boolean _isClosed;

	public TokenizerImpl() {
		_Delimeter = '$';
		_isClosed = true;
	}

	@Override
	public Message<T> nextMessage() {
		int c;
		MessageImpl<T> resultMessage = new MessageImpl<T>();
		InputStreamReader reader = _Readers.get(0);
		try {
			while ((c = reader.read()) != -1) {
				if (c == _Delimeter) {
					break;
				} else
					resultMessage.addChar((char) c);
			}
			if (c == -1) {
				_Readers.remove(0);
				if (_Readers.isEmpty()) {
					_isClosed = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultMessage;
	}

	@Override
	public boolean isAlive() {
		return _isClosed;
	}

	@Override
	public void addInputStream(InputStreamReader inputStreamReader) {
		_Readers.add(inputStreamReader);
		_isClosed = false;
	}
}
