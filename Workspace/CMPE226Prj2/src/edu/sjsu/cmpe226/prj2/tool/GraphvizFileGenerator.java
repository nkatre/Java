package edu.sjsu.cmpe226.prj2.tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sjsu.cmpe226.prj2.dao.CassandraNodeWordDAO;
import edu.sjsu.cmpe226.prj2.dao.Neo4JWordGraphDAO;
import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dao.WordGraphDAO.WordRelationship;

public class GraphvizFileGenerator 
{
	//public static final String NEO4J_DB_PATH = "//tmp//foo.db";
	public static final String NEO4J_DB_PATH = "/tmp/foo.db";

	//public static final String NEO4J_DB_PATH = "C://tmp//wiki_neo4j.db";
	private Neo4JWordGraphDAO _dao = null;
	
	//private CassandraWordDAO _dao = null;
	
	public static final String dotFile = "dot_file.dot";
	
	public static final String HEADER = "digraph wicktionary {";
	
	public static final String NODE = "node [color=Red,fontname=Courier]";
	
	public static final String EDGE = "edge [color=Blue]";
	
	public static final String FOOTER = "}";
	
	private BufferedWriter writer = null;
	
	
	public GraphvizFileGenerator() throws Exception
	{
		_dao = new Neo4JWordGraphDAO(NEO4J_DB_PATH);
		//_dao = new CassandraWordDAO();
		try
		{
			writer = new BufferedWriter(new FileWriter(dotFile));
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	// For a single node to convert to a graph
	public String convertANodeToText(String term) throws Exception
	{
		String w = _dao.getNode(term);
		String graphvizText = "";
		
		if(w == null){
			return null;
		}
		
		return generateText(w, graphvizText);
		
	}
	//Eliminating/Replacing the unnecessary patterns
	private String patternFinder(String text){
		
		Pattern style = Pattern.compile("-");
		Matcher mstyle = style.matcher(text);
		while (mstyle.find()) text = mstyle.replaceAll("_");
		
		Pattern style1 = Pattern.compile("\\(.*\\)");
		Matcher mstyle1 = style1.matcher(text);
		while (mstyle1.find()) text = mstyle1.replaceAll("");
	
		Pattern style3 = Pattern.compile(":");
		Matcher mstyle3 = style3.matcher(text);
		while (mstyle3.find()) text = mstyle3.replaceAll("_");
	
	/*	Pattern style4 = Pattern.compile("^\\s+");
		Matcher mstyle4 = style4.matcher(text);
		while (mstyle4.find()) text = mstyle4.replaceAll("");*/
	
		Pattern style4 = Pattern.compile("^\\s*$");
		Matcher mstyle4 = style4.matcher(text);
		while (mstyle4.find()) return null;//text = mstyle4.replaceAll("");*/
	
		Pattern style5 = Pattern.compile(".*+\\s.*");
		Matcher mstyle5 = style5.matcher(text);
		while (mstyle5.find()) text = mstyle5.replaceAll("_");
	
		return text;
		
	}
	
	//Generating the text
	private String generateText(String w, String graphvizText){
		
		Iterator<String> it = null;
	
		String name = w.getTerm();
		if ( name == null){
			System.out.println("Term not exists in the database");
			return null;
		}
		else{
			
			Set<String> synonyms = new HashSet<String>();
			synonyms = w.getSynonyms();
			it = synonyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
					graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Synonym+"\", fontcolor=blue];"+"\n";				 
				}
			}
		
			Set<String> antonyms = new HashSet<String>();
			antonyms = w.getAntonyms();
			it =  antonyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
					graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Antonym+"\", fontcolor=blue];"+"\n";
				}
			}
		
			Set<String> homonyms = new HashSet<String>();
			homonyms = w.getHomonyms();
			it =  homonyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Homonym+"\", fontcolor=blue];"+"\n";
				}
			}
	
			Set<String> meronyms = new HashSet<String>();
			meronyms = w.getMeronyms();
			it =  meronyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Meronym+"\", fontcolor=blue];"+"\n";
				}
			}
			
			Set<String> holonyms = new HashSet<String>();
			holonyms = w.getHolonyms();
			it =  holonyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Holonym+"\", fontcolor=blue];"+"\n";
				}
			}
			
			Set<String> hypernyms = new HashSet<String>();
			hypernyms = w.getHypernyms();
			it =  hypernyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Hypernym+"\", fontcolor=blue];"+"\n";
				}
			}
			
			Set<String> hyponyms = new HashSet<String>();
			hyponyms = w.getHyponyms();
			it =  hyponyms.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Hyponym+"\", fontcolor=blue];"+"\n";
				}
			}
			
			Set<String> others = new HashSet<String>();
			others = w.getOtherRelated();
			it =  others.iterator();			
			while(it.hasNext()){
				String text = null;
				text = patternFinder(it.next().toString());
				if(text!=null){
				graphvizText+=name+"->"+text+"[label=\""+WordRelationship.Other+"\", fontcolor=blue];"+"\n";
				}
			}
			//graphvizText+=name+"[label= \"term:" +name+",meaning:"+w.getMeaning()+",POF:"+w.getPartsOfSpeech()+"\" ]"; 
			
		}
			//System.out.println(graphvizText);
			return graphvizText;
		
	}
	
	
	//Graph based on a term and a relation
	public String ConvertByRelation(String term, WordRelationship relation){
		
		List<String> words = _dao.getAllConnectionsForWord(term, relation);
		String contents = "";
		String firstNode = "";
		
		for (String w : words){
			String text = null;
			text = patternFinder(w.getTerm());
			firstNode=term+"->"+text+"[label=\"" + relation.toString() +"\", fontcolor=blue];"+"\n";	
			contents = firstNode+generateText(w, contents);
		}
		return contents;
	}
	
	//Writing the dot file
	public void generateDOTFile(String contents) throws IOException{
		writer.write(HEADER);
		writer.write(NODE);
		writer.write(EDGE);
		writer.write(contents);
		writer.write(FOOTER);
		writer.close();
		System.out.println("Done converting");
		
	}
	
	//Generating text for a word and level
	public String convertNodeToTextByLevel(String term, int level){
	
		String graphvizText = "";
		String w = null;

		w = _dao.getNode(term);
		if(w == null){
			return null;
		}				
		graphvizText =  generateText(w, "");
		
		graphvizText += iterateOverRelation(w.getSynonyms(), level);
		graphvizText += iterateOverRelation(w.getAntonyms(), level);
		
		return graphvizText;
	}
	
	//Recursive function that goes till the level
	private String iterateOverRelation(Set<String> relation, int level){
		
		String w = null;
		Iterator<String> iterator = null;
		String graphvizText = "";
		int iter;
		
		if(level==1)
		{
			return graphvizText;
		}
		else {
			//iter = level-1;
			iterator = relation.iterator();		
			--level;
			while(iterator.hasNext()){
				w = _dao.getNode(iterator.next().toString());
				if(w != null){
				
					graphvizText += generateText(w, "") + iterateOverRelation(w.getSynonyms(), level) + iterateOverRelation(w.getAntonyms(), level) ;
					System.out.println("TEST = "+graphvizText);
				}
			}
			return graphvizText;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		GraphvizFileGenerator main = new GraphvizFileGenerator();
		String contents = null;
		
		/*contents = main.convertANodeToText("friend");
		if(contents == null){
			System.out.println("Term not exists in the database");
		}
		else{
			main.generateDOTFile(contents);
		}*/
		
		
		
	/*	contents = main.ConvertByRelation("caution", WordRelationship.Synonym);
		if(contents == null){
			System.out.println("Term not exists in the database");
		}
		else{
			main.generateDOTFile(contents);
		}*/
	
		 
		contents = main.convertNodeToTextByLevel("friend", 4);
		if(contents == null){
			System.out.println("Term not exists in the database");
		}
		else{
			main.generateDOTFile(contents);
		}
		
	}

}
