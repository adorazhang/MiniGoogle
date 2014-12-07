import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class WorkerServer extends Thread {

    private ServerSocket serverSocket;
    
    public WorkerServer() throws IOException{
        //serverSocket = new ServerSocket(0);  //if port equals 0, the system will allocate an idle port
        //serverSocket.setSoTimeout(10000);       //timeout
    }
    
    @Override
    public void run(){
        workerServerManager();
    }
    
    
    private void workerServerManager(){
      
    	
    	//socket
        /*try{     
            //get name server's IP and port from a public AFS file
            
            //register in the name server
            
            //listen to master's request
            Socket server = serverSocket.accept();
            DataInputStream in = new DataInputStream(server.getInputStream());
            String msg = in.readUTF();
            
            //split the message and handle the task
            String[] splitMsg = msg.split("#");
            int MsgID = Integer.parseInt(splitMsg[0]);
            switch (MsgID) {
            case MAPPER_TASK_REQUEST: 
                createNewMapperTask(inputFile, mapperID1, taskFlag);
            case 2:
                createNewCombinerTask(combinerInputFiles, combinerID);
            case 3:
                createNewReducerTask(reducerIutputFiles, reducerID, taskFlag, reducerFilenamePrefix);
                
            }
            
            System.out.println(msg);
            
        
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (serverSocket != null)
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }*/
        
        
       
        
        //mapper
        /*String inputFile = "Miserables.txt";
        String inputFile2 = "NotreDame_De_Paris.txt";
        String mapperID1 = "1", mapperID2 = "2";
        
        createIndexMapperTask(inputFile, mapperID1);
        createIndexMapperTask(inputFile2, mapperID2);
        System.out.println("finish mapper");*/
        
        //combiner
        /*List<String> combinerInputFiles = new ArrayList<String>();
        combinerInputFiles.add("mapper_1_outcome.txt");
        combinerInputFiles.add("mapper_2_outcome.txt");
        String combinerID = "1";
        
        createCombinerTask(combinerInputFiles, combinerID);
        System.out.println("finish combiner");*/
        
        //reducer
        /*String reducerFilenamePrefix = "invertedIndex";
        List<String> reducerIutputFiles = new ArrayList<String>();
        reducerIutputFiles.add("combiner_1_outcome_a.txt");
        reducerIutputFiles.add("combiner_2_outcome_a.txt");
        String reducerID = "1";
        
        createReducerTask(reducerIutputFiles, reducerID, reducerFilenamePrefix);
        System.out.println("finish reducer");*/
        
        //query
        createQueryMapperTask("a");
        
    }
    
    
    private void createIndexMapperTask(String inputFile, String mapperID){
        Thread m = new IndexMapper(inputFile, mapperID);
        m.start();
    }
    
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
