package redo;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Solution {
    
	public static void main(String args[] ) throws Exception {
    	
    	List<String> list = new LinkedList<String>();
    	List<String> nlist = new LinkedList<String>();
    	
    	Scanner scanner = new Scanner(System.in);
    	boolean dict = false;
    	boolean secret = false;
        
    	while(scanner.hasNextLine()) {
        	
        	String line = scanner.nextLine();  

        	if(line.isEmpty()) {
                break;
            }
        	
        	if(line.contains("//dict")) {
        		dict = true;
        		continue;
        	}
        	if(dict && !secret) {
        		if(!line.contains("//secret"))
        			list.add(line);
        	}
        	if(line.contains("//secret")) {
        		secret= true;
        		dict=false;
        		continue;
        	}
        	if(!dict && secret) {
        		nlist.add(line);
        	}
        }
    	System.out.println(list);
    	System.out.println(nlist);
    	
    	Set<Character> set1 = new TreeSet<Character>();
    	Set<Character> set2 = new TreeSet<Character>();
    	
    	for(String s : list) {
    		char[] xa = s.toCharArray();
    		for (int c=0; c<xa.length; c++ ) {
    			set1.add(xa[c]);
    		}
    	}
    	
    	for(String s : nlist) {
    		char[] xa = s.toCharArray();
    		for (int c=0; c<xa.length; c++ ) {
    			if(xa[c]!= ' ')
    				set2.add(xa[c]);
    		}
    	}
    	System.out.println(set1);
    	System.out.println(set2);
    	
        Collections.sort(list);
        Map<Character, Number> map = new HashMap<Character, Number>();
        int x = 0;
        for(String s : list) {
        	char[] c = s.toCharArray();
        	for(int i=0; i<c.length; i++) {
        		if(!map.containsKey(c[i]))  {
        			x++;
        			map.put(c[i], x);
        		}
        	}
        }
    
        List<String> outputList = new LinkedList<String>();
        
        for(String s: nlist) {
        	String[] sa  = s.split(" ");
        	String lineBuilder  = "";
        	
        	for(int y=0; y<sa.length; y++)  { 
        		char[] ca = sa[y].toCharArray();
        		StringBuilder sb = new StringBuilder();

        		for(int z=0; z< ca.length; z++)  { 
        			
        			Iterator<Entry<Character, Number>> it = map.entrySet().iterator();
        			while (it.hasNext()) {
        		        Map.Entry<Character, Number> pairs = (Map.Entry<Character, Number>)it.next();
        		        
        		        if(pairs.getValue().equals(Integer.parseInt(String.valueOf(ca[z])))) {
        		        	sb.append(pairs.getKey());
        		        	break;
        		        }
        		    }
        		}
        		lineBuilder = lineBuilder + sb.toString() + " ";
        	}
        	outputList.add(s + " = " + lineBuilder.trim());
        }
        
        for (String s : outputList) {
    		 System.out.println(s);
    	}
    }
}