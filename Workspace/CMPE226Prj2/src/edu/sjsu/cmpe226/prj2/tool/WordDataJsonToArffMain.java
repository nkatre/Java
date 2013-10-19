package edu.sjsu.cmpe226.prj2.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.map.ObjectMapper;

import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class WordDataJsonToArffMain 
{
	public static final String jsonFile = "/tmp/wiki_neo4j.json.txt";
	
	public static final String arffFile = "/tmp/wiki_words.arff";
	
	public static final String REL_HEADER = "@RELATION";
	
	public static final String ATTR_HEADER = "@ATTRIBUTE";
	
	public static final String DATA_HEADER = "@DATA";

	
	private Scanner reader = null;
	private BufferedWriter writer = null;
	
	private Set<String> terms = new TreeSet<String>();
	private Set<String> meanings = new TreeSet<String>();
	private Set<String> antonyms = new TreeSet<String>();
	private Set<String> synonyms = new TreeSet<String>();
	private Set<String> hypernyms = new TreeSet<String>();
	private Set<String> hyponyms = new TreeSet<String>();
	private Set<String> homonyms = new TreeSet<String>();
	private Set<String> holonyms = new TreeSet<String>();
	private Set<String> meronyms = new TreeSet<String>();
	private Set<String> othernyms = new TreeSet<String>();
	private Set<String> pos = new TreeSet<String>();
	private Map<String, String> dataMap = new TreeMap<String, String>();
	
	
	public WordDataJsonToArffMain()
		throws Exception
	{
		
		try
		{
			reader = new Scanner(new File(jsonFile));
			writer = new BufferedWriter(new FileWriter(arffFile));
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public void convert()
	throws Exception
	{
		ObjectMapper objMapper = new ObjectMapper();
		while (reader.hasNextLine())
		{
			String line = reader.nextLine();
			String word = null;
			try {
				word = objMapper.readValue(line, String.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ( word != null )
			{
				String meaning = word.getMeaning();
				String term = word.getTerm();
				PartsOfSpeech pos = word.getPartsOfSpeech();
				terms.add(term);
				
				if ( (null != meaning) && (!meaning.isEmpty()))
				{
					meanings.add(meaning);
				} else
					meanings.add("NONE");
				
				if ( null != pos)
					this.pos.add(pos.toString());
				else
					this.pos.add(PartsOfSpeech.None.toString());
				
				if ( null != word.getSynonyms())
					synonyms.addAll(word.getSynonyms());				
				if ( null != word.getAntonyms())
					antonyms.addAll(word.getAntonyms());
				if ( null != word.getHyponyms())
					antonyms.addAll(word.getHyponyms());
				if ( null != word.getHypernyms())
					antonyms.addAll(word.getHypernyms());
				if ( null != word.getHomonyms())
					antonyms.addAll(word.getHomonyms());
				if ( null != word.getHolonyms())
					antonyms.addAll(word.getHolonyms());
				if ( null != word.getMeronyms())
					antonyms.addAll(word.getMeronyms());
				if ( null != word.getOtherRelated())
					antonyms.addAll(word.getOtherRelated());
				
				dataMap.put(term, word);
			}
		}
		
		reader.close();
		
		// ARFF Generator
		
		// Create Headers
		writer.write(REL_HEADER);
		writer.write(" ");
		writer.write("wordDictionary");
		writer.newLine();
		writer.newLine();
		
		writeAttributeSet("term", terms);
		writeAttributeSet("'Part Of Speech'", pos);
		writeAttributeSet("meaning", meanings);
		
		writeAttributeSet2("synonym :", synonyms);
		writeAttributeSet2("antonym :", antonyms);
		writeAttributeSet2("hypernym :", hypernyms);
		writeAttributeSet2("hyponym :", hyponyms);
		writeAttributeSet2("homonym :", homonyms);
		writeAttributeSet2("holonym :", holonyms);
		writeAttributeSet2("meronym :", meronyms);
		writeAttributeSet2("othernym :", othernyms);
		
		writer.newLine();
		writer.write(DATA_HEADER);
		writer.newLine();
		
		Iterator<String> itr = dataMap.keySet().iterator();
		
		while ( itr.hasNext())
		{
			String k = itr.next();
			String w = dataMap.get(k);
			
			String term = w.getTerm();
			term = term.replaceAll("[{}()']", " ");
			term = "'" + term + "'";
			writer.write(term);
			writer.write(",");
			
			
			if ( null != w.getPartsOfSpeech())
				writer.write(w.getPartsOfSpeech().toString());
			else
				writer.write(PartsOfSpeech.None.toString());
			writer.write(",");
			
			
			if ( (null != w.getMeaning()) && (! w.getMeaning().isEmpty()))
			{
				String meaning = w.getMeaning().replaceAll("[{}()']", " ");
				meaning = "'" + meaning + "'";
				writer.write(meaning);
			} else
				writer.write("NONE");
			
			writer.write(",");
			

			writeValues(w.getSynonyms(), synonyms);
			writeValues(w.getAntonyms(), antonyms);
			writeValues(w.getHypernyms(), hypernyms);
			writeValues(w.getHyponyms(), hyponyms);
			writeValues(w.getHomonyms(), homonyms);
			writeValues(w.getHolonyms(), holonyms);
			writeValues(w.getMeronyms(), meronyms);
			writeValues(w.getOtherRelated(), othernyms);
			writer.newLine();
		}
		
		writer.close();
		
		System.out.println("Done writing to " + arffFile);
	} 
	
	public void writeValues(Set<String> values, Set<String> headings)
		throws Exception
	{
		if ( headings.isEmpty())
			return;
		
		for (String s1 : headings)
		{
			writer.write(",");
			if (values.contains(s1))
				writer.write("true");
			else 
				writer.write("false");
		}
	}
	
	public void writeAttributeSet2(String prefix, Set<String> attrSet)
		throws Exception
	{
		if (attrSet.isEmpty())
			return;
		
		for (String s : attrSet)
		{
			s = s.replaceAll("[{}()']", " ");
			writer.write(ATTR_HEADER);
			writer.write(" '");
			writer.write(prefix + s);
			writer.write("' {false, true}");
			writer.newLine();
		}		
	}
	
	public void writeAttributeSet(String attr, Set<String> attrSet)
		throws Exception
	{
		if (attrSet.isEmpty())
			return;
		
		writer.write(ATTR_HEADER);
		writer.write(" ");
		writer.write(attr);
		writer.write(" {");
		
		String[] arr = new String[attrSet.size()];
		attrSet.toArray(arr);
		
		for ( int i = 0; i < (arr.length -1); i++ )
		{
			String s = arr[i];
			s = s.replaceAll("[{}()']"," ");
			s = "'" + s + "'";
			writer.write(s + ",");
		}
		
		String s = arr[arr.length - 1];
		s = s.replaceAll("[{}()']"," ");
		s = "'" + s + "'";
		writer.write(s);
		writer.write("}");
		writer.newLine();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
		throws Exception
	{
		WordDataJsonToArffMain main  = new WordDataJsonToArffMain();
		
		main.convert();

	}
}
