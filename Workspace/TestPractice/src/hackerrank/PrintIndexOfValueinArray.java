package hackerrank;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class PrintIndexOfValueinArray {

    public static void main(String[] args) {
        /* Enter your code here. 
        Read input from STDIN. 
        Print output to STDOUT. 
        Your class should be named Solution. 
        */
        Scanner scanner = new Scanner(System.in);
        try{
            Integer V = new Integer(scanner.nextLine());
            String[] s = scanner.nextLine().split(" ");
            
            for(int i=0; i< s.length; i++) {
                if(V.equals(Integer.valueOf(Integer.parseInt(s[i])))) {
                    System.out.println(i);
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
}