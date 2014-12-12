package MiniGoogleServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Adora on 12/10/2014.
 */
public class queryManagerThread extends Thread {
    protected BlockingQueue<Request> queue = null;
    queryManagerThread(BlockingQueue<Request> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Request req = queue.take();
                handleRequest(req);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(Request req) throws IOException {
        System.out.println(">>> Query request received: "+req);
        String[] keywords = req.query.split(",");

        Socket worker = utility.getService("Worker");
        String result = "";


        // send back results
        Socket client = new Socket(req.IP, req.Port);
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeByte(13); //success
        out.writeUTF(result);
    }
}
