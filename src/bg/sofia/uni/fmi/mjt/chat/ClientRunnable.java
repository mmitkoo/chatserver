package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClientRunnable implements Runnable {

	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String input;
			while (true) {
				if (socket.isClosed()) {
					System.out.println("You have been disconnected!");
					return;
				} else {
					input = reader.readLine();
					if (input.equals(Constants.DISCONNECT_COMMAND)) {
						socket.close();
					} else {
						Scanner sc = new Scanner(System.in);
						String str = sc.nextLine();
						System.out.println(str);
						System.out.println(input);
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}