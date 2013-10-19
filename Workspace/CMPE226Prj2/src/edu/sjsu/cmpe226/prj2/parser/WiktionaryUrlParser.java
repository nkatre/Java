package edu.sjsu.cmpe226.prj2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import junit.framework.Assert;

import edu.sjsu.cmpe226.prj2.dao.CassandraNodeWordDAO;
import edu.sjsu.cmpe226.prj2.dao.Neo4JWordGraphDAO;
import edu.sjsu.cmpe226.prj2.dao.WordGraphDAO;
import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class WiktionaryUrlParser {

	final static String[] wordCategory = {"Synonyms","Antonyms","Homonyms","Meronyms","Holonyms","Hypernyms","Hyponyms","Various","See also"};
	final static String[] partsOfSpeech = {"partsOfSpeech","Meaning"};
	static Map<String, String> wordBeanHashMap = new HashMap<String, String> ();
	static final String urlLinksFile = "resources/Wikisauruslink.txt";

	public static final String NEO4J_DB_PATH = "/tmp/wiki_neo4j.db";
	private WordGraphDAO _neo4jDAO = new Neo4JWordGraphDAO(NEO4J_DB_PATH);
	//private WordGraphDAO _neo4jDAO = new CassandraWordDAO();
	
	
	private static final String JSON_FILE = "/tmp/wiki_neo4j.json.txt";
	private File jsonFile = new File(JSON_FILE);
	private FileWriter jsonFileWriter ;
	private ObjectMapper objMapper;
	
	public WiktionaryUrlParser()
	{
		try {
			jsonFileWriter = new FileWriter(jsonFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objMapper = new ObjectMapper();
	}
	/**
	 * This method parse each url page from a file
	 */

	public  void iterateUrls(){
		try {
			WebPageContentParser parser = new WebPageContentParser();

			long startTime = System.currentTimeMillis();
			System.out.println("Parsing start time in milli sec is :" + startTime);
			BufferedReader input =  new BufferedReader(new FileReader(urlLinksFile));
			String url = null;
			while (( url = input.readLine()) != null){
				System.out.println("Current url is:" + url);
				
 				String word[] = url.split("http://en.wiktionary.org/wiki/Wikisaurus:");
 				if (word.length != 2)
 				{
 					throw new RuntimeException("Looks like invalid URL :" + url);
 				}
 				
 				StringBuilder builder = new StringBuilder();
 				builder.append("http://en.wiktionary.org/wiki/Wikisaurus:");				
				builder.append(word[1].replaceAll(" ", "_"));
 				
				URL address = new URL(builder.toString());	 

				Map<String, Set<String>> categoryWordsMap = parser.getWordCategoryForUrl(address);
				if(categoryWordsMap!= null &&!categoryWordsMap.isEmpty()){
					mapToWordBean(word[1],categoryWordsMap);
				}

				//	we can use wordBeanHashMap here to store it in data base or while processing which ever is faster

			}// while ends
			
			// Loading Data
			for (Entry<String, String> entry : wordBeanHashMap .entrySet()){
				System.out.println("Entering Data for term : " + entry.getKey());
				_neo4jDAO.loadWordData(entry.getValue());
			}
			
			// Validating Data
			for (Entry<String, String> entry : wordBeanHashMap .entrySet()){
				System.out.println("Validating Data for term : " + entry.getKey());
				String got = _neo4jDAO.getNode(entry.getKey());
				//Assert.assertEquals("Word Data Check" , entry.getValue(), got);	
			}

			long endTime = System.currentTimeMillis();
			System.out.println("Parsing end time in milli sec is :" + endTime);
			System.out.println("Total parsing time in milli sec is :" +(endTime - startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
/**
 * Method maps word, word categories to word and wordData 
 * @param word
 * @param pageContent
 * @return
 */

	public Map<String, String> mapToWordBean(String word, Map<String, Set<String>> pageContent){

		String wordBean = new String();
		for (Entry<String, Set<String>> entry : pageContent.entrySet()){
			wordBean.setTerm(word) ;
			if(entry.getKey().equals(partsOfSpeech[0])){
				Object[] arrObj = entry.getValue().toArray();

				System.out.println("Word is " +word);
				String partsOfSpeechObject = Arrays.toString(arrObj);
				String partsOfSpeech = partsOfSpeechObject.substring(1,partsOfSpeechObject.length()-1);
				System.out.println("PartsOfSpeech is:"+ partsOfSpeech);
				if(partsOfSpeech.equals("Noun")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Noun);

				}else if(partsOfSpeech.equals("Pronoun")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Pronoun);

				}
				else if(partsOfSpeech.equals("Adjective")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Adjective);

				}else if(partsOfSpeech.equals("Verb")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Verb);

				}else if(partsOfSpeech.equals("Adverb")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Adverb);

				}
				else if(partsOfSpeech.equals("Preposition")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Preposition);

				}else if(partsOfSpeech.equals("Conjunction")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Conjunction);

				}else if(partsOfSpeech.equals("Interjection")){
					wordBean.setPartsOfSpeech(PartsOfSpeech .Interjection);

				}else {
					wordBean.setPartsOfSpeech(PartsOfSpeech .None);

				}


			}else if(entry.getKey().equals(partsOfSpeech[1])){
				Object[] arrObj = entry.getValue().toArray();
				String meaningObject = Arrays.toString(arrObj);
				String meaning = meaningObject.substring(1,meaningObject.length()-1);
				System.out.println("Meaning is:"+ meaning);
				wordBean.setMeaning(meaning);

			}else if(entry.getKey().equals(wordCategory[0])){
				wordBean.setSynonyms(entry.getValue());

			}
			else if(entry.getKey().equals(wordCategory[1])){
				wordBean.setAntonyms(entry.getValue());

			}else if(entry.getKey().equals(wordCategory[2])){
				wordBean.setHomonyms(entry.getValue());

			}else if(entry.getKey().equals(wordCategory[3])){
				wordBean.setMeronyms(entry.getValue());

			}else if(entry.getKey().equals(wordCategory[4])){
				wordBean.setHolonyms(entry.getValue());

			}else if(entry.getKey().equals(wordCategory[5])){
				wordBean.setHypernyms(entry.getValue());

			}else if(entry.equals(wordCategory[6])){
				wordBean.setHyponyms(entry.getValue());

			}else if(entry.getKey().equals(wordCategory[7])){
				if(null != entry.getValue() && !entry.getValue().isEmpty()){
					if(wordBean != null && wordBean.getOtherRelated() == null){
						wordBean.setOtherRelated(entry.getValue());
					}else{
						//append to the set
						Set<String> hs=new HashSet<String>();
						hs =  wordBean.getOtherRelated();
						hs.addAll(entry.getValue());
						wordBean.setOtherRelated(hs);

					}


				}

			}else if(entry.getKey().equals(wordCategory[8])){



				if(null != entry.getValue() && !entry.getValue().isEmpty()){
					if(wordBean != null && wordBean.getOtherRelated() == null){
						wordBean.setOtherRelated(entry.getValue());
					}else{
						//append to the set
						Set<String> hs=new HashSet<String>();
						hs =  wordBean.getOtherRelated();
						hs.addAll(entry.getValue());
						wordBean.setOtherRelated(hs);

					}
				}

			}
			//System.out.println(entry.getKey() + "/" + entry.getValue());
			wordBeanHashMap.put(word, wordBean);

			//we can Use wordBeanHashMap from here also ; which while processing
		}
		return wordBeanHashMap;

	}

	public static void main(String args[]) throws Exception {
		WiktionaryUrlParser urlsParser = null;
		try{
			urlsParser = new WiktionaryUrlParser();
			urlsParser.iterateUrls();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			urlsParser.close();
		}
	}	
	
	
	public void close()
	{
		_neo4jDAO.close();
		try {
			jsonFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
