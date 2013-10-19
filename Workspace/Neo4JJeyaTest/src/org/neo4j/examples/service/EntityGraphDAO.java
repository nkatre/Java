package org.neo4j.examples.service;

import java.util.List;

import org.neo4j.graphdb.RelationshipType;

public interface EntityGraphDAO 
{
	/**
	 * Named edge to define the relationship between two entities
	 */
	public static enum EntityRelationship
		implements RelationshipType
	{
		FB_Friend, LN_Connection, Other
	}
	
	/**
	 * inserts the EntityData to the database. If the word data already exists, then update
	 */
	public void loadEntityData(EntityData entity);
	
	/**
	 * Get the Entity Data (Node) from DB
	 */
	public EntityData getNode(String name);
	
	/**
	 * Get all first degree connection of a specific name
	 * @param name Entity to be queried
	 * @param relation Relation Type
	 * @return
	 */
	public List<EntityData> getAllConnectionsForEntity(String name, EntityRelationship relation);
	
	/**
	 * Adds a bidirectional relationship between term1 and term2 with the relation
	 * If any of these terms are not present, they will be created and the relationship added
	 * @param name1 Name1
	 * @param name2 Name2
	 * @param relation  Relation between name1 and name2 (unidirectional)
	 */
	public void addRelationShip(String name1, String name2, EntityRelationship relation);
	
	/**
	 * Removes a bidirectional relationship between term1 and term2 with the relation
	 * @param name1 Term1
	 * @param name2 Term2
	 * @param relation  Relation between name1 and name2 (unidirectional)
	 */
	public void removeRelationShip(String name1, String name2, EntityRelationship relation);
	
	/**
	 * Close the DB
	 */
	public void close();
	
	public boolean isMaster();
	
	public void pullUpdates();
}
