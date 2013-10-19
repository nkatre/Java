package hackerrank;

import java.io.InputStreamReader;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.Test;

public class Tester {
	
	public int[] initInsertionSort1(int n, String s) {
		Scanner in = new Scanner(s);
		int[] ar = new int[n];
		
		for(int i=0;i<n;i++){
			ar[i]=in.nextInt(); 
		}
		return ar;
	}
	
	@Test
	public void testInsertionSort1TestCase1() {
	
		String expected = "2 3 4 5 6 7 8 9 10 10/n"
						+ "2 3 4 5 6 7 8 9 9 10/n"
						+ "2 3 4 5 6 7 8 8 9 10/n"
						+ "2 3 4 5 6 7 7 8 9 10/n"
						+ "2 3 4 5 6 6 7 8 9 10/n"
						+ "2 3 4 5 5 6 7 8 9 10/n"
						+ "2 3 4 4 5 6 7 8 9 10/n"
						+ "2 3 3 4 5 6 7 8 9 10/n"
						+ "2 2 3 4 5 6 7 8 9 10/n"
						+ "1 2 3 4 5 6 7 8 9 10";
		//Assert.assertEquals(expected, 
				//System.setOut(System.out.println(""+InsertionSort1.insertionSort(initInsertionSort1(10, "2 3 4 5 6 7 8 9 10 1")))));
		
	}

}
