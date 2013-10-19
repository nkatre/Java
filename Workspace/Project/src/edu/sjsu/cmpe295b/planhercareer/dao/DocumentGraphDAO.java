package edu.sjsu.cmpe295b.planhercareer.dao;

import java.util.List;

import org.neo4j.graphdb.RelationshipType;
 

import edu.sjsu.cmpe295b.planhercareer.dto.DocumentData;

public interface DocumentGraphDAO 
{
	/**
	 * Named edge to define the relationship between two Documents
	 *
	 */
	public static enum DocumentRelationship
		implements RelationshipType
	{
		Concept, KeyDocument, Category, Sentiment, Entity, Other
	}
	
	/**
	 * inserts the DocumentData to the database. If the Document data already exists, then update
	 */
	public void loadDocumentData(DocumentData Document);
	
	/**
	 * Get the Document Data (Node) from DB
	 */
	public DocumentData getNode(String url);
	
	/**
	 * Get all first degree connection of a specific url
	 * @param url Document to be queried
	 * @param relation Relation Type
	 * @return
	 */
	public List<DocumentData> getAllConnectionsForDocument(String url, DocumentRelationship relation);
	
	/**
	 * Adds a bidirectional relationship between url1 and url2 with the relation
	 * If any of these urls are not present, they will be created and the relationship added
	 * @param url1 url1
	 * @param url2 url2
	 * @param relation  Relation between url1 and url2 (unidirectional)
	 */
	public void addRelationShip(String url1, String url2, DocumentRelationship relation);
	
	/**
	 * Removes a bidirectional relationship between url1 and url2 with the relation
	 * @param url1 url1
	 * @param url2 url2
	 * @param relation  Relation between url1 and url2 (unidirectional)
	 */
	public void removeRelationShip(String url1, String url2, DocumentRelationship relation);
	
	/**
	 * Close the DB
	 */
	public void close();
	
	public boolean isMaster();
	
	public void pullUpdates();
}
