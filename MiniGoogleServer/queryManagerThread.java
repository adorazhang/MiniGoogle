package MiniGoogleServer;

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
            }
        }
    }

    private void handleRequest(Request req) {
        System.out.println(req);
    }
}
