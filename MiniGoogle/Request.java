/**
 * Created by Adora on 12/11/2014.
 */
public class Request {
    public String IP;
    public int Port;
    public String query;

    Request(String IP, int Port, String query){
        this.IP = IP;
        this.Port = Port;
        this.query = query;
    }

    public String toString(){
        return this.IP+":"+this.Port+" - "+this.query;
    }
}
