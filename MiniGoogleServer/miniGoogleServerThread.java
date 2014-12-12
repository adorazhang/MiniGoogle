package MiniGoogleServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class miniGoogleServerThread extends Thread {
	private ServerSocket serverSocket;
	static BlockingQueue<Request> queue;

	public void run() {
		queue = new ArrayBlockingQueue<Request>(30);
		// start the query manager running in background
		Thread querymanager = new queryManagerThread(queue);
		querymanager.start();

		try {
			serverSocket = new ServerSocket(0);
			// register on name server
			utility.registerService(serverSocket.getLocalPort());
			while (true) { // busy wait
				Socket conn = serverSocket.accept();
	            Thread handle = new handleConnGoogle(conn, queue);
	            handle.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
