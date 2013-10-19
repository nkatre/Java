package edu.sjsu.cmpe295b.planhercareer.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sjsu.cmpe295b.planhercareer.dao.Neo4jPlanHerSemanticsDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.Company;
import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.Person;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;

public class JobPositionRecommender {

	public static void main(String[] args)
		throws Exception
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Neo4jPlanHerSemanticsDAO dao = new Neo4jPlanHerSemanticsDAO("/Users/ksjeyabarani/Documents/neo4j-community-1.9.4/data/planhercareergen.db");

		int numWithReco = 0;
		int numWithoutReco = 0;
		try
		{
			conn = PostGresDAO.getConnection();

			// Add Person
			String sql = 
					"SELECT \"name\" FROM \"FB_PERSON_1\"";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String userName = rs.getString(1);
				List<PositionSalary> s = getRecommendations(userName, dao);
				
				if ( (null == s) || (s.isEmpty()))
					numWithoutReco++;
				else 
					numWithReco++;
			}
			
			System.out.println("Number of persons with recommendations :" + numWithReco);
			System.out.println("Number of persons without recommendations :" + numWithoutReco);

		} finally {
			if ( null != conn)
				conn.close();
			
			if ( null != stmt)
				stmt.close();
			
			if ( null != rs)
				rs.close();
			
		}
	}
	public static List<PositionSalary> getRecommendations(String userName, Neo4jPlanHerSemanticsDAO dao)
		throws SQLException
	{
		List<PositionSalary> result = new ArrayList<PositionSalary>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = PostGresDAO.getConnection();
			
			Person p = dao.getPersonByName(userName);
			
			// Select top 10 similar users
			String sql = 
					"SELECT \"USER2\" FROM \"PERSON_SIMILARITY_1\" " + 
			        "where \"USER1\" = '" + p.getId() + "' order by cumulative_score desc limit 10;";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<String> users = new ArrayList<String>();
			while (rs.next())
			{
				users.add(rs.getString(1));
			}

			// Get Position Salary of User1
			PositionSalary s = dao.getClosestSalary(p.getId());
			JobPosition jp = dao.getCurrentJobPositionForUser(p.getId());
			
			System.out.println("Current position is :" + jp);
			
			if (jp == null )
			{
				System.out.println("Unable to find job position for user :" + p.getName());
				return null;				
			}
			
			if (s == null)
			{
				System.out.println("Unable to find  position salary for user :" + p.getName());
				return null;
			}
			
			System.out.println("User (" + p.getName() + ")'s current job is  :" + jp + " at company  " + dao.getCompany(s.getCompanyId()));
			System.out.println("User (" + p.getName() + ")'s closest job-salary profile is  :" + s);
			
			Set<PositionSalary> posSalaries = new HashSet<PositionSalary>();
			for (String u : users)
			{
				PositionSalary p1 = dao.getClosestSalary(u);
				if ( null != p1)
					posSalaries.add(p1);
			}
			
			result.addAll(posSalaries);
			Collections.sort(result, new PositionSalaryComparator());

			System.out.println("Top Recommendations !!");
			for (PositionSalary p1 : result)
			{
				Company c = dao.getCompany(p1.getCompanyId());
				if ( c != null)
				p1.setCompanyName(c.getName());
				
				System.out.println("Position (" + p1.getPosition() + ")"  +
				                   " at company (" + c.getName() + ")  with salary range [ " +
				                   p1.getLowRangeSalary() + " - " + p1.getHighRangeSalary() + 
				                   "] and mean salary of " + p1.getMeanSalary());
			}
			
			
		} finally {
			
		}
		
		return result;
	}
	
	private static class PositionSalaryComparator
		implements Comparator<PositionSalary>
	{
		@Override
		public int compare(PositionSalary arg0, PositionSalary arg1) 
		{
			double diff = arg0.getHighRangeSalary() - arg1.getHighRangeSalary();
			if (diff == 0)
				diff = arg0.getMeanSalary() - arg1.getMeanSalary();
			
			//Sort by descending order
			if (diff > 0)
				return -1;
			else if ( diff == 0)
				return 0;
			return 1;
		}
	}
}
