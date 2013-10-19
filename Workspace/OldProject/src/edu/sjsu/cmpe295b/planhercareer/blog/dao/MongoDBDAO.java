package edu.sjsu.cmpe295b.planhercareer.blog.dao;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.sjsu.cmpe295b.planhercareer.blog.util.HTMLParser;
import edu.sjsu.cmpe295b.planhercareer.blog.util.RESTCall;



public class MongoDBDAO {

	private static MongoClient mongoClient;

	private static DB db;

	private static DBCollection documentCollection;

	private static DBCollection entityTagCollection;

	private static DBCollection keyDocumentTagCollection;

	private static DBCollection leaninStoryCollection;

	private static Gson gson = new Gson();

	private String[] leaninStoryTag = {"Career Transitions", "Finding Balance", "Gender in the Workplace", 
			"Mentors & Role Models", "Offers & Negotiations", "Overcoming Adversity", "Speaking Up", "Starting Out", "Taking Risks"
	};


	public static void initMongoDB() {
		try {
			mongoClient = new MongoClient("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db = mongoClient.getDB( "careerplan" );

		if (db.collectionExists("leaninstories")) {
			leaninStoryCollection = db.getCollection("leaninstories");
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 200000l).get();
			leaninStoryCollection = db.createCollection("leaninstories", options);
		}

		if (db.collectionExists("document")) {
			documentCollection = db.getCollection("document");
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 200000l).get();
			documentCollection = db.createCollection("document", options);
		}

		if (db.collectionExists("entityTag")) {
			entityTagCollection = db.getCollection("entityTag");
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 200000l).get();
			entityTagCollection = db.createCollection("entityTag", options);
		}

		if (db.collectionExists("keyDocumentTag")) {
			keyDocumentTagCollection = db.getCollection("keyDocumentTag");
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 200000l).get();
			keyDocumentTagCollection = db.createCollection("keyDocumentTag", options);
		}


	}

	public static void addDocumentToList() {
		// add leanin stories to the list

	}

	public static String extractCategoryFromDocument(String url) {
		String alchemyEntityURLForDocument="http://access.alchemyapi.com/calls/url/URLGetCategory?url=" + url 
				+ "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json";
		//System.out.println("----------------------" + gson.toJson(RESTCall.sendGet(alchemyEntityURLForDocument)));
		return RESTCall.sendGet(alchemyEntityURLForDocument);
	}

	public static String extractEntitiesFromDocument(String url) {
		String alchemyEntityURLForDocument="http://access.alchemyapi.com/calls/url/URLGetRankedNamedEntities?url=" + url 
				+ "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json";
		//System.out.println("----------------------" + gson.toJson(RESTCall.sendGet(alchemyEntityURLForDocument)));
		return RESTCall.sendGet(alchemyEntityURLForDocument);
	}

	public static String extractKeyDocumentFromDocument(String url) {
		String alchemyKeyDocumentURLForDocument="http://access.alchemyapi.com/calls/url/URLGetRankedKeyDocuments?url=" + url 
				+ "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json&keyDocumentExtractMode=strict&sentiment=1&showSourceText=1";
		return RESTCall.sendGet(alchemyKeyDocumentURLForDocument);
	}

	public static String extractSentimentFromDocument(String url) {
		String alchemySentimentURLForDocument="http://access.alchemyapi.com/calls/url/URLGetTextSentiment?url=" + url 
				+ "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json";
		return RESTCall.sendGet(alchemySentimentURLForDocument);
	}

	public static String extractConceptTagFromDocument(String url) {
		String alchemyConceptTagURLForDocument="http://access.alchemyapi.com/calls/url/URLGetRankedConcepts?url=" + url 
				+ "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json";
		return RESTCall.sendGet(alchemyConceptTagURLForDocument);
	}

	public static String extractSentimentFromEachEntityInDocument(String url) {
		// populate entites here
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(extractEntitiesFromDocument(url));
		JsonArray entities =  object.getAsJsonArray("entities");

		Map<String, String> sentimentEntityMap = new HashMap<String, String>();

		for(JsonElement entity : entities) {			

			String s = entity.getAsJsonObject().get("text").toString().replace("\"", "").replaceAll(" ", "%20");
			System.out.println("-***Entities***- " + s);

			String alchemySentimentEntityURLForDocument="http://access.alchemyapi.com/calls/url/URLGetTargetedSentiment?url=" + url 
					+ "&target=" + s + "&apikey=1c2603707cc58a12fa1413a28fdccbbc08194751&outputMode=json";
			sentimentEntityMap.put(s, RESTCall.sendGet(alchemySentimentEntityURLForDocument));
		}

		Gson gson = new Gson();
		String json = gson.toJson(sentimentEntityMap);

		return gson.toJson(sentimentEntityMap);
	}


	public static void loadLeanInStoriesDataInMongoDB() {

		initMongoDB();

		Set<String> urlSet = HTMLParser.extractLeanInStoryLink();

		
			for (String url : urlSet) {
				
				DBObject dbo1 = null;
				DBObject dbo2 = null;
				DBObject dbo3 = null;
				DBObject dbo4 = null;
				DBObject dbo5 = null;
				DBObject dbo6 = null;

				try{
				dbo1 = (DBObject) JSON.parse(extractEntitiesFromDocument(url));
				 dbo2 = (DBObject) JSON.parse(extractCategoryFromDocument(url));
				 dbo3 = (DBObject) JSON.parse(extractKeyDocumentFromDocument(url));
				 dbo4 = (DBObject) JSON.parse(extractSentimentFromDocument(url));		
				 dbo5 = (DBObject) JSON.parse(extractConceptTagFromDocument(url));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
				
				try{
				dbo6 = (DBObject) JSON.parse(extractSentimentFromEachEntityInDocument(url));
					dbo1.putAll(dbo2);
					dbo1.putAll(dbo3);
					dbo1.putAll(dbo4);
					dbo1.putAll(dbo5);	
					dbo1.putAll(dbo6);
					leaninStoryCollection.insert(dbo1);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Proceeding with the next....");
					continue;
				}
			}

		//String url="http://leanin.org/stories/booyeon-lee-allen";
		//System.out.println("-###################- " + dbo1);
		//String entities = gson.toJson(dbo.get("entities"));
		//System.out.println("$$$$$$$$$$$$ " + entities);

		/*JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(extractEntitiesFromDocument(url));
		JsonArray entities =  object.getAsJsonArray("entities");
		for(JsonElement entity : entities) {			
			System.out.println("-***Entities***- " + entity.getAsJsonObject().get("text"));
		}
		//for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
		    //JsonArray array = entry.getValue().getAsJsonArray();

		    //String code = array.getAsJsonPrimitive().getAsString();
		//}
		 * 
		 * 
		 */

		//System.out.println(extractEntitiesFromDocument(url));
		//System.out.println(extractSentimentFromDocument(url));
		//System.out.println(extractKeyDocumentFromDocument(url));
		//System.out.println(extractConceptTagFromDocument(url));
		//System.out.println(extractSentimentFromEachEntityInDocument(url));

	}

	public static void main(String[] args) {
		loadLeanInStoriesDataInMongoDB();
	}

}
