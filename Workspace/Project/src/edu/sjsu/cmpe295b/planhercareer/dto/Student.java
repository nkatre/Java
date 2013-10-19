package edu.sjsu.cmpe295b.planhercareer.dto;

public class Student {
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
	
	@Override
	public String toString() {
		return "Student [yearId=" + yearId + ", year=" + year
				+ ", concentrationId=" + concentrationId + ", concentration="
				+ concentration + "]";
	}
}
