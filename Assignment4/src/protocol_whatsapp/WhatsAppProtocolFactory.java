package protocol_whatsapp;

import protocol.ServerProtocolFactory;
import protocol_http.Message;

public class WhatsAppProtocolFactory implements
		ServerProtocolFactory<Message<WhatsAppProtocol>> {

	@Override
	public WhatsAppServerProtocol create() {
		return new WhatsAppServerProtocol();
	}

}
