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

		/////////////////////////////////////////////////////////////////////////////
		// done with the name server, now start requesting service from minigoogle //
		/////////////////////////////////////////////////////////////////////////////

		Socket uiShell = utility.getService("MiniGoogle");
		OutputStream outToGoogle = uiShell.getOutputStream();
		DataOutputStream output = new DataOutputStream(outToGoogle);

		// ask for service
		if(args.length!=2){
			System.out.println("Usage:\n>./java client 1 \"PathName\"\n>./java client 2 \"Keywords\"");
			return;
		}
		int request = Integer.parseInt(args[0]);
		if(request == 1) // indexing request
		{
			output.writeByte(10);
			output.writeUTF(args[1]);
			// get result
			InputStream inFromGoogle = uiShell.getInputStream();
			DataInputStream input = new DataInputStream(inFromGoogle);
			if(input.readByte()==11) {
				System.out.println(">>> Indexing success!");
			}
		}
		else if(request == 2) // query request
		{
			output.writeByte(12);
			output.writeUTF(args[1]);
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
