package general;

import general.LinkedListCareerCup.LList.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LinkedListCareerCup {
	
	java.util.List<Integer> list = new java.util.LinkedList<Integer>();
	
	class LList {
		
		Node head;
		Node tail;
	
		class Node {
			Integer data;
			Node next;
			
			public String toString() {
				return "data: " + data ;
			}
		}
		
		public boolean insert(Integer i) {
			
			Node n = new Node();
			n.data = i;
			
			if(null==head) {
				head = tail = n;
				return true;
			}
			
			tail.next = n;
			tail = tail.next;
			return true;		
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			Node n = head;
			while(n!=null) {
				sb.append(n.data + " ");
				n = n.next;
			}
			return sb.toString();
		}
	}

	/*
	 * Write code to remove duplicates from an unsorted linked list 
	 * FOLLOW UP
	 * How would you solve this problem if a temporary buffer is not allowed?
	 */
	public List<Integer> removeDuplicates(List<Integer> list) {
		if((null==list) || list.isEmpty())
			return list;
		
		Set<Integer> set = new HashSet<Integer>();
		
		Iterator<Integer> iterator = (Iterator<Integer>) list.iterator();
		
		while(iterator.hasNext()) {
			Integer integer = (Integer) iterator.next();
			//System.out.println("... integer " + integer);
			
			if(set.contains(integer)) {
				iterator.remove();
			} else {
				set.add(integer);
			}
		}
		
		//System.out.println(set.toString());
		return list;
	}
	
	public List<Integer> removeDuplicates1 (List<Integer> list) {
		
		if((null==list) || list.isEmpty())
			return list;
		
		Iterator<Integer> iterator = (Iterator<Integer>) list.iterator();
		
		while(iterator.hasNext()) {
			
			Integer integer = (Integer) iterator.next();
			Iterator<Integer> it2 = iterator;//(Iterator<Integer>) list.iterator();
			
			while(it2.hasNext()) {
				Integer i2 = it2.next();
				
				if(i2.equals(integer)) {
					iterator.remove();
					break;
				}
			}
			iterator.next();
		}
		return list;
	}
	
	/*
	 * Implement an algorithm to find the nth to last element of a singly linked list
	 */
	public Integer getNthLastElement(List<Integer> list, int n) {
		
		if((null==list) || (list.isEmpty()))
			return null;
		
		int size = list.size();
		
		if((n > size) || (n <0))
			return null;
		
		int x = 0; 
		
		Iterator<Integer> iter = list.iterator();
		
		while(iter.hasNext()) {
			Integer i = iter.next();
			if(x==(size-n)) {
				return i;
			}
			x++;
		}
		
		return null;
	}
	
	/*
	 * Implement an algorithm to delete a node in the middle of a single linked list, given only access to that node 
	 * EXAMPLE
	 * 		Input: the node ÔcÕ from the linked list a->b->c->d->e
	 * 		Result: nothing is returned, but the new linked list looks like a->b->d->e
	 */
	public void deleteNode(LList list, LList.Node c) {
		
		if((null==list) || (null==c))
			return;
		
		LList.Node n = list.head;
		
		if(n == c) {
			System.out.println("1:");
			list.head = list.head.next;
			return;
		}
		
		LList.Node nP = list.head;
		n = n.next;
		
		while (null != n) {
			System.out.println("2:");
			if(n.data == c.data) {
				System.out.println("3:");
				nP.next = c.next;
				c.data = null;
				c.next = null;
				return;
			}
			nP = n;
			n = n.next;
		}
		
	}
	
	
	/*
	 * You have two numbers represented by a linked list, where each node contains a single digit. 
	 * The digits are stored in reverse order, such that the 1Õs digit is at the head of the list
	 *  Write a function that adds the two numbers and returns the sum as a linked list 
	 *  EXAMPLE 
	 *  Input: (3 -> 1 -> 5) + (5 -> 9 -> 2)
	 *  Output: 8 -> 0 -> 8
	 */
	public List<Integer> addLists(List<Integer> list1, List<Integer> list2) {
		if((null==list1) || (list1.isEmpty()))
			return list2;
		
		if((null==list2)|| (list1.isEmpty()))
			return list1;
		
		List<Integer> list = new LinkedList<Integer>();
		int carry = 0;
		Iterator<Integer> i1 = list1.iterator();
		Iterator<Integer> i2 = list2.iterator();
		
		while(i1.hasNext() || i2.hasNext()) {
			int sum = i1.next() + i2.next() + carry;
			
			carry = 0;
			if(sum>9) {
				carry = sum/10;
				sum = sum%10;
				//System.out.println("sum:"+sum + " carry:"+carry);
			}
			list.add(sum);
		}
		
		if(carry != 0) {
			list.add(carry);
		}
		
		return list;
	}
	
	/*
	 * Given a circular linked list, implement an algorithm which returns node at the beginning of the loop 
	 * DEFINITION 
	 * 		Circular linked list: A (corrupt) linked list in which a nodeÕs next pointer points to an earlier node, 
	 * 		so as to make a loop in the linked list 
	 * EXAMPLE : input: A -> B -> C -> D -> E -> C [the same C as earlier]
	 * output: C
	 */
	public LList.Node getLoopBegining(LList list) {
		
		if((null==list) || (null==list.head))
			return null;
		
		Set<LList.Node> set = new HashSet<LList.Node>();
		LList.Node n = list.head;
		
		while(null != n) {
			if(set.contains(n)) {
				return n;
			}
			set.add(n);
			n = n.next;
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		LinkedListCareerCup l = new LinkedListCareerCup ();
		/*
		java.util.LinkedList<Integer> list = new java.util.LinkedList<Integer>();
			list.add(5);
			list.add(25);
			list.add(5);
			list.add(35);
		
		System.out.println(l.getNthLastElement(list, 4));
		
		System.out.println("list:" + list.toString());
		System.out.println("list became:" + l.removeDuplicates(list));
		
		list = new LinkedList<Integer>();
			list.add(5);
			list.add(25);
			list.add(5);
			list.add(35);
			
		System.out.println("list:" + list.toString());
		System.out.println("list became:" + l.removeDuplicates1(list));
		
		// --------------------------
		LList ls = l.new LList();
		ls.insert(1);
		ls.insert(2);
		ls.insert(3);
		ls.insert(4);
		ls.insert(5);
		
		//System.out.println(ls);
		LList.Node n = ls.head.next.next.next.next.next;
		//System.out.println("@@@ " + n);
		l.deleteNode(ls, n);
		System.out.println(ls);
		// --------------------------

		LinkedList<Integer> list1 = new LinkedList<Integer>();
		list1.add(3);
		list1.add(1);
		list1.add(9);
		
		LinkedList<Integer> list2 = new LinkedList<Integer>();
		list2.add(5);
		list2.add(9);
		list2.add(2);
		
		System.out.println(list1);
		System.out.println(list2);
		System.out.println(l.addLists(list1, list2));
		// --------------------------
		*/
		
		LList ls = l.new LList();
		ls.insert(1);
		ls.insert(2);
		ls.insert(3);
		ls.insert(4);
		ls.insert(5);
		
		System.out.println(ls);
		
		ls.tail.next = ls.head.next.next.next;
		System.out.println(l.getLoopBegining(ls));
		//System.out.println(ls);
	}
	
}
