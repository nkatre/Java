package edu.sjsu.cmpe295b.planhercareer.dao;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import edu.sjsu.cmpe295b.planhercareer.dto.JobPosition;
import edu.sjsu.cmpe295b.planhercareer.dto.PositionSalary;
import edu.sjsu.cmpe295b.planhercareer.dto.Student;

public class Neo4jPlanHerSemanticsDAO 
	extends Neo4JPlanHerDAO
{
	public Neo4jPlanHerSemanticsDAO(String dbPath)
	{
		super(dbPath);
	}
	
	public PositionSalary getClosestSalary(String userId)
	{
		Node n1 = getNodeById(userId);
		
		if ( null == n1)
			return null;
		
		Relationship r = getRelationship(n1, null, ConnectionRelationship.PERSON_DERIVED_SALARY);
		
		if ( null == r)
			return null;
		
		Node cNode = null;
		if (r.getEndNode().equals(n1))
			cNode = r.getStartNode();
		else 
			cNode = r.getEndNode();
		
		PositionSalary s = new PositionSalary();
		s.setHighRangeSalary((Double)cNode.getProperty(sSalaryHigh));
		s.setLowRangeSalary((Double)cNode.getProperty(sSalaryLow));
		s.setId((String)cNode.getProperty(sId));
		s.setMeanSalary((Double)cNode.getProperty(sSalaryMean));
		s.setNumSamples((Long)cNode.getProperty(sSalarySamples));
		s.setPosition((String)cNode.getProperty(sSalaryPosition));
		s.setCompanyId((String)r.getProperty(sCompanyId));
		
		return s;
	}
	
	public List<Student> getDegreesForUser(String userId)
	{
		Node n1 = getNodeById(userId);
		List<Student> result = new ArrayList<Student>();
		
		if ( n1 != null )
		{
			Iterator<Relationship> itr = n1.getRelationships(
										ConnectionRelationship.FB_EDUCATION_COLLEGE).iterator();
			while ( itr.hasNext())
			{
				Relationship r2 = itr.next();
				Node cNode = null;
				if (r2.getEndNode().equals(n1))
					cNode = r2.getStartNode();
				else 
					cNode = r2.getEndNode();
				
				Student  j = new Student();
				j.setYear((String)r2.getProperty(sStudentYear));
				j.setYearId((String)r2.getProperty(sStudentYearId));
				j.setConcentration((String)r2.getProperty(sStudentConcentration));
				j.setConcentrationId((String)r2.getProperty(sStudentConcentrationId));
				j.setInstitutionId((String)cNode.getProperty(sId));
				j.setId(userId);
				result.add(j);
			}
		}
		return result;
	}
	
	public JobPosition getCurrentJobPositionForUser(String userId)
	{
		Node n1 = getNodeById(userId);
		JobPosition result = null;
		
		if ( n1 != null )
		{
			Iterator<Relationship> itr = n1.getRelationships(
										ConnectionRelationship.FB_COMPANY).iterator();
			while ( itr.hasNext())
			{
				Relationship r2 = itr.next();
				Node jobNode = null;
				if (r2.getEndNode().equals(n1))
					jobNode = r2.getStartNode();
				else 
					jobNode = r2.getEndNode();
				
				JobPosition  j = new JobPosition();
				j.setPosition((String)r2.getProperty(sJobPosition));
				j.setPositionId((String)r2.getProperty(sJobPositionId));
				j.setLocation((String)r2.getProperty(sJobLocation));
				j.setLocationId((String)r2.getProperty(sJobLocationId));
				j.setStartDate((String)r2.getProperty(sJobStartDate));
				j.setEndDate((String)r2.getProperty(sJobEndDate));
				j.setCompanyId((String)jobNode.getProperty(sId));
				
				if (result == null)
					result = j;
				else if (result.compareDates(j) < 0)
					result = j;
			}
		}
		return result;
	}
	
	public List<PositionSalary> getAllCmpySalaries(String companyId)
	{
		Node n1 = getNodeById(companyId);
		List<PositionSalary> result = new ArrayList<PositionSalary>();
		
		if ( n1 != null )
		{
			Iterator<Relationship> itr = n1.getRelationships(
										ConnectionRelationship.COMPANY_SALARY_POS).iterator();
			while ( itr.hasNext())
			{
				Relationship r2 = itr.next();
				Node jobNode = null;
				if (r2.getEndNode().equals(n1))
					jobNode = r2.getStartNode();
				else 
					jobNode = r2.getEndNode();
				
				PositionSalary pos = new PositionSalary();
				pos.setId((String)jobNode.getProperty(sId));
				pos.setPosition((String)jobNode.getProperty(sSalaryPosition));
				pos.setLowRangeSalary((Double)jobNode.getProperty(sSalaryLow));
				pos.setHighRangeSalary((Double)jobNode.getProperty(sSalaryHigh));
				pos.setMeanSalary((Double)jobNode.getProperty(sSalaryMean));
				pos.setNumSamples((Long)jobNode.getProperty(sSalarySamples));
				pos.setCompanyId(companyId);
				result.add(pos);
			}
		}
		return result;
	}
}
