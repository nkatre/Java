package Amazon;

import java.util.List;
import java.util.LinkedList;

public class MergeTwoSortedList {


	public List<Integer> merge(List<Integer> list1, List<Integer> list2) {

		if((list1 == null) & (list2 == null)) {
			return null;
		}

		if(list1 == null) {
			return list2;
		}

		if(list2 == null) {
			return list1;
		}


		int pointerList1 = 0;
		int pointerList2 = 0; 

		int totalsize = list1.size() + list2.size();

		List<Integer> list = new LinkedList<Integer>();

		for(int i = 0; i < totalsize; i++) {

			if ((pointerList1 < list1.size()) && (pointerList2 < list2.size()) ) {
				if (list1.get(pointerList1) <=  list2.get(pointerList2)) {
					list.add(list1.get(pointerList1));
					pointerList1++;
				} else {
					list.add(list2.get(pointerList2));
					pointerList2++;
				}
			} else {
				if (pointerList1 < list1.size()) {
					while (true) {
						
						if (pointerList1 >= list1.size()) {
							break;
						}
						
						list.add(list1.get(pointerList1));
						
						pointerList1++;
					}
					break;
				} else {
					while (true) {
						
						if (pointerList2 >= list2.size()) {
							break;
						}
						
						list.add(list2.get(pointerList2));
						
						pointerList2++;
					}
					break;
				}
			}

		}

		return list;

	}
	
	
	public static void main(String[] args) {
		
		
		MergeTwoSortedList m = new MergeTwoSortedList();
		
		List<Integer> list1 = new LinkedList<Integer>();
		list1.add(1);
		list1.add(3);
		list1.add(4);
		list1.add(5);
		list1.add(7);
		
		List<Integer> list2 = new LinkedList<Integer>();
		list2.add(2);
		list2.add(4);
		list2.add(5);
		list2.add(6);
		list2.add(8);
		list2.add(9);
		list2.add(10);
		
		System.out.println(m.merge(list1, list2));
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}




	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}


	
	
	


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	

}
