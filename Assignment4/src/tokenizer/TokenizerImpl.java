package tokenizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import protocol_http.Message;
import protocol_http.MessageImpl;

public class TokenizerImpl<T> implements Tokenizer<Message<String>> {

	private Vector<InputStreamReader> _Readers;
	private char _Delimeter;
	private boolean _isClosed;

	public TokenizerImpl() {
		_Delimeter = '$';
		_isClosed = true;
	}

	@Override
	public Message<String> nextMessage() throws ArrayIndexOutOfBoundsException {
		int c;
		StringBuilder builder = new StringBuilder();
		if (!_Readers.isEmpty()) {
			InputStreamReader reader = _Readers.get(0);
			try {
				while ((c = reader.read()) != -1) {
					if (c == _Delimeter) {
						break;
					} else
						builder.append((char) c);
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

			return new MessageImpl(builder.toString());
		} else {
			throw new ArrayIndexOutOfBoundsException("No new messages to read");
		}
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
