package edu.sjsu.cmpe226.prj2.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import edu.sjsu.cmpe226.prj2.dao.WordGraphDAO.WordRelationship;
import edu.sjsu.cmpe226.prj2.dto.String;
import edu.sjsu.cmpe226.prj2.dto.String.PartsOfSpeech;

public class CassandraWordDAO implements WordGraphDAO {

	Cluster cluster;
	Keyspace keyspaceOperator;
	private static StringSerializer stringSerializer = StringSerializer.get();
	private static final String COLUMNFAMILY = "words";

	//properties
	private enum CFProperties{
		meaning,partsofspeech;
	}
	
	
	public CassandraWordDAO() {
		// connect to the database
		cluster = HFactory.getOrCreateCluster("TestCluster", "localhost:9160");
		keyspaceOperator = HFactory.createKeyspace("wicktionary", cluster);

	}

	protected void finalize(){
		//shutdown the database
		cluster.getConnectionManager().shutdown();
	}
	
	@Override
	public void loadWordData(String word) {
		
		//WordRelationship rel = getValidRelation(word);
		HSuperColumn<String, String, String> n = null;
		try {
				/*n = getWordColumn(word.getTerm());//,word.getMeaning());
				if (null == n) {
					System.out.println("NOT EXISTS == " + n);
					createNewColumn(word);
				} else {
					System.out.println("EXISTS == " + n);
				}*/
				createNewColumn(word);
		} catch(HectorException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public String getNode(String term) 
	{
        String w = getWordDataFromNode(term);
        return w;
	}
	
	@Override
	public List<String> getAllConnectionsForWord(String term, WordRelationship relation) {
		
		List<String> connections = new ArrayList<String>();
		String w = getNode(term);

		if (null != w) {
			Set<String> connWords = null;

			switch (relation) 
			{
				case Antonym: 	connWords = w.getAntonyms();
								break;
				case Holonym:	connWords = w.getHolonyms();
								break;
				case Homonym:	connWords = w.getHomonyms();
								break;
				case Hypernym:	connWords = w.getHypernyms();
								break;
				case Hyponym:	connWords = w.getHyponyms();
								break;
				case Meronym:	connWords = w.getMeronyms();
								break;
				case Other:		connWords = w.getOtherRelated();
								break;
				case Synonym:	connWords = w.getSynonyms();
								break;
			}
			if (null != connWords) {
				for (String w2 : connWords) {
					String cWord = getNode(w2);

					if (null != cWord)
						connections.add(cWord);
				}
			}
		}
		return connections;
	}
	
	@Override
	public void addRelationShip(String term1, String term2,WordRelationship relation) 
	{
		try{
			String w = getNode(term1);
			if (null != w) {
				Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, stringSerializer);
				mutator.insert(term1,COLUMNFAMILY,HFactory.createSuperColumn(relation.toString(),Arrays.asList(HFactory.createStringColumn(term2, term2)),stringSerializer, stringSerializer, stringSerializer));
			}else{
				System.out.println("Term not exists in the database");
			}
	
		}catch(HectorException e){
			e.printStackTrace();
		}
	}
	

	@Override
	public void removeRelationShip(String term1, String term2, WordRelationship relation) 
	{
		try{
			Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, stringSerializer);
			mutator.subDelete(term1, COLUMNFAMILY, relation.toString(),term2, stringSerializer, stringSerializer);
	
		}catch(HectorException e){
			e.printStackTrace();
		}	
	}
	
	private HSuperColumn<String, String, String> getWordColumn(String term) {
		HSuperColumn<String, String, String> n = null;
		try {

			SuperColumnQuery<String, String, String, String> superColumnQuery = HFactory
					.createSuperColumnQuery(keyspaceOperator, stringSerializer,
							stringSerializer, stringSerializer,
							stringSerializer);
			superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(CFProperties.meaning.toString());
			QueryResult<HSuperColumn<String, String, String>> result = superColumnQuery.execute();

			n = result.get(); 
			
		} finally {
		}
		return n;
	}

	
		
	private String getWordDataFromNode(String term)
	{
		String  w = new String();
		SuperColumnQuery<String, String, String, String> superColumnQuery = HFactory
		.createSuperColumnQuery(keyspaceOperator, stringSerializer,
				stringSerializer, stringSerializer,
				stringSerializer);
		HSuperColumn<String, String, String> n = null;
		QueryResult<HSuperColumn<String, String, String>> result = null;
		Iterator<HColumn<String, String>> it = null;
		List<HColumn<String, String>> colNames = null;
		
		
		String meaning = null;	
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(CFProperties.meaning.toString());
		result = superColumnQuery.execute();
		n = result.get();		
		if(n != null) 
			colNames = n.getColumns();
		else
			return null; //return if the meaning column does not exists. it means the word is not present in the database.
		
		if(colNames != null) {
			it = colNames.iterator();		
			if (it.hasNext()){				
				meaning = it.next().getName();				
			} 
		}else {
			System.out.println("meaning are Null in the database");
		}
		
		
		String partsOfSpeech = null;
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(CFProperties.partsofspeech.toString());
		result = superColumnQuery.execute();
		n = result.get();		
		if(n != null) colNames = n.getColumns();		
		if(colNames != null) {
			it = colNames.iterator();		
			if (it.hasNext()){				
				partsOfSpeech = it.next().getName();				
			} 
		}else {
			System.out.println("Parts of speech are Null in the database");
		}
		
		
		Set<String> synonyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Synonym.toString());
		result = superColumnQuery.execute();
		n = result.get();		
		if(n != null) colNames = n.getColumns();		
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				synonyms.add(it.next().getName());				
			} 
		}else {
			System.out.println("Synonyms are Null in the database");
		}
		
		
		Set<String> antonyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Antonym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				antonyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Antonyms are Null in the database");
		}
		
		
		Set<String> homonyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Homonym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				homonyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Homonym are Null in the database");
		}
		
		Set<String> meronyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Meronym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				meronyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Meronym are Null in the database");
		}
		
		Set<String> holonyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Holonym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				holonyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Holonym are Null in the database");
		}
		
		Set<String> hypernyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Hypernym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				hypernyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Hypernym are Null in the database");
		}
		
		Set<String> hyponyms = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Hyponym.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				hyponyms.add(it.next().getName());				
			}
		} else {
		System.out.println("Hyponym are Null in the database");
		}
		
		Set<String> others = new HashSet<String>();
		colNames = null;
		superColumnQuery.setColumnFamily(COLUMNFAMILY).setKey(term).setSuperName(WordRelationship.Other.toString());
		result = superColumnQuery.execute();
		n = result.get(); 	
		if(n != null) colNames = n.getColumns();
		if(colNames != null) {
			it = colNames.iterator();		
			while(it.hasNext()){				
				others.add(it.next().getName());				
			}
		} else {
		System.out.println("Other related are Null in the database");
		}
		
		w.setTerm(term);
		w.setMeaning(meaning);
		w.setPartsOfSpeech(PartsOfSpeech.valueOf(partsOfSpeech));
		w.setSynonyms(synonyms);
		w.setAntonyms(antonyms);
		w.setHomonyms(homonyms);
		w.setMeronyms(meronyms);
		w.setHolonyms(holonyms);
		w.setHypernyms(hypernyms);
		w.setHyponyms(hyponyms);
		w.setOtherRelated(others);
		
		return w;
	}
		
	
	private WordRelationship getValidRelation(String data){	
		
		if(data.getSynonyms()!=null){
			return WordRelationship.Synonym;
		}else if(data.getAntonyms()!=null){
			return WordRelationship.Antonym;
		}else if(data.getHomonyms() !=null){
			return WordRelationship.Homonym;
		}else if(data.getMeronyms()!=null){
			return WordRelationship.Meronym;
		}else if(data.getHolonyms()!=null){
			return WordRelationship.Holonym;
		}else if(data.getHypernyms()!=null){
			return WordRelationship.Hypernym;
		}else if(data.getHyponyms()!=null){
			return WordRelationship.Hyponym;
		}else if(data.getOtherRelated()!=null){
			return WordRelationship.Other;
		}
		return null;
	}
	
	private void createNewColumn(String w){
		
		Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, stringSerializer);
		
			if(w.getMeaning() != null){
				mutator.insert(w.getTerm(),COLUMNFAMILY,HFactory.createSuperColumn(CFProperties.meaning.toString(),Arrays.asList(HFactory.createStringColumn(w.getMeaning(), w.getMeaning())),stringSerializer, stringSerializer, stringSerializer));
				
			
		
			if(null == w.getPartsOfSpeech()){
				mutator.insert(w.getTerm(),COLUMNFAMILY,HFactory.createSuperColumn("partsofspeech",Arrays.asList(HFactory.createStringColumn(PartsOfSpeech.None.toString(), PartsOfSpeech.None.toString())),stringSerializer, stringSerializer, stringSerializer));
			}
			else{
				mutator.insert(w.getTerm(),COLUMNFAMILY,HFactory.createSuperColumn("partsofspeech",Arrays.asList(HFactory.createStringColumn(w.getPartsOfSpeech().toString(), w.getPartsOfSpeech().toString())),stringSerializer, stringSerializer, stringSerializer));
			}
	
			Set<String> words = w.getSynonyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Synonym);
			}
			
			words = w.getAntonyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Antonym);
			}
			
			words = w.getHomonyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Homonym);
			}
			
			words = w.getMeronyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Meronym);
			}
			
			words = w.getHolonyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Holonym);
			}
			
			words = w.getHypernyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Hypernym);
			}
			
			words = w.getHyponyms();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Hyponym);
			}
			words = w.getOtherRelated();
			if(words != null){
				addColumn(mutator, w.getTerm(), words, WordRelationship.Other);
			}
			}
		
	}
	
	
	private void addColumn(Mutator<String> mutator, String key, Set<String> words, WordRelationship rel ){
		
		Iterator<String> it = words.iterator();
		try{
	    while (it.hasNext()) {
        	String word = it.next().toString(); 
        	if(word!=null){
        		//System.out.println("Word ="+word);
        		//System.out.println("key="+key);
        		mutator.insert(key,COLUMNFAMILY,HFactory.createSuperColumn(rel.toString(),Arrays.asList(HFactory.createStringColumn(word, word)),stringSerializer, stringSerializer, stringSerializer));
        	}
 	    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
		
	public void close()
	{
		cluster.getConnectionManager().shutdown();
	}

	@Override
	public boolean isMaster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pullUpdates() {
		// TODO Auto-generated method stub
		
	}

	
}



