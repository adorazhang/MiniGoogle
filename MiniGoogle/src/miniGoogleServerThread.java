

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class miniGoogleServerThread extends Thread {
	private ServerSocket serverSocket;

	public void run() {
		try {
			serverSocket = new ServerSocket(0);
			// register on name server
			utility.registerService(serverSocket.getLocalPort());
			while (true) { // busy wait
				Socket conn = serverSocket.accept();
	            Thread handle = new handleConnGoogle(conn);
	            handle.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
