package MiniGoogleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class handleConnGoogle extends Thread {
	protected BlockingQueue<Request> queue = null;
	private Socket conn;
	private DataInputStream in;
	
	handleConnGoogle(Socket connection, BlockingQueue<Request> queue){
		this.queue = queue;
		conn = connection;
	}

	public void run(){
		try {
			in = new DataInputStream(conn.getInputStream());
			byte type = in.readByte();
		    String data = in.readUTF();
			String clientIP = in.readUTF();
			int clientPort = in.readInt();

		    switch(type){
		    case 10: // indexing request from client
				index(data, clientIP, clientPort); // not concurrent for indexing requests.
				in.close();
		    	break;
		    case 12: // query request from client
				// put in the queue
				queue.put(new Request(clientIP,clientPort,data));
				in.close();
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

	private void index(String dir, String IP, int Port) throws IOException {
		// deal with dir
		List<String> allFiles = utility.getFileList("C:\\MiniGoogle\\Books");

		int currentID = 1;
		for(String file:allFiles){
			// create a thread to assign a job
			// wait for all successes
			currentID++;
		}

		// if success
		Socket client = new Socket(IP, Port);
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		out.writeByte(11); //success
	}
}
