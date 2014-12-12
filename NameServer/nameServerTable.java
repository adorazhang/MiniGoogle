package NameServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Adora
 *
 */
public class nameServerTable {
	/**
	 * @param args
	 */
	volatile ConcurrentHashMap<String,ArrayList<String>> table;
	volatile HashMap<String,Integer> current; 
	
	nameServerTable(){
		table = new ConcurrentHashMap<String,ArrayList<String>>();
		current = new HashMap<String,Integer>();
	}
	
	void addEntry(String key, String value) {
		// if key exists, append to the end of the arraylist
		// if key doesn't exist, create new entry
		
		if(table.containsKey(key)){
			table.get(key).add(value);
		}
		else{
			ArrayList<String> newlist = new ArrayList<String>();
			newlist.add(value);
			table.put(key, newlist);
			current.put(key, 0);
		}
		printOut();
	}
	
	String getService(String key) {
		// if key exists, return the current address
		// if not, throw exception
		if(table.containsKey(key)){
			ArrayList<String> rowList = table.get(key);
			String[] rowArray = new String[rowList.size()];
			rowArray = rowList.toArray(rowArray);
			int cur = current.get(key);
			cur = (cur+1) % table.get(key).size();
			current.put(key, cur);
			return rowArray[cur];
		}
		else{
			return "Service doesn't exist!";
		}
	}
	
	void printOut() {
		System.out.println(table);
	}
}
