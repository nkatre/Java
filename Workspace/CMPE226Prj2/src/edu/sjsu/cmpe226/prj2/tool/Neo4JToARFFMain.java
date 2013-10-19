package edu.sjsu.cmpe226.prj2.tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.sjsu.cmpe226.prj2.dao.Neo4JWordGraphDAO;
import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class Neo4JToARFFMain 
{
	public static final String NEO4J_DB_PATH = "/tmp/wiki_neo4j.db";

	private Neo4JWordGraphDAO _dao = null;
	
	public static final String arffFile = "/tmp/wiki_words_new.arff";
	
	public static final String REL_HEADER = "@RELATION";
	
	public static final String ATTR_HEADER = "@ATTRIBUTE";
	
	public static final String DATA_HEADER = "@DATA";

	private BufferedWriter writer = null;
	
	private Set<String> terms = new TreeSet<String>();
	private Set<String> meanings = new TreeSet<String>();
	private Set<String> pos = new TreeSet<String>();
	private Set<String> allRelated = new TreeSet<String>();
	
	public Neo4JToARFFMain()
		throws Exception
	{
		_dao = new Neo4JWordGraphDAO(NEO4J_DB_PATH);
		try
		{
			writer = new BufferedWriter(new FileWriter(arffFile));
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public void convert()
		throws Exception
	{
		List<String> words = _dao.getAllNodes();
		
		for (String w : words)
		{
			if ( w.getTerm() == null)
				continue;
			
			terms.add(w.getTerm());
			
			if ( (w.getMeaning() ==null))
				meanings.add("");
			else
				meanings.add(w.getMeaning());
			
			if ( (w.getPartsOfSpeech() == null ))
				pos.add(PartsOfSpeech.None.toString());
			else
				pos.add(w.getPartsOfSpeech().toString());
			
			if (! w.getAntonyms().isEmpty())
				allRelated.addAll(w.getAntonyms());
			
			if (! w.getSynonyms().isEmpty())
				allRelated.addAll(w.getSynonyms());
			
			if (! w.getHomonyms().isEmpty())
				allRelated.addAll(w.getHomonyms());
			
			if (! w.getHolonyms().isEmpty())
				allRelated.addAll(w.getHolonyms());
			
			if (! w.getHypernyms().isEmpty())
				allRelated.addAll(w.getHypernyms());
			
			if (! w.getHyponyms().isEmpty())
				allRelated.addAll(w.getHyponyms());
			
			if (! w.getMeronyms().isEmpty())
				allRelated.addAll(w.getMeronyms());
			
			if (! w.getOtherRelated().isEmpty())
				allRelated.addAll(w.getOtherRelated());
		}
		
		// ARFF Generator
		
		// Create Headers
		writer.write(REL_HEADER);
		writer.write(" ");
		writer.write("wordDictionary");
		writer.newLine();
		writer.newLine();
		
		
		HashSet<String> typeSet = new HashSet<String>();
		typeSet.add("Synonym");
		typeSet.add("Antonym");
		typeSet.add("Hypernym");
		typeSet.add("Hyponym");
		typeSet.add("Homonym");
		typeSet.add("Holonym");
		typeSet.add("Meronym");
		typeSet.add("Other");
		
		writeAttributeSet("term", terms);
		writeAttributeSet("'Part Of Speech'", pos);
		writeAttributeSet("meaning", meanings);
		writeAttributeSet("type", typeSet);
		writeAttributeSet("related", allRelated);

		writer.newLine();
		writer.write(DATA_HEADER);
		writer.newLine();
		
		for (String w : words)
		{
			if ( w.getTerm() == null)
				continue;
			
			String term = w.getTerm();
			String meaning = w.getMeaning();
			
			String part = PartsOfSpeech.None.toString();			
			if ( null != w.getPartsOfSpeech())
				part = w.getPartsOfSpeech().toString();
			
			writeAttrValues("Synonym", term, part, meaning, allRelated, w.getSynonyms());
			writeAttrValues("Antonym", term, part, meaning, allRelated, w.getAntonyms());
			writeAttrValues("Hypernym", term, part, meaning, allRelated, w.getHypernyms());
			writeAttrValues("Hyponym", term, part, meaning, allRelated, w.getHyponyms());
			writeAttrValues("Homonym", term, part, meaning, allRelated, w.getHomonyms());
			writeAttrValues("Holonym", term, part, meaning, allRelated, w.getHolonyms());
			writeAttrValues("Meronym", term, part, meaning, allRelated, w.getMeronyms());
			writeAttrValues("Other", term, part, meaning, allRelated, w.getOtherRelated());
		}
		
		writer.close();
		System.out.println("Done writing to :" + arffFile);
	}
	
	public void writeAttrValues(String label,
								String term, 
								String pos, 
								String meaning, 
								Set<String> allRelated, 
								Set<String> related)
		throws Exception
	{
		if ( (null == related) || (related.isEmpty()))
				return;
		
		term = cleanString(term);
		pos = cleanString(pos);
		meaning = cleanString(meaning);
		
		for (String s : related)
		{
			writer.write(term);
			writer.write(",");
			writer.write(pos);
			writer.write(",");
			writer.write(meaning);
			writer.write(",");
			writer.write(label);
			writer.write(",");
			writer.write(cleanString(s));
			writer.newLine();
		}
	}
	
	private String cleanString(String s)
	{
		if ( (null == s) || (s.isEmpty()))
			return "NONE";
		
		s = s.replaceAll("[{}()']" ," ");
		s = "'" + s + "'";
		
		return s;
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
			String s = cleanString(arr[i]);
			writer.write(s + ",");
		}

		String s = cleanString(arr[arr.length - 1]);
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
		Neo4JToARFFMain main = new Neo4JToARFFMain();
		main.convert();
	}

}
