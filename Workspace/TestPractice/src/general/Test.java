package general;

public class Test {
	
	Integer[] list;
	
	Test(Integer[] l ) {
		list = l;
	}
	
	public class PalindromeTest {
	    public static void main(String[] args) {
	        String value = "mom"; // moom, NOT mommmy
	        System.out.println( isPalindrome( value ) );
	    }
	    
	    public static boolean isPalindrome( String value ) {
	        
	        if((null== value) || (value.isEmpty()))
	            return false;
	        
	        char[] c = value.toCharArray();
	        
	        int start = 0;
	        int end = c.length-1;
	        
	        boolean flag = true;
	        
	        while(start<=end) {
	            if(c[start]== c[end]) {
	                start++;
	                end--;
	            } else {
	                flag = false;
	                return flag;
	            }
	        }
	        return flag;
	    }
	    
	    public static boolean isPalindrome1( String value ) {
	        
	        if((null== value) || (value.isEmpty()))
	            return false;
	        
	        char[] c = value.toCharArray();
	        char[] d = new char[c.length];
	        
	        int x = 0;
	        
	        for(int i=(c.length-1); i >=0; i--) {
	            d[x] = c[i];
	            x++;
	        }
	        
	        boolean isPa = true;
	        for(int j=0; j<c.lenght; j++) {
	        
	            if(c[j] != d[j])
	                return false;
	        }
	        
	        return isPa;
	    
	    }
	}

	import java.util.List;
	import java.util.LinkedList;

	public class ReverseTest {
	    public static void main(String[] args) {
	        int value = 1234567;
	        System.out.println( reverse( value ) );
	    }
	    
	     public static int reverse(int value) {
	     
	         int x;
	         int y;
	         int sum = 0;
	         int v = value;
	         
	         while(v >=0) {
	             x = v/10;
	             y = v%10;
	             sum = (sum*10) + y;
	             v = x;
	         }
	         
	         return sum;
	    
	    }
	    
	    
	    public static int reverse1(int value) {
	        List<Integer> list = new LinkedList<Integer>();
	        
	        int x = 0;
	        int y = 0;
	        
	        while(y >= 0) { 
	            x = value%10;
	            y = value/10;
	            
	            list.add(0, x);
	            value = y;
	        }
	        
	        StringBuilder sb = new StringBuilder();
	        for(Integer in : list) {
	            s.append(in);
	        }
	        
	        return Integer.parseInt(sb.toString());
	    
	    }
	    
	}
	/*
	 * quicksort
	 */
	public void sort(int start, int end) {
		
		if(null== list)
			return;
		
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
			if(i<=j) {
				int temp = list[i] ; 
				list[i] = list[j];
				list[j] = temp;
				i++;
				j--;
			}
 		}
		if(start < j) {
			sort(start, j);
		}
		if (end > i) {
			sort (i, end);
		}
	}

	
	public int search(int n) {
		if(null==list) {
			return -1;
		}
		int start = 0;
		int end = list.length-1;
		int mid;
		
		while (start <=end) {
			mid = start + (end-start)/2;
			if(list[mid]==n)
				return mid;
			else if (list[mid] > n){
				end = mid -1;
			} else {
				start = mid+1;
			}
		}
		
		return -1;
	}

	public static void main(String[] args) {
		
		Integer[] a = {6,5,4,3,7,2,1};
		Test t = new Test(a);
		
		t.sort(0, a.length-1);
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0 ; i<a.length; i++) {
			sb.append(a[i] + " ");
		}
		System.out.println(sb.toString());
		
		System.out.println(t.search(1));
		System.out.println(t.search(2));
		System.out.println(t.search(3));
		System.out.println(t.search(4));
		System.out.println(t.search(5));
		System.out.println(t.search(6));
		System.out.println(t.search(7));
		System.out.println(t.search(8));
	}
	
}
