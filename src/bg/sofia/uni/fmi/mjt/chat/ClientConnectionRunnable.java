package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientConnectionRunnable implements Runnable {

	private String username;
	private Socket socket;
	private ChatServer server;

	public ClientConnectionRunnable(String username, Socket socket) {
		this.username = username;
		this.socket = socket;
		server = new ChatServer();
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
			while (true) {
				String commandInput = reader.readLine();
				processCommand(commandInput, writer);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void processCommand(String commandInput, PrintWriter writer) throws IOException {

		if (commandInput != null) {
			String[] tokens = commandInput.split(Constants.SPACE_SPLITTER);
			String command = tokens[0];
			System.out.println(tokens.length + " manqk");

			switch (command) {
			    case Constants.SEND_COMMAND:
				    String[] usernameAndMessage = tokens[1].split(Constants.SPACE_SPLITTER, 2);
				    String receiverName = usernameAndMessage[0];
				    String message = usernameAndMessage[1];
				    sendMessageToUser(receiverName, message, writer);
				    break;

			    case Constants.LIST_USERS_COMMAND:
			    	printActiveUsers(writer);
			    	break;

			    case Constants.DISCONNECT_COMMAND:
			    	disconnectFromServer(writer);
			    	break;

			    case Constants.SEND_ALL_COMMAND:
			    	sendMessageToAllActiveUsers(tokens[1], writer);
			    	break;

			    default:
			    	writer.println(command + " is not a valid operation!");
			    	break;
			}
		}
	}

	private synchronized void disconnectFromServer(PrintWriter writer) throws IOException {
		server.deleteUser(username);
		writer.println(Constants.DISCONNECT_COMMAND);
	}

	private void sendMessageToAllActiveUsers(String message, PrintWriter writer) throws IOException {
		Set<String> activeUsers = server.getActiveUsers();
		for (String activeUserName : activeUsers) {
			sendMessageToUser(activeUserName, message, writer);
		}
	}

	private void printActiveUsers(PrintWriter writer) {
		Set<String> activeUsers = server.getActiveUsers();
		if (activeUsers.size() == 1 || activeUsers.size() == 0) {
			writer.println("Nobody else is online now.");
		} else {
			activeUsers.stream().filter(user -> user != username).forEach(user -> writer.println(user));
		}
	}

	private void sendMessageToUser(String receiverName, String message, PrintWriter writer) throws IOException {
		Socket receiverSocket = server.getUser(receiverName);
		if (receiverSocket == null) {
			writer.println(String.format("=> %s seems to be offline", receiverName));
			return;
		}

		PrintWriter receiverWriter = new PrintWriter(receiverSocket.getOutputStream(), true);
		receiverWriter.println(String.format("[%s]: %s", username, message));
		System.out.println(username + " successfully sent message");
	}
}