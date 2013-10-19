package general;

import general.SortingAndSearchingCareerCup.Person;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SortingAndSearchingCareerCup {
	
	/*
	 * You are given two sorted arrays, A and B, and A has a large enough buffer at the end 
		to hold B Write a method to merge B into A in sorted order
	 */
	
	public Integer[] merge(Integer[] a, Integer[] b) {
		
		int size = 0;
		
		for(int i=0; i<a.length; i++) {
			size++;
			if(a[i] == null) {
				break;
			}
		}
		
		for(int i=0; i<b.length; i++) {
			int p = -1;
			for(int j=0; j<size; j++) {
				
				if((null==a[j]) || (a[j] > b[i])) {
					p = j;
					break;
				}
			}
			
			//System.out.println("p:" + p);
			if(p!=-1) {
				for(int x = size; x>p; x--) {
					a[x] = a[x-1];
				}
				a[p] = b[i];
			} else {
				for(int j=0; j<a.length; j++) {
					if(a[j] == null) {
						a[j] = b[i];
						break;
					}
				}
			}
			size++;
		}
		
		return a;
	}
	
	/*
	 * Write a method to sort an array of strings so that all the anagrams are next to each other 
	 */
	public void sortStringPlacingAnagramsNearby(String[] slist) {
		
		ArrayAndStringCareerCup<String> a = new ArrayAndStringCareerCup<String>();
		
		List<String> list = new LinkedList<String>();
		
		for(int i=0; i<slist.length; i++){
			
			Iterator<String> it = list.iterator();
			int h = 0;
			boolean flag = false;
			
			while(it.hasNext()) {
				if(a.areAnagrams(it.next(), slist[i])) {
					flag = true;
					break;
				} 
				h++;
			}
			if(!flag) {
				list.add(slist[i]);
			} else {
				list.add(h, slist[i]);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for(String s : list) {
			sb.append(s + " ");
		}
		System.out.println(sb.toString());
	}
	
	/*
	 * Given a sorted array of n integers that has been rotated an unknown number of 
		times, give an O(log n) algorithm that finds an element in the array You may assume 
		that the array was originally sorted in increasing order 
		EXAMPLE:
		Input: find 5 in array (15 16 19 20 25 1 3 4 5 7 10 14)
		Output: 8 (the index of 5 in the array)
	 */
	public void getIndexInARotatedArray(int[] a, int n){
		
		if(null==a)
			return;
		
		int rC = 0;
		for(int i=1;i<a.length;i++){
			if(a[i] < a[i-1]) {
				rC = i;
				break;
			}
		}
		
		int pos = -1;
		
		int start = rC;
		int end = a.length + rC -1;
		
		int mid = -1;
		
		while(start != end) {
			if(start < 0) {
				start= 0;
			}
			if (start >= a.length) {
				start = start - a.length;
			}
			if(end < 0) {
				end = 0;
			}
			if (end >= a.length) {
				end = end - a.length;
			}
			if (end > start) {
				mid = (start + end + 1)/2 ;
			} else {
				mid = (start + end + 1)/2 + rC + 1;
			}
			
			if(mid >= a.length)
				mid = mid - a.length;
			
			
			if(a[mid] == n) {
				pos = mid;
				break;
			} else if (a[mid] > n) {
				end = mid-1;
			} else {
				start = mid + 1;
			}
		}
		
		if(pos==-1) {
			pos = start;
		}
		
		System.out.println("pos:" + pos);
	}
	
	/*
	 * Given a sorted array of strings which is interspersed with empty strings, 
	 * write a method to find the location of a given string 
	 * Example: find "ball" in ["at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""] will return 4 
	 * Example: find "ballcar" in ["at", "", "", "", "", "ball", "car", "", "", "dad", "", ""] will return -1
	 */
	public int findStringPosition(String[] stAr, String w){
		
		if (null==stAr)
			return -1;
		// method 1
		/*
		for(int i =0; i<stAr.length; i++) {
			if(stAr[i].equals(w)) {
				return i;
			}
		}
		return -1;
		*/
		
		int start = 0;
		int end = stAr.length-1;
		int mid;
		
		while(start<=end) {
			mid = start + (end-start)/2;
			
			if(stAr[mid].equals(w)) {
				return mid;
			} 
			else if (stAr[mid].equals("")) {
				int p = mid+1;
				int q = p;
				while(p<stAr.length) {
					
					if(stAr[p].equals(w)){
						return p;
					} 
					if(!stAr[p].equals("")){
						int c = stAr[p].compareTo(w);
						if(c > 0) {
							end = q;
						} else {
							start = p;
						}
					}
					p++;
				}
				end = mid;
			}
			else if (stAr[mid].compareTo(w) > 0) {
				start = mid+1;
			} 
			else {
				end = mid-1;
			}
		}
		
		return -1;
		
	}
	
	/*
	 * Given a matrix in which each row and each column is sorted, write a method to find an element in it 
	 */
	public String findElementInASortedMatrix(Integer[][] matrix, int vsize,  int hsize, int value) {
		
		// assuming it is a n*n matrix
		int start;
		int end;
		int top = 0;
		int bottom = vsize-1;
		
		int midHor;
		int midVer;
		
		while(top<=bottom) {
			midVer = top + (bottom-top)/2;
			start = 0;
			end = hsize-1;
			
			while(start <= end) {				
				midHor	= start + (end-start)/2;

				if(value == matrix[midVer][midHor]) {
					return "value%:" + value + " at:" + midVer + "," + midHor;
				} else if (value > matrix[midVer][midHor]) {
					if(value >  matrix[midVer][hsize-1]) {
						top = midVer+1;

						break;
					} else {
						while (midHor < hsize) {
							if(value == matrix[midVer][midHor]) {
								return "value#:" + value + " at:" + midVer + "," + midHor;
							}
							midHor++;
						}
						return "value-:" + value +  " not present --";
					}
				} else {
					bottom = midVer;
					
					if(value <  matrix[midVer][0]) {
						
						bottom = midVer-1;
						break;
					} else {
						while (midHor >= 0) {
							if(value == matrix[midVer][midHor]) {
								return "value+:" + value + " at:" + midVer + "," + midHor;
							}
							midHor--;
						}
						return "value:" + value + " not present ++";
					}
				}
				
			}
		}
		
		return  value + " not present:::";
		
	}
	
	
	/*
	 * A circus is designing a tower routine consisting of people standing atop one anotherŐs shoulders 
	 * For practical and aesthetic reasons, each person must be both shorter and lighter than the person below him or her 
	 * Given the heights and weights of each person in the circus, 
	 * write a method to compute the largest possible number of people in such a tower 
	 * EXAMPLE:
	 * Input (ht, wt): (65, 100) (70, 150) (56, 90) (75, 190) (60, 95) (68, 110)
	 * Output: The longest tower is length 6 and includes from top to bottom: 
	 * 			(56, 90) (60,95) (65,100) (68,110) (70,150) (75,190)
	 */
	// thinking of implementing radix sort
	class Person implements Comparable{
		int height;
		int weight;
		
		Person(int height, int weight) {
			this.height = height;
			this.weight = weight;
		}
		
		public int compareTo(Object o) {
			Person p = (Person) o;
			if(this.height == p.height) {
				return 0;
			}
			
			if(this.height > p.height) {
				return 1;
			}
			
			if(this.height < p.height) {
				return -1;
			}

			return (this.height-p.height);
		}
		
		public String toString() {
			return "h:" + this.height + " w:" + this.weight;
		}

	}
	
	public List<Person> sortPersonBySize(List<Person> persons) {
		
		if ((null==persons) || persons.isEmpty())
			return persons;
		
		// this list will contain person sorted by height
		List<Person> list = new LinkedList<Person>();
		
		for(Person p : persons) {
			Iterator<Person> it = list.iterator();
			int index = 0;
			while(it.hasNext()) {
				Person per = (Person) it.next();
				if(p.compareTo(per) <= 0) {
					break;
				}
				index++;
			}
			list.add(index, p);
		}
		// list has all person sorted by height
		
		// remove from the list person who are heavier than the predessor
		Person parent = null;
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		for(Person p: list) {
			if(null != parent) {
				if(p.weight < parent.weight) {
					sb.append(i + ",");
				}
			}
			parent = p;
			i++;
		}
		
		if(sb.length()!=0) {
			String[] s = sb.toString().split(",");
			for (int x=0; x<s.length; x++) {
				list.remove(Integer.parseInt(s[x]));
			}
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		SortingAndSearchingCareerCup s = new SortingAndSearchingCareerCup();
		/*
		Integer[] a = new Integer[10];
		a[0] = 0;
		a[1] = 2;
		
		Integer[] b = new Integer[3];
		b[0] = 1;
		b[1] = 3;
		b[2] = 5;
		
		Integer[] c = s.merge(a, b);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<c.length; i++) {
			sb.append(c[i] + " ");
		}
		
		System.out.println(sb.toString());

		String[] list = {"abc", "def", "fgh", "cba", "gfh"};
		s.sortStringPlacingAnagramsNearby(list);

		int[] a = {15,16,19,20,25,1,3,4,5,7,10,14};
		s.getIndexInARotatedArray(a, 15);
		s.getIndexInARotatedArray(a, 16);
		s.getIndexInARotatedArray(a, 19);
		s.getIndexInARotatedArray(a, 20);
		s.getIndexInARotatedArray(a, 25);
		s.getIndexInARotatedArray(a, 1);
		s.getIndexInARotatedArray(a, 3);
		s.getIndexInARotatedArray(a, 4);
		s.getIndexInARotatedArray(a, 5);
		s.getIndexInARotatedArray(a, 7);
		s.getIndexInARotatedArray(a, 10);
		s.getIndexInARotatedArray(a, 14);
		
		
		String[] stAr = {"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""};
		System.out.println(s.findStringPosition(stAr, "at"));
		System.out.println(s.findStringPosition(stAr, "ball"));
		System.out.println(s.findStringPosition(stAr, "car"));
		System.out.println(s.findStringPosition(stAr, "dad"));
		
		
		Integer[][] matrix = new Integer[3][5];
		int x = 1;
		for(int i=0; i<3; i++) {
			for(int j=0; j<5; j++) {
				matrix[i][j] = x;
				x++;
				System.out.print(matrix[i][j] + "   ");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println(s.findElementInASortedMatrix(matrix, 3, 5, 1));
		System.out.println(s.findElementInASortedMatrix(matrix, 3, 5, 5));	
		System.out.println(s.findElementInASortedMatrix(matrix, 3, 5,0));
		*/
		// (ht, wt): (65, 100) (70, 150) (56, 90) (75, 190) (60, 95) (68, 110)
		
		List<Person> list = new LinkedList<Person>();
		list.add(s.new Person(65,100));
		list.add(s.new Person(70,150));
		list.add(s.new Person(56,97));
		list.add(s.new Person(75,190));
		list.add(s.new Person(60,95));
		list.add(s.new Person(68,110));
		// [h:56 w:90, h:60 w:95, h:65 w:100, h:68 w:110, h:70 w:150, h:75 w:190]
		
		System.out.println(s.sortPersonBySize(list));
	}
	
	

}
