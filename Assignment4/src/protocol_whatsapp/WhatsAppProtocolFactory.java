package protocol_whatsapp;

import protocol.ServerProtocolFactory;
import protocol.WhatsAppServerProtocol;
import protocol_http.Message;

public class WhatsAppProtocolFactory implements
		ServerProtocolFactory<Message<String>> {

	@Override
	public WhatsAppServerProtocol create() {
		return new WhatsAppServerProtocol();
	}

}
