package edu.sjsu.cmpe226.prj2.dao;

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

import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;


public class Neo4JWordGraphDAO implements WordGraphDAO 
{
	
	public static final String sDBPath = "db.path";
	
	/**
	 * the word field
	 */
	public static final String sTerm = "word.term";	

	/**
	 * the sense/meaning of the word field
	 */
	public static final String sMeaning = "word.meaning";
	
	/**
	 * the parts of speech of the word field
	 */
	public static final String sPartOfSpeech = "word.partOfSpeech";
	
	private String _dbpath;
	private GraphDatabaseService _db;
	private AutoIndexer<Node> wordIndex;
    private Index<Node> nodeIndex;
	
	public Neo4JWordGraphDAO(Properties conf)
	{	
		this(conf.getProperty(sDBPath));
	}
	
	public Neo4JWordGraphDAO(String dbPath)
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
		wordIndex.startAutoIndexingProperty(sTerm);
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
	public void loadWordData(String word) 
	{
        Transaction tx = _db.beginTx();
        try
        {
        	Node n = getWordNode(word.getTerm());
		
        	if ( null == n)
        	{
        		//System.out.println("Word (" + word.getTerm() + ") not present !!");
        		n  = createNewNode(word.getTerm());
        	}
        	
        	populateWordNode(n, word);
        	
        	
        	tx.success();
        } finally {
        	tx.finish();
        }
        
	}

	@Override
	public String getNode(String term) 
	{
        Transaction tx = _db.beginTx();
        String w = null;
        try
        {
            Node n = nodeIndex.get(sTerm, term).getSingle();
        	
            System.out.println("getNode() : Node fetched is :" + n);
            if ( null != n)            
            	w = getWordDataFromNode(n);
            
        	tx.success();
        } finally {
        	tx.finish();
        }
		return w;
	}
	
	@Override
	public List<String> getAllConnectionsForWord(String term,
			WordRelationship relation) 
	{
		Transaction tx = _db.beginTx();
		List<String> connections = new ArrayList<String>();
        try
        {
        	String w = getNode(term);
        	
        	if ( null != w)
        	{
        		Set<String> connWords = null;
        		
        		switch (relation)
        		{
	        		case Antonym: connWords = w.getAntonyms();
	        					  	break;
	        		case Holonym : connWords = w.getHolonyms();
					  				break;
	        		case Homonym: connWords = w.getHomonyms();
					  				break;
	        		case Hypernym: connWords = w.getHypernyms();
					  				break;
	        		case Hyponym: connWords = w.getHyponyms();
					  				break;
	        		case Meronym: connWords = w.getMeronyms();
					  				break;
	        		case Other: connWords = w.getOtherRelated();
	        						break;
	        		case Synonym: connWords = w.getSynonyms();
					  				break;
	        	}
        		if ( null != connWords)
        		{
        			for (String w2 : connWords)
        			{
        				String cWord = getNode(w2);
        				
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
	public void addRelationShip(String term1, String term2,
			WordRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getWordNode(term1);
			Node n2 = getWordNode(term2);
			
			if( null == n1)
				n1 = createNewNode(term1);
			
			if ( null == n2)
				n2 = createNewNode(term2);
			
			n1.createRelationshipTo(n2, relation);
			
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void removeRelationShip(String term1, String term2,
			WordRelationship relation) 
	{
		Transaction tx = _db.beginTx();

		try
		{
			Node n1 = getWordNode(term1);
			Node n2 = getWordNode(term2);
			
			if( (null != n1) && (null != n2))
			{
				for ( Relationship r : n1.getRelationships(relation, Direction.OUTGOING))
				{
					if (term2.equals(r.getEndNode().getProperty(sTerm)))
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
		n.setProperty(sTerm, term);
		nodeIndex.add(n, sTerm, term);
		return n;
	}
	
	private Node getWordNode(String term)
	{
        Transaction tx = _db.beginTx();
        Node n = null;
        try
        {
            n = nodeIndex.get(sTerm, term).getSingle();
        
        	tx.success();
        } finally {
        	tx.finish();
        }
		return n;
	}
	
	private void populateWordNode(Node n, String w)
	{
		n.setProperty(sTerm, (null == w.getTerm()) ? "" : w.getTerm());
		n.setProperty(sMeaning, (null == w.getMeaning()) ? "" : w.getMeaning());
		n.setProperty(sPartOfSpeech, ((null == w.getPartsOfSpeech()) ? PartsOfSpeech.None : w.getPartsOfSpeech()).toString());
		
		Set<String> words = w.getSynonyms();
		addRelationshipsTo(n, words, WordRelationship.Synonym);

		words = w.getHolonyms();
		addRelationshipsTo(n, words, WordRelationship.Holonym);

		words = w.getAntonyms();
		addRelationshipsTo(n, words, WordRelationship.Antonym);

		words = w.getHomonyms();
		addRelationshipsTo(n, words, WordRelationship.Homonym);

		words = w.getHypernyms();
		addRelationshipsTo(n, words, WordRelationship.Hypernym);

		words = w.getHyponyms();
		addRelationshipsTo(n, words, WordRelationship.Hyponym);
		
		words = w.getMeronyms();
		addRelationshipsTo(n, words, WordRelationship.Meronym);
		
		words = w.getOtherRelated();
		addRelationshipsTo(n, words, WordRelationship.Other);
	}
	
	private void addRelationshipsTo(Node n, Set<String> words, WordRelationship rel)
	{
		if ( null != words)
		{
			for (String w2 : words)
			{
				Node n2  = getWordNode(w2);
				
				if (n2 == null)
					n2 = createNewNode(w2);
				
				n.createRelationshipTo(n2, rel);
			}
		}
	}
	private String getWordDataFromNode(Node n)
	{
		String  w = new String();
	
		Set<String> synonyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Synonym, Direction.OUTGOING))
		{
			synonyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> antonyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Antonym, Direction.OUTGOING))
		{
			antonyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> holonyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Holonym, Direction.OUTGOING))
		{
			holonyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> homonyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Homonym, Direction.OUTGOING))
		{
			homonyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> hypernyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Hypernym, Direction.OUTGOING))
		{
			hypernyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> hyponyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Hyponym, Direction.OUTGOING))
		{
			hyponyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		Set<String> meronyms = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Meronym, Direction.OUTGOING))
		{
			meronyms.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		Set<String> otherRelateds = new HashSet<String>();		
		for ( Relationship r : n.getRelationships(WordRelationship.Other, Direction.OUTGOING))
		{
			otherRelateds.add((String)(r.getEndNode().getProperty(sTerm)));
		}
		
		if (n.hasProperty(sTerm))
			w.setTerm((String)n.getProperty(sTerm));		
		if (n.hasProperty(sMeaning))
			w.setMeaning((String)n.getProperty(sMeaning));
		if (n.hasProperty(sPartOfSpeech))
			w.setPartsOfSpeech(String.PartsOfSpeech.valueOf((String)n.getProperty(sPartOfSpeech)));
			
		w.setAntonyms(antonyms);		
		w.setHolonyms(holonyms);
		w.setHomonyms(homonyms);
		w.setHypernyms(hypernyms);
		w.setHyponyms(hyponyms);
		w.setMeronyms(meronyms);
		w.setOtherRelated(otherRelateds);
		w.setSynonyms(synonyms);
		return w;	
	}

	public void close()
	{
		_db.shutdown();
		_db = null;
		nodeIndex = null;
		wordIndex = null;
	}
	
	public List<String> getAllNodes()
	{
		
		List<String> data = null;
		
        Transaction tx = _db.beginTx();
		try
		{
			data = new ArrayList<String>();
		
			GlobalGraphOperations ops = GlobalGraphOperations.at(_db);
		
			Iterator<Node>	itr = ops.getAllNodes().iterator();
		
			while(itr.hasNext())
			{
				data.add(getWordDataFromNode(itr.next()));
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
