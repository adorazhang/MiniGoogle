package Worker;
//inverted index: consists of index nodes
//<key, value> = <word, arraylist>

public class IndexNode implements Comparable<IndexNode>{

    private String key;
    private String value;
    
    public IndexNode(String key0, String value0){
        key = key0;
        value = value0;
    }
    
    public String getKey(){
        return key;
    }
    
    public String getValue(){
        return value;
    }
    
    public int compareTo(IndexNode o){
        return this.getKey().compareTo(o.getKey());
    }
    
}
