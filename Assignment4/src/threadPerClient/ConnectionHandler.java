package threadPerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import protocol.ServerProtocol;
import tokenizer.Tokenizer;

public class ConnectionHandler<T> implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	Socket clientSocket;
	ServerProtocol<T> protocol;
	Tokenizer<T> tokenizer;

	public ConnectionHandler(Socket acceptedSocket,
			ServerProtocol<T> serverProtocol, Tokenizer<T> tokenizer2) {
		in = null;
		out = null;
		clientSocket = acceptedSocket;
		protocol = serverProtocol;
		tokenizer = tokenizer2;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: "
				+ acceptedSocket.getInetAddress() + ":"
				+ acceptedSocket.getPort());
	}

	public void run() {

		// T msg;

		try {
			initialize();
		} catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} catch (IOException e) {
			System.out.println("Error in I/O");
		}

		System.out.println("Connection closed - bye bye...");
		close();

	}

	public void process() throws IOException {
		T msg;

		while ((msg = tokenizer.nextMessage()) != null) {
			System.out.println("Received \"" + msg + "\" from client");
			T response = protocol.processMessage(msg);
			if (response != null) {
				System.out.println(response);
				out.println(response);
			}

			if (protocol.isEnd(msg)) {
				break;
			}

		}
	}

	// Starts listening
	public void initialize() throws IOException {
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream(), "UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(
				clientSocket.getOutputStream(), "UTF-8"), true);
		tokenizer.addInputStream(new InputStreamReader(clientSocket
				.getInputStream(), "UTF-8"));
		System.out.println("I/O initialized");
	}

	// Closes the connection
	public void close() {
		try {
			if (tokenizer.isAlive())// Handle this in tokenizer
			{
				in.close();

			}
			if (out != null) {
				out.println("shutdown");
				out.close();
			}

			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Exception in closing I/O");
		}
	}

}