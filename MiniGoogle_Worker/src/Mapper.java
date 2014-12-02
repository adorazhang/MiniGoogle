import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class Mapper extends Thread{

    private String inputFilename;
    private String mapperID;
    private int taskFlag; //index (flag=1) or query task (flag=2)
    private String outputFilename;//???change to return value
    
    public Mapper(String inputFilename0, String mapperID0, int taskFlag0){
        inputFilename = inputFilename0;
        mapperID = mapperID0;
        taskFlag = taskFlag0;
        outputFilename = new String();
    }
    
    @Override
    public void run(){
        map();
        
        //apply server 
        
    }
    
    private void map(){
        if (taskFlag == 1)
            indexMap();
        else
            queryMap();
    }
    
    //read source file and make word count then output the result to local disk
    private void indexMap(){

        try{
            //input file: source file
            File file = new File(inputFilename);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            
            //output file: temporary outcome
            outputFilename += ("mapper_" + mapperID + "_outcome.txt");
            File outputFile = new File(outputFilename);
            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedWriter writer = new BufferedWriter(fileWriter); 
            
            //split word and store the word count in file
            String line = null;
            while ((line = reader.readLine()) != null){
                
                //replace the punctuation with blank
                line = line.replaceAll("[^a-zA-Z]+", " ");
                //split words
                StringTokenizer tokenizer = new StringTokenizer(line);
                
                while(tokenizer.hasMoreTokens()){
                    String word = tokenizer.nextToken().toLowerCase();
                    writer.write(word + "\t" + inputFilename + " " + "1\n");
                }
            }
            reader.close();
            writer.close();
        
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }
    
    
    private void queryMap(){
        //
    }
    
    /*private boolean writeWord(String word, String wordInfo, String inputFile, String locationPrefix){
        String location = new String();
        
        //get full output filename: 
        //e.g. the word apple, the filename is prefix + 'a.txt'
        location = locationPrefix + word.charAt(0) + ".txt";
        
        try{
            //output file: temporary outcome
            File outputFile = new File(location);
            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            
            writer.write(word + "\t" + inputFile + " " + "1\n");
            writer.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/
    
    
  //2.1 mapper
    //2.1.1 indexing service 
    //word count
    //combiner
    //generate local outcome file
    //return Master the result
    
}
