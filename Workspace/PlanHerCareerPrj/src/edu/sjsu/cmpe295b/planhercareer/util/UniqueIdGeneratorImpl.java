package edu.sjsu.cmpe295b.planhercareer.util;

import java.util.Properties;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * UniqueIdGeneratorImpl - Implementation class for unique id generation
 * Implements: UniqueIdGenerator
 * 
 * @author Team 5
 *
 */
public class UniqueIdGeneratorImpl 
	implements UniqueIdGenerator 
{
	// mongodb setup - each collection should be thought of as a database
	/**
	 * Mongo Database Collection
	 */
	protected static final String sCollection = "mongo.collection";
	
	/**
	 * Mongo Database
	 */
	protected static final String sDB = "mongo.db";
	
	/**
	 * Mongo host
	 */
	protected static final String sHost = "mongo.host";
	
	/**
	 * Root user id
	 */
	public static final String ROOT_ID = "ROOT_USER"; //keyDocument not expected to be a userId
	
	/**
	 * Document will be in the format { "id" : "<userId>" , "next_seq" : <some_number> }
	 */
	public static final String ID_KEY = "id";
	
	/**
	 * Next sequence key
	 */
	public static final String NEXT_SEQ_KEY = "next_seq";
	
	/**
	 * DBCollection object
	 */
	private DBCollection collection;
	
	/**
	 * Properties object for setting and getting Database configuration and properties
	 */
	private Properties props;
	
	/**
	 * Initializes Properties object and sets properties for Database setup
	 * @throws Exception
	 */
	public UniqueIdGeneratorImpl() throws Exception 
	{
		props = new Properties();
		props.setProperty(sHost, "localhost");
		props.setProperty(sDB, "blogGen"); // a different db for uniqueId generator
		props.setProperty(sCollection, "blogGen");
	}
	
	@Override
	public synchronized String getUniqueId(String userId) 
	{
		String id = userId;
		if (null == userId)
		{
			id = ROOT_ID;
		}
		
		BasicDBObject query = new BasicDBObject(ID_KEY,
				Pattern.compile(id, Pattern.CASE_INSENSITIVE));
		
		Long returnVal = null;
		String seqInDB = null;
		DBObject data = null;
		try 
		{
			connect();
			DBCursor cursor = collection.find(query);
			
			if ( (null != cursor) && ( cursor.size() > 0 ))
			{
				data = cursor.next();
				seqInDB = (String) data.get(NEXT_SEQ_KEY);
				
				returnVal = new Long(seqInDB);
				seqInDB = "" + ( returnVal.longValue() + 1 );
				
				cursor.close();
			}
			
			
			if ( null != data)
			{
				collection.remove(data);
			} else {
				// initialize values
				returnVal = 0L;
				seqInDB = "1";
			}
			
			// persist the incremented or initial values
			BasicDBObject newObj = new BasicDBObject();
			newObj.append(ID_KEY, id);
			newObj.append(NEXT_SEQ_KEY, seqInDB);
			collection.insert(newObj);
			
			return returnVal.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "0";
	}

	/**
	 * Fetches a connection to the database
	 */
	private void connect() 
	{
		try 
		{
			if (collection != null && collection.getName() != null)
				return ;
		} catch (Exception ex) {
			collection = null;
		}

		try {
			Mongo m = new Mongo(props.getProperty(sHost));
			DB db = m.getDB(props.getProperty(sDB));
			
			collection = db.getCollection(props.getProperty(sCollection));
			
			if (collection == null)
				throw new RuntimeException("Missing collection: " + props.getProperty(sCollection));

			return;
		} catch (Exception ex) {
			// should never get here unless no directory is available
			throw new RuntimeException("Unable to connect to mongodb on " + props.getProperty(sHost));
		}
	}
	
	/**
	 * Release Database connection
	 */
	public void release() 
	{
		collection = null;
	}

	@Override
	public void reset() 
	{
		connect();
		collection.drop();
	}
}
