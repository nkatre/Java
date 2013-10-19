package hackerrank;
import java.util.*;

public class InsertionSort2 {

	public static void insertionSort(int[] ar){
		
		for (int y=1; y<ar.length; y++) {
			
			int[] newIntArray = new int[y+1];
			
			for(int i=0; i<= y; i++) {
				newIntArray[i] = ar[i];
			}
			
			newIntArray = iSort(newIntArray);
			
			for(int i=0; i<= y; i++) {
				ar[i] = newIntArray[i];
			}
			
			printArray(ar);
			
		}
		
		//printArray(ar);
		
	}
	
	private static int[] iSort(int[] ar){
		
		//System.out.println("ar.length: " + ar.length);
		
		if (null == ar) {return null;}
		
		int num = ar[ar.length-1];
		boolean flag = false;
		boolean isSmallest = false;

		for(int i=(ar.length-2); i >= 0; i--) {

			if(num < ar[i]) {
				ar[i+1] = ar[i];
				
				if(i==0) {
					isSmallest = true;
				}
			} else {
				if((i+1) < ar.length) {
					ar[i+1] = num;
					flag = true;
				}
			}
			
			if(flag)  {
				break;
			}

		}     
		
		if(isSmallest) {
			ar[0] = num;
			//printArray(ar);
		}
		
		return ar;
	}

	/* Tail starts here */
	static void printArray(int[] ar) {
		for(int n: ar){
			System.out.print(n+" ");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int[] ar = new int[n];
		for(int i=0;i<n;i++){
			ar[i]=in.nextInt(); 
		}
		insertionSort(ar);
	}    
}