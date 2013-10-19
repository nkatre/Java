package edu.sjsu.cmpe295b.planhercareer.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.sjsu.cmpe295b.planhercareer.dto.Company;
import edu.sjsu.cmpe295b.planhercareer.dto.Group;
import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.Person;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;
import edu.sjsu.cmpe295b.planhercareer.dto.School;
import edu.sjsu.cmpe295b.planhercareer.dto.Student;

public class Neo4JPlanHerDAO 
{
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



	private String _dbpath;
	private GraphDatabaseService _db;
    private Index<Node> nameIndex;
    private Index<Node> idIndex;


	public Neo4JPlanHerDAO(String dbPath)
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

	public boolean addFriendConnection(String person1, String person2)
	{
		return addConnection(person1, person2, ConnectionRelationship.FB_FRIEND, VertexType.PERSON, VertexType.PERSON, null);
	}

	public boolean addCompanyConnection(String person1, String companyId, JobPosition j)
	{
		Map<String, String> m = new HashMap<String,String>();
		
		m.put(sJobPositionId, j.getPositionId());
		m.put(sJobPosition, j.getPosition());
		m.put(sJobLocationId, j.getLocationId());
		m.put(sJobLocation, j.getLocation());
		m.put(sJobStartDate, j.getStartDate());
		m.put(sJobEndDate, j.getEndDate());
		
		
		return addConnection(person1, companyId, ConnectionRelationship.FB_COMPANY, VertexType.PERSON, VertexType.COMPANY, m);
	}
	
	public boolean addSchoolConnection(String person1, String schoolId, Student s)
	{
		Map<String, String> m = new HashMap<String,String>();
		
		m.put(sStudentYearId, s.getYearId());
		m.put(sStudentYear, s.getYear());
		m.put(sStudentConcentrationId, s.getConcentrationId());
		m.put(sStudentConcentration, s.getConcentration());
		
		return addConnection(person1, schoolId, ConnectionRelationship.FB_EDUCATION_HIGH_SCHOOL, VertexType.PERSON, VertexType.SCHOOL, m);
	}
	
	public boolean addCollegeConnection(String person1, String collegeId, Student s)
	{
		Map<String, String> m = new HashMap<String,String>();
		
		m.put(sStudentYearId, s.getYearId());
		m.put(sStudentYear, s.getYear());
		m.put(sStudentConcentrationId, s.getConcentrationId());
		m.put(sStudentConcentration, s.getConcentration());
		
		return addConnection(person1, collegeId, ConnectionRelationship.FB_EDUCATION_COLLEGE, VertexType.PERSON, VertexType.COLLEGE, m);
	}
	
	public boolean addCompanyRoleConnection(String companyId, String positionId)
	{
		return addConnection(companyId, positionId, ConnectionRelationship.FB_JOB_POSITION, VertexType.COMPANY, VertexType.JOB_ROLE, null);
	}
	
	public boolean addEduConcentrationConnection(String collegeId, String concentrationId)
	{
		return addConnection(collegeId, concentrationId, ConnectionRelationship.FB_EDU_BRANCHES, VertexType.COLLEGE, VertexType.EDU_CONCENTRATION, null);
	}
	
	public boolean addGroupConnection(String person1, String groupId)
	{
		return addConnection(person1, groupId, ConnectionRelationship.FB_GROUPS, VertexType.PERSON, VertexType.GROUP, null);
	}
	
	public void updateSimilarityVectorForFirstDegree(String id)
	{
		Node n1 = getNodeById(id);
		Iterable<Relationship> relations = n1.getRelationships(ConnectionRelationship.FB_FRIEND);
		
		Iterator<Relationship> itr = relations.iterator();
		while (itr.hasNext())
		{
			Relationship r = itr.next();
			r.getEndNode();
			
		}
	}
	
	private boolean addConnection(String id1, 
								String id2, 
								ConnectionRelationship relation, 
								VertexType expectedType1, 
								VertexType expectedType2,
								Map<String, String> propMap)
	{
		//System.out.println("ID1 :" + id1 + ", ID2 :" + id2 + ".");
		
		Node n1 = getNodeById(id1);
		Node n2 = getNodeById(id2);
		
		if ((n1 == null) || (n2 == null))
			return false;
		if ( ! expectedType1.name().equals(n1.getProperty(sType)))
			throw new RuntimeException("Unexpected type for id : " + id1 + ", Expected :" + expectedType1 + ", Got :" + n1.getProperty(sType));
		
		if ( ! expectedType2.name().equals(n2.getProperty(sType)))
			throw new RuntimeException("Unexpected type for id : " + id2 + ", Expected :" + expectedType2 + ", Got :" + n2.getProperty(sType));
        
		Transaction tx = _db.beginTx();
        try
        {
        		Relationship r = getRelationship(n1, n2, relation);
        		
        		if ( null == r)
        			r = n1.createRelationshipTo(n2, relation);
        		
        		if ( null != propMap)
        		{
        			for (Entry<String,String> e : propMap.entrySet())
        			{
        				r.setProperty(e.getKey(), e.getValue());
        			}
        		}
        		tx.success();
        } finally {
        		tx.finish();
        }
        return true;
	}
	
	private Relationship getRelationship(Node n1, Node n2, ConnectionRelationship r)
	{
		Iterator<Relationship> itr = n1.getRelationships(r).iterator();
		while ( itr.hasNext())
		{
			Relationship r2 = itr.next();
			
			if (r2.getEndNode().equals(n2))
				return r2;
		}
		return null;
	}
	private Node getNodeById(String id)
	{
        Node n = idIndex.get(sId,id).getSingle();
		return n;
	}
	
	private Node getNodeByName(String name)
	{
        Node n = nameIndex.get(sName,name).getSingle();
		return n;
	}
	
	public Node createNewCompanyNode(Company c)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(c.getId(), c.getName(), VertexType.COMPANY);
			List<PositionSalary> salaries = c.getRoleSalaryData();
			int i = 0;
			for (PositionSalary s : salaries)
			{
				String sId = getSalaryPosId(c.getId(), "" + i);
				String name = getSalaryPosId(c.getId(), s.getPosition());
				Node n2 = createNewNode(sId, name, VertexType.SALARY_POSITION);
				n2.setProperty(sSalaryPosition, s.getPosition());
				n2.setProperty(sSalaryLow, s.getLowRangeSalary());
				n2.setProperty(sSalaryHigh, s.getHighRangeSalary());
				n2.setProperty(sSalaryMean, s.getMeanSalary());
				n2.setProperty(sSalarySamples, s.getNumSamples());
 
				//Add CompanySalaryPos
				addConnection(c.getId(),
							sId, 
							ConnectionRelationship.COMPANY_SALARY_POS,
							VertexType.COMPANY, 
							VertexType.SALARY_POSITION, 
							null);
				i++;
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	private static String getSalaryPosId(String parentId, String childId)
	{
		return SALARY_POSITION_ID_PREFIX + parentId + "_" + childId;
	}
	public Node createNewSchoolNode(School s)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(s.getId(), s.getName(), VertexType.SCHOOL);
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	public Node createNewConcentrationNode(Student s)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(s.getConcentrationId(), s.getConcentration(), VertexType.EDU_CONCENTRATION);
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	public Node createNewJobRoleNode(JobPosition s)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(s.getPositionId(), s.getPosition(), VertexType.JOB_ROLE);
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	public Node createNewCollegeNode(School s)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(s.getId(), s.getName(), VertexType.COLLEGE);
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	public Node createNewGroupNode(Group g)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = createNewNode(g.getId(), g.getName(), VertexType.GROUP);
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}
	
	public Node createNewPersonNode(Person p)
	{
		Node n = null;
		Transaction tx = _db.beginTx();
		try
		{
			n = 	createNewNode(p.getId(), p.getName(), VertexType.PERSON);
			n.setProperty(sPersonId,p.getUserName());
			n.setProperty(sPersonGender, p.getGender().name());
			n.setProperty(sPersonDob, p.getDob());
			tx.success();
		} finally {
			tx.finish();
		}
		return n;
	}

	private Node createNewNode(String id, String name, VertexType type)
	{
		Node n = getNodeById(id);
		if ( null == n)
		{
			n = _db.createNode();	
			n.setProperty(sId, id);
			idIndex.add(n,sId,id);
			nameIndex.add(n,sName,name);
		} else {
			if (! name.equals(n.getProperty(sName)))
			{
				nameIndex.remove(n, sName, n.getProperty(sName));
				nameIndex.add(n, sName, name);
			}
		}
		n.setProperty(sName, name);
		n.setProperty(sType, type.name());
		return n;
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
	private static void onShutdown(final Neo4JPlanHerDAO dao) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if ( null != dao.getDb())
					dao.getDb().shutdown();
			}
		});
	}




}
