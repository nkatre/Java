package edu.sjsu.cmpe226.prj2.tool;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.visualization.graphviz.*;
import org.neo4j.walk.*;


public class Neo4JToDOTMain 
{
	public static final String NEO4J_DB_PATH = "/tmp/foo.db";
	
	private GraphDatabaseService db;

	public static final String dotFile = "/tmp/graphFile.dot";
	
	public static final String graphvizText = null;
	
	
	public Neo4JToDOTMain(String dbPath)
	{
		if (dbPath == null)
			throw new RuntimeException("Missing database path");

		db = new EmbeddedGraphDatabase(dbPath);
		onShutdown(db);
	}
	
	public void convert() throws Exception
	{
		GraphvizWriter writer = new GraphvizWriter();
		OutputStream output = new FileOutputStream(dotFile);
		writer.emit( output, Walker.fullGraph( db ));
		System.out.println("Done creating dot file");
	}
	
	/**
	 * on exit, close the database
	 * 
	 * @param graphDb
	 */
	private static void onShutdown(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Neo4JToDOTMain main = new Neo4JToDOTMain(NEO4J_DB_PATH);
		main.convert();
	}

}
