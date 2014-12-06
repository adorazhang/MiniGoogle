import java.io.*;
import java.net.*;

public class miniGoogle {
	
	private static miniGoogleServerThread minigoogle;
	
	public static void main(String[] args) throws IOException {
		// start service
		minigoogle = new miniGoogleServerThread();
		minigoogle.start();
	}

}
