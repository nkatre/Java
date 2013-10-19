package edu.sjsu.cmpe226.prj2.parser;

//Inferno Development: http://www.infernodevelopment.com/how-write-html-parser-java


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPageContentParser {
	
	final static String[] wordCategory = {"Synonyms","Antonyms","Homonyms","Meronyms","Holonyms","Hypernyms","Hyponyms","Various","See also"};
	final static String[] partsOfSpeech = {"partsOfSpeech","Meaning","English"};


	public static void main (String[] args) throws IOException {
		WebPageContentParser parser = new WebPageContentParser();
		URL address = new URL("http://en.wiktionary.org/wiki/Wikisaurus:abandoned");		

		//various link
		//URL address = new URL("http://en.wiktionary.org/wiki/Wikisaurus:hodgepodge");	
		// for See also 
		//URL address = new URL("http://en.wiktionary.org/wiki/Wikisaurus:hate");	
		try {
			parser.getWordCategoryForUrl(address);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	

	/**
	 * Method which finds Word Category and lists corresponding location from the page
	 * @param htmlcontent
	 * @return Map<String, Set<String>>
	 */


	public static Map<String, Set<String>> getWordCategory(String htmlcontent) {
		String[] words = htmlcontent.split("\n");

		Map<String,List<Integer>> wordLocation = new HashMap<String,List<Integer>>();
		List<Integer> locationlist_wordCategory0 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory1 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory2 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory3 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory4 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory5 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory6 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory7 = new ArrayList<Integer>();
		List<Integer> locationlist_wordCategory8 = new ArrayList<Integer>();
		List<Integer> partsOfSpeech2 = new ArrayList<Integer>();



		for(int i=0; i< words.length; i++){

			//System.out.println(words[i]); 

			if(words[i].startsWith("[edit] ")){

				//System.out.println(words[i]); 
				String source[] =  words[i].split(" ");
			//	String relatedSource = null;
				if(source[1].equals("See") && source[2].equals("also")){
				//	relatedSource = source[1]+ " " +source[2];
					source[1]= source[1]+ " " +source[2];
				}
				if(source[1].equals(partsOfSpeech[2])){
					partsOfSpeech2.add(i);

				}
				else if(source[1].equals(wordCategory[0])){
					locationlist_wordCategory0.add(i);
				}
				else if(source[1].equals(wordCategory[1])){
					locationlist_wordCategory1.add(i);
				}else if(source[1].equals(wordCategory[2])){
					locationlist_wordCategory2.add(i);
				}else if(source[1].equals(wordCategory[3])){
					locationlist_wordCategory3.add(i);
				}else if(source[1].equals(wordCategory[4])){
					locationlist_wordCategory4.add(i);
				}else if(source[1].equals(wordCategory[5])){
					locationlist_wordCategory5.add(i);
				}else if(source[1].equals(wordCategory[6])){
					locationlist_wordCategory6.add(i);
				}else if(source[1].equals(wordCategory[7])){
					locationlist_wordCategory7.add(i);
				}else if(source[1].equals(wordCategory[8])){
					locationlist_wordCategory8.add(i);
				}
			}

		}

		wordLocation.put(wordCategory[0], locationlist_wordCategory0);
		wordLocation.put(wordCategory[1], locationlist_wordCategory1);
		wordLocation.put(wordCategory[2], locationlist_wordCategory2);
		wordLocation.put(wordCategory[3], locationlist_wordCategory3);
		wordLocation.put(wordCategory[4], locationlist_wordCategory4);
		wordLocation.put(wordCategory[5], locationlist_wordCategory5);
		wordLocation.put(wordCategory[6], locationlist_wordCategory6);
		wordLocation.put(wordCategory[7], locationlist_wordCategory7);
		wordLocation.put(wordCategory[8], locationlist_wordCategory8);
		wordLocation.put(partsOfSpeech[2],partsOfSpeech2);

		Map<String, Set<String>> listofwordsMap = getWordCategoryList( words ,  wordLocation);
		for (Entry<String, Set<String>> entry : listofwordsMap.entrySet()){
			System.out.println(entry.getKey() + "/" + entry.getValue());
		}


		return listofwordsMap;

	}


	/**
	 * Method takes word and corresponding location list and return set of word categories from the page
	 * @param words
	 * @param wordLocation
	 * @return Map<String, Set<String>>
	 */
	public static Map<String, Set<String>> getWordCategoryList(String[] words , Map<String,List<Integer>> wordLocation) {
		
		Map<String, Set<String>> categoryWordsMap =  new HashMap<String, Set<String>> ();
		try{
	//	Map<String, Set<String>> categoryWordsMap =  new HashMap<String, Set<String>> ();
		for (Entry<String, List<Integer>> entry : wordLocation.entrySet()){
			List<Integer> locationList =   entry.getValue();
			Set<String> categoryListWords= new HashSet<String>();
			String meaningOfWord =partsOfSpeech[1];
			String partsOfSpeech1 =partsOfSpeech[0];
			if(entry.getKey().equals(partsOfSpeech[2])){
				for(Integer location : locationList){
					location++;
					if(words[location].startsWith("[edit] ")){
						String source[] =  words[location].split(" ");
						List<String> partsOfSpeechList	= Arrays.asList(source[1]);
						Set<String>  partsOfSpeech = new HashSet<String>(partsOfSpeechList);
						categoryWordsMap.put(partsOfSpeech1, partsOfSpeech);
                        location++;
                        if(words[location].startsWith("[edit] ")){
                        	if(words[location].contains(":")){
                        	String meaning[] =  words[location].split(":");
                        	List<String> meaningList	= Arrays.asList(meaning[1]);
    						Set<String>  partsOfSpeechMeaning = new HashSet<String>(meaningList);
    						                     	                       	                        	
                        	categoryWordsMap.put(meaningOfWord, partsOfSpeechMeaning);
                        	}else{
                        	categoryWordsMap.put(meaningOfWord, new HashSet<String>());
                        	}
                        }
					}
					

				}


			}else{
				for(Integer location : locationList){
					int newLocation = ++location;
					int count = 0;
					while(newLocation < words.length && !words[newLocation].startsWith("[edit] ")){
						if(!words[newLocation].isEmpty()){
							count = 0;
							System.out.println("Word is " + words[newLocation]);
							categoryListWords.add(words[newLocation]);
						}else{
							count ++;
						}
						if(count >= 3 ){
							break;
						}
						newLocation++;
					
					}
				}
				categoryWordsMap.put(entry.getKey(), categoryListWords);
			}
			
		} 
		}catch(Exception e){
			System.out.println("Came inside exception");
			e.printStackTrace();
		}
		return categoryWordsMap;
	}


	/**
	 * Method takes uls address and parse the page for processing
	 * @param address
	 * @return  Map<String, Set<String>>
	 * @throws Exception
	 */
	
	public Map<String, Set<String>> getWordCategoryForUrl(URL address) throws Exception{
		String sourceLine;
		String content = "";
		Map<String, Set<String>> categoryWordsMap = new HashMap<String, Set<String>> ();


		try{
			InputStreamReader pageInput = new InputStreamReader(address.openStream());
			BufferedReader source = new BufferedReader(pageInput);

			//BufferedReader source = new BufferedReader(pageInput);

			// Append each new HTML line into one string. Add a tab character.
			while ((sourceLine = source.readLine()) != null)
				content += sourceLine + "\t";

			// Remove style tags & inclusive content
			Pattern style = Pattern.compile("<style.*?>.*?</style>");
			Matcher mstyle = style.matcher(content);
			while (mstyle.find()) content = mstyle.replaceAll("");

			// Remove script tags & inclusive content
			Pattern script = Pattern.compile("<script.*?>.*?</script>");
			Matcher mscript = script.matcher(content);
			while (mscript.find()) content = mscript.replaceAll("");

			// Remove comment tags & inclusive content
			Pattern comment = Pattern.compile("<!--.*?-->");
			Matcher mcomment = comment.matcher(content);
			while (mcomment.find()) content = mcomment.replaceAll("");

			// Remove special characters, such as &nbsp;
			Pattern sChar = Pattern.compile("&.*?;");
			Matcher msChar = sChar.matcher(content);
			while (msChar.find()) content = msChar.replaceAll("");

			// Remove the tab characters. Replace with new line characters.
			Pattern nLineChar = Pattern.compile("\t+");
			Matcher mnLine = nLineChar.matcher(content);
			while (mnLine.find()) content = mnLine.replaceAll("\n");

			// Remove primary HTML tags
			Pattern tag = Pattern.compile("<.*?>");
			Matcher mtag = tag.matcher(content);
			while (mtag.find()) content = mtag.replaceAll(""); 

			// Print the clean content & close the Readers
		//	System.out.println(content.trim());
			categoryWordsMap = getWordCategory(content);
			pageInput.close();
			source.close();
		}catch(Exception e){
			//	e.printStackTrace();
			return null;
		}
		return categoryWordsMap;

	}

}