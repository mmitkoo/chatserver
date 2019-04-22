package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private PrintWriter writer;
	private Socket socket;

	public static void main(String[] args) throws IOException {
		new ChatClient().run();
	}

	public void run() throws IOException {
		try (Scanner scanner = new Scanner(System.in)) {
			String input;
			while (scanner.hasNextLine()) {
				input = scanner.nextLine();
				processInput(input);
			}
		}
	}

	private void processInput(String input) throws IOException {
		String[] tokens = input.split(Constants.SPACE_SPLITTER);
		String command = tokens[0];
		if (Constants.CONNECT_COMMAND.equals(command) && socket == null) {
			String host = tokens[1];
			int port = Integer.parseInt(tokens[2]);
			String username = tokens[Constants.CONNECT_COMMAND_USERNAME];
			connect(host, port, username);
		} else if (Constants.DISCONNECT_COMMAND.equals(command) && (socket == null || socket.isClosed())) {
			System.out.println("=> cannot disconnect, try to connect first");
		} else {
			writer.println(input);
		}
	}

	private void connect(String host, int port, String username) {
		try {
			socket = new Socket(host, port);
			writer = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("=> connected to server running on localhost:8080 as " + username);
			
			writer.println(username);
			ClientRunnable clientRunnable = new ClientRunnable(socket);
			new Thread(clientRunnable).start();	
		} catch (IOException e) {
			System.out.println("=> cannot connect to server on localhost:8080, make sure that the server is started!");
		}
	}
}