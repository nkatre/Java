package edu.sjsu.cmpe295b.planhercareer.dto;

public class Student 
	implements Comparable<Student>
{
	private String id;
	private String institutionId;
	
	private String yearId;
	private String year;
	private String concentrationId;
	private String concentration;
	public String getYearId() {
		return yearId;
	}
	public void setYearId(String yearId) {
		this.yearId = yearId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getConcentrationId() {
		return concentrationId;
	}
	public void setConcentrationId(String concentrationId) {
		this.concentrationId = concentrationId;
	}
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", institutionId=" + institutionId
				+ ", yearId=" + yearId + ", year=" + year
				+ ", concentrationId=" + concentrationId + ", concentration="
				+ concentration + "]";
	}
	@Override
	public int compareTo(Student arg0) {
		return Integer.parseInt(year) - Integer.parseInt(arg0.year);
	}
}
