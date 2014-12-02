////not used yet
//Document name + word frequency, the elements of a word's information
public class DocuNode {
    
    private String DocuName;
    private String wordFrequency;
    
    public DocuNode(String DocuName0, String wordFrequency0){
        DocuName = DocuName0;
        wordFrequency = wordFrequency0;
    }
    
    public String getDocuName(){
        return DocuName;
    }
    
    public String getWordFrequency(){
        return wordFrequency;
    }
    
    public void setWordFrequency(String updateFrequency){
        wordFrequency = updateFrequency;
    }
}
