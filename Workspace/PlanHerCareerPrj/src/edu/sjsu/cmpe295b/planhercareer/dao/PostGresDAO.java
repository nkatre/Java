package edu.sjsu.cmpe295b.planhercareer.dao;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.sjsu.cmpe295b.planhercareer.dto.ConnectionSimilarityMetrics;
import edu.sjsu.cmpe295b.planhercareer.util.RESTCall;

public class PostGresDAO {

	static Connection connection = null;

	public static void main(String[] args) {

		/*
		getConnection();

		Statement st;
		try {
			st = connection.createStatement();
			st.executeQuery("SELECT *  FROM \"FB_PERSON\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 */

	}


	public static boolean deletePersonData(String personId) {

		getConnection();		

		if ((null == personId) || (personId.trim().equals("")) ) {
			return true;			
		} else {
			Statement stmt = null;

			try {
				stmt = connection.createStatement();

				stmt.addBatch("DELETE FROM \"FB_PERSON\" WHERE ID = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_ACTIVITY\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_BOOK\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_CONNECTED_TO\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_EDUCATION\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_GROUPS\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_LIKES\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_MOVIE\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_MUSIC\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_POST\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_SUBSCRIBED_TO\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_TELEVISION\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");
				stmt.addBatch("DELETE FROM \"FB_PERSON_WORK\" WHERE \"PERSON_ID\" = " + "'" + personId + "';");

				int counts[] = stmt.executeBatch();

				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

		System.out.println("Deleted user data");

		return true;

	}


	public static boolean insertPersonData(String data) {

		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(data);
		String nextUrl;

		getConnection();

		try {
		String query = "INSERT INTO \"FB_PERSON\"( " + 
				"id, name, age_range, bio, birthday, cover, devices, education, " + 
				"email, first_name, last_name, gender, hometown, languages, link, " + 
				"location, middle_name, quotes, relationship_status, religion, " + 
				"significant_other, timezone, username, updated_time, work, address, " + 
				"favorite_athletes, favorite_teams, inspirational_people, interested_in, " + 
				"meeting_for, name_format, political, sports, friends, family, " + 
				"events, books, apprequests, albums, activities, accounts, games, " + 
				"groups, interests, likes, locations, movies, music, posts, questions, " + 
				"subscribedto, subscribers, television)" + 
				"VALUES ( " + 
				"'" + ((null == object.get("id"))? "": object.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("name"))? "": object.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("age_range"))? "": object.get("age_range")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("bio"))? "": object.get("bio")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("birthday"))? "": object.get("birthday")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("cover"))? "": object.get("cover")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("devices"))? "": object.get("devices")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("education"))? "": object.get("education")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("email"))? "": object.get("email")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("first_name"))? "": object.get("first_name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("last_name"))? "": object.get("last_name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("gender"))? "": object.get("gender")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("hometown"))? "": object.get("hometown")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("languages"))? "": object.get("languages")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("link"))? "": object.get("link")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("location"))? "": object.get("location")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("middle_name"))? "": object.get("middle_name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("quotes"))? "": object.get("quotes")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("relationship_status"))? "": object.get("relationship_status")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("religion"))? "": object.get("religion")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("significant_other"))? "": object.get("significant_other")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("timezone"))? "": object.get("timezone")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("username"))? "": object.get("username")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("updated_time"))? "": object.get("updated_time")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("work"))? "": object.get("work")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("address"))? "": object.get("address")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("favorite_athletes"))? "": object.get("favorite_athletes")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("favorite_teams"))? "": object.get("favorite_teams")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("inspirational_people"))? "": object.get("inspirational_people")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("interested_in"))? "": object.get("interested_in")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("meeting_for"))? "": object.get("meeting_for")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
				"'" + ((null == object.get("name_format")? "": object.get("name_format")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("political"))? "": object.get("political")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("sports"))? "": object.get("sports")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("friends"))? "": object.get("friends")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("family"))? "": object.get("family")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("events"))? "": object.get("events")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("books"))? "": object.get("books")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("apprequests"))? "": object.get("apprequests")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("albums"))? "": object.get("albums")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("activities"))? "": object.get("activities")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("accounts"))? "": object.get("accounts")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("games"))? "": object.get("games")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("groups"))? "": object.get("groups")).toString().replaceAll("\"","").replaceAll("'","")  + "'"+  "," +
						"'" + ((null == object.get("interests"))? "": object.get("interests")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("likes"))? "": object.get("likes")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("locations"))? "": object.get("locations")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("movies"))? "": object.get("movies")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("music"))? "": object.get("music")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("posts"))? "": object.get("posts")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("questions"))? "": object.get("questions")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("subscribedto"))? "": object.get("subscribedto")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("subscribers"))? "": object.get("subscribers")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == object.get("television"))? "": object.get("television")).toString().replaceAll("\"","").replaceAll("'","")  + "')" );

		Statement stmt = null;

		
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
			//connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// ------------------- insert into postgres individual categories -----------------------------------//

		// insert person education data in postgres
		if( null != object.get("education"))
			insertPersonEducationData(object.get("id").getAsString(), object.get("education").toString());

		// insert person work data in postgres
		if( null != object.get("work"))
			insertPersonWorkData(object.get("id").getAsString(), object.get("work").toString());

		// insert person friends data in postgres
		if( null != object.get("friends")) {

			insertPersonFriendsData(object.get("id").getAsString(), object.get("friends").getAsJsonObject().get("data").toString());
			nextUrl =  (null == object.get("friends").getAsJsonObject().get("paging")? null : object.get("friends").getAsJsonObject().get("paging").getAsJsonObject().get("next").toString());
			while(true) {			

				if ((null==nextUrl) || (nextUrl.equals(""))) {
					break;
				}

				try{

					System.out.println("nextUrl: " + nextUrl );
					String nextData = RESTCall.sendGet(nextUrl.replaceAll("\"", ""));						

					JsonObject nextObject = (JsonObject) parser.parse(nextData);

					insertPersonFriendsData(object.get("id").getAsString(), nextObject.get("data").toString());
					nextUrl =  (null == nextObject.get("paging")? null :nextObject.get("paging").getAsJsonObject().get("next").toString());
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			nextUrl = null;
		}

		// insert person likes data in postgres
		if( null != object.get("likes")) 
			insertPersonLikesData(object.get("id").getAsString(), object.get("likes").getAsJsonObject().get("data").toString());
		/*
		nextUrl =  (null == object.get("likes").getAsJsonObject().get("paging")? null : object.get("likes").getAsJsonObject().get("paging").getAsJsonObject().get("next").toString());
		while(true) {			

			if ((null==nextUrl) || (nextUrl.equals(""))) {
				break;
			}

			try{

			System.out.println("nextUrl: " + nextUrl );
			String nextData = RESTCall.sendGet(nextUrl.replaceAll("\"", ""));						

			JsonObject nextObject = (JsonObject) parser.parse(nextData);

			insertPersonLikesData(object.get("id").getAsString(), nextObject.get("data").toString());
			nextUrl =  (null == nextObject.get("paging")? null :nextObject.get("paging").getAsJsonObject().get("next").toString());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		nextUrl = null;
		 */

		// insert person groups data in postgres
		if( null != object.get("groups")) {
			insertPersonGroupsData(object.get("id").getAsString(), object.get("groups").getAsJsonObject().get("data").toString());
			nextUrl =  (null == object.get("groups").getAsJsonObject().get("paging")? null : object.get("groups").getAsJsonObject().get("paging").getAsJsonObject().get("next").toString());
			while(true) {			

				if ((null==nextUrl) || (nextUrl.equals(""))) {
					break;
				}

				try{

					System.out.println("nextUrl: " + nextUrl );
					String nextData = RESTCall.sendGet(nextUrl.replaceAll("\"", ""));						

					JsonObject nextObject = (JsonObject) parser.parse(nextData);

					insertPersonGroupsData(object.get("id").getAsString(), nextObject.get("data").toString());
					nextUrl =  (null == nextObject.get("paging")? null :nextObject.get("paging").getAsJsonObject().get("next").toString());
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			nextUrl = null;
		}

		// insert person subscribed to data in postgres
		if( null != object.get("subscribedto")) 		
			insertPersonSubscribedToData(object.get("id").getAsString(), object.get("subscribedto").getAsJsonObject().get("data").toString());

		// insert person books to data in postgres
		if( null != object.get("books")) 		
			insertPersonBooksData(object.get("id").getAsString(), object.get("books").getAsJsonObject().get("data").toString());

		// insert person activities data in postgres
		if( null != object.get("activities")) 		
			insertPersonActivitiesData(object.get("id").getAsString(), object.get("activities").getAsJsonObject().get("data").toString());

		// insert person music data in postgres
		if( null != object.get("music")) 		
			insertPersonMusicData(object.get("id").getAsString(), object.get("music").getAsJsonObject().get("data").toString());

		// insert person movies data in postgres
		if( null != object.get("movies")) 		
			insertPersonMovieData(object.get("id").getAsString(), object.get("movies").getAsJsonObject().get("data").toString());

		// insert person television data in postgres
		if( null != object.get("television")) 		
			insertPersonTelevisionData(object.get("id").getAsString(), object.get("television").getAsJsonObject().get("data").toString());

		// insert person post data in postgres
		if( null != object.get("posts")) {	

			insertPersonPostData(object.get("id").getAsString(), object.get("posts").getAsJsonObject().get("data").toString());
			nextUrl =  (null == object.get("posts").getAsJsonObject().get("paging")? null : object.get("posts").getAsJsonObject().get("paging").getAsJsonObject().get("next").toString());
			while(true) {			

				if ((null==nextUrl) || (nextUrl.equals(""))) {
					break;
				}

				try{

					System.out.println("nextUrl: " + nextUrl );
					String nextData = RESTCall.sendGet(nextUrl.replaceAll("\"", ""));						

					JsonObject nextObject = (JsonObject) parser.parse(nextData);

					insertPersonPostData(object.get("id").getAsString(), nextObject.get("data").toString());
					nextUrl =  (null == nextObject.get("paging")? null :nextObject.get("paging").getAsJsonObject().get("next").toString());
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			nextUrl = null;
		}

		return true;
	}


	private static boolean insertPersonEducationData(String personId, String personEducation) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personEducation)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i);   
			/*System.out.println("subJson: " + subJson);
			System.out.println("personId: " + personId);
			System.out.println("school id: " + subJson.get("school").getAsJsonObject().get("id"));
			System.out.println("school name: " + subJson.get("school").getAsJsonObject().get("name"));
			System.out.println("type: " + subJson.get("type"));
			System.out.println("year id: " + (null == subJson.get("year")? "": subJson.get("year").getAsJsonObject().get("id")));
			System.out.println("year name: " + (null == subJson.get("year")? "": subJson.get("year").getAsJsonObject().get("name")));
			System.out.println("concentration id: " + (null == subJson.get("concentration")? "": subJson.get("concentration").getAsJsonArray().get(0).getAsJsonObject().get("id")));
			System.out.println("concentration name: " + (null == subJson.get("concentration")? "": subJson.getAsJsonArray("concentration").getAsJsonArray().get(0).getAsJsonObject().get("name")));


			System.out.println("array size: " + ((null == subJson.get("concentration")) ? "": subJson.getAsJsonArray("concentration").getAsJsonArray().size()));
			// if array size greater than 1 insert one row for each concentration
			 */

			int cSize = 0;

			if(null != subJson.get("concentration")) {
				cSize = subJson.getAsJsonArray("concentration").getAsJsonArray().size();				
			} 

			if (cSize < 2) {
				try {
				String query = "INSERT INTO \"FB_PERSON_EDUCATION\"( \"PERSON_ID\", \"SCHOOL_ID\", \"SCHOOL_NAME\", \"SCHOOL_TYPE\", " +
						"\"YEAR_ID\", \"YEAR_NAME\", \"CONCENTRATION_ID\", \"CONCENTRATION_NAME\")" + 
						" VALUES ( " + 
						"'" + personId  + "'" +  "," +
						"'" + ((null == subJson.get("school"))? "": subJson.get("school").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("school"))? "": subJson.get("school").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("type"))? "": subJson.get("type")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("year"))? "": subJson.get("year").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("year"))? "": subJson.get("year").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("concentration"))? "": subJson.get("concentration").getAsJsonArray().get(0).getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
						"'" + ((null == subJson.get("concentration"))? "": subJson.getAsJsonArray("concentration").getAsJsonArray().get(0).getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
						")";

				System.out.println("query: " + query);


				Statement stmt = null;

				
					stmt = connection.createStatement();
					stmt.executeUpdate(query);
					stmt.close();
					//connection.commit();
					//connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			} else {
				
				for (int j = 0; j < cSize; j++) {
					try {
						
					String query = "INSERT INTO \"FB_PERSON_EDUCATION\"( \"PERSON_ID\", \"SCHOOL_ID\", \"SCHOOL_NAME\", \"SCHOOL_TYPE\", " +
							"\"YEAR_ID\", \"YEAR_NAME\", \"CONCENTRATION_ID\", \"CONCENTRATION_NAME\")" + 
							" VALUES ( " + 
							"'" + personId  + "'" +  "," +
							"'" + ((null == subJson.get("school"))? "": subJson.get("school").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("school"))? "": subJson.get("school").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("type"))? "": subJson.get("type")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("year"))? "": subJson.get("year").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("year"))? "": subJson.get("year").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("concentration"))? "": subJson.get("concentration").getAsJsonArray().get(0).getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
							"'" + ((null == subJson.get("concentration"))? "": subJson.getAsJsonArray("concentration").getAsJsonArray().get(j).getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
							")";

					System.out.println("query: " + query);


					Statement stmt = null;

					
						stmt = connection.createStatement();
						stmt.executeUpdate(query);
						stmt.close();
						//connection.commit();
						//connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}		

		}
		return true;
	}

	private static boolean insertPersonWorkData(String personId, String personWork) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personWork)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			try {
			String query = "INSERT INTO \"FB_PERSON_WORK\"( \"PERSON_ID\", \"EMPLOYER_ID\", \"EMPLOYER_NAME\", \"LOCATION_ID\", " +
					"\"LOCATION_NAME\", \"POSITION_ID\", \"POSITION_NAME\", \"START_DATE\", \"END_DATE\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("employer"))? "": subJson.get("employer").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("employer"))? "": subJson.get("employer").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("location"))? "": subJson.get("location").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("location"))? "": subJson.get("location").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("position"))? "": subJson.get("position").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("position"))? "": subJson.get("position").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("start_date"))? "": subJson.get("start_date")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("end_date"))? "": subJson.get("end_date")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonFriendsData(String personId, String personFriends) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personFriends)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 
			
			try {
			String query = "INSERT INTO \"FB_PERSON_CONNECTED_TO\"( \"PERSON_ID\", \"CONNECTED_PERSON_ID\", \"RELATIONSHIP\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'FRIEND'" +  
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonLikesData(String personId, String personLikes) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personLikes)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 
			
			try {

			String query = "INSERT INTO \"FB_PERSON_LIKES\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"CATEGORY\", \"CREATED_TIME\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("category"))? "": subJson.get("category")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("created_time"))? "": subJson.get("created_time")).toString().replaceAll("\"","").replaceAll("'","")  + "'" + 
					")";

			System.out.println("query: " + query);


			Statement stmt = null;
			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonGroupsData(String personId, String personGroups) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personGroups)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 
			try {

			String query = "INSERT INTO \"FB_PERSON_GROUPS\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonSubscribedToData(String personId, String personSubscribedTo) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personSubscribedTo)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			try {
				
			String query = "INSERT INTO \"FB_PERSON_SUBSCRIBED_TO\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}	
		return true;
	}

	private static boolean insertPersonBooksData(String personId, String personBooks) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personBooks)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 
			try {

			String query = "INSERT INTO \"FB_PERSON_BOOK\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"FB_LINK\", \"WEBSITE\", \"DESCRIPTION\", \"TALKING_ABOUT_COUNT\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +  "," +
					"'" + ((null == subJson.get("link"))? "": subJson.get("link")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("website"))? "": subJson.get("website")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("description"))? "": subJson.get("description")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("talking_about_count"))? "": subJson.get("talking_about_count")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonActivitiesData(String personId, String personActivities) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personActivities)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			String query = "INSERT INTO \"FB_PERSON_ACTIVITY\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"FB_LINK\", \"DESCRIPTION\", \"TALKING_ABOUT_COUNT\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +  "," +
					"'" + ((null == subJson.get("link"))? "": subJson.get("link")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("description"))? "": subJson.get("description")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("talking_about_count"))? "": subJson.get("talking_about_count")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonMusicData(String personId, String personMusic) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personMusic)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			String query = "INSERT INTO \"FB_PERSON_MUSIC\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"CATEGORY\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +  "," +
					"'" + ((null == subJson.get("category"))? "": subJson.get("category")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonMovieData(String personId, String personMovie) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personMovie)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			String query = "INSERT INTO \"FB_PERSON_MOVIE\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"CATEGORY\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +  "," +
					"'" + ((null == subJson.get("category"))? "": subJson.get("category")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}


	private static boolean insertPersonTelevisionData(String personId, String personTelevision) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personTelevision)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 

			String query = "INSERT INTO \"FB_PERSON_TELEVISION\"( \"PERSON_ID\", \"NAME\", \"NAME_ID\", \"CATEGORY\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("id"))? "": subJson.get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'"  +  "," +
					"'" + ((null == subJson.get("category"))? "": subJson.get("category")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		return true;
	}

	private static boolean insertPersonPostData(String personId, String personPosts) {

		JsonParser parser = new JsonParser();
		JsonArray object = (parser.parse(personPosts)).getAsJsonArray();

		getConnection();

		for (int i = 0; i < object.size(); i++) {

			JsonObject subJson = (JsonObject) object.get(i); 
			try {

			String query = "INSERT INTO \"FB_PERSON_POST\"( \"PERSON_ID\", \"NAME\", \"DESCRIPTION\", \"STORY\", \"LINK\", \"SOURCE\", " +
					"\"TYPE\", \"STATUS_TYPE\", \"OBJECT_ID\", \"APPLICATION_ID\", \"APPLICATION_NAME\", " +
					"\"LIKES_PERSON_ID_LIST\", \"LIKES_COUNT\", \"SHARES_COUNT\")" + 
					" VALUES ( " + 
					"'" + personId  + "'" +  "," +
					"'" + ((null == subJson.get("name"))? "": subJson.get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("description"))? "": subJson.get("description")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("story"))? "": subJson.get("story")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("link"))? "": subJson.get("link")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("source"))? "": subJson.get("source")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("type"))? "": subJson.get("type")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("status_type"))? "": subJson.get("status_type")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("object_id"))? "": subJson.get("object_id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("application"))? "": subJson.get("application").getAsJsonObject().get("id")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("application"))? "": subJson.get("application").getAsJsonObject().get("name")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("likes"))? "": subJson.get("likes").getAsJsonObject().get("data").getAsJsonArray()).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("likes"))? "": subJson.get("likes").getAsJsonObject().get("count")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +  "," +
					"'" + ((null == subJson.get("shares"))? "": subJson.get("shares").getAsJsonObject().get("count")).toString().replaceAll("\"","").replaceAll("'","")  + "'" +
					")";

			System.out.println("query: " + query);


			Statement stmt = null;

			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				//connection.commit();
				//connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}	
		return true;
	}

	public static void setupTables() throws Exception
	{
		Connection conn = null;
		
		try
		{
			conn = getConnection();
			String derivedSalarySql =
					"CREATE TABLE IF NOT EXISTS \"DERIVED_SALARY\" (   userid text NOT NULL,   salary_range text,   salary double precision,   closest_salary_id text,   CONSTRAINT userid PRIMARY KEY (userid) ) WITH (   OIDS=FALSE );";
			
			String fbPersonSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON\"( id text," +
					"name text, age_range text, bio text,birthday text,cover text, devices text, education text," +
					"email text, first_name text, last_name text, gender text, hometown text, languages text, link text, location text," + 
					"middle_name text, quotes text, relationship_status text,religion text, significant_other text, timezone text, username text," + 
					"updated_time text, work text, address text, favorite_athletes text, favorite_teams text, inspirational_people text, " + 
					"interested_in text,meeting_for text, name_format text,political text, sports text, friends text, family text, events text," + 
					"books text, apprequests text, albums text, activities text, accounts text, games text, groups text, interests text, likes text," +
					"locations text, movies text, music text, posts text, questions text, subscribedto text, subscribers text, television text ) WITH ( OIDS=FALSE );";
			String fbPersonActivitySql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_ACTIVITY\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"FB_LINK\" text,   \"DESCRIPTION\" text," + 
					"\"TALKING_ABOUT_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonBookSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_BOOK\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"FB_LINK\" text,   \"WEBSITE\" text," +
					"\"DESCRIPTION\" text,   \"TALKING_ABOUT_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonConnectedToSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_CONNECTED_TO\" (   \"PERSON_ID\" text,   \"CONNECTED_PERSON_ID\" text,   \"RELATIONSHIP\" text,   " + 
					"\"EDUCATION_SIMILARITY_SCORE\" text,   \"WORK_SIMILARITY_SCORE\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonEducationSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_EDUCATION\" (   \"PERSON_ID\" text,   \"SCHOOL_ID\" text,   \"SCHOOL_NAME\" text," + 
			        " \"SCHOOL_TYPE\" text,   \"YEAR_ID\" text,   \"YEAR_NAME\" text,   \"CONCENTRATION_ID\" text,   \"CONCENTRATION_NAME\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonGroupsSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_GROUPS\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text ) WITH (   OIDS=FALSE );";
			String fbPersonLikesSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_LIKES\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text,   \"CREATED_TIME\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonMovieSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_MOVIE\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonMusicSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_MUSIC\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonPostSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_POST\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"DESCRIPTION\" text,   \"STORY\" text,   \"LINK\" text,   \"SOURCE\" text,   " + 
					"\"TYPE\" text,   \"STATUS_TYPE\" text,   \"OBJECT_ID\" text,   \"APPLICATION_ID\" text,   \"APPLICATION_NAME\" text,   \"LIKES_PERSON_ID_LIST\" text,   \"LIKES_COUNT\" text,   \"SHARES_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonSubscribedToSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_SUBSCRIBED_TO\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonTelevisionSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_TELEVISION\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonWorkSql  = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_WORK\" (   \"PERSON_ID\" text,   \"EMPLOYER_ID\" text,   \"EMPLOYER_NAME\" text,   \"LOCATION_ID\" text,   \"LOCATION_NAME\" text,   \"POSITION_ID\" text,   \"POSITION_NAME\" text,   " + 
					"\"START_DATE\" text,   \"END_DATE\" text ) WITH (   OIDS=FALSE );";
			String lnPersonSql =
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON\" (   id text,   first_name text,   last_name text,   maiden_name text,   formatted_name text,   headline text,   location text,   email_address text,   industry text,   relation_to_viewer text,   current_status text,   current_status_timestamp text,   current_share text,   connections text,   num_connections text,   num_connections_capped text,   group_memberships text,   summary text,   specialties text,   positions text,   picture_url text,   site_standard_profile_request text,   public_profile_url text,   network text,   phone_numbers text,   bound_account_types text,   im_accounts text,   main_address text,   twitter_accounts text,   primary_twitter_account text,   last_modified_timestamp text,   proposal_comments text,   associations text,   interests text,   publications text,   patents text,   languages text,   skills text,   certifications text,   educations text,   courses text,   volunteer text,   three_current_positions text,   three_past_positions text,   num_recommenders text,   recommendations_received text,   mfeed_rss_url text,   following text,   job_bookmarks text,   suggestions text,   date_of_birth text,   member_url_resources text,   related_profile_views text,   honors_awards text ) WITH (   OIDS=FALSE );";
			String lnPersonCompanySql = 
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_COMPANY\" (   person_id text,   position_id text,   company_id text,   name text,   type text,   size text,   industry text,   ticker text ) WITH (   OIDS=FALSE );";
			String lnPersonEducationSql = 
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_EDUCATION\" (   person_id text,   education_id text,   school_name text,   field_of_study text,   start_date text,   end_date text,   degree text,   activities text,   notes text ) WITH (   OIDS=FALSE );";
			String lnPersonPositionsSql =
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_POSITIONS\" (   person_id text,   position_id text,   title text,   summary text,   start_date text,   end_date text,   is_current text,   company text ) WITH (   OIDS=FALSE );";
			String fbPersonSimilarityMetricsSql = 
					"CREATE TABLE IF NOT EXISTS \"PERSON_SIMILARITY\" (   \"USER1\" text NOT NULL,   \"USER2\" text NOT NULL,   friendship_strength double precision,   job_pos_sim double precision,   job_field_sim double precision,   salary_sim double precision,   loc_sim double precision,   company_sim double precision,   grad_degree_sim double precision,   cumulative_score double precision,   CONSTRAINT \"Primary\" PRIMARY KEY (\"USER1\", \"USER2\") ) WITH (   OIDS=FALSE );";
			
			String[] tables = {
					fbPersonSql,
					fbPersonActivitySql,
					fbPersonBookSql,
					fbPersonConnectedToSql,
					fbPersonEducationSql,
					fbPersonGroupsSql,
					fbPersonLikesSql,
					fbPersonMovieSql,
					fbPersonMusicSql,
					fbPersonPostSql,
					fbPersonSimilarityMetricsSql,
					fbPersonSubscribedToSql,
					fbPersonTelevisionSql,
					fbPersonWorkSql,
					lnPersonCompanySql,
					lnPersonEducationSql,
					lnPersonPositionsSql,
					lnPersonSql,
					derivedSalarySql
			};
			
			for (String t : tables)
			{
				System.out.println("Executing :" + t);
				executeNonQueryStatement(conn, t);
			}
		} finally {
			
		}
	}
	
	public static boolean upsertSimilarityMetrics(ConnectionSimilarityMetrics connectionSim)
		throws Exception
	{
		Connection conn = getConnection();
		StringBuilder sql1 = new StringBuilder();
		sql1.append(" update \"PERSON_SIMILARITY\" set friendship_strength = ");
		sql1.append(connectionSim.getFriendshipStrength()).append(",");
		sql1.append("job_pos_sim = ").append(connectionSim.getJobPositionSimilarity()).append(",");
		sql1.append("job_field_sim = ").append(connectionSim.getJobFieldSimilarity()).append(",");
		sql1.append("salary_sim = ").append(connectionSim.getSalarySimilarity()).append(",");
		sql1.append("loc_sim = ").append(connectionSim.getLocationSimilarity()).append(",");
		sql1.append("company_sim = ").append(connectionSim.getCompanySimilarity()).append(",");
		sql1.append("grad_degree_sim = ").append(connectionSim.getGradDegreeSimilarity()).append(",");
		sql1.append("cumulative_score = ").append(connectionSim.getCumulativeScore()).append(",");
		sql1.append(" where \"USER1\" = '").append(connectionSim.getUser1()).append("' and ");
		sql1.append(" \"USER2\" = '").append(connectionSim.getUser2()).append("'");

		int count =executeNonQueryStatement(conn, sql1.toString());

		if (count <= 0)
		{
			StringBuilder sql2 = new StringBuilder();
			sql2.append("insert into \"PERSON_SIMILARITY\" (");
			sql2.append("\"USER1\", \"USER2\" friendship_strength, job_pos_sim, job_field_sim");
			sql2.append("salary_sim, loc_sim, company_sim, grad_degree_sim");
			sql2.append("cumulative_score VALUES (");
			sql2.append("'").append(connectionSim.getUser1()).append("',");
			sql2.append("'").append(connectionSim.getUser2()).append("',");
			sql2.append(connectionSim.getFriendshipStrength()).append(",");
			sql2.append(connectionSim.getJobPositionSimilarity()).append(",");
			sql2.append(connectionSim.getJobFieldSimilarity()).append(",");
			sql2.append(connectionSim.getSalarySimilarity()).append(",");
			sql2.append(connectionSim.getLocationSimilarity()).append(",");
			sql2.append(connectionSim.getCompanySimilarity()).append(",");
			sql2.append(connectionSim.getGradDegreeSimilarity()).append(",");
			sql2.append(connectionSim.getCumulativeScore()).append(")");
			count =executeNonQueryStatement(conn, sql2.toString());
		}
		
		return count > 0;
	}

	public static int executeNonQueryStatement(Connection conn, String sql)
		throws Exception
	{
		Statement stmt = null;
		System.out.println("SQL : " + sql);
		try
		{
			stmt = conn.createStatement();
			int count = stmt.executeUpdate(sql);
			//conn.commit();
			return count;
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	public static Connection getConnection() {

		if (connection != null) {
			System.out.println("returning existing connection");
			return connection;
		} else {
			System.out.println("-------- Establishing PostgreSQL JDBC Connection ----------------");

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Include  PostgreSQL JDBC Driver in your library path!");
				e.printStackTrace();
			}

			System.out.println("PostgreSQL JDBC Driver Registered!");

			try {
				connection = DriverManager.getConnection(
						"jdbc:postgresql://localhost:5432/postgres",
						"postgres", "cmpe295b");
			} catch (SQLException e) {
				System.out.println("Connection Failed! Check output console");
				e.printStackTrace();

			}

			System.out.println("returning new connection" + connection);
			return connection;
		}

	}

}