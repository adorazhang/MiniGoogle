import java.io.*;
import java.net.Socket;


public class QueryMapper extends Thread{
	
	private String keyword;
	
	public QueryMapper(String keyword0){
		keyword =  keyword0;
	}
	
	public void run(){
		String s = queryMap();
		// report finish to google
		Socket soc = null;
		try {
			soc = utility.getService("MiniGoogle");
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			out.writeByte(51);
			out.writeUTF(keyword+":\n"+s);
			soc.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String queryMap(){
        
		String invertedIndexPath = "/afs/cs.pitt.edu/usr0/qz/public/MiniGoogle/reducerout/" + keyword.charAt(0);

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
        	return "";
    }
	
}
