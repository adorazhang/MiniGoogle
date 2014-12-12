import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class nameServerThread extends Thread {
	private ServerSocket serverSocket;

	public void run() {
		try {
			serverSocket = new ServerSocket(0);
			while (true) { // busy wait
				Socket conn = serverSocket.accept();
	            Thread handle = new handleConnNS(conn);
	            handle.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPort(){
		return serverSocket.getLocalPort();
	}
}
