package org.neo4j.examples.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class Neo4JService {
	
	private static final GraphDatabaseService service 
		= new EmbeddedGraphDatabase("data/graph.db");
	
	
	//public static Relationship createRelationship() {}
	
	
	public static void shutDown() {
		service.shutdown();
	}
	
	public static void main(String[] args) {
		Transaction tx = service.beginTx();
		try
	        {
			//Transaction tx = service.beginTx();
		Node underlyingNode = service.createNode();
		underlyingNode.setProperty("name", "Jeya");
		tx.success();
	        }
	        finally
	        {
	            tx.finish();
	            service.shutdown();
	        }
		
		
	}

}
