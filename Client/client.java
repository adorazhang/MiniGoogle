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

		/////////////////////////////////////////////////////////////////////////////
		// done with the name server, now start requesting service from minigoogle //
		/////////////////////////////////////////////////////////////////////////////

		ServerSocket clientServer = new ServerSocket(0);
		String myIP = utility.getIP();
		int myPort = clientServer.getLocalPort();

		Socket uiShell = utility.getService("MiniGoogle");
		OutputStream outToGoogle = uiShell.getOutputStream();
		DataOutputStream output = new DataOutputStream(outToGoogle);

		int request=2;
		String para="";

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

		while (true) { // busy wait for result
			Socket conn = clientServer.accept();
			if (request == 1) { // index request
				InputStream inFromGoogle = conn.getInputStream();
				DataInputStream input = new DataInputStream(inFromGoogle);
				if (input.readByte() == 11) {
					System.out.println(">>> Indexing success!");
				}
			}
			else if (request == 2) { // query request
				InputStream inFromGoogle = conn.getInputStream();
				DataInputStream input = new DataInputStream(inFromGoogle);
				if(input.readByte()==13) {
					System.out.println(">>> miniGoogle returned:");
					//String data = input.readUTF();
				}
			}
		}
	}
}
