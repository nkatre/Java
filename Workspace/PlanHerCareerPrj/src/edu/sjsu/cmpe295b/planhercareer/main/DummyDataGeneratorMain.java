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
import java.util.Random;
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

public class DummyDataGeneratorMain {

	public static String salaryRangePatternStr = "\\[(\\d+),(\\d+)\\]";
	public static Pattern salaryRangepattern = Pattern.compile(salaryRangePatternStr);
	
	public static final long FRIEND_ID_BASE = 100;
	
	public static final long UNIVERSITY_ID_BASE = 1000000;

	public static final long DEGREE_ID_BASE = 2000000;
	
	public static final long POSITION_ID_BASE = 3000000;

	public static final long COMPANY_ID_BASE = 4000000;

	public static String[] universityList = {
		"San Jose State University",
		"Standford University",
		"University Of Alabama",
		"Ouachita Baptist University",
		"Philander Smith College",
		"Southern Arkansas University",
		"University Of California, Berkeley",
		"Spring Hill College",
		"Stillman College",
		"Talladega College",
		"Troy University"
	};
	
	public static String[] degreeList = {
		"Software Engineering",
		"Computer Science",
		"Chemical Engineering",
		"Civil Engineering",
		"Music",
	};
	
	public static String[] positionsList = {
		"Computer Programmer",
		"Software Enginner I",
		"Software Engineer II",
		"Senior Software Engineer",
		"Senior Data Scientist",
		"Principle Software Enginner",
	};
	
	public static String[] companiesList = {
		"IBM",
		"Cisco",
		"BAE Systems",
		"Yahoo Inc",
		"Ebay Inc",
		"Oracle Corp",
		"Linkedin Corp",
		"Google",
		"Facebook"
	};
	
	public static String[] skills = {
		"java",
		"distributed systems",
		"scala",
		"python",
		"UI",
		"HTML",
		"Networking Protocols",
		"C/C++",
		"perl",
	};
	
	public static  String[] personList = {
		"Sophia",
		"Emma",
		"Olivia",
		"Isabella",
		"Ava",
		"Lily",
		"Zoe",
		"Chloe",
		"Mia",
		"Madison",
		"Emily",
		"Ella",
		"Madelyn",
		"Abigail",
		"Aubrey",
		"Addison",
		"Avery",
		"Layla",
		"Hailey",
		"Amelia",
		"Hannah",
		"Charlotte",
		"Kaitlyn",
		"Harper",
		"Kaylee",
		"Sophie",
		"Mackenzie",
		"Peyton",
		"Riley",
		"Grace",
		"Brooklyn",
		"Sarah",
		"Aaliyah",
		"Anna",
		"Arianna",
		"Ellie",
		"Natalie",
		"Isabelle",
		"Lillian",
		"Evelyn",
		"Elizabeth",
		"Lyla",
		"Lucy",
		"Claire",
		"Makayla",
		"Kylie",
		"Audrey",
		"Maya",
		"Leah",
		"Gabriella",
		"Annabelle",
		"Savannah",
		"Nora",
		"Reagan",
		"Scarlett",
		"Samantha",
		"Alyssa",
		"Allison",
		"Elena",
		"Stella",
		"Alexis",
		"Victoria",
		"Aria",
		"Molly",
		"Maria",
		"Bailey",
		"Sydney",
		"Bella",
		"Mila",
		"Taylor",
		"Kayla",
		"Eva",
		"Jasmine",
		"Gianna",
		"Alexandra",
		"Julia",
		"Eliana",
		"Kennedy",
		"Brianna",
		"Ruby",
		"Lauren",
		"Alice",
		"Violet",
		"Kendall",
		"Morgan",
		"Caroline",
		"Piper",
		"Brooke",
		"Elise",
		"Alexa",
		"Sienna",
		"Reese",
		"Clara",
		"Paige",
		"Kate",
		"Nevaeh",
		"Sadie",
		"Quinn",
		"Isla",
		"Eleanor"
	};
	
	public static void main(String[] args)
		throws Exception
	{
		setupTables();
		
		Random r = new Random();
		
		// Insert Person Data
		for (int i = 0; i < personList.length;i++)
		{
			String query = "INSERT INTO \"FB_PERSON_1\"( " + 
					"id, name, age_range, bio, birthday, cover, devices, education, " + 
					"email, first_name, last_name, gender, hometown, languages, link, " + 
					"location, middle_name, quotes, relationship_status, religion, " + 
					"significant_other, timezone, username, updated_time, work, address, " + 
					"favorite_athletes, favorite_teams, inspirational_people, interested_in, " + 
					"meeting_for, name_format, political, sports, friends, family, " + 
					"events, books, apprequests, albums, activities, accounts, games, " + 
					"groups, interests, likes, locations, movies, music, posts, questions, " + 
					"subscribedto, subscribers, television)" +
					"VALUES ( '" + (FRIEND_ID_BASE + i) + "', '" + personList[i] + "'," 
					+ "'','','','','','','','','','FEMALE',"  +
					"'','','','','','','','','',''"  +
					",'" + personList[i] + "','','','','','','','','','',"  +
					"'','','','','','','','','','',"  +
					"'','','','','','','','','','',"  +
					"'','')";
			PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), query);
		}
		
		// Insert Connection Data
		for (int i = 1; i < personList.length;i++)
		{
			String query = "INSERT INTO \"FB_PERSON_CONNECTED_TO_1\"( \"PERSON_ID\", \"CONNECTED_PERSON_ID\", \"RELATIONSHIP\")" + 
					" VALUES ( '" + FRIEND_ID_BASE + "','" + (FRIEND_ID_BASE + i) +"','FRIEND')";
			PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), query);
		}
		
		// Insert Companies Data
		for (int i = 0 ; i < personList.length; i++)
		{
			int randomNumPos = (r.nextInt() % 2) + 1; // 1-2 College Education
			String[] edu = { "College", "Grad College" };
			for ( int j = 0; j < randomNumPos; j++)
			{
				int k = Math.abs(r.nextInt())%universityList.length;
				int l = Math.abs(r.nextInt())%degreeList.length;
				String query = "INSERT INTO \"FB_PERSON_EDUCATION_1\"( \"PERSON_ID\", \"SCHOOL_ID\", \"SCHOOL_NAME\", \"SCHOOL_TYPE\", " +
						"\"YEAR_ID\", \"YEAR_NAME\", \"CONCENTRATION_ID\", \"CONCENTRATION_NAME\")" + 
						" VALUES ( " + 
						"'" + (FRIEND_ID_BASE + i)  + "'," +
						"'" + (UNIVERSITY_ID_BASE + k) + "'," +
						"'" + universityList[k] + "'," +
						"'" + edu[j] + "'," +
						"'','','" + (DEGREE_ID_BASE + l) + "',"  +
						"'" + degreeList[l] + "')";
				PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), query);
			}
		}
		// Insert Companies Data
		for (int i = 0 ; i < personList.length; i++)
		{
			int randomNumPos = (Math.abs(r.nextInt()) % 3) + 1; // 1-3 Work Positions
			String lastEndDate = "2013-12-12";
			Pair<Integer,Integer> lastPos = null;
			for ( int j = 0; j < randomNumPos; j++)
			{
				int k = Math.abs(r.nextInt())%companiesList.length;
				
				// Select random position but make sure the order is maintained
				lastPos = getReveseTraversingRange(positionsList.length, randomNumPos, j+1, lastPos);
				int begin = lastPos.getElement1();
				int end = lastPos.getElement2();
				int l = Math.abs(r.nextInt())%(end - begin -1) + begin;
				String lastStartDate = generateDateStr(r, 2013 - ((j+1)*5), 2013 - (j*5));
				String query = "INSERT INTO \"FB_PERSON_WORK_1\"( \"PERSON_ID\", \"EMPLOYER_ID\", \"EMPLOYER_NAME\", \"LOCATION_ID\", " +
					"\"LOCATION_NAME\", \"POSITION_ID\", \"POSITION_NAME\", \"START_DATE\", \"END_DATE\")" + 
					" VALUES ( '" + (FRIEND_ID_BASE + i)  + "', '" 
					+ (COMPANY_ID_BASE + k) + "', '"
					+ companiesList[k] + "', '10', 'San Francisco Bay Area','"
					+ (POSITION_ID_BASE + l) + "', '"
					+ positionsList[l] + "', '" + lastStartDate + "', '" + lastEndDate + "')";
				PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), query);
				lastEndDate = lastStartDate;
			}
		}
		
		//Build Neo4J DB
		updateNeo4J();
	}
	
	private static String generateDateStr(Random r, int minYear,  int maxYear )
	{
		int year = Math.abs(r.nextInt())% (maxYear - minYear) + minYear;
		return "" + year + "-01-01";
	}

	/**
	 * For size (6) and numPartitions = 3, the ranges provided will be
	 * [4-6]
	 * [2-4]
	 * [0-2]
	 */
	private static Pair<Integer,Integer> getReveseTraversingRange(int totalSize, 
			                                               int numPartitions, 
			                                               int numCalls,
			                                               Pair<Integer,Integer> lastPos)
	{
		int startPos = (totalSize/numPartitions) * (numPartitions - numCalls);
		int endPos = (null == lastPos) ? totalSize : lastPos.getElement1();
		return new Pair<Integer,Integer>(startPos, endPos);
	}
	
	public static class Pair<T1,T2>
	{
		private final T1 element1;
		private final T2 element2;
		
		public T1 getElement1() {
			return element1;
		}

		public T2 getElement2() {
			return element2;
		}

		public Pair(T1 element1, T2 element2) {
			super();
			this.element1 = element1;
			this.element2 = element2;
		}
	}
	public static void setupTables() throws Exception
	{		
		try
		{
			String truncateFbPersonSql = "truncate \"FB_PERSON_1\"";
			String truncateFbPersonEducationSql = "truncate \"FB_PERSON_EDUCATION_1\"";
			String truncateFbPersonWorkSql = "truncate \"FB_PERSON_WORK_1\"";
			String truncateFbPersonConnectedToSql = "truncate \"FB_PERSON_CONNECTED_TO_1\"";

			String fbPersonSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_1\"( id text," +
					"name text, age_range text, bio text,birthday text,cover text, devices text, education text," +
					"email text, first_name text, last_name text, gender text, hometown text, languages text, link text, location text," + 
					"middle_name text, quotes text, relationship_status text,religion text, significant_other text, timezone text, username text," + 
					"updated_time text, work text, address text, favorite_athletes text, favorite_teams text, inspirational_people text, " + 
					"interested_in text,meeting_for text, name_format text,political text, sports text, friends text, family text, events text," + 
					"books text, apprequests text, albums text, activities text, accounts text, games text, groups text, interests text, likes text," +
					"locations text, movies text, music text, posts text, questions text, subscribedto text, subscribers text, television text ) WITH ( OIDS=FALSE );";
			String fbPersonActivitySql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_ACTIVITY_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"FB_LINK\" text,   \"DESCRIPTION\" text," + 
					"\"TALKING_ABOUT_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonBookSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_BOOK_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"FB_LINK\" text,   \"WEBSITE\" text," +
					"\"DESCRIPTION\" text,   \"TALKING_ABOUT_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonConnectedToSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_CONNECTED_TO_1\" (   \"PERSON_ID\" text,   \"CONNECTED_PERSON_ID\" text,   \"RELATIONSHIP\" text,   " + 
					"\"EDUCATION_SIMILARITY_SCORE\" text,   \"WORK_SIMILARITY_SCORE\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonEducationSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_EDUCATION_1\" (   \"PERSON_ID\" text,   \"SCHOOL_ID\" text,   \"SCHOOL_NAME\" text," + 
			        " \"SCHOOL_TYPE\" text,   \"YEAR_ID\" text,   \"YEAR_NAME\" text,   \"CONCENTRATION_ID\" text,   \"CONCENTRATION_NAME\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonGroupsSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_GROUPS_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text ) WITH (   OIDS=FALSE );";
			String fbPersonLikesSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_LIKES_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text,   \"CREATED_TIME\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonMovieSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_MOVIE_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonMusicSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_MUSIC_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonPostSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_POST_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"DESCRIPTION\" text,   \"STORY\" text,   \"LINK\" text,   \"SOURCE\" text,   " + 
					"\"TYPE\" text,   \"STATUS_TYPE\" text,   \"OBJECT_ID\" text,   \"APPLICATION_ID\" text,   \"APPLICATION_NAME\" text,   \"LIKES_PERSON_ID_LIST\" text,   \"LIKES_COUNT\" text,   \"SHARES_COUNT\" text ) WITH (   OIDS=FALSE );";
			String fbPersonSubscribedToSql = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_SUBSCRIBED_TO_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonTelevisionSql =
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_TELEVISION_1\" (   \"PERSON_ID\" text,   \"NAME\" text,   \"NAME_ID\" text,   \"CATEGORY\" text ) WITH (   OIDS=FALSE ); ";
			String fbPersonWorkSql  = 
					"CREATE TABLE IF NOT EXISTS \"FB_PERSON_WORK_1\" (   \"PERSON_ID\" text,   \"EMPLOYER_ID\" text,   \"EMPLOYER_NAME\" text,   \"LOCATION_ID\" text,   \"LOCATION_NAME\" text,   \"POSITION_ID\" text,   \"POSITION_NAME\" text,   " + 
					"\"START_DATE\" text,   \"END_DATE\" text ) WITH (   OIDS=FALSE );";
			String lnPersonSql =
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_1\" (   id text,   first_name text,   last_name text,   maiden_name text,   formatted_name text,   headline text,   location text,   email_address text,   industry text,   relation_to_viewer text,   current_status text,   current_status_timestamp text,   current_share text,   connections text,   num_connections text,   num_connections_capped text,   group_memberships text,   summary text,   specialties text,   positions text,   picture_url text,   site_standard_profile_request text,   public_profile_url text,   network text,   phone_numbers text,   bound_account_types text,   im_accounts text,   main_address text,   twitter_accounts text,   primary_twitter_account text,   last_modified_timestamp text,   proposal_comments text,   associations text,   interests text,   publications text,   patents text,   languages text,   skills text,   certifications text,   educations text,   courses text,   volunteer text,   three_current_positions text,   three_past_positions text,   num_recommenders text,   recommendations_received text,   mfeed_rss_url text,   following text,   job_bookmarks text,   suggestions text,   date_of_birth text,   member_url_resources text,   related_profile_views text,   honors_awards text ) WITH (   OIDS=FALSE );";
			String lnPersonCompanySql = 
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_COMPANY_1\" (   person_id text,   position_id text,   company_id text,   name text,   type text,   size text,   industry text,   ticker text ) WITH (   OIDS=FALSE );";
			String lnPersonEducationSql = 
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_EDUCATION_1\" (   person_id text,   education_id text,   school_name text,   field_of_study text,   start_date text,   end_date text,   degree text,   activities text,   notes text ) WITH (   OIDS=FALSE );";
			String lnPersonPositionsSql =
					"CREATE TABLE IF NOT EXISTS \"LN_PERSON_POSITIONS_1\" (   person_id text,   position_id text,   title text,   summary text,   start_date text,   end_date text,   is_current text,   company text ) WITH (   OIDS=FALSE );";
			String fbPersonSimilarityMetricsSql = 
					"CREATE TABLE IF NOT EXISTS \"PERSON_SIMILARITY_1\" (   \"USER1\" text NOT NULL,   \"USER2\" text NOT NULL,   friendship_strength double precision,   job_pos_sim double precision,   job_field_sim double precision,   salary_sim double precision,   loc_sim double precision,   company_sim double precision,   grad_degree_sim double precision,   cumulative_score double precision,   CONSTRAINT \"Primary\" PRIMARY KEY (\"USER1\", \"USER2\") ) WITH (   OIDS=FALSE );";
			String derivedSalarySql =
					"CREATE TABLE IF NOT EXISTS \"DERIVED_SALARY\" (   userid text NOT NULL,   salary_range text,   salary double precision,   closest_salary_id text,   CONSTRAINT userid PRIMARY KEY (userid) ) WITH (   OIDS=FALSE );";
			
			String[] tables = {
					fbPersonSql,
					fbPersonActivitySql,
					fbPersonBookSql,
					fbPersonConnectedToSql,
					fbPersonEducationSql,
					fbPersonGroupsSql,
					fbPersonLikesSql,
					fbPersonMovieSql,
					fbPersonMusicSql,
					fbPersonPostSql,
					fbPersonSimilarityMetricsSql,
					fbPersonSubscribedToSql,
					fbPersonTelevisionSql,
					fbPersonWorkSql,
					lnPersonCompanySql,
					lnPersonEducationSql,
					lnPersonPositionsSql,
					lnPersonSql,
					truncateFbPersonConnectedToSql,
					truncateFbPersonEducationSql,
					truncateFbPersonSql,
					truncateFbPersonWorkSql,
					derivedSalarySql
			};
			
			for (String t : tables)
			{
				System.out.println("Executing :" + t);
				PostGresDAO.executeNonQueryStatement(PostGresDAO.getConnection(), t);
			}
		
		} finally {
			
		}
	}
	
	public static void updateNeo4J()
			throws Exception
		{
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			Neo4JPlanHerDAO dao = new Neo4JPlanHerDAO("/Users/ksjeyabarani/Documents/neo4j-community-1.9.4/data/planhercareergen.db");
			try
			{
				conn = PostGresDAO.getConnection();

				// Add Person
				String sql = 
						"SELECT ID, \"name\", \"birthday\", \"gender\", \"username\" FROM \"FB_PERSON_1\" ";
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
						"SELECT \"PERSON_ID\", \"CONNECTED_PERSON_ID\" FROM \"FB_PERSON_CONNECTED_TO_1\" ";
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
				sql =  "SELECT \"PERSON_ID\", \"EMPLOYER_ID\", \"EMPLOYER_NAME\", \"LOCATION_ID\", \"LOCATION_NAME\", \"POSITION_ID\", \"POSITION_NAME\", \"START_DATE\", \"END_DATE\" FROM \"FB_PERSON_WORK_1\" ";
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
				sql =  "SELECT \"PERSON_ID\", \"SCHOOL_ID\", \"SCHOOL_NAME\", \"SCHOOL_TYPE\", \"YEAR_ID\", \"YEAR_NAME\", \"CONCENTRATION_ID\", \"CONCENTRATION_NAME\" FROM \"FB_PERSON_EDUCATION_1\" ";
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
				sql =  "SELECT \"PERSON_ID\", \"NAME_ID\", \"NAME\" FROM \"FB_PERSON_GROUPS_1\" ";
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
