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
                handleConnGoogle.querierComplete.removeAllElements();
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
        String[] keywords = req.query.split(" ");


        for(String keyword:keywords){	// assign jobs
            Socket soc = utility.getService("Worker");
            DataOutputStream out = new DataOutputStream(soc.getOutputStream());
            out.writeByte(3); // Query
            out.writeUTF(keyword);
        }

        String result = "";
        // wait for successes from the queriers
        while(true){
            // received all results
            if(handleConnGoogle.querierComplete.size() == keywords.length) break;
        }

        for(String res:handleConnGoogle.querierComplete){
            result += res;
        }

        // send back results
        Socket client = new Socket(req.IP, req.Port);
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeByte(13); //success
        out.writeUTF(result);
    }
}
