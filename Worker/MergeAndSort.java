package Worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;


public class MergeAndSort {
    
    
    public void mergeAndSort(List<String> inputFiles, List<IndexNode> nodeList){
        
        /*****merge: read all the mappers' outcome in this machine*****/
        for(String nextFile : inputFiles){
            try{
                File file = new File(nextFile);
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);
                
                String line = null;
                while ((line = reader.readLine()) != null){
                    String [] split = line.split("\t");
                    IndexNode newIndexNode = new IndexNode(split[0], split[1]);
                    nodeList.add(newIndexNode);
                }
                
                reader.close();
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        /*****sort: arrange the list in keyword ascending sequence*****/
        Collections.sort(nodeList);
    }
}
