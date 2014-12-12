import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class WorkerServer extends Thread {

    private ServerSocket serverSocket;
    
    public WorkerServer() throws IOException{
        serverSocket = new ServerSocket(0);  //if port equals 0, the system will allocate an idle port
        utility.registerService(serverSocket.getLocalPort(), "Worker");
    }
    
    @Override
    public void run(){
        while(true) {
            workerServerManager();
        }
    }


    private void workerServerManager() {
        //socket
        try {
            //listen to master's request
            Socket server = serverSocket.accept();
            DataInputStream in = new DataInputStream(server.getInputStream());

            //split the message and handle the task
            byte MsgID = in.readByte();
            switch (MsgID) {
                case 1:
                    String inputFile = in.readUTF();
                    String mapperID = in.readUTF();
                    createIndexMapperTask(inputFile, mapperID);
                    break;
                case 2: // TODO
                    String reducerID = in.readUTF();
                    String input = in.readUTF();
                    String[] names = input.split("#");

                    String reducerFilenamePrefix = "../reducerout/";
                    List<String> inputFiles = new ArrayList<String>();
                    for(String name:names){
                        inputFiles.add(name);
                    }

                    createReducerTask(inputFiles, reducerID, reducerFilenamePrefix);
                    break;
                case 3:
                    //String
                    String query = in.readUTF();
                    createQueryMapperTask(query);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createIndexMapperTask(String inputFile, String mapperID){
        Thread m = new IndexMapper(inputFile, mapperID);
        m.start();
    }
    
    private void createReducerTask(List<String> inputFiles, String combinerID, String outputFilePrefix){
        Thread r = new Reducer(inputFiles, combinerID, outputFilePrefix);
        r.start();
    }
    
    private void createQueryMapperTask(String keyword){
    	Thread r = new QueryMapper(keyword);
    	r.start();
    }
    
    
    //1. listen to port, get Master's request (indexing or query, mapper or reducer)
    
    //2. create a thread for the task
    
        //2.1 mapper
        //2.1.1 indexing service 
        //read source file
        //word count
        //combiner
        //generate local outcome file
        //return Master the result

        //2.2 reducer
        //2.2.1 indexing service
        //get intermediate outcome from mapper (AFS read)
        //word count, sort and merge 
        //generate global outcome file (partial inverted index)  
        //return Master the result (succ and file path)

}
