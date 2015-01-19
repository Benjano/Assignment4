package tokenizer_whatsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import protocol_http.HttpProtocol;
import protocol_http.Message;
import protocol_http.MessageImpl;
import protocol_whatsapp.WhatsAppHttpReqeust;
import tokenizer_http.HttpTokenizer;

public class WhatsAppTokenizer extends HttpTokenizer<HttpProtocol> {

	@Override
	public Message<HttpProtocol> nextMessage() {

		Message<HttpProtocol> httpMessage = super.nextMessage();

		if (httpMessage != null && httpMessage.getValue() != null) {

			WhatsAppHttpReqeust whatsAppRequest = new WhatsAppHttpReqeust(
					httpMessage.getValue());

			String[] values = whatsAppRequest.getBodyMessage().split("&");
			for (String value : values) {
				String[] valueSplit = value.split("=");
				if (valueSplit.length == 2) {
					try {
						whatsAppRequest.addValue(valueSplit[0].trim(),
								URLDecoder.decode(valueSplit[1], "UTF-8")
										.trim());
					} catch (UnsupportedEncodingException e) {
						whatsAppRequest.setBadType();
						break;
					}
				} else {
					whatsAppRequest.setBadType();
					break;
				}
			}
			return new MessageImpl<HttpProtocol>(whatsAppRequest.toString(),
					whatsAppRequest);
		}
		return httpMessage;
	}

}
