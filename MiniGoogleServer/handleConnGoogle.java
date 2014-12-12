package MiniGoogleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;


public class handleConnGoogle extends Thread {
	protected BlockingQueue<Request> queue = null;
	private Socket conn;
	private DataInputStream in;
	private DataOutputStream out;
	
	handleConnGoogle(Socket connection, BlockingQueue<Request> queue){
		this.queue = queue;
		conn = connection;
	}

	public void run(){
		// listen to client requests
		try {
			in = new DataInputStream(conn.getInputStream());
			byte type = in.readByte();
		    String data = in.readUTF();
			String clientIP = in.readUTF();
			int clientPort = in.readInt();
			Socket socket2client = new Socket(clientIP, clientPort);
			DataOutputStream out = new DataOutputStream(socket2client.getOutputStream());

		    switch(type){
		    case 10: // indexing request from client
				index(data); // not concurrent at all.
		    	break;
		    case 12: // query request from client
				// put in the queue
				queue.put(new Request(clientIP,clientPort,data));
		    	break;
		    default:
		    	System.out.println("Packet type not supported!");
		    	break;
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void index(String dir) {

	}
}
