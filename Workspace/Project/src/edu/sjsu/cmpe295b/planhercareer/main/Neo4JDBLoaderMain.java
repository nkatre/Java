package edu.sjsu.cmpe295b.planhercareer.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.sjsu.cmpe295b.planhercareer.dao.Neo4JPlanHerDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.Company;
import edu.sjsu.cmpe295b.planhercareer.dto.Group;
import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.Person;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;
import edu.sjsu.cmpe295b.planhercareer.dto.School;
import edu.sjsu.cmpe295b.planhercareer.dto.Student;

public class Neo4JDBLoaderMain {

	public static String salaryRangePatternStr = "\\[(\\d+),(\\d+)\\]";
	public static Pattern salaryRangepattern = Pattern.compile(salaryRangePatternStr);

	
	public static void main(String[] args)
		throws Exception
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Neo4JPlanHerDAO dao = new Neo4JPlanHerDAO("/Users/ksjeyabarani/Documents/neo4j-community-1.9.4/data/planhercareer.db");
		try
		{
			conn = PostGresDAO.getConnection();

			// Add Person
			String sql = 
					"SELECT ID, \"name\", \"birthday\", \"gender\", \"username\" FROM \"FB_PERSON\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				Person p = new Person();
				p.setId(rs.getString(1));
				p.setName(rs.getString(2));
				p.setDob(rs.getString(3));
				p.setGender(rs.getString(4));
				p.setUserName(rs.getString(5));
				dao.createNewPersonNode(p);
			}

			closeDB(null,stmt,rs);

			//Add Person-Person Connection
			sql = 
					"SELECT \"PERSON_ID\", \"CONNECTED_PERSON_ID\" FROM \"FB_PERSON_CONNECTED_TO\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String id1 = rs.getString(1);
				String id2 = rs.getString(2);
				dao.addFriendConnection(id1, id2);
			}
			closeDB(null,stmt,rs);

			// Add Person-Company Connection
			sql =  "SELECT \"PERSON_ID\", \"EMPLOYER_ID\", \"EMPLOYER_NAME\", \"LOCATION_ID\", \"LOCATION_NAME\", \"POSITION_ID\", \"POSITION_NAME\", \"START_DATE\", \"END_DATE\" FROM \"FB_PERSON_WORK\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			Set<String> seenCompanies = new HashSet<String>();
			
			while (rs.next())
			{
				String id1 = rs.getString(1);
				String id2 = rs.getString(2);
				String name = rs.getString(3);

				if ((null == id2) || (id2.isEmpty() 
						|| (name == null) || (name.trim().isEmpty())))
					continue;

				if ( ! seenCompanies.contains(id2))
				{
					Company c = new Company();
					c.setId(id2);
					c.setName(name);
					List<PositionSalary> posSalaries = getPositionSalary(name);
					c.setRoleSalaryData(posSalaries);
					dao.createNewCompanyNode(c);
				}
				seenCompanies.add(id2);
				
				JobPosition j = new JobPosition();
				j.setLocationId(rs.getString(4));
				j.setLocation(rs.getString(5));
				j.setPositionId(rs.getString(6));
				j.setPosition(rs.getString(7));
				j.setStartDate(rs.getString(8));
				j.setEndDate(rs.getString(9));
				
				dao.createNewJobRoleNode(j);

				dao.addCompanyConnection(id1, id2, j);
				dao.addCompanyRoleConnection(id2, j.getPositionId());
			}
			closeDB(null,stmt,rs);
			
			// Add Person-College/School Connection
			sql =  "SELECT \"PERSON_ID\", \"SCHOOL_ID\", \"SCHOOL_NAME\", \"SCHOOL_TYPE\", \"YEAR_ID\", \"YEAR_NAME\", \"CONCENTRATION_ID\", \"CONCENTRATION_NAME\" FROM \"FB_PERSON_EDUCATION\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String id1 = rs.getString(1);
				String id2 = rs.getString(2);
				String name = rs.getString(3);
				String type = rs.getString(4);

				if ((null == id2) || (id2.isEmpty() 
						|| (name == null) || (name.trim().isEmpty())))
					continue;

				School c = new School();
				c.setId(id2);
				c.setName(name);
				
				Student s = new Student();
				s.setYearId(rs.getString(5));
				s.setYear(rs.getString(6));
				s.setConcentrationId(rs.getString(7));
				s.setConcentration(rs.getString(8));
				
				if (type.toLowerCase().contains("high"))
				{
					dao.createNewSchoolNode(c);
					dao.addSchoolConnection(id1, id2, s);
				} else {
					// Treat as College
					dao.createNewCollegeNode(c);
					dao.createNewConcentrationNode(s);
					dao.addCollegeConnection(id1, id2, s);
					dao.addEduConcentrationConnection(id2, s.getConcentrationId());
				}
			}
			closeDB(null,stmt,rs);

			// Add Person-Group Connection
			sql =  "SELECT \"PERSON_ID\", \"NAME_ID\", \"NAME\" FROM \"FB_PERSON_GROUPS\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String id1 = rs.getString(1);
				String id2 = rs.getString(2);
				String name = rs.getString(3);

				if ((null == id2) || (id2.isEmpty() 
						|| (name == null) || (name.trim().isEmpty())))
					continue;

				Group c = new Group();
				c.setId(id2);
				c.setName(name);
				dao.createNewGroupNode(c);
				dao.addGroupConnection(id1, id2);
			}

		} finally {
			closeDB(conn,stmt,rs);
		}
		System.out.println("Done !!");
	}
	
	public static void closeDB(Connection conn, Statement stmt, ResultSet rs)
		throws SQLException
	{
		if (null != conn)
			conn.close();
		if (null != stmt)
			stmt.close();
		if (null != rs)
			rs.close();
	}
	
	private static String executeAndGetOutput(String companyName)
	{
		Process p;
		String output = "";
		try{
		    String[] cmd = {
		    		"python",
		    		"/usr/local/bin/glassdoor",
				"\"" + companyName + "\"",
		    };
		    
		    p = Runtime.getRuntime().exec(cmd);
		    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String l = null;
		    while ((l = br.readLine()) != null )
		    {
		    		output += l; 
		    }
		    p.waitFor();
		    p.destroy();
		} catch (Exception e) {throw new RuntimeException(e);}
		return output;
	}
	
	private static List<PositionSalary> getPositionSalary(String companyName) 
	{
		List<PositionSalary> salaries = new ArrayList<PositionSalary>();
		String jsonOutput  = executeAndGetOutput(companyName);
		
		if ((null != jsonOutput) && (! jsonOutput.isEmpty()))
		{
			JSONObject json = null;
			Object obj = null;
			try {
				json = new JSONObject(jsonOutput);
				obj = json.get("salary");
				if ( obj != null)
				{
					JSONArray array = (JSONArray)obj;
					for (int i =0; i < array.length(); i++)
					{
						JSONObject j = array.getJSONObject(i);
						System.out.println("Val : " + j);
						PositionSalary salary = new PositionSalary();
						salary.setPosition(j.getString("position"));
						String range = j.getString("range");
						if (null != range)
						{	
							range = range.trim();
							Matcher m = salaryRangepattern.matcher(range);
							
							if ( m.matches())
							{
								salary.setLowRangeSalary(Double.valueOf(m.group(1)));
								salary.setHighRangeSalary(Double.valueOf(m.group(2)));
							}
						}
						
						String samples = j.getString("samples");
						if (null != samples)
						{
							samples = samples.trim();
							salary.setNumSamples(Long.valueOf(samples));
						}
						String mean = j.getString("mean");
						if (null != mean)
						{
							mean = mean.trim();
							salary.setMeanSalary(Double.valueOf(mean));
						}
						System.out.println("Company (" + companyName +") : Salary is :" + salary);
						salaries.add(salary);
					}
				}
			} catch (JSONException e) {
				System.err.println("Got exception (" + e + ") for Json String : (" + jsonOutput + ")");
				//throw new RuntimeException(e);
			}
		}
		
		return salaries;
	}
}
