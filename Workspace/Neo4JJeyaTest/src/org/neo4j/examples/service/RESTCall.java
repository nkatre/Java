package org.neo4j.examples.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
 
import javax.net.ssl.HttpsURLConnection;

 
public class RESTCall {
 
	public static void main(String[] args) throws Exception {
 
		System.out.println("Testing 1 - Send Http GET request");
		
		//String url = "https://graph.facebook.com/me/mutualfriends/lini.kuriyan?access_token=CAACEdEose0cBAFtUxJC5wVXeNhYQStCV6WJptYlHgdIhHOcyForOtZC4kZBuDoOx0Bqfhn69AumIKxnduFX76AK6C48hF81cGduytLZAJ9MvOXUYxlclB8m6ZCafMGQ2LZCJdA05aZBVxMPJX0KFDXpcIm6OOnTwPyZAnQyRZCyXCqfiTJFPcPGQmZCqPtw4ESl3Y1gzbCLIZCRgZDZD";
		// url to get personal information from facebook for current user
		//String myDetailsUrl  = "https://graph.facebook.com/me?access_token=CAACEdEose0cBABxER3XPI7hZCk2ieayolwcG23ymqKTRTcAcqj5Rh49YW1xV8ATQZA3dyAkni9E1BGGWvUT09Onfhd2tgWVeBlDowDHudZACQqMZCJbZA6UCoc0CCHuh3eCCUBWvOs5u66u7oH1UPZCZB0PMovXJu8qvEQEZC0AB6MXQTTtWwQF804gZAnMW1Upy9p6OIxB6k2wZDZD&fields=id,name,about,address,age_range,bio,birthday,cover,devices,education,email,favorite_athletes,favorite_teams,first_name,gender,hometown,inspirational_people,interested_in,languages,last_name,link,location,meeting_for,middle_name,name_format,political,quotes,relationship_status,religion,significant_other,sports,timezone,username,updated_time,website,work";
		String myFriendsDetailsUrl  = "https://graph.facebook.com/me?access_token=CAACEdEose0cBABxER3XPI7hZCk2ieayolwcG23ymqKTRTcAcqj5Rh49YW1xV8ATQZA3dyAkni9E1BGGWvUT09Onfhd2tgWVeBlDowDHudZACQqMZCJbZA6UCoc0CCHuh3eCCUBWvOs5u66u7oH1UPZCZB0PMovXJu8qvEQEZC0AB6MXQTTtWwQF804gZAnMW1Upy9p6OIxB6k2wZDZD&fields=friends.fields(id,name,about,address,age_range,bio,birthday,cover,devices,education,email,favorite_athletes,favorite_teams,first_name,gender,hometown,inspirational_people,interested_in,languages,last_name,link,location,meeting_for,middle_name,name_format,political,quotes,relationship_status,religion,significant_other,sports,timezone,username,updated_time,website,work)";
		
		//System.out.println(RESTCall.sendGet(myDetailsUrl));
		System.out.println(RESTCall.sendGet(myFriendsDetailsUrl));
 
		//System.out.println("\nTesting 2 - Send Http POST request");
		//url = "https://selfsolve.apple.com/wcResults.do";
		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		//RESTCall.sendPost(url, urlParameters);
 
	}
 
	// HTTP GET request
	public static String sendGet(String url) {
		URL obj;
		HttpURLConnection con = null;
		String inputLine;
		StringBuffer response = new StringBuffer();
		int responseCode;
		BufferedReader in;
		
        try{
        	System.out.println("URL is : (" + url + ")");
        	obj = new URL(url);
    		con = (HttpURLConnection) obj.openConnection();
    		
    		con.setRequestMethod("GET"); // optional default is GET


    		responseCode = con.getResponseCode();
    		System.out.println("\nSending 'GET' request to URL : " + url);
    		System.out.println("Response Code : " + responseCode);
     
    		in = new BufferedReader(
    		        new InputStreamReader(con.getInputStream()));
    		
    		
     
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( con != null )
			con.disconnect();
		}
 
		//print result
		return response.toString();
 
	}
 
	// HTTP POST request
	public static String sendPost(String url, String urlParameters) {
		
		URL obj;
		HttpsURLConnection con;
		StringBuffer response = new StringBuffer();
		DataOutputStream wr;
		int responseCode;
		BufferedReader in;
		
		try {
			obj = new URL(url);
			con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			responseCode = con.getResponseCode();
			
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return response.toString();
	}
 
}