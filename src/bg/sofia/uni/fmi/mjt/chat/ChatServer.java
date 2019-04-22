package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatServer {

	
	private boolean isRunning = true;
	private static Map<String, Socket> users = new HashMap<>();

	public static void main(String[] args) {
		new ChatServer().start(Constants.PORT);
	}

	public synchronized Socket getUser(String username) {
		return users.get(username);
	}

	public synchronized Set<String> getActiveUsers() {
		return users.keySet();
	}

	public synchronized void deleteUser(String username) {
		users.remove(username);
	}

	public void start(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.printf("Server is running on localhost:%d%n", port);

			while (isRunning) {
				Socket socket = serverSocket.accept();
				System.out.println("A client connected to server " + socket.getInetAddress());

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String username = reader.readLine();
				// proverka dali go ima tozi user?? i ako go nqma go dobavqma inache pishem saobshenie
				users.put(username, socket);

				ClientConnectionRunnable runnable = new ClientConnectionRunnable(username, socket);
				new Thread(runnable).start();
			}
		} catch (IOException e) {
			System.out.println("Maybe another server is running on port " + port);
		}
	}
	
	public void terminateServer() {
		isRunning = false;
	}
}