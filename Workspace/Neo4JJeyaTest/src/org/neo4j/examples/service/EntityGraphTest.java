package org.neo4j.examples.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EntityGraphTest {
	
	private EntityGraphDAO _dao;

	
	public static final String NEO4J_DB = "/Users/ksjeyabarani/Documents/neo4j-community-1.9.4/data/test.db";

	private EntityData entity = null;
	
	@Before
	public void setUpBeforeTest() throws Exception 
	{
		
		_dao = new Neo4JEntityGraphDAO(NEO4J_DB);
		
		
		entity = new EntityData();
		entity.setName("Jeya");	
		Set<String> otherRelated = new HashSet<String>();
		otherRelated.add("Balaji");
		otherRelated.add("AV");
		entity.setOtherRelated(otherRelated);

		
		EntityData entity1 = new EntityData();
		entity1.setName("Lini");	
		
		_dao.loadEntityData(entity);
		_dao.loadEntityData(entity1);
		
		EntityData data = _dao.getNode("Jeya");
		System.out.println("Got entity data is :" + data);
		Assert.assertNotNull(data);
		
		data = _dao.getNode("Lini");
		System.out.println("Got entity data is :" + data);
		Assert.assertNotNull(data);
		
	}
	
	@After
	public void tearDownAfterClass() throws Exception 
	{
		_dao.close();
	}
	
	@Test
	public void testLoadEntityData() throws Exception {
		EntityData data = _dao.getNode("Jeya");
		System.out.println("Got entity data is :" + data);
		Assert.assertNotNull(data);
		
	}
	
	@Test
	public void testGetNode() throws Exception {
		EntityData w2 = _dao.getNode("testterm");
		System.out.println(w2);
		Assert.assertNotNull(w2);
		Assert.assertEquals(entity, w2);
	}
	
	@Test
	public void testGetAllConnectionsForWord() throws Exception 
	{
		EntityData word = new EntityData();
		word.setName("testterm1");
		
		Set<String> others = new HashSet<String>();
		others.add("a");
		others.add("b");
		others.add("o");
		others.add("p");

		word.setOtherRelated(others);
		
		_dao.loadEntityData(word);
		EntityData word2 = _dao.getNode("testterm1");
		System.out.println(word2);
		Assert.assertNotNull(word2);
		Assert.assertEquals("Word Match", word, word2);
		
		List<EntityData> relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());

		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		
		//System.out.println("Is Master 1 : "  + _dao.isMaster());
		//System.out.println("Is Master 2 : "  + _dao2.isMaster());
		//System.out.println("Is Master 3 : "  + _dao3.isMaster());
		_dao.pullUpdates();

		relations = _dao.getAllConnectionsForEntity("testterm1", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		Assert.assertEquals("Size ", 2, relations.size());
		
		
	}
	
	
	@Test
	public void testAddRelationShip() throws Exception {
		_dao.addRelationShip("testterm", "testterm1111", EntityGraphDAO.EntityRelationship.Other);
		Assert.assertNotNull(_dao.getNode("testterm"));
		Assert.assertNotNull(_dao.getNode("testterm1111"));

		System.out.println(_dao.getAllConnectionsForEntity("testterm", EntityGraphDAO.EntityRelationship.Other));
		Assert.assertEquals("Size check", 3, _dao.getAllConnectionsForEntity("testterm", EntityGraphDAO.EntityRelationship.Other).size());
	}
	
	
	@Test
	public void testRemoveRelationShip() 
	   throws Exception 
	{
		_dao.removeRelationShip("testterm", "testterm111", EntityGraphDAO.EntityRelationship.Other);
		_dao.removeRelationShip("testterm", "Two", EntityGraphDAO.EntityRelationship.Other);
		Assert.assertEquals("Size check", 0, _dao.getAllConnectionsForEntity("testterm", EntityGraphDAO.EntityRelationship.Other).size());

		System.out.println(_dao.getAllConnectionsForEntity("testterm", EntityGraphDAO.EntityRelationship.Other));
	}
	
	@Test
	public void testLoadRelatedWordsMaster() {
		
		EntityData entity = new EntityData();
		entity.setName("brave");
		
		Set<String> others = new HashSet<String>();
		others.add("bold");
		others.add("adventurous");
		others.add("chivalrous");
		others.add("daredevil");
		others.add("daring");
		others.add("dauntless");
		others.add("fearless");
		others.add("gallant");
		others.add("dauntless");
		others.add("heroic");
		others.add("intrepid");
		others.add("stalwart");
		others.add("valiant");
		others.add("valarous");
		
		
		others.add("coward");
		others.add("courage");
		others.add("hero");
		

		entity.setOtherRelated(others);
		
		_dao.loadEntityData(entity);
		
		
		entity = new EntityData();
		entity.setName("bold");
		
		others = new HashSet<String>();
		others.add("courage");
		others.add("adventurous");
		others.add("daring");
		others.add("embolden");

		entity.setOtherRelated(others);
		
		_dao.loadEntityData(entity);
		
		
		entity = new EntityData();
		entity.setName("courage");
		
		others = new HashSet<String>();
		others.add("hero");
		others.add("brave");
		others.add("bold");
		others.add("");
		others.add("");

		entity.setOtherRelated(others);
		
		_dao.loadEntityData(entity);
		
		
		//retrieve time taken for one node
		long StartTime = System.currentTimeMillis();	
		EntityData word2 = _dao.getNode("brave");
		System.out.println("IN HERE"+word2);		
		List<EntityData> relations = _dao.getAllConnectionsForEntity("brave", EntityGraphDAO.EntityRelationship.Other);
		
		System.out.println(relations);
		//Assert.assertEquals("Size ", 2, relations.size());
		long stoptimeonedegree = System.currentTimeMillis();
		long diff = stoptimeonedegree - StartTime;
		System.out.println("The diff time for 1st degree :" + diff);
		
		word2 =_dao.getNode("bold"); // one of the 1st degree 
		relations = _dao.getAllConnectionsForEntity("bold", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);		
		long stoptimetwodegree = System.currentTimeMillis();
		diff = stoptimetwodegree - StartTime;
		System.out.println("The diff time 2 degree is :" + diff);
		
		word2 =_dao.getNode("courage"); // one of 3rd degree
		relations = _dao.getAllConnectionsForEntity("courage", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		long stoptimethreedegree = System.currentTimeMillis();
		diff = stoptimethreedegree - StartTime;
		System.out.println("The diff time three degree is" + diff);
	}
	
	
	@Test
	public void testLoadRelatedWordsMasterSlave() {
		
		EntityData word = new EntityData();
		word.setName("brave");
		
		Set<String> others = new HashSet<String>();
		others.add("bold");
		others.add("adventurous");
		others.add("chivalrous");
		others.add("daredevil");
		others.add("daring");
		others.add("dauntless");
		others.add("fearless");
		others.add("gallant");
		others.add("dauntless");
		others.add("heroic");
		others.add("intrepid");
		others.add("stalwart");
		others.add("valiant");
		others.add("valarous");
		others.add("coward");
		others.add("courage");
		others.add("hero");
		word.setOtherRelated(others);
		
		_dao.loadEntityData(word);
				
		word = new EntityData();
		word.setName("bold");

		
		others = new HashSet<String>();
		others.add("daring");
		others.add("embolden");

		word.setOtherRelated(others);

		_dao.loadEntityData(word);
		
		
		
		word = new EntityData();
		word.setName("courage");
		
		 others = new HashSet<String>();
		others.add("");
		others.add("");
		
		word.setOtherRelated(others);
		
		_dao.loadEntityData(word);
		
		
		//retrieve time taken for one node
		long StartTime = System.currentTimeMillis();	
		_dao.pullUpdates();
		EntityData word2 = _dao.getNode("brave");
		System.out.println("IN HERE"+word2);		
		List<EntityData> relations = _dao.getAllConnectionsForEntity("brave", EntityGraphDAO.EntityRelationship.Other);
		
		System.out.println(relations);
		//Assert.assertEquals("Size ", 2, relations.size());
		long stoptimeonedegree = System.currentTimeMillis();
		long diff = stoptimeonedegree - StartTime;
		System.out.println("The diff time is" + diff);
		
		word2 =_dao.getNode("bold"); // one of the 1st degree 
		relations = _dao.getAllConnectionsForEntity("bold", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);		
		long stoptimetwodegree = System.currentTimeMillis();
		diff = stoptimetwodegree - StartTime;
		System.out.println("The diff time 2 degree is" + diff);
		
		word2 =_dao.getNode("courage"); // one of 3rd degree
		relations = _dao.getAllConnectionsForEntity("courage", EntityGraphDAO.EntityRelationship.Other);
		System.out.println(relations);
		long stoptimethreedegree = System.currentTimeMillis();
		diff = stoptimethreedegree - StartTime;
		System.out.println("The diff time three degree is" + diff);
	}
}
