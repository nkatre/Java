package general;

public class QuickSort {
	
	Integer[] list;
	
	QuickSort(Integer[] list) {
		this.list = list;
	}
	
	public void sort() {
		quickSort(0, list.length-1);
	}
	
private Integer[] quickSort(int start, int end) {
		
		if(null==list)
			return null;
		
		int i = start;
		int j = end;
		
		int pivot = list[start + (end-start)/2];
		
		while(i<=j) {
			
			while(list[i] < pivot) {
				i++;
			}
			
			while(list[j] > pivot) {
				j--;
			}
			
			if(i<=j){
				int temp = list[i];
				list[i] = list[j];
				list[j] = temp;
				i++;
				j--;
			}
		}
		
		if(start<j) {
			quickSort(start, j);
		} 
		if(end>i) {
			quickSort(i, end);
		}
		
		return list;
	}
	
	
	public static void main(String[] args) {
		Integer[] l = {555,6,123,21,9};
		QuickSort b = new QuickSort(l);
		b.sort();
		
		for (int i=0; i<b.list.length; i++) {
			System.out.print(" " + b.list[i]);
		}
		System.out.println();
	}

}
