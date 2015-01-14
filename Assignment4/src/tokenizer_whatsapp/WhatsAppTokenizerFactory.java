package tokenizer_whatsapp;

import protocol_http.HttpProtocol;
import protocol_http.Message;
import protocol_whatsapp.WhatsAppHttpReqeust;
import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

public class WhatsAppTokenizerFactory<T> implements
		TokenizerFactory<Message<HttpProtocol>> {

	@Override
	public Tokenizer<Message<HttpProtocol>> create() {
		return new WhatsAppTokenizer<WhatsAppHttpReqeust>();
	}

}
