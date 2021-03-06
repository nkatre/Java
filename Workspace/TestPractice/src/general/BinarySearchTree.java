package general;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

public class BinarySearchTree implements Cloneable{
	
	Node root;   // Comment : Using private/public/protected and dont use default unless necessary. 
					//Make sure Encapsulation is correct
	
	boolean allowDuplicateValues = false;  // Same here
	
	public BinarySearchTree() {
		super();
	}
	
	// Comment: Since Node is internal to this class, why is this passed in Constructor
	// THe Constructor should just take in data and manage nodes internally
	public BinarySearchTree(Node root) {
		super();
		this.root = root;
	}
	
	public BinarySearchTree(boolean allowDuplicateValues) {
		super();
		this.allowDuplicateValues = allowDuplicateValues;
	}
	
	public BinarySearchTree(Node root, boolean allowDuplicateValues) {
		super();
		this.root = root;
		this.allowDuplicateValues = allowDuplicateValues;
	}

	// Commnet : Shouldnt this be static ? Should it be public? 
	public class Node {
		
		private Integer data;
		private Node leftChild;
		private Node rightChild;
		
		private Node parent;
		
		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public Integer getData() {
			return data;
		}

		public void setData(Integer data) {
			this.data = data;
		}

		public Node getLeftChild() {
			return leftChild;
		}

		public void setLeftChild(Node leftChild) {
			this.leftChild = leftChild;
		}

		public Node getRightChild() {
			return rightChild;
		}

		public void setRightChild(Node rightChild) {
			this.rightChild = rightChild;
		}
		
		Node (Integer data) {
			
			this.data = data;
		}

		@Override
		public String toString() {
			return "Node [data=" + data + ", leftChild=" + leftChild
					+ ", rightChild=" + rightChild + "]";
		}
	}
	
	
	public boolean insertNode(Integer data) {
		
		Node node = new Node(data);
		
		if(null == root) {
			root = node; 
			return true;
		} else {
			
			if(!allowDuplicateValues && hasValue(data)){
				System.out.println("Value already exists");
				return false;
			}
			
			Node current = root;
			
			while(true) {
				int cValue = current.getData().compareTo(data);
			    if(cValue > 0) {
			    	if(null == current.getLeftChild()) {
			    		current.setLeftChild(node);
			    		node.setParent(current);
			    		return true;
			    	} 

			    	current = current.getLeftChild();
					
				} else {
					if(null == current.getRightChild()) {
			    		current.setRightChild(node);
			    		node.setParent(current);
			    		return true;
			    	} else {
			    		current = current.getRightChild();
			    	}
					
				}
			}
			
		}

	}
	
	//VB: implement this
	public Integer getLeastCommonAncestor(Integer data1, Integer data2)
	{
		return null;
	}
	
	
	/**
	 * 
	 * Comment:  To serialize a BST, just an preOrderTraversal is enough as you can construct the
	 *            BST from the preorder traversal.
	 *           For Regular Binary Tree though , you need one of the following pairs
	 *              1. Preorder and Inorder traversal
	 *              2. PostOrder and Inorder traversal
	 */ 
	public static BinarySearchTree deserializeBinarySearchTree(List<Integer> preOrderTraversal)
	{
		// VB : Implement this
		return null;
	}
	
	/**
	 * 
	 * Comment:  To serialize a BST, just an preOrderTraversal is enough as you can construct the
	 *            BST from the preorder traversal.
	 *           For Regular Binary Tree though , you need one of the following pairs
	 *              1. Preorder and Inorder traversal
	 *              2. PostOrder and Inorder traversal
	 */ 
	public static List<Integer> serializeBinarySearchTree(BinarySearchTree searchTree)
	{
		//VB : Implement this
		return null;
	}
	
	
	// Comment : It would be better to have parent pointer in the node
	public Node getParent(Integer data) {
		
		if(!hasValue(data))
			return null;
		
		//Node node = new Node(data);
		
		if (null == this.root) {
			return null;
		}
		
		if(data == root.getData())
			return null;
		
		Node currentNode = root;
		Node parent = null;
		
		while(true) {
			// Comment : Null check needed on data. Otherwise, you might get NullPointerException
			int compareValue = currentNode.getData().compareTo(data);
			if(compareValue == 0) {
				return parent;
			}
			if(compareValue > 0) {
				parent = currentNode;
				currentNode = currentNode.getLeftChild();
			}
			if(compareValue < 0) {
				parent = currentNode;
				currentNode = currentNode.getRightChild();
			}	
		}	
	}
	
	public boolean hasValue(Integer value) {
		
		if (null == root)
			return false;
		
		Node current = root;
		
		while(true) {
			if (null == current) 
				return false;
			
			if(current.getData() == value) {
				return true;
			} else {
				if(value < current.getData()) {
					current = current.getLeftChild();
					continue;
				} else {
					current = current.getRightChild();
					continue;
				}
			}
		}
		
	}
	
	public boolean deleteNode(Integer data) {
		
		if (hasValue(data)) {
			
			Node parent = getParent(data);
			Node current;
			
			// Comment : Parent will be NULL if root is getting deleted. THis will cause NullPointerException
			if(parent.getLeftChild().getData() == data) { // it is left child
				current = parent.getLeftChild();
				if((null ==current.getLeftChild()) && (null == current.getRightChild())) {
					current = null;
					return true;
				}
				if((null !=current.getLeftChild()) && (null == current.getRightChild())) {
					current.getLeftChild().setParent(parent);
					parent.setLeftChild(current.getLeftChild());
					current = null;
					return true;
				}
				if((null !=current.getRightChild()) && (null == current.getLeftChild())) {
					current.getRightChild().setParent(parent);
					parent.setLeftChild(current.getRightChild());
					current = null;
					return true;
				}
				// if both sub trees are present
				
			} else { // it is a right child
				current = parent.getRightChild();
				if((null ==current.getLeftChild()) && (null == current.getRightChild())) {
					current = null;
					return true;
				}
				if((null !=current.getLeftChild()) && (null == current.getRightChild())) {
					current.getLeftChild().setParent(parent);
					parent.setRightChild(current.getLeftChild());
					current = null;
					return true;
				}
				if((null !=current.getRightChild()) && (null == current.getLeftChild())) {
					current.getRightChild().setParent(parent);
					parent.setRightChild(current.getRightChild());
					current = null;
					return true;
				}
				// if both sub trees are present
				Node nodeToSwap = getMaxValueLesserThanNode(current);
				if(null==nodeToSwap) {
					return false;
				}
				if(nodeToSwap == current) {
					current = null;
					return true;
				} else {
					current.setData(nodeToSwap.getData());
					nodeToSwap = null;
					return true;
				}
			}
			// 
			
		} else {
			System.out.println("Tree does not have the value");
			return false;
		}
		
		return false;
	}
	
	/**
	 * @param node the node for which the level is needed
	 * @return the level of the node (level of root is 0)
	 */
	public int getLevel(Integer data) {
		
		// Comment : This is traversing twice. Inefficient. Please change the implementation to be efficient.
		if(!hasValue(data)) {
			return -1;
		}
		
		if(null==data)
			return -1;
		
		if(null==root)
			return -1;
		
		if(root.getData()==data)
			return 0;
		
		int level = 0;
		Node current = root;
		
		while(null != current) {
			int compareValue = data.compareTo(current.getData());
			
			if(compareValue == 0) {
				return level;
			} else if (compareValue < 0) {
				current = current.getLeftChild();
				
			} else {
				current = current.getRightChild();
			}
			level++;
		}
		
		Assert.assertFalse(true); // should not come here. CHECK the logic

		return level; 
	}
	
	public int getSmallestValue() {
		
		if (null==root)
			return -1;
		
		if(null==root.getLeftChild())
			return root.getData();
		
		Node node = root;
		while(null!=node.getLeftChild()) {
			node = node.getLeftChild();
		}
			
		return node.getData();
	}
	
	public int getLargestValue() {
		
		if (null==root)
			return -1;
		
		if(null==root.getRightChild())
			return root.getData();
		
		Node node = root;
		while(null!=node.getRightChild()) {
			node = node.getRightChild();
		}
			
		return node.getData();
	}
	
	public boolean isLeaf(Node node) {
		if (null==node)
			return false;
		
		if((null == node.getLeftChild()) && (null == node.getRightChild()))
			return true;
		
		return false;
	}
	
	// Comment : Can you print in the level ordered fashion. THat is the original question
	public List<Integer> levelOrderTraversal() {
		if (root == null)
			return null;
		
		if((root.getLeftChild() == null) && (root.getRightChild() == null)) {
			//System.out.println(root.getData());
			return null;
		}
	
		Node current = root;
		List<Integer> list = new LinkedList<Integer>(); 
		List<Node> queue = new LinkedList<Node>(); 
		// breadth first
		queue.add(root);
		
		while (queue.size() !=0) {
		    current = queue.get(0);
		    list.add(current.getData());
			if(null != current.getLeftChild()) {
				queue.add(current.getLeftChild());
				//list.add(current.getLeftChild().getData());
			}
			if(null != current.getRightChild()) {
				queue.add(current.getRightChild());
				//list.add(current.getRightChild().getData());
			}
			queue.remove(0);
		}
		
		return list;
	}

	public List<Integer> inOrderTravesal (Node node) {

		List<Integer> list = new LinkedList<Integer>();
		if(null==root)
			return null;
		
		if((null==root.getLeftChild()) && (null==root.getRightChild())) {
			list.add(root.getData());
			return list;
		}
		
		Node current = node;
		
		// Comment: The below if-else is unnecessarily complex. The last part ("else") alone is needed. 
		//You can do the null check there
		// Also, the other variant is you can pass the list object to recursive calls to avoid addAll
		// Usually, they will ask you to print the traversal. You would need to carefully listen to the question 
		//base case
		if(isLeaf(current)) {
			list.add(current.data);
			return list;
		} else if (null == current.getRightChild()) {
			list.addAll(inOrderTravesal(current.getLeftChild()));
			return list;
		} else if(null == current.getLeftChild()) {
			list.add(current.getData());
			list.addAll(inOrderTravesal(current.getRightChild()));
			return list;
		} else {
			list.addAll(inOrderTravesal(current.getLeftChild()));
			list.add(current.getData());
			list.addAll(inOrderTravesal(current.getRightChild()));
			return list;
		}

	}

	
	public List<Integer> preOrderTravesal (Node node) {

		List<Integer> list = new LinkedList<Integer>();
		if(null==root)
			return null; 
		
		if((null==root.getLeftChild()) && (null==root.getRightChild())) {
			list.add(root.getData());
			return list;
		}
		
		Node current = node;
		
		// Comment: The below if-else is unnecessarily complex. The last part ("else") alone is needed. You can do the null check there
        // Also, the other variant is you can pass the list object to recursive calls to avoid addAll
		// Usually, they will ask you to print the traversal. You would need to carefully listen to the question 
		//base case
		if(isLeaf(current)) {
			list.add(current.data);

			return list;
		} else if (null == current.getRightChild()) {
			list.add(current.getData());
			list.addAll(preOrderTravesal(current.getLeftChild()));

			return list;
		} else if(null == current.getLeftChild()) {
			list.add(current.getData());
			list.addAll(preOrderTravesal(current.getRightChild()));

			return list;
		} else {
			list.add(current.getData());
			list.addAll(preOrderTravesal(current.getLeftChild()));
			list.addAll(preOrderTravesal(current.getRightChild()));

			return list;
		}
		
	}
	
	
	public List<Integer> postOrderTravesal (Node node) {

		List<Integer> list = new LinkedList<Integer>();
		if(null==root)
			return null;
		
		if((null==root.getLeftChild()) && (null==root.getRightChild())) {
			list.add(root.getData());
			return list;
		}
		
		Node current = node;
		
		// Comment: The below if-else is unnecessarily complex. The last part ("else") alone is needed. You can do the null check there
	    // Also, the other variant is you can pass the list object to recursive calls to avoid addAll
		// Usually, they will ask you to print the traversal. You would need to carefully listen to the question 
		//base case
		if(isLeaf(current)) {
			list.add(current.data);
			return list;
		} else if (null == current.getRightChild()) {
			list.addAll(postOrderTravesal(current.getLeftChild()));
			list.add(current.getData());
			return list;
		} else if(null == current.getLeftChild()) {
			list.addAll(postOrderTravesal(current.getRightChild()));
			list.add(current.getData());
			return list;
		} else {
			
			list.addAll(postOrderTravesal(current.getLeftChild()));
			list.addAll(postOrderTravesal(current.getRightChild()));
			list.add(current.getData());
			
			return list;
		}
		
	}
	
	@Override
	// Comment : You can call inorder/preorder/postorder and print it
	public String toString() {
		return "BinaryTree [root=" + root + "]";
	}

	
	private Node getMaxValueLesserThanNode(Node root) {
		
		if(null==root)
			return null;
		
		if(null==root.getLeftChild()) {
			return root;
		} else {
			Node cNode = root.getLeftChild();
			// get the largest number in its left subtree
			while(null != cNode.getRightChild()) {
				cNode = cNode.getRightChild();
			}
			return cNode;
		}
		
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		BinarySearchTree binaryTree = new BinarySearchTree(true);
	    //BinaryTree.Node n = new BinaryTree.Node(5);
		binaryTree.insertNode(5);
		binaryTree.insertNode(10);
		binaryTree.insertNode(20);
		binaryTree.insertNode(1);
		binaryTree.insertNode(2);
		//binaryTree.insertNode(10);
		
		//System.out.println(binaryTree.hasValue(25));
		System.out.println("1: " + binaryTree);
		System.out.println("2: " + binaryTree.levelOrderTraversal()); 
		System.out.println("3: " + binaryTree.inOrderTravesal(binaryTree.root)); 
		System.out.println("4: " + binaryTree.preOrderTravesal(binaryTree.root)); 
		System.out.println("5: " + binaryTree.postOrderTravesal(binaryTree.root)); 
		
	}

}
