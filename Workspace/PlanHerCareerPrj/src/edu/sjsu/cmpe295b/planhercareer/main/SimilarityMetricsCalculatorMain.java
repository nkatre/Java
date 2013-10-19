package edu.sjsu.cmpe295b.planhercareer.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import edu.sjsu.cmpe295b.planhercareer.dao.Neo4jPlanHerSemanticsDAO;
import edu.sjsu.cmpe295b.planhercareer.dao.PostGresDAO;
import edu.sjsu.cmpe295b.planhercareer.dto.Company;
import edu.sjsu.cmpe295b.planhercareer.dto.ConnectionSimilarityMetrics;
import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;
import edu.sjsu.cmpe295b.planhercareer.dto.Student;

public class SimilarityMetricsCalculatorMain 
{
	private static 	final AbstractStringMetric similarityMetric = new Levenshtein();

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
					"SELECT ID FROM \"FB_PERSON_1\" ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			Set<String> userIds = new HashSet<String>();
			while (rs.next())
			{
				userIds.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			
			
			for ( String userId1 : userIds)
			{
				for (String userId2 : userIds)
				{
					if (userId1.equals(userId2))
						continue;
					
					ConnectionSimilarityMetrics m = populateSimilarityMetric(userId1, userId2, dao);
					String sqlStr = 
							"UPDATE \"PERSON_SIMILARITY_1\" " + 
							"SET friendship_strength=" + m.getFriendshipStrength() +
							", job_pos_sim=" + m.getJobPositionSimilarity() + 
							", job_field_sim=" + m.getJobPositionSimilarity() +
							", salary_sim=" + m.getSalarySimilarity() + 
							", loc_sim=" + m.getLocationSimilarity() +
							", company_sim=" + m.getCompanySimilarity() +
							", grad_degree_sim=" + m.getGradDegreeSimilarity() +
							", cumulative_score=" + m.getCumulativeScore() + 
							" WHERE \"USER1\" = '" + userId1 + "' and \"USER2\" = '" + userId2 + "';";
					int count = PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), sqlStr);
					
					if ( count <= 0)
					{
						sqlStr = "INSERT INTO \"PERSON_SIMILARITY_1\"(" + 
					            "\"USER1\", \"USER2\", friendship_strength, job_pos_sim, job_field_sim," +  
					            "salary_sim, loc_sim, company_sim, grad_degree_sim, cumulative_score)" + 
					            " VALUES ('" +
					            m.getUser1() + "','" +
					            m.getUser2() + "'," +
					            m.getFriendshipStrength() + "," +
					            m.getJobPositionSimilarity() + "," +
					            m.getJobFieldSimilarity() + "," +
					            m.getSalarySimilarity() + "," +
					            m.getLocationSimilarity() + "," +
					            m.getCompanySimilarity() + "," +
					            m.getGradDegreeSimilarity() + "," +
					            m.getCumulativeScore() + ")"; 
						PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), sqlStr);  
					}	
				}
			}
		} finally {
			if ( null != conn)
				conn.close();
		}
	}
	
	private static ConnectionSimilarityMetrics populateSimilarityMetric(String id1, 
																	   String id2,
																	   Neo4jPlanHerSemanticsDAO dao)
	{
		ConnectionSimilarityMetrics s = new ConnectionSimilarityMetrics();
		s.setUser1(id1);
		s.setUser2(id2);
		
		JobPosition j1 = dao.getCurrentJobPositionForUser(id1);
		JobPosition j2 = dao.getCurrentJobPositionForUser(id1);

		s.setJobPositionSimilarity(similarityMetric.getSimilarity(j1.getPosition(), 
																 j2.getPosition()));
		
		//TODO: We need to replace textual similarity with geo based similarity
		// See GeoLocatorUtils for getting latitude and longitude from Google API
		s.setLocationSimilarity(similarityMetric.getSimilarity(j1.getLocation(), j2.getLocation()));
		
		// 1 if directly linked, else 0. 
		//TODO: find distance and use it to determine strength
		if ( dao.isLinked(id1, id2))
			s.setFriendshipStrength(1); 
		else
			s.setFriendshipStrength(0);
		
		Company c1 = dao.getCompany(j1.getCompanyId());
		Company c2 = dao.getCompany(j2.getCompanyId());
		
		// TODO : Company Similarity should include competitors and Industry similarity
		s.setCompanySimilarity(similarityMetric.getSimilarity(c1.getName(),c2.getName()));
		
		List<Student> s1 = dao.getDegreesForUser(id1);
		List<Student> s2 = dao.getDegreesForUser(id2);
		
		s.setGradDegreeSimilarity(getGradDegreeSimilarity(s1, s2));
		
		// s.setCollegeSimilarity(); //TODO: Need to use College ranking for this
		
		// Salary Similarity
		PositionSalary sal1 = dao.getClosestSalary(id1);
		PositionSalary sal2 = dao.getClosestSalary(id2);
		
		s.setSalarySimilarity(getSalarySimilarity(sal1, sal2));
		
		generateCumulativeScore(s); 
		
		return s;
	}

	/**
	 * Get similarity between 2 student records.
	 * TODO: Need to be more sophisticated by looking at fields and synonyms for similarity
	 */
	private static float getGradDegreeSimilarity(List<Student> s1, List<Student> s2)
	{
		float score = 0;
		
		for (Student x : s1)
			for (Student y : s2)
				score = Math.max(score, similarityMetric.getSimilarity(x.getConcentration(), y.getConcentration()));
		
		return score;
	}
	
	
	private static float getSalarySimilarity(PositionSalary pos1 , PositionSalary pos2 )
	{
		if ( (null == pos1) || ( null == pos2))
			return 0.0f;
		
		double meanSalDiff = Math.abs(pos1.getMeanSalary() - pos2.getMeanSalary());
		double highMeanSal = Math.max(pos1.getMeanSalary(),pos2.getMeanSalary());

		int numUnits = (int) (highMeanSal/5000); //Divide into 5K ranges 
		
		if (numUnits == 0)
			return 0.0f; // avoid dividing by 0
		
		// score between 0-1. Bigger the range and smaller the mean sal difference, similarity is more
		float score = 1.0f - (1.0f/numUnits) * ((int) meanSalDiff/5000); 
			
		return score;
	}
	
	private static void generateCumulativeScore(ConnectionSimilarityMetrics m)
	{
		// For now, use 0.8 weight for grad Degree and 0.1 weight for friendship and 0.1 weight for Location
		double score = 0.8 * m.getGradDegreeSimilarity() + 0.1 * m.getFriendshipStrength() + 0.1 * m.getLocationSimilarity();
		m.setCumulativeScore(score);
	}
}
