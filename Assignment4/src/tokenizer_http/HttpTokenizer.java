package tokenizer_http;

import java.util.StringTokenizer;

import constants.RequestType;
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
		String rawMessage = message.toString();

		StringTokenizer tokenizer = new StringTokenizer(rawMessage);

		String location, httpVersion;
		RequestType requestType = RequestType.BAD_REQUEST;

		if (tokenizer.countTokens() >= 3) {
			try {
				requestType = RequestType.valueOf(tokenizer.nextToken());
			} catch (Exception e) {
				return null;
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
					.substring(rawMessage.indexOf('\n'), rawMessage.length())
					.trim().split("\n");

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
							return null;
						}
					} else {
						if (request.getReqeustType() == RequestType.POST
								& bodyMessage == null) {
							bodyMessage = line;
							request.setMessageBody(bodyMessage);
						} else {
							request.setBadType();
							return null;
						}
					}
				}
			}

			return new MessageImpl<HttpProtocol>(rawMessage, request);
		} else {
			return null;
		}
	}

}