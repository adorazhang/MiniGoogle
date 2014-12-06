import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class client {
	private static ServerSocket serverSocket;
	private static String googleIP;
	private static int googlePort;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// read the location of the name server
		BufferedReader br = new BufferedReader(new FileReader("C:/nameServer"));
		String nsIP = br.readLine();
		int nsPort = Integer.parseInt(br.readLine());

		// connect to the name server, ask for miniGoogle's location
		Socket client = new Socket(nsIP, nsPort);
		OutputStream outToServer = client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);

		// ask for service
		out.writeByte(2);
		out.writeUTF("MiniGoogle");

		// get acknowledgment
		InputStream inFromServer = client.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		if(in.readByte()==3) {
			String data = in.readUTF();
			String[] parts = data.split(":");
			googleIP = parts[0];
			googlePort = Integer.parseInt(parts[1]);
		}

		client.close();

		/////////////////////////////////////////////////////////////////////////////
		// done with the name server, now start requesting service from minigoogle //
		/////////////////////////////////////////////////////////////////////////////

		Socket uiShell = new Socket(googleIP, googlePort);
		OutputStream outToGoogle = uiShell.getOutputStream();
		DataOutputStream output = new DataOutputStream(outToGoogle);

		// ask for service
		int request = 0;
		if(request == 0) // indexing request
		{
			output.writeByte(10);
			output.writeUTF("C:/Books");
			// get result
			InputStream inFromGoogle = uiShell.getInputStream();
			DataInputStream input = new DataInputStream(inFromGoogle);
			if(input.readByte()==11) {
				System.out.println(">>> Indexing success!");
			}
		}
		else if(request == 1) // query request
		{
			output.writeByte(12);
			output.writeUTF("love,I,a");
			// get result
			InputStream inFromGoogle = uiShell.getInputStream();
			DataInputStream input = new DataInputStream(inFromGoogle);
			if(input.readByte()==13) {
				System.out.println(">>> miniGoogle returned:");
				//String data = input.readUTF();
			}
		}
		uiShell.close();
	}

}
