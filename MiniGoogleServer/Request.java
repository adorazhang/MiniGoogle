package MiniGoogleServer;

/**
 * Created by Adora on 12/11/2014.
 */
public class Request {
    private String IP;
    private int Port;
    private String query;

    Request(String IP, int Port, String query){
        this.IP = IP;
        this.Port = Port;
        this.query = query;
    }

    public String toString(){
        return this.IP+":"+this.Port+" - "+this.query;
    }
}
