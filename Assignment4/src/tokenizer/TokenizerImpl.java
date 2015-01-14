package tokenizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import protocol_http.Message;
import protocol_http.MessageString;

public class TokenizerImpl implements Tokenizer<Message<String>> {

	private InputStreamReader _Reader;
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

		try {
			while ((c = _Reader.read()) != -1) {
				if (c == _Delimeter) {
					break;
				} else
					builder.append((char) c);
			}
		} catch (IOException e) {
			System.out.println("Connection Lost");
			return null;
		}

		return new MessageString(builder.toString());

	}

	@Override
	public boolean isAlive() {
		return _isClosed;
	}

	@Override
	public void addInputStream(InputStreamReader inputStreamReader) {
		if (_Reader == null) {
			_Reader = inputStreamReader;
			_isClosed = false;
		}
	}
}
