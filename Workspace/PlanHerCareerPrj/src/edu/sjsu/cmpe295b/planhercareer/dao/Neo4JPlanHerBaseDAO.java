package edu.sjsu.cmpe295b.planhercareer.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

public class Neo4JPlanHerBaseDAO {
	public static final String SALARY_POSITION_ID_PREFIX = "salpos_";
	
	/**
	 * Named edge to define the relationship between two Nodes
	 */
	public static enum ConnectionRelationship
	implements RelationshipType
	{
		FB_FRIEND, // Attributes : Friend type
		FB_EDUCATION_HIGH_SCHOOL, //Attributes : Grad date
		FB_EDUCATION_COLLEGE, // Attributes: Concentration, Grad date
		FB_GROUPS, 
		FB_COMPANY, // Attributes, Dates, Position 
		FB_JOB_POSITION,
		FB_EDU_BRANCHES,
		COMPANY_SALARY_POS,
		PERSON_DERIVED_SALARY,
	}

	public static enum VertexType
	{
		PERSON,
		COMPANY,
		GROUP,
		SCHOOL,
		COLLEGE,
		JOB_ROLE,
		EDU_CONCENTRATION,
		SALARY_POSITION,
	}

	public static final String sDBPath = "db.path";

	/***    					COMMON FIELDS           **/
	/**
	 * the Name field for all nodes
	 */
	public static final String sId = "common.id";	

	/**
	 * the Name field for all nodes
	 */
	public static final String sType = "common.type";	
	
	/**
	 * the DOB field for Person type Nodes
	 */
	public static final String sName = "common.name";


	/***    					PERSON FIELDS           **/

	/**
	 * the DOB field for Person type Nodes
	 */
	public static final String sPersonGender = "person.gender";

	/**
	 * the DOB field for Person type Nodes
	 */
	public static final String sPersonDob = "person.dob";

	/**
	 * the DOB field for Person type Nodes
	 */
	public static final String sPersonId = "person.userid";

	/***  Job Position **/
	public static final String sJobPositionId = "job.positionid";
	public static final String sJobPosition = "job.position";
	public static final String sJobLocationId = "job.locationId";
	public static final String sJobLocation = "job.location";
	public static final String sJobStartDate = "job.startdate";
	public static final String sJobEndDate = "job.enddate";

	/** Student  **/
	public static final String sStudentYearId = "student.yearid";
	public static final String sStudentYear = "student.year";
	public static final String sStudentConcentrationId = "student.concentrationid";
	public static final String sStudentConcentration = "student.concentration";
	
	/** Salary Position **/
	public static final String sSalaryPosition = "sal.pos";
	public static final String sSalaryLow = "sal.low";
	public static final String sSalaryHigh = "sal.high";
	public static final String sSalaryMean = "sal.mean";
	public static final String sSalarySamples = "sal.samples";

	public static final String sCompanyId = "gen.companyid";

	protected String _dbpath;
	protected GraphDatabaseService _db;
	protected Index<Node> nameIndex;
	protected Index<Node> idIndex;
	
	public Neo4JPlanHerBaseDAO(String dbPath)
	{
		_dbpath = dbPath;
		init(dbPath);
	}

	protected void init(String dbpath) 
	{

		if (dbpath == null)
			throw new RuntimeException("Missing database path");

		_db = new GraphDatabaseFactory().newEmbeddedDatabase(dbpath);
		onShutdown(this);

		nameIndex = _db.index().forNodes("nodes");
		idIndex = _db.index().forNodes("nodes");
	}

	public void close()
	{
		if (_db != null)
		{
			_db.shutdown();
			_db = null;
		}
	}

	private GraphDatabaseService getDb()
	{
		return _db;
	}


	/**
	 * on exit, close the database
	 * 
	 * @param graphDb
	 */
	private static void onShutdown(final Neo4JPlanHerBaseDAO dao) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if ( null != dao.getDb())
					dao.getDb().shutdown();
			}
		});
	}
	
	protected Relationship getRelationship(Node n1, Node n2, ConnectionRelationship r)
	{
		Iterator<Relationship> itr = null;
		
		if ( null != r )
			itr = n1.getRelationships(r).iterator();
		else
			itr = n1.getRelationships().iterator();

		while ( itr.hasNext())
		{
			Relationship r2 = itr.next();			
			if ( null != n2)
			{
				if (r2.getEndNode().equals(n2))
					return r2;
			} else {
				return r2;
			}
		}
		return null;
	}
	
	public void removeRelationshipBetweenNodes(String id1, String id2)
	{
		Node n1 = getNodeById(id1);
		Node n2 = getNodeById(id2);
		
		if ((n1 == null) || (n2 == null))
			return ;
		
		Transaction tx = _db.beginTx();
        try
        {
        		List<Relationship> relations = getAllRelationships(n1, n2, null);
        		
        		for(Relationship r : relations)
        			r.delete();
        		
        		tx.success();
        } finally {
        		tx.finish();
        }        	
	}
	
	public boolean isLinked(String id1, String id2)
	{
		Node n1 = getNodeById(id1);
		Node n2 = getNodeById(id2);
		
		if ((n1 == null) || (n2 == null))
			return false;
		
		List<Relationship> relations = getAllRelationships(n1, n2, null);
		return relations.size() > 0;       	
	}
	
	public void removeRelationshipByType(String id1, ConnectionRelationship rel)
	{
		Node n1 = getNodeById(id1);
		
		if (n1 == null)
			return ;
		
		Transaction tx = _db.beginTx();
        try
        {
        		List<Relationship> relations = getAllRelationships(n1, null, rel);
        		
        		for(Relationship r : relations)
        			r.delete();
        		
        		tx.success();
        } finally {
        		tx.finish();
        }        	
	}
	
	protected List<Relationship> getAllRelationships(Node n1, Node n2, ConnectionRelationship r)
	{
		Iterator<Relationship> itr = null;
		List<Relationship> relations = new ArrayList<Relationship>();
		
		if ( null != r )
			itr = n1.getRelationships(r).iterator();
		else
			itr = n1.getRelationships().iterator();

		while ( itr.hasNext())
		{
			Relationship r2 = itr.next();
			
			if ( null != n2)
			{
				if (r2.getEndNode().equals(n2))
					relations.add(r2);
			} else {
				relations.add(r2);
			}
		}
		return relations;
	}
	
	protected Node getNodeById(String id)
	{
        Node n = idIndex.get(sId,id).getSingle();
		return n;
	}
	
	protected Node getNodeByName(String name)
	{
        Node n = nameIndex.get(sName,name).getSingle();
		return n;
	}
}
