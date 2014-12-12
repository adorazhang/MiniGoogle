package Worker;

////not used yet
//Document name + word frequency, the elements of a word's information
public class DocuNode implements Comparable<DocuNode>{
    
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
    
    @Override
	public int compareTo(DocuNode o) {
		return -Integer.compare(Integer.parseInt(this.wordFrequency), 
								Integer.parseInt(o.wordFrequency)); //- to implement descending order
	}
}
