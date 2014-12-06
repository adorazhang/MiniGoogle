/**
 * 
 */


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class nameServer {
	static nameServerTable table;
	private static nameServerThread thread;
	
	public static void main(String[] args) throws SocketException, FileNotFoundException, UnsupportedEncodingException {
		// start a thread listening
		table = new nameServerTable();
		thread = new nameServerThread();
		thread.start();
		
		// write itself to an AFS file
		String IP = nameServerUtility.getIP();
		System.out.println(">>> Name server starts on "+IP+":"+thread.getPort());
		PrintWriter writer = new PrintWriter("C:/NameServer", "UTF-8");
		writer.println(IP+"\n"+thread.getPort());
		writer.close();
	}

}
