package general;

import java.util.LinkedList;
import java.util.List;

public class BubbleSort {
	
	Integer[] list;
	
	BubbleSort(Integer[] list) {
		this.list = list;
	}
	
	public void sort() {
		bubbleSort(list, list.length);
	}
	
	private Integer[] bubbleSort(Integer[] list, int n) {
		
		if(n==0) 
			return list;
		
		if(null==list)
			return null;
		
		int max = list[n-1];
		int pointer=-1;
		
		//for (int i=0; i<list.length; i++) {
			//System.out.print(" " + list[i]);
		//}
		//System.out.println();
		
		for(int i=(n-2); i>=0;i--) {		
			if(list[i] > max) {
				max = list[i];
				pointer = i;
			}
		}
		//System.out.println("n: " + n + " pointer: " + pointer);
		
		if(pointer != -1) {
			list[pointer] = list[n-1];
			list[n-1] = max;
		}
		n = n-1;
		return bubbleSort(list, n);
	}
	
	//VB : Implement iterative solution for this
	
	public static void main(String[] args) {
		Integer[] l = {555,6,123,21,9};
		BubbleSort b = new BubbleSort(l);
		b.sort();
		
		for (int i=0; i<b.list.length; i++) {
			System.out.print(" " + b.list[i]);
		}
		System.out.println();
	}

}
