package general;

import java.util.Iterator;

public class DoublyLinkedList<T> implements Iterable<T>{
	
	Node head;
	Node tail;
	
	public boolean insert(T data) {
		
		if(null==head) {
			head = tail = new Node(data);
			return true;
		}
		
		Node newNode = new Node(data);
		
		newNode.previous = tail;
		tail.next = newNode;
		tail = newNode;
		
		return true;
		
	}
	
	public boolean insert(int index, T data) {
		
		if((index > size()) || (index < 0)) {
			return false;
		}
		Node n = new Node(data);
		
		if (index == 0) {
			if(null==head) {
				head = tail =n;
				return true;
			}
			head.previous = n;
			n.next = head;
			head = n;
			return true;
		} 
		if(index == size()) {
			tail.next = n;
			n.previous = tail;
			tail = n;
			return true;
		}
		
		Node pNode = head;
		for (int i=0; i<index; i++) {
			pNode = pNode.next;
		}
		pNode = pNode.previous;
		pNode.next.previous = n;
		n.next = pNode.next;
		n.previous = pNode;
		pNode.next = n;
		
		return true;
	}
	
	public boolean removeNode(Node node) {
		
		if(null==node) {
			return false;
		}
		
		Node previousNode = node.previous;
		Node nextNode = node.next;
		
		if(null == previousNode) {
			if(null == nextNode) {
				tail = head = null;
				node = null;
				return true;
			}
			nextNode.previous = null;
			head = nextNode;
			
			node = null;
			return true;
		}
		
		if(null == nextNode) {
			previousNode.next = null;
			tail = previousNode;
			node = null;
			return true;
		}
		
		return false;
	}
	
	public boolean removeNode(T data) {
		if(null== head)
			return false;
		
		if(head.data.equals(data)) {
			Node n = head;
			head = head.next;
			head.previous = null;
			n = null;
			return true;
		}
		
		if(tail.data.equals(data)) {
			Node n = tail;
			tail = tail.previous;
			tail.next = null;
			n = null;
			return true;
		}
		
		Node n = head;
		while(null != n) {
			if(n.data.equals(data)) {
				n.previous.next = n.next;
				n.next.previous = n.previous;
				n = null;
				return true;
			}
			n = n.next;
		}
		
		return false;
	}
	

	public boolean remove(int position) {
		
		if ((position < 0) || (position >= size())) 
			return false;
		
		if(null==head)
			return false;
	
		if(position==0) {
			if(null == head.next) {
				head = tail = null;
				return true;
			}
				
			Node n = head;
			n = null;
			head = head.next;
			head.previous = null;
			return true;
		}
		if(position == (size()-1)) {
			Node n = tail;
			tail.previous.next = null;
			tail = tail.previous;
			n = null;
			return true;
		}
		
		Node n = head;
		for(int i=0; i<position; i++) {
			n = n.next;
		}
		n = n.previous;
		
		n.previous.next = n.next;
		n.next.previous = n.previous;
		n = null;
		return true;
	}
	
	
	public T getValue(int index) {
		if(null == head)
			return null;
		
		if(index==0) {
			return head.data;
		}
		
		int size = size();
		
		if(index == (size-1)) {
			return tail.data;
		}
		
		if((index<0) || (index>=size)) {
			return null;
		}
		
		Node n = head;
		for(int i=0; i<index; i++) {
			n = n.next;
		}
		//n = n.previous;
		return n.data;
	}
	
	public Node getNode(int index) {
		if(null == head)
			return null;
		
		if(index==0) {
			return head;
		}
		
		int size = size();
		
		if(index == (size-1)) {
			return tail;
		}
		
		if((index<0) || (index>=size)) {
			return null;
		}
		
		Node n = head;
		for(int i=0; i<index; i++) {
			n = n.next;
		}
		//n = n.previous;
		return n;
	}
	
	public int getIndex(T data) {
		
		
		if (null == head) {
			return -1;
		}
		
		int i = 0;
		Node n = head;
		while (null != n) {
			if(n.data == data) {
				return i;
			}
			i++;
			n = n.next;
		}
		
		return i;
	}
	
	public int size() {
		Node n = head;
		int size = 0;
		while(null != n) {
			size++;
			n=n.next;
		}
		return size;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new DoublyLinkedListIterator(this);
	}

	@Override
	public String toString() {
		
		if(null == head) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		Node n = head;
		//sb.append(n.data + " ");
		
		while(true) {
			sb.append(n.data + " ");
			n = n.next;
			if(null==n) {
				break;
			}
		}
		
		return sb.toString();
	}
	
	public 
	
	class Node {
		T data;
		Node next;
		Node previous;
		
		public Node(T data) {
			super();
			this.data = data;
		}
		
		public Node(T data, Node next, Node previous) {
			super();
			this.data = data;
			this.next = next;
			this.previous = previous;
		}
		
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
		public Node getNext() {
			return next;
		}
		public void setNext(Node next) {
			this.next = next;
		}
		public Node getPrevious() {
			return previous;
		}
		public void setPrevious(Node previous) {
			this.previous = previous;
		}
		
		@Override
		public String toString() {
			return "Node [data=" + data + "]";
		}
		
	}
	
	public class DoublyLinkedListIterator implements Iterator {
		
		DoublyLinkedList<T> list;
	    
		Node pointer;
		
		DoublyLinkedListIterator() {
			super();
			pointer = head;
		}
		
		DoublyLinkedListIterator(DoublyLinkedList<T> list) {
			super();
			pointer = list.head;
		}
		
		public Node getPointer() {
			return pointer;
		}
	
		@Override
		public boolean hasNext() {
			
			if (null == pointer) {
				return false;
			}
			
			return true;
		}
	
		@Override
		public Node next() {
			
			if(hasNext()) {
				Node n = pointer;
				pointer = pointer.next;
				return n;
			}	
			
			return null;
		}
	
		@Override
		public void remove() {
			
			removeNode(pointer);
			pointer = pointer.next;
			
		}

		@Override
		public String toString() {
			return "DoublyLinkedListIterator [pointer=" + pointer + "]";
		}
		
	}

	public static void main(String[] args) {
		DoublyLinkedList<String> list = new DoublyLinkedList<String>();
		list.insert("One");
		list.insert("Two");
		list.insert("Three");
		list.insert("Four");
		list.insert("Five");
		System.out.println(list);

		Iterator iterator =  list.iterator();

		while(iterator.hasNext()) {
			iterator.remove();
		}
		
		System.out.println(list);
	}
	
}
