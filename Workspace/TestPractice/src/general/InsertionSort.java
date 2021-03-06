package general;

public class InsertionSort<T> implements Comparable{
	
	T[] array;
	
	InsertionSort(T[] array) {
		super();
		this.array = array;
	}
	
	public T[] sort() {
		
		if(this.array == null)
			return null;
		
		int arraySize = this.array.length;
		
		for(int i = 1; i<arraySize; i++) {
			
			T key = array[i];
			int position  = -1;
			
			for(int j = (i-1); j >=0; j--) {
				if(((Comparable) key).compareTo(array[j]) < 0) {
					//VB : Why cant you also move here. This would avoid double iteration
					position = j;
				}
			}

			if(position !=-1) {
				for(int z=i; z>position; z--) {
					array[z]=array[z-1];
				}
				array[position]=key;
			}
		}
		return array;
	}
	
	@Override
	public int compareTo(Object object) {
		T newObject = (T) object;
		return this.compareTo(newObject);
	}

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
	
	public static void main(String[] args) {
		Double[] array = {9.84, 12.0, 1.234};
		InsertionSort iSort = new InsertionSort<Double>(array); //VB: Should be  InsertionSort<Double> iSort
		iSort.sort();
		System.out.println(iSort);
	}

}
