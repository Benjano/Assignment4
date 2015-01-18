package threadPerClient;

import java.io.IOException;
import java.net.ServerSocket;

import protocol.ServerProtocolFactory;
import protocol_http.HttpProtocol;
import protocol_http.Message;
import protocol_whatsapp.WhatsAppProtocolFactory;
import tokenizer.TokenizerFactory;
import tokenizer_whatsapp.WhatsAppTokenizerFactory;


public class MultipleClientProtocolServer<T> implements Runnable {
	private ServerSocket serverSocket;
	private int listenPort;
	private ServerProtocolFactory<T> _protocolFactory;
	private TokenizerFactory<T> _tokenizerFactory;

	public MultipleClientProtocolServer(int port,
			ServerProtocolFactory<T> protocolFactory,
			TokenizerFactory<T> tokenizerFactory) {
		serverSocket = null;
		listenPort = port;
		_protocolFactory = protocolFactory;
		_tokenizerFactory = tokenizerFactory;
	}

	public void run() {
		boolean isPortOk = true;
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening...");
		} catch (IOException e) {
			isPortOk = false;
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (true & isPortOk) {
			try {
				ConnectionHandler<T> newConnection = new ConnectionHandler<T>(
						serverSocket.accept(), _protocolFactory.create(),
						_tokenizerFactory.create());
				new Thread(newConnection).start();
			} catch (IOException e) {
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
	}

	// Closes the connection
	public void close() throws IOException {
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException {
		// Get port
		int port = Integer.decode(args[0]).intValue();

		// int port = 5555;
		// MultipleClientProtocolServer server = new
		// MultipleClientProtocolServer(port, new HttpProtocolFactory(), new
		// HttpTokenizerFactory());
		MultipleClientProtocolServer<Message<HttpProtocol>> server = new MultipleClientProtocolServer<Message<HttpProtocol>>(
				port, new WhatsAppProtocolFactory(),
				new WhatsAppTokenizerFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			System.out.println("Server stopped");
		}

	}
}
