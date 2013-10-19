package edu.sjsu.cmpe295b.planhercareer.main;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.util.RESTCall;

public class FBManager {
	
	public static String getUserData(String id, String access_token)  {
		 
		String url  = "https://graph.facebook.com/" + id +
				"?access_token=" + access_token + 
				"&fields=id,name,age_range,bio,birthday,cover,devices,education,email,first_name,last_name,gender,hometown,languages,link,location,middle_name,quotes,relationship_status,religion,significant_other,timezone,username,updated_time,work,address,favorite_athletes,favorite_teams,inspirational_people,interested_in,meeting_for,name_format,political,sports,friends,family,albums,accounts,games,groups,interests,likes,locations,movies,music,posts,questions,subscribedto,subscribers,television,about,website,accounts,activities.fields(description,link,name,talking_about_count,can_post,cover,created_time,id,website),albums,apprequests.fields(application,data),books.fields(description,link,name,website,cover,can_post,created_time,id,talking_about_count),checkins,events.fields(cover,description,end_time,feed_targeting,is_date_only,id,location,name,owner,parent_group,privacy,rsvp_status,start_time,ticket_uri,timezone,updated_time,venue),family.fields(id,first_name,gender),friends.fields(id)";
		
		return RESTCall.sendGet(url);
 
	}
	
	public static String getUserConnectionData(String connectionId, String access_token)  {
		 
		String url  = "https://graph.facebook.com/" + connectionId +
				"?access_token=" + access_token + 
				"&fields=id,name,age_range,bio,birthday,cover,devices,education,email,first_name,last_name,gender,hometown,languages,link,location,middle_name,quotes,relationship_status,religion,significant_other,timezone,username,updated_time,work,address,favorite_athletes,favorite_teams,inspirational_people,interested_in,meeting_for,name_format,political,sports,family,albums,accounts,games,groups,interests,likes,locations,movies,music,posts,questions,subscribedto,subscribers,television,about,website,accounts,activities.fields(description,link,name,talking_about_count,can_post,cover,created_time,id,website),albums,apprequests.fields(application,data),books.fields(description,link,name,website,cover,can_post,created_time,id,talking_about_count),checkins,events.fields(cover,description,end_time,feed_targeting,is_date_only,id,location,name,owner,parent_group,privacy,rsvp_status,start_time,ticket_uri,timezone,updated_time,venue)";
		
		return RESTCall.sendGet(url);
 
	}
	
	public static void insertUserDataInDB(String id, String access_token)  {	
		
		String url  = "https://graph.facebook.com/" + id +
				"?access_token=" + access_token + 
				"&fields=id,name,age_range,bio,birthday,cover,devices,education,email,first_name,last_name,gender,hometown,languages," +
				"link,location,middle_name,quotes,relationship_status,religion,significant_other,timezone,username,updated_time,work,address," +
				"favorite_athletes,favorite_teams,inspirational_people,interested_in,meeting_for,name_format,political,sports,friends,family," +
				"albums,accounts,games,groups,interests,likes,locations,movies,music,posts,questions,subscribedto,subscribers,television,about," +
				"website,accounts,activities.fields(description,link,name,talking_about_count,can_post,cover,created_time,id,website),albums," +
				"apprequests.fields(application,data),books.fields(description,link,name,website,cover,can_post,created_time,id,talking_about_count)," +
				"checkins,events.fields(cover,description,end_time,feed_targeting,is_date_only,id,location,name,owner,parent_group,privacy,rsvp_status," +
				"start_time,ticket_uri,timezone,updated_time,venue),family.fields(id,first_name,gender),friends.fields(id)";
		
		// insert into postgres
		try {
			PostGresDAO.insertPersonData(RESTCall.sendGet(url));
			insertUserConnectionDataInDB(id, access_token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void insertUserConnectionDataInDB(String id, String access_token)  {
		
		String[] idList = getUserConnectionList(id, access_token);
		
		if( null == idList) {
			return;
		} else {
			for (int x = 0; x < idList.length; x++) {
				// insert into postgres
				try {
					PostGresDAO.insertPersonData(getUserConnectionData(idList[x], access_token));
				} catch (Exception e) {
					e.printStackTrace();				}
			}
		}
	}
	
	public static String[] getUserConnectionList(String id, String access_token)  {		 
		String url  = "https://graph.facebook.com/" + id +
				"?access_token=" + access_token + 
				"&fields=friends.fields(id)";
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(RESTCall.sendGet(url));
		
		JsonArray friendsObject = (null == object.get("friends") ? null : object.get("friends").getAsJsonObject().get("data").getAsJsonArray());


		if (null == friendsObject) {
			return null; 
		}
		
		String[] friendIDList = new String[friendsObject.size()];
		
		for (int j = 0; j < friendsObject.size(); j++) {
			friendIDList[j] =  friendsObject.get(j).getAsJsonObject().get("id").getAsString();			
		}		
		
		return friendIDList;
	}
	
	
	public static void deleteUserDataInDB(String personId)  {	
		PostGresDAO.deletePersonData(personId);
	}
	
	
	public static void main(String[] args) {
		
		insertUserDataInDB("601472282", "CAACEdEose0cBAIL8LaIVHlGzjah4iQlHNMOA7YPktVdXHYa3wbC1sayZAheRv8qYa0iTFA6JbaGsd01crNtUUfW2vnouJTeVKRYLdL3BB9P5vzEqWZAUbr0pKKT69FZCdzvaNslxv4VO0g0Do4VLMkcB1UhK95bv9JYvOZAs1hTChfLZBCWOzd6uAh6HLZB2ZARMvfZCa2Md3wZDZD");
		/*
		deleteUserDataInDB("23436784");
		deleteUserDataInDB("12453991");
		deleteUserDataInDB("601472282");
		deleteUserDataInDB("12453991");
		deleteUserDataInDB("16738180");
		deleteUserDataInDB("17132254");
		deleteUserDataInDB("506241118");
		deleteUserDataInDB("509214838");
		deleteUserDataInDB("509564111");
		deleteUserDataInDB("528240498");
		deleteUserDataInDB("534904876");
		deleteUserDataInDB("534948166");
		deleteUserDataInDB("536076612");
		deleteUserDataInDB("547541848");
		deleteUserDataInDB("550858064");
		deleteUserDataInDB("557789577");
		deleteUserDataInDB("558899527");
		*/
		
		
	}
	

}
