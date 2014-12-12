import java.io.*;
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
					String ip = inetAddress.getHostAddress().toString();
					if(ip.startsWith("136")) return ip;
				}
			}
		}
		return "";
	}

	public static void registerService(int PORT) throws IOException {
		// read the location of the name server
		BufferedReader br = new BufferedReader(new FileReader("/afs/cs.pitt.edu/usr0/qz/public/MiniGoogle/nameserver"));
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

	public static Socket getService(String service) throws IOException {
		// read the location of the name server
		BufferedReader br = new BufferedReader(new FileReader("/afs/cs.pitt.edu/usr0/qz/public/MiniGoogle/nameserver"));
		String nsIP = br.readLine();
		int nsPort = Integer.parseInt(br.readLine());

		// connect to the name server, ask for miniGoogle's location
		Socket client = new Socket(nsIP, nsPort);
		OutputStream outToServer = client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);

		// ask for service
		out.writeByte(2);
		out.writeUTF(service);

		// get requested data
		InputStream inFromServer = client.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		if(in.readByte()==3) {
			String data = in.readUTF();
			String[] parts = data.split(":");
			String IP = parts[0];
			int Port = Integer.parseInt(parts[1]);
			return new Socket(IP, Port);
		}
		client.close();
		return null;
	}
}
