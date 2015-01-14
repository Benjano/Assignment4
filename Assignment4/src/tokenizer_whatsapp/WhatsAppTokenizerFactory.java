package tokenizer_whatsapp;

import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

public class WhatsAppTokenizerFactory implements TokenizerFactory {

	@Override
	public Tokenizer create() {
		return new tokenizer.TokenizerImpl();
	}

}
