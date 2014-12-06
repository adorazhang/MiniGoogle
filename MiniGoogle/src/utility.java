

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class utility {
	static String getIP() throws SocketException{
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "";
	}

	public static void registerService(int PORT) throws IOException {
		// read the location of the name server
		BufferedReader br = new BufferedReader(new FileReader("C:/nameServer"));
		String nsIP = br.readLine();
		int nsPort = Integer.parseInt(br.readLine());
		
		// connect to the name server, register
		Socket client = new Socket(nsIP, nsPort);
		OutputStream outToServer = client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);
		
		// register service
		out.writeByte(0);
		out.writeUTF("MiniGoogle#"+getIP()+":"+PORT);
		
		// get acknowledgment
		InputStream inFromServer = client.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		if(in.readByte()==1) System.out.println(">>> Service " + in.readUTF() + " successfully registered!");
		client.close();
	}
}
