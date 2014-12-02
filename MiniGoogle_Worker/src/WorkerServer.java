import java.util.ArrayList;
import java.util.List;


public class WorkerServer {

    public static void main(String [] args) throws Exception{
        
        WorkerServer worker = new WorkerServer();
        worker.workerServerRun();

    }
    
    private void workerServerRun(){
        
        int taskFlag = 1;
        /*//mapper
        String inputFile = "Miserables.txt";
        String inputFile2 = "NotreDame_De_Paris.txt";
        String mapperID1 = "1", mapperID2 = "2";
        
        createNewMapperTask(inputFile, mapperID1, taskFlag);
        createNewMapperTask(inputFile2, mapperID2, taskFlag);
        System.out.println("finish mapper");*/
        
        /*//combiner
        List<String> combinerInputFiles = new ArrayList<String>();
        combinerInputFiles.add("mapper_1_outcome.txt");
        combinerInputFiles.add("mapper_2_outcome.txt");
        String combinerID = "1";
        
        createNewCombinerTask(combinerInputFiles, combinerID);
        System.out.println("finish combiner");*/
        
        //reducer
        String reducerFilenamePrefix = "invertedIndex";
        List<String> reducerIutputFiles = new ArrayList<String>();
        reducerIutputFiles.add("combiner_1_outcome_a.txt");
        //reducerIutputFiles.add("combiner_2_outcome_a.txt");
        String reducerID = "1";
        
        createNewReducerTask(reducerIutputFiles, reducerID, taskFlag, reducerFilenamePrefix);
        System.out.println("finish reducer");
    }
    
    
    private void createNewMapperTask(String inputFile, String mapperID, int taskFlag){
        Thread m = new Mapper(inputFile, mapperID, taskFlag);
        m.start();
    }
    
    private void createNewCombinerTask(List<String> inputFiles, String combinerID){
        Thread c = new Combiner(inputFiles, combinerID);
        c.start();
    }
    
    private void createNewReducerTask(List<String> inputFiles, String combinerID, int taskFlag, String outputFilePrefix){
        Thread r = new Reducer(inputFiles, combinerID, taskFlag, outputFilePrefix);
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
