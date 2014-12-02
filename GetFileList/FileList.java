package java_test;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class FileList {

    private String dirName=null;
    private List<String> fileList=null;
    private Vector<String> ver=null;
    
    public FileList(String dir_name, List<String> list_name){
        this.dirName=dir_name;    
        this.fileList=list_name;    
        this.ver=new Vector<String>();  //use as stack 
    }
    
    public void getList(){
        
        ver.add(dirName);   //put the root folder into stack
        while(ver.size()>0){
            File[] files = new File(ver.get(0).toString()).listFiles();
            ver.remove(0);
            
            int len=files.length;
            for(int i=0; i<len ; ++i){
                String tmp=files[i].getAbsolutePath();
                if(files[i].isDirectory())    //if is directory, put into stack
                    ver.add(tmp);
                else                    
                    fileList.add(tmp);
            }
        }
    }
    
}
