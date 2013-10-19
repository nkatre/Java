package edu.sjsu.cmpe226.prj2.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.sjsu.cmpe226.prj2.dao.Neo4JHAWordGraphDAO;
import edu.sjsu.cmpe226.prj2.dao.Neo4JWordGraphDAO;
import edu.sjsu.cmpe226.prj2.dao.WordGraphDAO;
import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class WordGraphTest {
	
	private WordGraphDAO _dao;
	private WordGraphDAO _dao2;
	private WordGraphDAO _dao3;

	private WordGraphDAO _masterDAO;
	private WordGraphDAO _slaveDAO;

	
	public static final String NEO4J_DB = "/tmp/foo.db";
	public static final String NEO4J2_DB = "/tmp/foo2.db";
	public static final String NEO4J3_DB = "/tmp/foo3.db";
	public static final String NEO4J4_DB = "/tmp/foo4.db";

	private String word = null;
	
	@Before
	public void setUpBeforeTestHA() throws Exception 
	{
		FileUtils.deleteDirectory(new File(NEO4J_DB));
		FileUtils.deleteDirectory(new File(NEO4J2_DB));
		FileUtils.deleteDirectory(new File(NEO4J3_DB));
		FileUtils.deleteDirectory(new File(NEO4J4_DB));

		System.out.println("Is File exist :" + new File("/tmp/foo.db").exists());
		
		_dao2 = new Neo4JHAWordGraphDAO(NEO4J2_DB,"1","localhost:6001", "localhost:2181,localhost:2182,localhost:2183", "port=1331");		
		
		_dao = new Neo4JHAWordGraphDAO(NEO4J3_DB,"2","localhost:6002", "localhost:2181,localhost:2182,localhost:2183", "port=1332");		
		
		_dao3 = new Neo4JHAWordGraphDAO(NEO4J4_DB,"3","localhost:6003", "localhost:2181,localhost:2182,localhost:2183", "port=1333");		
		
		
		//_dao = new Neo4JWordGraphDAO(NEO4J_DB);
		Set<String> synonym = new HashSet<String>();
		synonym.add("testterm111");
		synonym.add("Two");
		
		word = new String();
		word.setTerm("testterm");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("testMeaning");			
		word.setSynonyms(synonym);
		word.setAntonyms(new HashSet<String>());
		word.setHolonyms(new HashSet<String>());
		word.setHomonyms(new HashSet<String>());
		word.setHypernyms(new HashSet<String>());
		word.setHyponyms(new HashSet<String>());
		word.setMeronyms(new HashSet<String>());
		word.setOtherRelated(new HashSet<String>());

		
		String word1 = new String();
		word1.setTerm("testterm111");
		word1.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word1.setMeaning("testMeaning111");			
		//word1.setSynonyms(synonym);
		
		_dao.loadWordData(word);
		String data = _dao.getNode("testterm");
		System.out.println("Got Word data is :" + data);
		Assert.assertNotNull(data);
		
		if ( _dao.isMaster())
		{
		    _slaveDAO = _dao2;
			_masterDAO = _dao;
		} else if ( _dao2.isMaster()) {
			_slaveDAO = _dao;
			_masterDAO = _dao2;
		} else {
			_slaveDAO = _dao;
			_masterDAO = _dao3;
		}
		
	}
	
	/*
	@Before
	public void setUpBeforeTest() throws Exception 
	{
		FileUtils.deleteDirectory(new File(NEO4J_DB));
		System.out.println("Is File exist :" + new File("/tmp/foo.db").exists());
		
		_dao = new Neo4JWordGraphDAO(NEO4J_DB);		
		
		Set<String> synonym = new HashSet<String>();
		synonym.add("testterm111");
		synonym.add("Two");
		
		word = new WordData();
		word.setTerm("testterm");
		word.setPartsOfSpeech(WordData.PartsOfSpeech.Noun);
		word.setMeaning("testMeaning");			
		word.setSynonyms(synonym);
		word.setAntonyms(new HashSet<String>());
		word.setHolonyms(new HashSet<String>());
		word.setHomonyms(new HashSet<String>());
		word.setHypernyms(new HashSet<String>());
		word.setHyponyms(new HashSet<String>());
		word.setMeronyms(new HashSet<String>());
		word.setOtherRelated(new HashSet<String>());

		
		WordData word1 = new WordData();
		word1.setTerm("testterm111");
		word1.setPartsOfSpeech(WordData.PartsOfSpeech.Noun);
		word1.setMeaning("testMeaning111");			
		//word1.setSynonyms(synonym);
		
		_dao.loadWordData(word);
		WordData data = _dao.getNode("testterm");
		System.out.println("Got Word data is :" + data);
		Assert.assertNotNull(data);
		
	}
	*/
	
	@After
	public void tearDownAfterClass() throws Exception 
	{
		_dao.close();
		_dao2.close();
		_dao3.close();
	}
	
	@Test
	public void testLoadWordData() throws Exception {
		String word = new String();
		word.setTerm("testterm1");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("testMeaning1");	
		
		Set<String> synonyms = new HashSet<String>();
		synonyms.add("a");
		synonyms.add("b");
		
		Set<String> antonyms = new HashSet<String>();
		antonyms.add("c");
		antonyms.add("d");
		
		Set<String> hypernyms = new HashSet<String>();
		hypernyms.add("e");
		hypernyms.add("f");
		
		Set<String> hyponyms = new HashSet<String>();
		hyponyms.add("g");
		hyponyms.add("h");
		
		Set<String> meronyms = new HashSet<String>();
		meronyms.add("i");
		meronyms.add("j");
		
		Set<String> homonyms = new HashSet<String>();
		homonyms.add("k");
		homonyms.add("l");
		
		Set<String> holonyms = new HashSet<String>();
		holonyms.add("m");
		holonyms.add("n");
		
		Set<String> others = new HashSet<String>();
		others.add("o");
		others.add("p");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_dao.loadWordData(word);
		String word2 = _dao.getNode("testterm1");
		System.out.println(word2);
		Assert.assertNotNull(word2);
		Assert.assertEquals("Word Match", word, word2);
		
		//Check if referenced words are present
		String tmp = _dao.getNode("a");
		System.out.println("getNode(a) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("b");
		System.out.println("getNode(b) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("c");
		System.out.println("getNode(c) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("d");
		System.out.println("getNode(d) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("e");
		System.out.println("getNode(e) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("f");
		System.out.println("getNode(f) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("g");
		System.out.println("getNode(g) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("h");
		System.out.println("getNode(h) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("i");
		System.out.println("getNode(i) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("j");
		System.out.println("getNode(j) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("k");
		System.out.println("getNode(k) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("l");
		System.out.println("getNode(l) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("m");
		System.out.println("getNode(m) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("n");
		System.out.println("getNode(n) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("o");
		System.out.println("getNode(o) : " + tmp );
		Assert.assertNotNull(tmp);
		
		tmp = _dao.getNode("p");
		System.out.println("getNode(p) : " + tmp );
		Assert.assertNotNull(tmp);
		
		// Load again with the same value
		_dao.loadWordData(word);
		word2 = _dao.getNode("testterm1");
		System.out.println(word2);
		Assert.assertNotNull(word2);
		Assert.assertEquals("Word Match", word, word2);
		
		// Now load "a" which is the synonym
		String a = _dao.getNode("a");
		a.setMeaning("new Meaning");
		a.setPartsOfSpeech(PartsOfSpeech.Adjective);
		a.setAntonyms(antonyms);
		_dao.loadWordData(a);
		word2 = _dao.getNode("a");
		System.out.println(word2);
		Assert.assertNotNull(word2);
		Assert.assertEquals("Word Match", a, word2);
		
		
	}
	
	@Test
	public void testGetNode() throws Exception {
		String w2 = _dao.getNode("testterm");
		System.out.println(w2);
		Assert.assertNotNull(w2);
		Assert.assertEquals(word, w2);
	}
	
	@Test
	public void testGetAllConnectionsForWord() throws Exception 
	{
		String word = new String();
		word.setTerm("testterm1");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("testMeaning1");	
		
		Set<String> synonyms = new HashSet<String>();
		synonyms.add("a");
		synonyms.add("b");
		
		Set<String> antonyms = new HashSet<String>();
		antonyms.add("c");
		antonyms.add("d");
		
		Set<String> hypernyms = new HashSet<String>();
		hypernyms.add("e");
		hypernyms.add("f");
		
		Set<String> hyponyms = new HashSet<String>();
		hyponyms.add("g");
		hyponyms.add("h");
		
		Set<String> meronyms = new HashSet<String>();
		meronyms.add("i");
		meronyms.add("j");
		
		Set<String> homonyms = new HashSet<String>();
		homonyms.add("k");
		homonyms.add("l");
		
		Set<String> holonyms = new HashSet<String>();
		holonyms.add("m");
		holonyms.add("n");
		
		Set<String> others = new HashSet<String>();
		others.add("o");
		others.add("p");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		String word2 = _masterDAO.getNode("testterm1");
		System.out.println(word2);
		Assert.assertNotNull(word2);
		Assert.assertEquals("Word Match", word, word2);
		
		List<String> relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Antonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Holonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Homonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Hypernym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Hyponym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Meronym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _masterDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		
		//System.out.println("Is Master 1 : "  + _dao.isMaster());
		//System.out.println("Is Master 2 : "  + _dao2.isMaster());
		//System.out.println("Is Master 3 : "  + _dao3.isMaster());
		_slaveDAO.pullUpdates();

		relations = _slaveDAO.getAllConnectionsForWord("testterm1", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		
	}
	
	
	@Test
	public void testAddRelationShip() throws Exception {
		_dao.addRelationShip("testterm", "testterm1111", WordGraphDAO.WordRelationship.Synonym);
		Assert.assertNotNull(_dao.getNode("testterm"));
		Assert.assertNotNull(_dao.getNode("testterm1111"));

		System.out.println(_dao.getAllConnectionsForWord("testterm", WordGraphDAO.WordRelationship.Synonym));
		Assert.assertEquals("Size check", 3, _dao.getAllConnectionsForWord("testterm", WordGraphDAO.WordRelationship.Synonym).size());
	}
	
	
	@Test
	public void testRemoveRelationShip() 
	   throws Exception 
	{
		_dao.removeRelationShip("testterm", "testterm111", WordGraphDAO.WordRelationship.Synonym);
		_dao.removeRelationShip("testterm", "Two", WordGraphDAO.WordRelationship.Synonym);
		Assert.assertEquals("Size check", 0, _dao.getAllConnectionsForWord("testterm", WordGraphDAO.WordRelationship.Synonym).size());

		System.out.println(_dao.getAllConnectionsForWord("testterm", WordGraphDAO.WordRelationship.Synonym));
	}
	
	@Test
	public void testLoadRelatedWordsMaster() {
		
		String word = new String();
		word.setTerm("brave");
		word.setPartsOfSpeech(String.PartsOfSpeech.Adjective);
		word.setMeaning("strong in the face of fear; courageous; of a person");	
		
		Set<String> synonyms = new HashSet<String>();
		synonyms.add("bold");
		synonyms.add("adventurous");
		synonyms.add("chivalrous");
		synonyms.add("daredevil");
		synonyms.add("daring");
		synonyms.add("dauntless");
		synonyms.add("fearless");
		synonyms.add("gallant");
		synonyms.add("dauntless");
		synonyms.add("heroic");
		synonyms.add("intrepid");
		synonyms.add("stalwart");
		synonyms.add("valiant");
		synonyms.add("valarous");
		
		Set<String> antonyms = new HashSet<String>();
		antonyms.add("coward");
		antonyms.add("afraid");
		antonyms.add("fearful");
		antonyms.add("frightened");
		antonyms.add("meek");
		antonyms.add("shy");
		antonyms.add("timid");
		antonyms.add("cautious");
		
		Set<String> hypernyms = new HashSet<String>();
		hypernyms.add("person");
		hypernyms.add("");
		
		Set<String> hyponyms = new HashSet<String>();
		hyponyms.add("embolden");
		hyponyms.add("");
		
		Set<String> meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		Set<String> homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		Set<String> holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		Set<String> others = new HashSet<String>();
		others.add("coward");
		others.add("courage");
		others.add("hero");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		 word = new String();
		word.setTerm("bold");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("presumptious");	
		
		 synonyms = new HashSet<String>();
		synonyms.add("courage");
		synonyms.add("adventurous");
		
		
	    antonyms = new HashSet<String>();
		antonyms.add("avarice");
	
		 hypernyms = new HashSet<String>();
		hypernyms.add("");
		hypernyms.add("");
		
		 hyponyms = new HashSet<String>();
		hyponyms.add("");
		hyponyms.add("");
		
		 meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		 others = new HashSet<String>();
		others.add("daring");
		others.add("embolden");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		
		word = new String();
		word.setTerm("courage");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("courageMeaning");	
		
		 synonyms = new HashSet<String>();
		synonyms.add("hero");
		synonyms.add("brave");
		synonyms.add("bold");
		
	 antonyms = new HashSet<String>();
		antonyms.add("fool");
		antonyms.add("avarice");
		
		 hypernyms = new HashSet<String>();
		hypernyms.add("");
		hypernyms.add("");
		
		 hyponyms = new HashSet<String>();
		hyponyms.add("");
		hyponyms.add("");
		
		 meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		 others = new HashSet<String>();
		others.add("");
		others.add("");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		//retrieve time taken for one node
		long StartTime = System.currentTimeMillis();	
		String word2 = _masterDAO.getNode("brave");
		System.out.println("IN HERE"+word2);		
		List<String> relations = _masterDAO.getAllConnectionsForWord("brave", WordGraphDAO.WordRelationship.Synonym);
		
		System.out.println(relations);
		//Assert.assertEquals("Size ", 2, relations.size());
		long stoptimeonedegree = System.currentTimeMillis();
		long diff = stoptimeonedegree - StartTime;
		System.out.println("The diff time for 1st degree :" + diff);
		
		word2 =_masterDAO.getNode("bold"); // one of the 1st degree 
		relations = _masterDAO.getAllConnectionsForWord("bold", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);		
		long stoptimetwodegree = System.currentTimeMillis();
		diff = stoptimetwodegree - StartTime;
		System.out.println("The diff time 2 degree is :" + diff);
		
		word2 =_masterDAO.getNode("courage"); // one of 3rd degree
		relations = _masterDAO.getAllConnectionsForWord("courage", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);
		long stoptimethreedegree = System.currentTimeMillis();
		diff = stoptimethreedegree - StartTime;
		System.out.println("The diff time three degree is" + diff);
	}
	
	
	@Test
	public void testLoadRelatedWordsMasterSlave() {
		
		String word = new String();
		word.setTerm("brave");
		word.setPartsOfSpeech(String.PartsOfSpeech.Adjective);
		word.setMeaning("strong in the face of fear; courageous; of a person");	
		
		Set<String> synonyms = new HashSet<String>();
		synonyms.add("bold");
		synonyms.add("adventurous");
		synonyms.add("chivalrous");
		synonyms.add("daredevil");
		synonyms.add("daring");
		synonyms.add("dauntless");
		synonyms.add("fearless");
		synonyms.add("gallant");
		synonyms.add("dauntless");
		synonyms.add("heroic");
		synonyms.add("intrepid");
		synonyms.add("stalwart");
		synonyms.add("valiant");
		synonyms.add("valarous");
		
		Set<String> antonyms = new HashSet<String>();
		antonyms.add("coward");
		antonyms.add("afraid");
		antonyms.add("fearful");
		antonyms.add("frightened");
		antonyms.add("meek");
		antonyms.add("shy");
		antonyms.add("timid");
		antonyms.add("cautious");
		
		Set<String> hypernyms = new HashSet<String>();
		hypernyms.add("person");
		hypernyms.add("");
		
		Set<String> hyponyms = new HashSet<String>();
		hyponyms.add("embolden");
		hyponyms.add("");
		
		Set<String> meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		Set<String> homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		Set<String> holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		Set<String> others = new HashSet<String>();
		others.add("coward");
		others.add("courage");
		others.add("hero");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		 word = new String();
		word.setTerm("bold");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("presumptious");	
		
		 synonyms = new HashSet<String>();
		synonyms.add("courage");
		synonyms.add("adventurous");
		
		
	    antonyms = new HashSet<String>();
		antonyms.add("avarice");
	
		 hypernyms = new HashSet<String>();
		hypernyms.add("");
		hypernyms.add("");
		
		 hyponyms = new HashSet<String>();
		hyponyms.add("");
		hyponyms.add("");
		
		 meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		 others = new HashSet<String>();
		others.add("daring");
		others.add("embolden");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		
		word = new String();
		word.setTerm("courage");
		word.setPartsOfSpeech(String.PartsOfSpeech.Noun);
		word.setMeaning("courageMeaning");	
		
		 synonyms = new HashSet<String>();
		synonyms.add("hero");
		synonyms.add("brave");
		synonyms.add("bold");
		
	 antonyms = new HashSet<String>();
		antonyms.add("fool");
		antonyms.add("avarice");
		
		 hypernyms = new HashSet<String>();
		hypernyms.add("");
		hypernyms.add("");
		
		 hyponyms = new HashSet<String>();
		hyponyms.add("");
		hyponyms.add("");
		
		 meronyms = new HashSet<String>();
		meronyms.add("");
		meronyms.add("");
		
		homonyms = new HashSet<String>();
		homonyms.add("");
		homonyms.add("");
		
		holonyms = new HashSet<String>();
		holonyms.add("");
		holonyms.add("");
		
		 others = new HashSet<String>();
		others.add("");
		others.add("");
		
		word.setAntonyms(antonyms);
		word.setHolonyms(holonyms);
		word.setHomonyms(homonyms);
		word.setHypernyms(hypernyms);
		word.setHyponyms(hyponyms);
		word.setHyponyms(hyponyms);
		word.setMeronyms(meronyms);
		word.setOtherRelated(others);
		word.setSynonyms(synonyms);
		
		_masterDAO.loadWordData(word);
		
		
		//retrieve time taken for one node
		long StartTime = System.currentTimeMillis();	
		_slaveDAO.pullUpdates();
		String word2 = _slaveDAO.getNode("brave");
		System.out.println("IN HERE"+word2);		
		List<String> relations = _slaveDAO.getAllConnectionsForWord("brave", WordGraphDAO.WordRelationship.Synonym);
		
		System.out.println(relations);
		//Assert.assertEquals("Size ", 2, relations.size());
		long stoptimeonedegree = System.currentTimeMillis();
		long diff = stoptimeonedegree - StartTime;
		System.out.println("The diff time is" + diff);
		
		word2 =_slaveDAO.getNode("bold"); // one of the 1st degree 
		relations = _slaveDAO.getAllConnectionsForWord("bold", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);		
		long stoptimetwodegree = System.currentTimeMillis();
		diff = stoptimetwodegree - StartTime;
		System.out.println("The diff time 2 degree is" + diff);
		
		word2 =_slaveDAO.getNode("courage"); // one of 3rd degree
		relations = _slaveDAO.getAllConnectionsForWord("courage", WordGraphDAO.WordRelationship.Synonym);
		System.out.println(relations);
		long stoptimethreedegree = System.currentTimeMillis();
		diff = stoptimethreedegree - StartTime;
		System.out.println("The diff time three degree is" + diff);
	}
}
