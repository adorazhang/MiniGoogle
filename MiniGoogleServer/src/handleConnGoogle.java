import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class handleConnGoogle extends Thread {
	private Socket conn;
	private DataInputStream in;
	private DataOutputStream out;
	
	handleConnGoogle(Socket connection){
		conn = connection;
	}

	public void run(){
		// listen to client requests
		try {
			in = new DataInputStream(conn.getInputStream());
		    out = new DataOutputStream( conn.getOutputStream());
		    byte type = in.readByte();
		    String data = in.readUTF();
		    switch(type){
		    case 10: // indexing request from client
		    	String dir = data;
		    	index(dir);
		    	break;
		    case 12: // query request from client
		    	String[] keywords = data.split(",");
		    	query(keywords);
		    	break;
		    default:
		    	System.out.println("Packet type not supported!");
		    	break;
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void query(String[] keywords) throws IOException {
		for(String keyword:keywords){

		}
		//send back result
	    out.writeByte(13);
	}

	private void index(String dir) throws IOException {

		//send back result
	    out.writeByte(11); 
	}
}
