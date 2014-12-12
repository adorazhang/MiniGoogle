package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class client {
	private static ServerSocket serverSocket;
	private static String googleIP;
	private static int googlePort;

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		Socket uiShell = utility.getService("MiniGoogle");
		DataOutputStream output = new DataOutputStream(uiShell.getOutputStream());

		int request=2;
		String para="";

		ServerSocket clientServer = new ServerSocket(0);
		String myIP = utility.getIP();
		int myPort = clientServer.getLocalPort();

		// ask for service
		if(args.length!=2){
			System.out.println("Usage:\n>./java client 1 \"PathName\"\n>./java client 2 \"Keywords\"");
		}
		request = Integer.parseInt(args[0]);
		para = args[1];
		if(request == 1) // indexing request
		{
			output.writeByte(10);
			output.writeUTF(para);
			output.writeUTF(myIP);
			output.writeInt(myPort);
		}
		else if(request == 2) // query request
		{
			output.writeByte(12);
			output.writeUTF(para);
			output.writeUTF(myIP);
			output.writeInt(myPort);
		}
		uiShell.close();
		output.close();

		Socket conn = null;

		//wait for result
		conn = clientServer.accept();
		DataInputStream input = new DataInputStream(conn.getInputStream());
		if (request == 1 && input.readByte() == 11 ) { // index request
			System.out.println(">>> Indexing success!");
		}
		else if (request == 2 && input.readByte() == 13 ) { // query request
			System.out.println(">>> miniGoogle returned:");
			String data = input.readUTF();
			System.out.print(data);
		}
		conn.close();
	}
}
