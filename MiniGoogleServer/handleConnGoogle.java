package MiniGoogleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class handleConnGoogle extends Thread {
	protected BlockingQueue<Request> queue = null;
	private Socket conn;
	private DataInputStream in;
	static Vector<String> mapperComplete = new Vector<String>();
	static Vector<String> querierComplete = new Vector<String>();
	static Vector<String> inputFiles = new Vector<String>();

	handleConnGoogle(Socket connection, BlockingQueue<Request> queue){
		this.queue = queue;
		conn = connection;
	}

	public void run(){
		try {
			in = new DataInputStream(conn.getInputStream());
			byte type = in.readByte();
			String data, clientIP;
			int clientPort;

			switch(type){
		    case 10: // indexing request from client
				data = in.readUTF();
				clientIP = in.readUTF();
				clientPort = in.readInt();
				indexingMaster(data, clientIP, clientPort); // not concurrent for indexing requests.
				in.close();
		    	break;
		    case 12: // query request from client
				data = in.readUTF();
				clientIP = in.readUTF();
				clientPort = in.readInt();
				// put in the queue
				queue.put(new Request(clientIP,clientPort,data));
				in.close();
		    	break;
			case 50: // indexing result from mapper
				String mapperID = in.readUTF();
				String filename = in.readUTF();
				mapperComplete.add(mapperID);
				inputFiles.add(filename);
				break;
			case 51: // indexing result from querier
				String result = in.readUTF();
				querierComplete.add(result+"\n");
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

	private void indexingMaster(String dir, String IP, int Port) throws IOException {
		// deal with dir
		List<String> allFiles = utility.getFileList("C:\\MiniGoogle\\Books");

		int currentID = 1;
		for(String file:allFiles){	// assign jobs
			String key = Integer.toString(currentID);
			Socket soc = utility.getService("Worker");
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			out.writeByte(1); // Indexing
			out.writeUTF(file);
			out.writeUTF(key);
			currentID++;
		}

		// wait for successes from the mappers
		while(true){
			// received all results
			if(mapperComplete.size() == allFiles.size()) break;
		}

		// do reducing
		for(String file:inputFiles){
			String key = Integer.toString(currentID);
			Socket soc = utility.getService("Worker");
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			out.writeByte(2); // Reducing
			out.writeUTF(key);
			out.writeUTF(file);
			currentID++;
		}

		// if success
		Socket client = new Socket(IP, Port);
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		out.writeByte(11); //success
	}
}
