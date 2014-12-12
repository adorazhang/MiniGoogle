package MiniGoogleServer;

import java.io.IOException;
import java.util.Vector;

public class miniGoogle {
	
	private static miniGoogleServerThread minigoogle;
	static Vector<String> mapperComplete = new Vector<String>();
	static Vector<String> inputFiles = new Vector<String>();
	
	public static void main(String[] args) throws IOException {
		// start service
		minigoogle = new miniGoogleServerThread();
		minigoogle.start();
	}

}
