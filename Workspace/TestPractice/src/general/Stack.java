package general;

import java.util.LinkedList;
import java.util.List;

interface StackInterface<T> {

	public boolean push(T data);
	
	public T peek();
	
	public T pop();
	
	public int size();

}

public class Stack<T> implements StackInterface<T>{
	
private DoublyLinkedList<T> stack = new DoublyLinkedList<T>();
	
	private int capacity;
	
	Stack() {
		super();
		capacity = 5;
	}


	Stack(int capacity) {
		super();
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public boolean push(T data) {
		if(size() < capacity) {
			stack.insert(size(), data);
			return true;
		}
		return false;
	}

	@Override
	public T peek() {
		return stack.getValue(size()-1);
	}

	@Override
	public T pop() {
		T data = stack.getValue(size()-1);
		stack.remove(size()-1);
		return data;
	}

	@Override
	public int size() {
		if(null == stack)
			return 0;
		return stack.size();
	}
	
	public boolean isEmpty() {
		if(null == stack)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=(stack.size()-1); i>=0; i--) {
			sb.append(stack.getValue(i) + " ");
		}
		
		return sb.toString();
	}
	
	
	public static void main (String[] args) {
		Stack<String> stack = new Stack<String>(3);
		stack.push("1");
		stack.push("2");
		stack.push("3");
		System.out.println(stack.peek());
		System.out.println(stack.pop());
		System.out.println(stack);
	}

}
