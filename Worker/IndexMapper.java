import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IndexMapper extends Thread{

    private String inputFilename;
    private String mapperID;
    private String outputFilename;//???change to return value
    
    public IndexMapper(String inputFilename0, String mapperID0){
        inputFilename = inputFilename0;
        mapperID = mapperID0;
        outputFilename = new String();
    }
    
    @Override
    public void run(){
        indexMap();
        //reply server
    }

    
    //read source file and make word count then output the result to local disk
    private void indexMap(){

        try{
            //input file: source file
            File file = new File(inputFilename);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            
            //output file: temporary outcome
            outputFilename += ("/afs/cs.pitt.edu/usr0/qz/public/MiniGoogle/mapperout/" + mapperID + ".txt");
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
        List<String> combinerInputFiles = new ArrayList<String>();
        combinerInputFiles.add(outputFilename);
        createCombinerTask(combinerInputFiles, mapperID);
    }

    private void createCombinerTask(List<String> inputFiles, String combinerID){
        Thread c = new Combiner(inputFiles, combinerID);
        c.start();
    }
  //2.1 mapper
    //2.1.1 indexing service 
    //word count
    //combiner
    //generate local outcome file
    //return Master the result
    
}

