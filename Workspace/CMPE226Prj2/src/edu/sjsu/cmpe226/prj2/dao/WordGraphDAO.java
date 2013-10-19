package edu.sjsu.cmpe226.prj2.dao;

import java.util.List;

import org.neo4j.graphdb.RelationshipType;
 
import edu.sjsu.cmpe226.prj2.dto.String;

public interface WordGraphDAO 
{
	/**
	 * Named edge to define the relationship between two words
	 * 
	 * synonym - A word that means the same as another word. Two words that can be interchanged in a context are said to be synonymous relative to that context
	 * antonym - Words considered to represent opposite meanings. A word that expresses a meaning opposed to the meaning of another word, in which case the two words are antonyms of each other.
	 * homonym - Two words are homonyms if they are pronounced or spelled the same way but have different meanings.
	 * meronym - A word that names a part of a larger whole; "`brim' and `crown' are meronyms of `hat'".
	 * holonym - A word for the whole of which other words are part, in the way house contains roof, door and window; or car contains steering-wheel and engine ...
	 * hypernym - A word whose meaning denotes a superordinate or superclass. Animal is a hypernym of dog. Opposite of hyponym. See also superclass and term-equivalence dictionary.
	 * hyponym - A word whose meaning is included in that of another word `scarlet', `vermilion', and `crimson' are hyponyms of `red'.
	 *
	 */
	public static enum WordRelationship
		implements RelationshipType
	{
		Synonym, Antonym, Homonym, Meronym, Holonym, Hypernym, Hyponym, Other
	}
	
	/**
	 * inserts the WordData to the database. If the word data already exists, then update
	 */
	public void loadWordData(String word);
	
	/**
	 * Get the Word Data (Node) from DB
	 */
	public String getNode(String term);
	
	/**
	 * Get all first degree connection of a specific term
	 * @param term Word to be queried
	 * @param relation Relation Type
	 * @return
	 */
	public List<String> getAllConnectionsForWord(String term, WordRelationship relation);
	
	/**
	 * Adds a bidirectional relationship between term1 and term2 with the relation
	 * If any of these terms are not present, they will be created and the relationship added
	 * @param term1 Term1
	 * @param term2 Term2
	 * @param relation  Relation between term1 and term2 (unidirectional)
	 */
	public void addRelationShip(String term1, String term2, WordRelationship relation);
	
	/**
	 * Removes a bidirectional relationship between term1 and term2 with the relation
	 * @param term1 Term1
	 * @param term2 Term2
	 * @param relation  Relation between term1 and term2 (unidirectional)
	 */
	public void removeRelationShip(String term1, String term2, WordRelationship relation);
	
	/**
	 * Close the DB
	 */
	public void close();
	
	public boolean isMaster();
	
	public void pullUpdates();
}
