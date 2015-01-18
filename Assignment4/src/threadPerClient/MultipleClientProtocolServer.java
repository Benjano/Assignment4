package threadPerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import java.util.Vector;

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
	private List<ConnectionHandler<T>> _handlers;

	public MultipleClientProtocolServer(int port,
			ServerProtocolFactory<T> protocolFactory,
			TokenizerFactory<T> tokenizerFactory) {
		serverSocket = null;
		listenPort = port;
		_protocolFactory = protocolFactory;
		_tokenizerFactory = tokenizerFactory;
		_handlers = new Vector<ConnectionHandler<T>>();
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
				_handlers.add(newConnection);
				new Thread(newConnection).start();
			} catch (IOException e) {
				System.out.println("Server closed on port " + listenPort);
				break;
			}
		}
	}

	// Closes the connection
	public void close() throws IOException {
		serverSocket.close();
		System.out.println("Closing " + _handlers.size() + " connections");
		for (ConnectionHandler<T> handler : _handlers) {
			handler.close();
		}
		System.out.println("All connections were closed ");
	}

	public static void main(String[] args) throws IOException {
		// Get port
		// int port = Integer.decode(args[0]).intValue();

		int port = 5555;
		final MultipleClientProtocolServer<Message<HttpProtocol>> server = new MultipleClientProtocolServer<Message<HttpProtocol>>(
				port, new WhatsAppProtocolFactory(),
				new WhatsAppTokenizerFactory());
		Thread serverThread = new Thread(server);

		// Thread to close the server proparly
		new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				try {
					while (!br.readLine().equals("Exit")) {
						// Do nothing
					}
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		serverThread.start();
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			System.out.println("Server stopped");
		}

	}
}
