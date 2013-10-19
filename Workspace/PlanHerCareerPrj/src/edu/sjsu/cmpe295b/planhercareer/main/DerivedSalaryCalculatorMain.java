package edu.sjsu.cmpe295b.planhercareer.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4JPlanHerBaseDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4JPlanHerBaseDAO.ConnectionRelationship;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4JPlanHerBaseDAO.VertexType;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4jPlanHerSemanticsDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;

public class DerivedSalaryCalculatorMain 
{
	public static void main(String[] args)
		throws Exception
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Neo4jPlanHerSemanticsDAO dao = new Neo4jPlanHerSemanticsDAO("/Users/ksjeyabarani/Documents/neo4j-community-1.9.4/data/planhercareergen.db");
		try
		{
			conn = PostGresDAO.getConnection();

			// Add Person
			String sql = 
					"SELECT ID, \"name\" FROM \"FB_PERSON_1\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String userId = rs.getString(1);
				String userName = rs.getString(2);
				System.out.println("Calculating approx Salary for : " + userName 
						            + "(" + userId + ")");
				
				JobPosition j = dao.getCurrentJobPositionForUser(userId);
				System.out.println(userName + "has job position :" + j);
				List<PositionSalary> s = dao.getAllCmpySalaries(j.getCompanyId());
				PositionSalary closest = getClosestPositionInCompany(j, s);
				System.out.println("Closest approx position with salary is :" + closest);
				
				// Delete Old derived value
				if ( null != closest)
				{
					dao.removeRelationshipByType(userId, ConnectionRelationship.PERSON_DERIVED_SALARY);
					HashMap<String, String> m = new HashMap<String,String>();
					m.put(Neo4JPlanHerBaseDAO.sCompanyId, j.getCompanyId()); // Add companyId as relationship attribute
					boolean success = dao.addConnection(userId, 
						          closest.getId(), 
						          ConnectionRelationship.PERSON_DERIVED_SALARY, 
						          VertexType.PERSON, 
						          VertexType.SALARY_POSITION, 
						          m);
					if ( !success)
						throw new Exception("Add connection failed !!");
				}
			}
		} finally {
			if ( null != conn)
				conn.close();
			dao.close();
		}
	}
	
	// Using Levenshtein as metric for job position similarity
	public static PositionSalary getClosestPositionInCompany(JobPosition p, 
													List<PositionSalary> cmpyPos)
	{
		PositionSalary selected = null;
		AbstractStringMetric metric = new Levenshtein();
		float f = 0.0f;
		for (PositionSalary s : cmpyPos)
		{
			float f2 = metric.getSimilarity(p.getPosition(), s.getPosition());
			if ( f2 > f)
			{
				f = f2;
				selected = s; 
			}
		}
		
		return selected;
	}
	
}
