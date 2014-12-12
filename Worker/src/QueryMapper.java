import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class QueryMapper extends Thread{
	
	private String keyword;
	
	public QueryMapper(String keyword0){
		keyword =  keyword0;
	}
	
	public void run(){
		String s = queryMap();
		System.out.print(s);
	}

	public String queryMap(){
        
		String invertedIndexPath = "invertedIndex_" + keyword.charAt(0) + ".txt";
		
    	String outcome = null;
    	int findFlag = -1;
    	
    	try{
            //input file: source file
            File file = new File(invertedIndexPath);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            
            //split word and store the word count in file
            String line = null;
            while ((line = reader.readLine()) != null){
                
                //split words
                String[] split = line.split("\t");
                if (split[0].equals(keyword)){
                	outcome = new String(split[1]);
                	findFlag = 1;
                	break;
                }
            }
            reader.close();
            
            
        
        } catch (Exception e) {
            e.printStackTrace();
        } 
    	
    	if (findFlag == 1){
        	return outcome;
        }
        else
        	return "fail";
    }
	
}
