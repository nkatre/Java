package edu.sjsu.cmpe226.prj2.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class WordDataJsonToCSVMain 
{
	public static final String jsonFile = "/tmp/wiki_neo4j.json.txt";

	public static final String csvFilePrefix = "/tmp/wiki_relations_";
	public static final String csvFileSuffix = ".csv";
	
	private Scanner reader = null;
	private BufferedWriter meaningWriter = null;
	private BufferedWriter posWriter = null;
	private BufferedWriter synonymWriter = null;
	private BufferedWriter antonymWriter = null;
	private BufferedWriter hyponymWriter = null;
	private BufferedWriter hypernymWriter = null;
	private BufferedWriter homonymWriter = null;
	private BufferedWriter holonymWriter = null;
	private BufferedWriter meronymWriter = null;
	private BufferedWriter otherWriter = null;

	public WordDataJsonToCSVMain()
	{
		try
		{
			reader = new Scanner(new File(jsonFile));
			meaningWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "meaning" + csvFileSuffix));
			posWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "pos" + csvFileSuffix));
			synonymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "synonym" + csvFileSuffix));
			antonymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "antonym" + csvFileSuffix));
			hyponymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "hyponym" + csvFileSuffix));
			hypernymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "hypernym" + csvFileSuffix));
			homonymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "homonym" + csvFileSuffix));
			holonymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "holonym" + csvFileSuffix));
			meronymWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "meronym" + csvFileSuffix));
			otherWriter = new BufferedWriter(new FileWriter(csvFilePrefix + "others" + csvFileSuffix));

		
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
				
				if ( null != meaning )
				{
					meaningWriter.write(term + "," + meaning);
					meaningWriter.newLine();
				}
				
				if (null != pos)
				{
					posWriter.write(term + "," + pos.toString());
					posWriter.newLine();
				}
				
				Set<String> synonyms = word.getSynonyms();
				
				writeSet(term, word.getSynonyms(), synonymWriter);
				writeSet(term, word.getAntonyms(), antonymWriter);
				writeSet(term, word.getHypernyms(), hypernymWriter);
				writeSet(term, word.getHyponyms(), hyponymWriter);
				writeSet(term, word.getHomonyms(), homonymWriter);
				writeSet(term, word.getHolonyms(), holonymWriter);
				writeSet(term, word.getMeronyms(), meronymWriter);
				writeSet(term, word.getOtherRelated(), otherWriter);
			}
		}
		
		close();
	}

	
	private void writeSet(String term, Set<String> relations, BufferedWriter writer)
		throws Exception
	{
		if (( null == relations) || relations.isEmpty())
			return;
		
		for (String s : relations)
		{
			writer.write(term + "," + s);
			writer.newLine();
		}
	}
	
	public void close()
		throws Exception
	{
		meaningWriter.close();
		synonymWriter.close();
		antonymWriter.close();
		posWriter.close();
		hypernymWriter.close();
		hyponymWriter.close();
		holonymWriter.close();
		homonymWriter.close();
		meronymWriter.close();
		otherWriter.close();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
		throws Exception
	{
		WordDataJsonToCSVMain main = new WordDataJsonToCSVMain();
		main.convert();
	}

}
