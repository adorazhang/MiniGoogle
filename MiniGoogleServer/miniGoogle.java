package MiniGoogleServer;

import java.io.IOException;

public class miniGoogle {
	
	private static miniGoogleServerThread minigoogle;
	
	public static void main(String[] args) throws IOException {
		// start service
		minigoogle = new miniGoogleServerThread();
		minigoogle.start();
	}

}
