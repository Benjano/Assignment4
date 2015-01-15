package tokenizer_http;

import java.util.StringTokenizer;

import constants.HttpType;
import protocol_http.HttpRequest;
import protocol_http.HttpProtocol;
import protocol_http.Message;
import protocol_http.MessageImpl;
import tokenizer.TokenizerImpl;

public class HttpTokenizer<T> extends TokenizerImpl<HttpProtocol> {

	@Override
	public Message<HttpProtocol> nextMessage()
			throws ArrayIndexOutOfBoundsException {

		Message<HttpProtocol> message = super.nextMessage();
		if (message != null) {
			String rawMessage = message.toString();

			StringTokenizer tokenizer = new StringTokenizer(rawMessage);

			String location, httpVersion;
			HttpType requestType = HttpType.BAD_REQUEST;

			if (tokenizer.countTokens() >= 3) {
				try {
					requestType = HttpType.valueOf(tokenizer.nextToken());
				} catch (Exception e) {
					requestType = HttpType.BAD_REQUEST;
				}
				location = tokenizer.nextToken();
				httpVersion = tokenizer.nextToken();

				HttpRequest request = new HttpRequest(requestType, location,
						httpVersion);
				int start = 0;
				for (int i = 0; i < rawMessage.length(); i++) {
					if (rawMessage.charAt(i) == '\n') {
						start++;
					} else
						break;
				}
				rawMessage = rawMessage.substring(start, rawMessage.length());
				String[] lines = rawMessage
						.substring(rawMessage.indexOf('\n'),
								rawMessage.length()).trim().split("\n");

				String bodyMessage = null;

				for (String line : lines) {
					line = line.trim();
					if (line.length() > 0) {
						// Check if line is a header
						if (line.contains(":")
								& request.getBodyMessage().equals("")) {
							line = line.trim();
							String[] header = line.split(":");
							if (header.length == 2) {
								request.addHeader(header[0].trim(),
										header[1].trim());
							} else {
								request.setBadType();
								break;
							}
						} else {
							if (request.getReqeustType() == HttpType.POST
									& bodyMessage == null) {
								bodyMessage = line;
								request.setMessageBody(bodyMessage);
							} else {
								request.setBadType();
								break;
							}
						}
					}
				}

				return new MessageImpl<HttpProtocol>(rawMessage, request);
			} else {
				return null;
			}
		}
		return message;
	}
}
