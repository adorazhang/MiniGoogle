import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Combiner extends Thread{
    
    private List<String> inputFiles;
    private String combinerID;
    private List<String> outputFilenames;
    private String outputFilePrefix;
    
    public Combiner(List<String> inputFiles0, String combinerID0){
        inputFiles = inputFiles0;
        combinerID = combinerID0;
        outputFilenames = new ArrayList<String>();
        outputFilePrefix = "/afs/cs.pitt.edu/usr0/qz/public/MiniGoogle/combinerout/" + combinerID;
    }
    
    @Override
    public void run(){
        //combine
        outputFilenames = combine();
        String outputFilenamesAll = "";
        for(String name:outputFilenames){
            outputFilenamesAll = outputFilenamesAll + "#" + name;
        }

        // report finish to google
        Socket soc = null;
        try {
            soc = utility.getService("MiniGoogle");
            DataOutputStream out = new DataOutputStream(soc.getOutputStream());
            out.writeByte(50);
            out.writeUTF(combinerID);
            out.writeUTF(outputFilenamesAll);
            soc.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    //main function of Combiner
    private List<String> combine(){

        /*merge and sort*/
        ArrayList<IndexNode> nodeList = new ArrayList<IndexNode>();
        MergeAndSort m = new MergeAndSort();
        m.mergeAndSort(inputFiles, nodeList);
        /*end merge and sort*/
        
        //get the first node in List
        IndexNode curNode = nodeList.get(0);
        //key
        String curKeyword = curNode.getKey();               //current word
        char curInitLetter = curKeyword.charAt(0);          //current word's initial letter
        String preKeyword = curKeyword;                     //previous word
        char preInitLetter = curInitLetter;                 //previous word's initial letter
        //output filename
        String location = outputFilePrefix + curInitLetter;         //output file location, varies according to initial letter
        outputFilenames.add(location);
        
        //value
        String [] split = curNode.getValue().split(" ");
        String curDocuName = split[0];
        String wordFrequency = split[1];
        
        //current word's information, include the document name and word frequency
        Map<String, String> ExistDocuMap = new HashMap<String, String>();
        ExistDocuMap.put(curDocuName, wordFrequency);

        
        int i=1;
        boolean finishFlag = false;
        while (!finishFlag){
            BufferedWriter writer = null;
            try{
                //open a new file
                File outputFile = new File(location);
                FileWriter fileWriter = new FileWriter(outputFile);
                writer = new BufferedWriter(fileWriter);
                
                //handling all the nodes in list
                for( ; i<nodeList.size() ; ++i){
                    curNode = nodeList.get(i);
                    curKeyword = curNode.getKey();            
                    curInitLetter = curKeyword.charAt(0);      
                    
                    //get a same word as previous word
                    if (curKeyword.equals(preKeyword)){     
                        
                        //split the value into document name and frequency
                        split = curNode.getValue().split(" ");
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
                    	
                        /*write the previous word and its information into file*/
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
                        //split the value into document name and frequency
                        split = curNode.getValue().split(" ");
                        curDocuName = split[0];
                        wordFrequency = split[1];
                        ExistDocuMap.put(curDocuName, wordFrequency);
                        
                        preKeyword = curKeyword;
                        
                        //the new word has different initial letter as previous word
                        if (curInitLetter != preInitLetter){
                            location = outputFilePrefix + curInitLetter;//new output file name
                            outputFilenames.add(location);
                            preInitLetter = curInitLetter;
                            writer.close();

                            outputFile = new File(location);
                            fileWriter = new FileWriter(outputFile);
                            writer = new BufferedWriter(fileWriter);

                            ++i;
                            break;
                        } 
                    }//end if-else   
                }//end for
                
                
                if (i == nodeList.size()){//end of data: the last node in list
                    finishFlag = true;
                    
                    //sort by frequency
                    List<DocuNode> DocuList = new ArrayList<DocuNode>();
                    Iterator<Entry<String, String>> iter = ExistDocuMap.entrySet().iterator();
                    while(iter.hasNext()){
                    	Entry<String, String> entry = (Entry<String, String>) iter.next();
                    	DocuNode d = new DocuNode(entry.getKey(), entry.getValue());
                    	DocuList.add(d);
                    }
                    Collections.sort(DocuList);
                    
                    //write the last word's information in output file
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
                    writer.close();
                }

                
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
            
        }//end while
          
        return outputFilenames;
        
    }//end combine()
}
