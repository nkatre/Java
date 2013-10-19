package edu.sjsu.cmpe295b.planhercareer.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;

public class GenerateCompaniesListMain {
	
	public static String salaryRangePatternStr = "\\[(\\d+),(\\d+)\\]";
	public static Pattern salaryRangepattern = Pattern.compile(salaryRangePatternStr);
	
	public static void main(String[] args)
			throws Exception
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			String sql = "SELECT DISTINCT \"EMPLOYER_ID\", \"EMPLOYER_NAME\" FROM \"FB_PERSON_WORK\"";
			conn = PostGresDAO.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				String id = rs.getString(1);
				String name = rs.getString(2);
				getPositionSalary(name);
				
				//String output = executeAndGetOutput(name);
				//System.out.println(id + "," + name + ": Output :" + output);
				break;
			}
		} finally {
			closeDB(conn,stmt,rs);
		}
	}

	public static List<PositionSalary> getPositionSalary(String companyName) 
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
						System.out.println("Salary is :" + salary);
						salaries.add(salary);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return salaries;
	}
	public static String executeAndGetOutput(String companyName)
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
}
