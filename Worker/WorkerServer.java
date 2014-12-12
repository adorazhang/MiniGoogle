package Worker;

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
        workerServerManager();
    }


    private void workerServerManager() {
        //socket
        try {
            //listen to master's request
            Socket server = serverSocket.accept();
            DataInputStream in = new DataInputStream(server.getInputStream());

            //split the message and handle the task
            byte MsgID = in.readByte();

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

            switch (MsgID) {
                case 1:
                    String inputFile = in.readUTF();
                    String mapperID = in.readUTF();
                    createIndexMapperTask(inputFile, mapperID);
                    System.out.println("finish mapper");
                case 2: // TODO
                    String reducerID = in.readUTF();
                    String reducerFilenamePrefix = in.readUTF();
                    List<String> inputFiles = new ArrayList<String>();
                    int num = in.readInt();
                    for (int i = 0; i < num; i++) {
                        String input = in.readUTF();
                        inputFiles.add(input);
                    }
                    createReducerTask(inputFiles, reducerID, reducerFilenamePrefix);

                /*String reducerFilenamePrefix = "invertedIndex";
                List<String> reducerIutputFiles = new ArrayList<String>();
                reducerIutputFiles.add("combiner_1_outcome_a.txt");
                reducerIutputFiles.add("combiner_2_outcome_a.txt");
                String reducerID = "1";*/
                    System.out.println("finish reducer");
                case 3:
                    //String
                    String query = in.readUTF();
                    createQueryMapperTask(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void createIndexMapperTask(String inputFile, String mapperID){
        Thread m = new IndexMapper(inputFile, mapperID);
        m.start();
    }

    //combiner
    /*List<String> combinerInputFiles = new ArrayList<String>();
    combinerInputFiles.add("mapper_1_outcome.txt");
    combinerInputFiles.add("mapper_2_outcome.txt");
    String combinerID = "1";

    createCombinerTask(combinerInputFiles, combinerID);
    System.out.println("finish combiner");*/

    private void createCombinerTask(List<String> inputFiles, String combinerID){
        Thread c = new Combiner(inputFiles, combinerID);
        c.start();
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
