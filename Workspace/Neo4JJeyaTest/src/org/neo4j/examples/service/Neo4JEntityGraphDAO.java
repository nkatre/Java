package org.neo4j.examples.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.tooling.GlobalGraphOperations;


public class Neo4JEntityGraphDAO implements EntityGraphDAO 
{
	
	public static final String sDBPath = "db.path";
	
	/**
	 * the word field
	 */
	public static final String sName = "entity.name";	
	
	private String _dbpath;
	private GraphDatabaseService _db;
	private AutoIndexer<Node> wordIndex;
    private Index<Node> nodeIndex;
	
	public Neo4JEntityGraphDAO(Properties conf)
	{	
		this(conf.getProperty(sDBPath));
	}
	
	public Neo4JEntityGraphDAO(String dbPath)
	{
		_dbpath = dbPath;
		init(dbPath);
	}
	
	protected void init(String dbpath) 
	{

		if (dbpath == null)
			throw new RuntimeException("Missing database path");

		_db = new EmbeddedGraphDatabase(dbpath);
		onShutdown(_db);
		
		wordIndex = _db.index().getNodeAutoIndexer();
		wordIndex.startAutoIndexingProperty(sName);
		nodeIndex = _db.index().forNodes("nodes");
	}
	
	/**
	 * on exit, close the database
	 * 
	 * @param graphDb
	 */
	private static void onShutdown(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
	
	@Override
	public void loadEntityData(EntityData entity) 
	{
        Transaction tx = _db.beginTx();
        try
        {
        	Node n = getEntityNode(entity.getName());
		
        	if ( null == n)
        	{
        		n  = createNewNode(entity.getName());
        	}
        	
        	populateEntityNode(n, entity);
        	 	
        	tx.success();
        } finally {
        	tx.finish();
        }
        
	}

	@Override
	public EntityData getNode(String name) 
	{
        Transaction tx = _db.beginTx();
        EntityData w = null;
        try
        {
            Node n = nodeIndex.get(sName, name).getSingle();
        	
            System.out.println("getNode() : Node fetched is :" + n);
            if ( null != n)            
            	w = getEntityDataFromNode(n);
            
        	tx.success();
        } finally {
        	tx.finish();
        }
		return w;
	}
	
	@Override
	public List<EntityData> getAllConnectionsForEntity(String name,
			EntityRelationship relation) 
	{
		Transaction tx = _db.beginTx();
		List<EntityData> connections = new ArrayList<EntityData>();
        try
        {
        	EntityData e = getNode(name);
        	
        	if ( null != e)
        	{
        		Set<String> connEntities = null;
        		
        		switch (relation)
        		{
	        		case Other: connEntities = e.getOtherRelated();
	        						break;
	        	}
        		if ( null != connEntities)
        		{
        			for (String e2 : connEntities)
        			{
        				EntityData cWord = getNode(e2);
        				
        				if ( null != cWord)
        					connections.add(cWord);
        			}
        		}
        	}
        	tx.success();
        } finally {
        	tx.finish();
        }
        return connections;
	}

	@Override
	public void addRelationShip(String name1, String name2,
			EntityRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getEntityNode(name1);
			Node n2 = getEntityNode(name2);
			
			if( null == n1)
				n1 = createNewNode(name1);
			
			if ( null == n2)
				n2 = createNewNode(name2);
			
			n1.createRelationshipTo(n2, relation);
			
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void removeRelationShip(String name1, String name2,
			EntityRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getEntityNode(name1);
			Node n2 = getEntityNode(name2);
			
			if( (null != n1) && (null != n2))
			{
				for ( Relationship r : n1.getRelationships(relation, Direction.OUTGOING))
				{
					if (name2.equals(r.getEndNode().getProperty(sName)))
					{
						r.delete();
						break;
					}
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}	
	}
	
	
	private Node createNewNode(String term)
	{
		Node n = _db.createNode();	
		n.setProperty(sName, term);
		nodeIndex.add(n, sName, term);
		return n;
	}
	
	private Node getEntityNode(String name)
	{
        Transaction tx = _db.beginTx();
        Node n = null;
        try
        {
            n = nodeIndex.get(sName, name).getSingle();
        
        	tx.success();
        } finally {
        	tx.finish();
        }
		return n;
	}
	
	private void populateEntityNode(Node n, EntityData e)
	{
		n.setProperty(sName, (null == e.getName()) ? "" : e.getName());
		
		Set<String> entities = e.getOtherRelated();
		entities = e.getOtherRelated();
		addRelationshipsTo(n, entities, EntityRelationship.Other);
	}
	
	private void addRelationshipsTo(Node n, Set<String> entities, EntityRelationship rel)
	{
		if ( null != entities)
		{
			for (String w2 : entities)
			{
				Node n2  = getEntityNode(w2);
				
				if (n2 == null)
					n2 = createNewNode(w2);
				
				n.createRelationshipTo(n2, rel);
			}
		}
	}
	
	private EntityData getEntityDataFromNode(Node n)
	{
		EntityData  w = new EntityData();
		
		Set<String> otherRelateds = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(EntityRelationship.Other, Direction.OUTGOING))
		{
			otherRelateds.add((String)(r.getEndNode().getProperty(sName)));
		}
		
		if (n.hasProperty(sName))
			w.setName((String)n.getProperty(sName));		

		w.setOtherRelated(otherRelateds);

		return w;	
	}

	public void close()
	{
		_db.shutdown();
		_db = null;
		nodeIndex = null;
		wordIndex = null;
	}
	
	public List<EntityData> getAllNodes()
	{
		
		List<EntityData> data = null;
		
        Transaction tx = _db.beginTx();
		try
		{
			data = new ArrayList<EntityData>();
		
			GlobalGraphOperations ops = GlobalGraphOperations.at(_db);
		
			Iterator<Node>	itr = ops.getAllNodes().iterator();
		
			while(itr.hasNext())
			{
				data.add(getEntityDataFromNode(itr.next()));
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return data;
	}

	@Override
	public boolean isMaster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pullUpdates() {
		// TODO Auto-generated method stub
		
	}
}
