package java_test;

import java.util.ArrayList;
import java.util.List;


public class Test {
    
    public static void main(String [] args){
        
          String path="/Users/loumengsi/Desktop/Documents";
          List<String> list = new ArrayList<String>();
          FileList f = new FileList(path, list);
          f.getList();

          System.out.print(list);
        
    }

}
