package general;

import java.util.Random;

public class MergeSort<T> implements Comparable {

	T[] array;
	T[] helper;

	MergeSort(T[] data) {
		this.array = data;
		
		if(array.length!=0)
		 this.helper = (T[]) new Object[array.length];
	}

	public void sort() {
		mergeSort(0, (array.length-1));
	}

	private void mergeSort(int start, int end) {

		if(start < end) {
			int mid = (start + (end - start))/2;
			mergeSort(start, mid);
			mergeSort(mid+1, end);
			merge(start, mid, end);
		}
	}

	private void merge(int start, int mid, int end) {
		
		// copy the numbers in the range to the helper array
		for(int i=start; i<=end; i++) {
			helper[i] = array[i];
		}
		
		int i = start;
		int j = mid+1;
		int k = start;
		
		while(i<=mid && j<=end) {
			int c = ((Comparable) helper[i]).compareTo(helper[j]);
			if(c <= 0) {
				array[k] = helper[i];
				i++;
			} else {
				array[k] = helper[j];
				j++;
			}
			k++;
		}
		
		while (i<= mid) {
			array[i] = helper[i];
			k++;
			i++;
		}

		//VB : Where is the  case for while (j <= end) ?
	}

	@Override
	public int compareTo(Object obj) {
		return this.compareTo((T) obj);		
	}

	@Override
	public String toString(){

		if (null == array) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for(int i= 0; i< array.length; i++) {
			sb.append(array[i]+ " ");
		}

		return sb.toString();
	}

	private String arrayToString(T[] a){

		if (null == a) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for(int i= 0; i< a.length; i++) {
			sb.append(a[i]+ " ");
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		Integer[] array = {10, 8, 7, 4, 5, 6, 3, 2, 1, 0};
		MergeSort<Integer> mSort = new MergeSort<Integer>(array);
		mSort.sort();
		System.out.println(mSort);
	}

}
