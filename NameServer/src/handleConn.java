

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class handleConn extends Thread {
	private Socket conn;
	
	handleConn(Socket connection){
		conn = connection;
	}
	
	public void run(){
		DataInputStream in;
		DataOutputStream out;
		try {
			in = new DataInputStream(conn.getInputStream());
		    out = new DataOutputStream( conn.getOutputStream());
		    byte type = in.readByte();
		    String data = in.readUTF();
		    String[] parts = data.split("#");
		    switch(type){
		    case 0:
			    nameServer.table.addEntry(parts[0], parts[1]);
			    nameServer.table.printOut();
			    out.writeByte(1);
			    out.writeUTF(parts[0]);
		    	break;
		    case 2:
			    out.writeByte(1);
			    out.writeUTF(nameServer.table.getService(data));
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
}
