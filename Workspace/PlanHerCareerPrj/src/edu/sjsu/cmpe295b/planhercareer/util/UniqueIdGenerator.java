package edu.sjsu.cmpe295b.planhercareer.util;

/**
 * Interface: UniqueIdGenerator
 * 		- to generate an unique if for a database entity
 * @author Team 5
 *
 */
public interface UniqueIdGenerator 
{	
	/**
	 * Generates a unique id within the name-scope defined by parentId
	 * @param parentId Namescope, if null, then global Namescope
	 * @return uniqueId
	 */
	public String getUniqueId(String parentId);
	
	/**
	 * reset the state associated with id generation
	 */
	public void reset();
}
