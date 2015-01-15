package protocol_whatsapp;

import protocol.ServerProtocolFactory;
import protocol_http.HttpProtocol;
import protocol_http.Message;

public class WhatsAppProtocolFactory implements
		ServerProtocolFactory<Message<HttpProtocol>> {

	@Override
	public WhatsAppServerProtocol create() {
		return new WhatsAppServerProtocol();
	}

}
