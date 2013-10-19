package edu.sjsu.cmpe295b.planhercareer.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;





import edu.sjsu.cmpe295b.planhercareer.dao.DocumentGraphDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4JDocumentGraphDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.DocumentData;
import edu.sjsu.cmpe295b.planhercareer.util.RateMonitor;

public class BulkLoadTestNeo 
{
	public static final String NEO4J_DB = "/tmp/foo.db";

	private DocumentData Document = null;
	private DocumentGraphDAO _dao;
	private RateMonitor _rate = new RateMonitor("bulk");
	private Scanner _scanner = null;
	public BulkLoadTestNeo(String file) 
		throws IOException
	{
		_scanner = new Scanner(new File(file));
		FileUtils.deleteDirectory(new File(NEO4J_DB));
		_dao = new Neo4JDocumentGraphDAO(NEO4J_DB);
		//_dao = new CassandraDocumentDAO();
	}

	private Set<String> getSetFromString(String s)
	{
		Set<String> l = new HashSet<String>();
		
		int start = s.indexOf("[");
		int end = s.indexOf("]");
		
		String[] toks = s.substring(start+1, end).split(",");
		for (String s1 : toks)
		{
			s1 = s1.replace("'","");
			s1 = s1.replace("\"", "");
			l.add(s1);
		}

		return l;
	}
	
	private DocumentData createDocumentData(String line)
	{
		DocumentData DocumentData = null;
		try
		{
			if ( null == line )
				return null;
			
			String[] cols = line.split(";");
			
			if (cols.length != 6)
			{
				System.err.println("cols length not expected. Got :" + cols.length +", Line is :" + line);
				return null;
			}
			
			DocumentData = new DocumentData();
			DocumentData.setUrl(cols[0]);
			//DocumentData.setConcept(cols[1]);
			DocumentData.setCategory(getSetFromString(cols[2]));
			DocumentData.setEntities(getSetFromString(cols[3]));
			DocumentData.setSentiment(getSetFromString(cols[4]));
			DocumentData.setOtherRelated(getSetFromString(cols[5]));
		} catch (Exception ex) {
			System.err.println("Exception for line : " + line);
			ex.printStackTrace();
		}
		return DocumentData;
	}
	
	public void load()
	{
		System.out.println("Starting to load !!");
		String line = null;
		long index = 0L;
		_rate.start();
		_rate.suspend();
		long beginTime = System.currentTimeMillis();
		long prevTime = beginTime;
		long currTime = beginTime;
		while(_scanner.hasNextLine())
		{
			try
			{
				line = _scanner.nextLine();
				DocumentData w = createDocumentData(line);
				
				// Make sure rate only covers insertion time by resume and suspend
				_rate.resume();
				_dao.loadDocumentData(w);
				_rate.tick();
				_rate.suspend();
				
				index++;
				//System.out.println("inserting");
				// Generate Statistics per 10K rows
				if (index%1000 == 0)
				{
					prevTime = currTime;
					currTime = System.currentTimeMillis();
					System.out.println("Inserted 10K rows in " + (currTime - prevTime) 
							  +  " milli seconds. Total time elapsed :" + ((currTime - beginTime)/1000) + " seconds. Insertion Only Rate: " + _rate );
				}
				
			} catch (Exception ex) {
				System.err.println("Got exception :" + ex + " at index :" + index + ", line :" + line);
			}
		}
		
		_rate.stop();
		System.out.println("Rate Result: " + _rate);
	}
	
	public void close()
	  throws Exception
	{
		if ( null != _scanner)
			_scanner.close();
		
		if ( null != _dao)
			_dao.close();
	}
	
	public static void main(String[] args)
		throws Exception
	{
		String inputFile = "C:\\data\\new_level3_Documents_all.scsv";
		
		BulkLoadTestNeo blkLoader = new BulkLoadTestNeo(inputFile);

		blkLoader.load();
		
		blkLoader.close();
	}

}
