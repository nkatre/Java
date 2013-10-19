package edu.sjsu.cmpe295b.planhercareer.blog.dao;

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

import edu.sjsu.cmpe295b.planhercareer.blog.dto.DocumentData;
import edu.sjsu.cmpe295b.planhercareer.blog.dto.DocumentData.SourceWebsite;


public class Neo4JDocumentGraphDAO implements DocumentGraphDAO 
{
	
	public static final String sDBPath = "db.path";
	
	/**
	 * the Document field
	 */
	public static final String sUrl = "Document.url";	

	/**
	 * the sense/category of the Document field
	 */
	public static final String sCategory = "Document.category";
	
	/**
	 * the parts of speech of the Document field
	 */
	public static final String sSourceWebsite = "Document.sourceWebsite";
	
	private String _dbpath;
	private GraphDatabaseService _db;
	private AutoIndexer<Node> DocumentIndex;
    private Index<Node> nodeIndex;
	
	public Neo4JDocumentGraphDAO(Properties conf)
	{	
		this(conf.getProperty(sDBPath));
	}
	
	public Neo4JDocumentGraphDAO(String dbPath)
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
		
		DocumentIndex = _db.index().getNodeAutoIndexer();
		DocumentIndex.startAutoIndexingProperty(sUrl);
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
	public void loadDocumentData(DocumentData Document) 
	{
        Transaction tx = _db.beginTx();
        try
        {
        	Node n = getDocumentNode(Document.getUrl());
		
        	if ( null == n)
        	{
        		//System.out.println("Document (" + Document.geturl() + ") not present !!");
        		n  = createNewNode(Document.getUrl());
        	}
        	
        	populateDocumentNode(n, Document);
        	
        	
        	tx.success();
        } finally {
        	tx.finish();
        }
        
	}

	@Override
	public DocumentData getNode(String url) 
	{
        Transaction tx = _db.beginTx();
        DocumentData w = null;
        try
        {
            Node n = nodeIndex.get(sUrl, url).getSingle();
        	
            System.out.println("getNode() : Node fetched is :" + n);
            if ( null != n)            
            	w = getDocumentDataFromNode(n);
            
        	tx.success();
        } finally {
        	tx.finish();
        }
		return w;
	}
	
	@Override
	public List<DocumentData> getAllConnectionsForDocument(String url,
			DocumentRelationship relation) 
	{
		Transaction tx = _db.beginTx();
		List<DocumentData> connections = new ArrayList<DocumentData>();
        try
        {
        	DocumentData w = getNode(url);
        	
        	if ( null != w)
        	{
        		Set<String> connDocuments = null;
        		
        		switch (relation)
        		{
	        		case Concept: connDocuments = w.getConcept();
	        					  	break;
	        		case KeyDocument : connDocuments = w.getKeyDocument();
					  				break;
	        		case Category: connDocuments = w.getCategory();
					  				break;
	        		case Sentiment: connDocuments = w.getSentiment();
					  				break;
	        		case Entity: connDocuments = w.getEntities();
					  				break;
	        		case Other: connDocuments = w.getOtherRelated();
	        						break;
	        		
	        	}
        		if ( null != connDocuments)
        		{
        			for (String w2 : connDocuments)
        			{
        				DocumentData cDocument = getNode(w2);
        				
        				if ( null != cDocument)
        					connections.add(cDocument);
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
	public void addRelationShip(String url1, String url2,
			DocumentRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getDocumentNode(url1);
			Node n2 = getDocumentNode(url2);
			
			if( null == n1)
				n1 = createNewNode(url1);
			
			if ( null == n2)
				n2 = createNewNode(url2);
			
			n1.createRelationshipTo(n2, relation);
			
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void removeRelationShip(String url1, String url2,
			DocumentRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getDocumentNode(url1);
			Node n2 = getDocumentNode(url2);
			
			if( (null != n1) && (null != n2))
			{
				for ( Relationship r : n1.getRelationships(relation, Direction.OUTGOING))
				{
					if (url2.equals(r.getEndNode().getProperty(sUrl)))
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
	
	
	private Node createNewNode(String url)
	{
		Node n = _db.createNode();	
		n.setProperty(sUrl, url);
		nodeIndex.add(n, sUrl, url);
		return n;
	}
	
	private Node getDocumentNode(String url)
	{
        Transaction tx = _db.beginTx();
        Node n = null;
        try
        {
            n = nodeIndex.get(sUrl, url).getSingle();
        
        	tx.success();
        } finally {
        	tx.finish();
        }
		return n;
	}
	
	private void populateDocumentNode(Node n, DocumentData w)
	{
		n.setProperty(sUrl, (null == w.getUrl()) ? "" : w.getUrl());
		n.setProperty(sCategory, (null == w.getCategory()) ? "" : w.getCategory());
		n.setProperty(sSourceWebsite, ((null == w.getSourceWebsite()) ? SourceWebsite.None : w.getSourceWebsite()).toString());
		
		Set<String> Documents = w.getCategory();
		addRelationshipsTo(n, Documents, DocumentRelationship.Category);

		Documents = w.getConcept();
		addRelationshipsTo(n, Documents, DocumentRelationship.Concept);

		Documents = w.getEntities();
		addRelationshipsTo(n, Documents, DocumentRelationship.Entity);

		Documents = w.getKeyDocument();
		addRelationshipsTo(n, Documents, DocumentRelationship.KeyDocument);
		
		Documents = w.getSentiment();
		addRelationshipsTo(n, Documents, DocumentRelationship.Sentiment);
		
		Documents = w.getOtherRelated();
		addRelationshipsTo(n, Documents, DocumentRelationship.Other);
	}
	
	private void addRelationshipsTo(Node n, Set<String> Documents, DocumentRelationship rel)
	{
		if ( null != Documents)
		{
			for (String w2 : Documents)
			{
				Node n2  = getDocumentNode(w2);
				
				if (n2 == null)
					n2 = createNewNode(w2);
				
				n.createRelationshipTo(n2, rel);
			}
		}
	}
	private DocumentData getDocumentDataFromNode(Node n)
	{
		DocumentData  w = new DocumentData();
	
		Set<String> category = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.Category, Direction.OUTGOING))
		{
			category.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		Set<String> concept = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.Concept, Direction.OUTGOING))
		{
			concept.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		Set<String> entities = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.Entity, Direction.OUTGOING))
		{
			entities.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		Set<String> keyDocuments = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.KeyDocument, Direction.OUTGOING))
		{
			keyDocuments.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		Set<String> sentiments = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.Sentiment, Direction.OUTGOING))
		{
			sentiments.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		Set<String> otherRelateds = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(DocumentRelationship.Other, Direction.OUTGOING))
		{
			otherRelateds.add((String)(r.getEndNode().getProperty(sUrl)));
		}
		
		if (n.hasProperty(sUrl))
			w.setUrl((String)n.getProperty(sUrl));		
		if (n.hasProperty(sCategory))
			w.setCategory((Set<String>)n.getProperty(sCategory));
		if (n.hasProperty(sSourceWebsite))
			w.setSourceWebsite(DocumentData.SourceWebsite.valueOf((String)n.getProperty(sSourceWebsite)));
			
		w.setConcept(concept);		
		w.setKeyDocument(keyDocuments);
		w.setEntities(entities);
		w.setSentiment(sentiments);
		w.setOtherRelated(otherRelateds);
		return w;	
	}

	public void close()
	{
		_db.shutdown();
		_db = null;
		nodeIndex = null;
		DocumentIndex = null;
	}
	
	public List<DocumentData> getAllNodes()
	{
		
		List<DocumentData> data = null;
		
        Transaction tx = _db.beginTx();
		try
		{
			data = new ArrayList<DocumentData>();
		
			GlobalGraphOperations ops = GlobalGraphOperations.at(_db);
		
			Iterator<Node>	itr = ops.getAllNodes().iterator();
		
			while(itr.hasNext())
			{
				data.add(getDocumentDataFromNode(itr.next()));
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
	
	public static void main(String[] args) {
		
		String NEO4J_DB = "C://neo4jdbdata//careerplan.db";
		
		DocumentGraphDAO _dao = new Neo4JDocumentGraphDAO(NEO4J_DB);
		
		DocumentData w = new DocumentData();
		Set<String> s = new HashSet<String>();
		s.add("test");
		w.setUrl("test url");
		w.setCategory(s);
		w.setConcept(s);
		w.setEntities(s);
		w.setKeyDocument(s);
		_dao.loadDocumentData(w);
		
		_dao.close();
		
	}
}
