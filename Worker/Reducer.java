package Worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;


public class Reducer extends Thread{

    //2.2 reducer
    //2.2.1 indexing service
    //get intermediate outcome from mapper (AFS read)
    //word count, sort and merge 
    //generate global outcome file (partial inverted index)  
    //return Master the result (succ and file path)
    
    private List<String> inputFiles;
    private String reducerID;
    private int taskFlag; //index (flag=1) or query task (flag=2)
    private String outputFilePrefix;//master informs
    
    
    public Reducer(List<String> inputFiles0, String reducerID0, String outputFilePrefix0){
        inputFiles = inputFiles0;
        reducerID = reducerID0;
        outputFilePrefix = outputFilePrefix0;
    }
    
    @Override
    public void run(){
        indexReduce();
    }
    
    
    private String indexReduce(){
        
        /*merge and sort*/
        ArrayList<IndexNode> nodeList = new ArrayList<IndexNode>();
        MergeAndSort m = new MergeAndSort();
        m.mergeAndSort(inputFiles, nodeList);
        /*end merge and sort*/
        
        /*reduce*/
        //get the first node in List
        IndexNode curNode = nodeList.get(0);
        //key: word
        String curKeyword = curNode.getKey();               //current word
        char curInitLetter = curKeyword.charAt(0);          //current word's initial letter
        String preKeyword = curKeyword;                     //previous word
        //char preInitLetter = curInitLetter;                 //previous word's initial letter
        String location = outputFilePrefix + curInitLetter + ".txt";         //output file location, varies according to initial letter
        String outputFilename = new String(location);
        
        //value: document1 frequency1, focument2 frequency2, ...
        Map<String, String> ExistDocuMap = new HashMap<String, String>();
        String[] Docus = curNode.getValue().split(",");
        String curDocuName;
        String wordFrequency;
        for(String info : Docus){
            String [] split = info.trim().split(" ");
            curDocuName = split[0];
            wordFrequency = split[1];
            ExistDocuMap.put(curDocuName, wordFrequency);
        }
        
        
        BufferedWriter writer = null;
        try{
            //open a new file
            File outputFile = new File(location);
            FileWriter fileWriter = new FileWriter(outputFile);
            writer = new BufferedWriter(fileWriter);
            
            //handling all the nodes in list
            for(int i=1 ; i<nodeList.size() ; ++i){
                curNode = nodeList.get(i);
                curKeyword = curNode.getKey();             
                Docus = curNode.getValue().split(",");
                
                if (curKeyword.equals(preKeyword)){
                
                    for(String info : Docus){
                        String [] split = info.trim().split(" ");
                        curDocuName = split[0];
                        wordFrequency = split[1];
                        
                        //document already exist, update word frequency
                        if ( ExistDocuMap.containsKey(curDocuName) ){        
                            int updateFrequency = Integer.parseInt(wordFrequency) + 
                                    Integer.parseInt(ExistDocuMap.get(curDocuName));
                            ExistDocuMap.put(curDocuName, Integer.toString(updateFrequency));
                        }
                        //new document
                        else{      
                            ExistDocuMap.put(curDocuName, wordFrequency);
                        }        
                       
                    }
                }
                //get a new word
                else { 
                	//sort by frequency
                    List<DocuNode> DocuList = new ArrayList<DocuNode>();
                    Iterator<Entry<String, String>> iter = ExistDocuMap.entrySet().iterator();
                    while(iter.hasNext()){
                    	Entry<String, String> entry = (Entry<String, String>) iter.next();
                    	DocuNode d = new DocuNode(entry.getKey(), entry.getValue());
                    	DocuList.add(d);
                    }
                    Collections.sort(DocuList);

                    /**write the previous word and its information into file**/
                    StringBuilder wordInfo = new StringBuilder();

                    Iterator<DocuNode> iter2 = DocuList.iterator();
                    if(iter2.hasNext()){
                    	DocuNode d = iter2.next();
                        wordInfo.append(d.getDocuName() + " " + d.getWordFrequency());
                    }
                    while(iter2.hasNext()){
                    	DocuNode d = iter2.next();
                    	wordInfo.append(", ");
                        wordInfo.append(d.getDocuName() + " " + d.getWordFrequency());
                    }

                    //write
                    writer.write(preKeyword + "\t" + wordInfo + "\n");
                    //reset
                    ExistDocuMap.clear();

                    /*handling the new word*/
                    Docus = curNode.getValue().split(",");
                    for(String info : Docus){
                        String [] split = info.trim().split(" ");
                        curDocuName = split[0];
                        wordFrequency = split[1];
                        ExistDocuMap.put(curDocuName, wordFrequency);
                    }

                    preKeyword = curKeyword;
                }//end if-else
            }//end for

            //write the last word's information in output file
            StringBuilder wordInfo = new StringBuilder();
            Iterator<Entry<String, String>> iter = ExistDocuMap.entrySet().iterator();
            if(iter.hasNext()){
                Entry<String, String> entry = (Entry<String, String>) iter.next();
                wordInfo.append(entry.getKey() + " " + entry.getValue());
            }
            while(iter.hasNext()){
                Entry<String, String> entry = (Entry<String, String>) iter.next();
                wordInfo.append(", " + entry.getKey() + " " + entry.getValue());
            }
            //write
            writer.write(preKeyword + "\t" + wordInfo + "\n");
            writer.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return outputFilename;

    }//end indexReduce()
    
    
    private void queryReduce(){
        
    }
    
    
}
