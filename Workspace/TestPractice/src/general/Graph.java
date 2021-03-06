package general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph {
	
	private Map<Node, LinkedList<Node>> nodes;
	private Set<Edge> edges;
	
	Node source;
	
	private int time = 0;
	
	Graph() {
		super();
		nodes = new HashMap<Node, LinkedList<Node>>();
		edges = new HashSet<Edge>();
	}
	
	public boolean addNode(Node n) {
		
		if(null == source) {
			source = n;
		}
		
		List<Node> adjSet  = new LinkedList<Node>();
		Set<Node> reachableFromSet = n.getReachableFromSet();
		Set<Node> connectedToSet = n.getConnectedToSet();
		//Set<Node> adjSet = new HashSet<Node>();
		adjSet.addAll(reachableFromSet);
		adjSet.addAll(connectedToSet);
		nodes.put(n, (LinkedList<Node>) adjSet);
		
		if (null != reachableFromSet) {
			for(Node x : reachableFromSet ) {
				edges.add(new Edge(x, n));
			}
		}
		
		if (null != connectedToSet) {
			for(Node x : connectedToSet ) {
				edges.add(new Edge(n, x));
			}
		}
		return true;
	}
	
	public void breadthFirstSearch() {
		
		for(Node n : nodes.keySet()) {
			if (n == source) {
				continue;
			}
			n.setColour(Node.color.WHITE);
			n.setParent(null);
			n.setDistance(Integer.MAX_VALUE);
		}
		
		source.setColour(Node.color.GREY);
		source.setDistance(0);
		source.setParent(null);
		
		Queue<Node> q = new LinkedList<Graph.Node>();
		q.add(source);
		
		while(!q.isEmpty()) {
			
			Node u = q.poll(); // dequeue source
			
			System.out.println(u);
			
			Set<Node> adjacencyList =new HashSet<Node>();
			adjacencyList.addAll(u.getConnectedToSet());
			adjacencyList.addAll(u.getReachableFromSet());
			
			if(null != adjacencyList) {
				for(Node v : adjacencyList) {
					if(v.getColour() == Node.color.WHITE) {
						v.setColour(Node.color.GREY);
						v.setDistance(u.getDistance() + 1);
						v.setParent(u);
						q.add(v); // enqueue in the q
					}
				}
				u.setColour(Node.color.BLACK);
			}
			
		}
		
	}
	
	public void depthFirstSearch() {
		
		if((null == nodes) && (nodes.isEmpty())) {
			return;
		}		
		for (Node n : nodes.keySet()) {
			n.setColour(Node.color.WHITE);
			n.parent = null;
		}
		
		for (Node n : nodes.keySet()) {
			if(n.getColour() == Node.color.WHITE) {
				depthFirstSearchVisit(n);
			}
		}
	}
	
	private void depthFirstSearchVisit(Node u) {
		time = time + 1;
		u.setDiscoveryTime(time);
		u.setColour(Node.color.GREY);
		
		Set<Node> adjacencyList =new HashSet<Node>();
		adjacencyList.addAll(u.getConnectedToSet());
		adjacencyList.addAll(u.getReachableFromSet());
		
		for (Node v : adjacencyList) {
			if(v.getColour() == Node.color.WHITE) {
				v.setParent(u);
				depthFirstSearchVisit(v);
			}
		}
		u.setColour(Node.color.BLACK);
		time = time + 1;
		u.setFinishTime(time);
	}
	
	public String toString() {
		return source.toString1() + "\n" + nodes +  "\n" +edges;
	}
	
	private static class Node<T> {
		
		static enum color {WHITE, GREY, BLACK};
		
		private T data;
		private color  colour;
		private int distance;
		private Node<T> parent;
		
		private int discoveryTime = Integer.MIN_VALUE;
		private int finishTime = Integer.MIN_VALUE;
		
		public int getDiscoveryTime() {
			return discoveryTime;
		}

		public void setDiscoveryTime(int discoveryTime) {
			this.discoveryTime = discoveryTime;
		}

		public int getFinishTime() {
			return finishTime;
		}

		public void setFinishTime(int finishTime) {
			this.finishTime = finishTime;
		}

		public void setParent(Node<T> parent) {
			this.parent = parent;
		}

		Set<Node> reachableFromSet = new HashSet<Graph.Node>();
		Set<Node> connectedToSet = new HashSet<Graph.Node>();
		
		Node(T data) {
			super();
			this.data = data;
			//reachableFromSet = new HashSet<Graph.Node>();
			//connectedToSet = new HashSet<Graph.Node>();
		}
		
		Node(T data, Set<Node> reachableFromSet, Set<Node> connectedToSet) {
			this(data);
			if(null != reachableFromSet) {
				this.reachableFromSet = reachableFromSet;
			}
			if(null != reachableFromSet) {
				this.connectedToSet = connectedToSet;
			}
		}
		
		public color getColour() {
			return colour;
		}

		public void setColour(color colour) {
			this.colour = colour;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public Node getParent() {
			return parent;
		}

		public String toString1() {
			return "[" + data + "]";
		}

		@Override
		public String toString() {
			return " [" + data + ", " + colour + ", " + discoveryTime
					+ ", " + finishTime + "]";
		}

		public boolean addReachableFromNode(Node n) {
			return reachableFromSet.add(n);
		}
		
		public boolean addConnectedToNode(Node n) {
			return connectedToSet.add(n);
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public Set<Node> getReachableFromSet() {
			return reachableFromSet;
		}

		public void setReachableFromSet(Set<Node> reachableFromSet) {
			this.reachableFromSet = reachableFromSet;
		}

		public Set<Node> getConnectedToSet() {
			return connectedToSet;
		}

		public void setConnectedToSet(Set<Node> connectedToSet) {
			this.connectedToSet = connectedToSet;
		}
		
		
	}
	
	private static class Edge {
		Node fromNode;
		Node toNode;
		Map <String, Object> edgeProperties;
		
		Edge (Node fromNode, Node toNode) {
			super();
			this.fromNode = fromNode;
			this.toNode = toNode;
			edgeProperties = new HashMap<String, Object>();
		}
		
		Edge (Node fromNode, Node toNode, Map <String, Object> edgeProperties) {
			super();
			this.fromNode = fromNode;
			this.toNode = toNode;
			this.edgeProperties = edgeProperties;
		}

		public Map<String, Object> getEdgeProperties() {
			return edgeProperties;
		}

		public void setEdgeProperties(Map<String, Object> edgeProperties) {
			this.edgeProperties = edgeProperties;
		}
		
		public Couple getNodesConnectedByEdge() {
			Couple couple = new Couple(fromNode, toNode, this);
			return couple;
		}
		
		@Override
		public String toString() {
			return fromNode.getData() + "->" + toNode.getData();
		}
	}
	
	
	private static class Couple<T> {
		Node<T> fromNode;
		Node<T> toNode;
		Edge connectingEdge;
		
		Couple (Node<T> fromNode, Node<T> toNode, Edge connectingEdge) {
			super();
			this.fromNode = fromNode;
			this.toNode  = toNode;
			this.connectingEdge = connectingEdge;
		}

		public Node<T> getFromNode() {
			return fromNode;
		}

		public void setFromNode(Node<T> fromNode) {
			this.fromNode = fromNode;
		}

		public Node<T> getToNode() {
			return toNode;
		}

		public void setToNode(Node<T> toNode) {
			this.toNode = toNode;
		}

		public Edge getConnectingEdge() {
			return connectingEdge;
		}

		public void setConnectingEdge(Edge connectingEdge) {
			this.connectingEdge = connectingEdge;
		}
		
		
	}
	
	public static void main(String[] args) {
		
		Graph dfsGraph = new Graph();
		Node<String> u = new Node<String>("u");
		Node<String> v = new Node<String>("v");
		Node<String> w = new Node<String>("w");
		Node<String> x = new Node<String>("x");
		Node<String> y = new Node<String>("y");
		Node<String> z = new Node<String>("z");
		u.addConnectedToNode(v);
		u.addConnectedToNode(x);
		v.addConnectedToNode(y);
		y.addConnectedToNode(x);
		x.addConnectedToNode(v);
		w.addConnectedToNode(y);
		w.addConnectedToNode(z);
		z.addConnectedToNode(z);
		
		dfsGraph.addNode(u);
		dfsGraph.addNode(v);
		dfsGraph.addNode(w);
		dfsGraph.addNode(x);
		dfsGraph.addNode(y);
		dfsGraph.addNode(z);
		
		System.out.println(dfsGraph);
		System.out.println("-------------");
		dfsGraph.depthFirstSearch();
		System.out.println("-------------");
		System.out.println(dfsGraph);
		
		/*
		Graph bfsGraph = new Graph();
		Node<String> r = new Node<String>("r");
		Node<String> s = new Node<String>("s");
		Node<String> t = new Node<String>("t");
		Node<String> u = new Node<String>("u");
		Node<String> v = new Node<String>("v");
		Node<String> w = new Node<String>("w");
		Node<String> x = new Node<String>("x");
		Node<String> y = new Node<String>("y");
		v.addConnectedToNode(r);
		r.addConnectedToNode(v);
		s.addConnectedToNode(r);
		r.addConnectedToNode(s);
		s.addConnectedToNode(w);
		w.addConnectedToNode(s);
		t.addConnectedToNode(w);
		w.addConnectedToNode(t);
		t.addConnectedToNode(x);
		x.addConnectedToNode(t);
		x.addConnectedToNode(w);
		w.addConnectedToNode(x);
		t.addConnectedToNode(u);
		u.addConnectedToNode(t);
		x.addConnectedToNode(u);
		u.addConnectedToNode(x);
		x.addConnectedToNode(y);
		y.addConnectedToNode(x);
		y.addConnectedToNode(u);
		u.addConnectedToNode(y);
		bfsGraph.addNode(s);
		bfsGraph.addNode(r);
		bfsGraph.addNode(t);
		bfsGraph.addNode(u);
		bfsGraph.addNode(v);
		bfsGraph.addNode(w);
		bfsGraph.addNode(x);
		bfsGraph.addNode(y);
		System.out.println(bfsGraph);
		System.out.println("-------------");
		bfsGraph.breadthFirstSearch();
		System.out.println("-------------");
		System.out.println(bfsGraph);
		*/
	}
}