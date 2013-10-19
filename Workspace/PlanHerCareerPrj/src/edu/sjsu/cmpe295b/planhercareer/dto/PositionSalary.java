package edu.sjsu.cmpe295b.planhercareer.dto;

public class PositionSalary {
	private String id;
	private String position;
	
	private Double lowRangeSalary;
	private Double highRangeSalary;
	private Long numSamples;
	private Double meanSalary;
	private String companyId;
	
	private String companyName;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Double getLowRangeSalary() {
		return lowRangeSalary;
	}
	public void setLowRangeSalary(Double lowRangeSalary) {
		this.lowRangeSalary = lowRangeSalary;
	}
	public Double getHighRangeSalary() {
		return highRangeSalary;
	}
	public void setHighRangeSalary(Double highRangeSalary) {
		this.highRangeSalary = highRangeSalary;
	}
	public Long getNumSamples() {
		return numSamples;
	}
	public void setNumSamples(Long numSamples) {
		this.numSamples = numSamples;
	}
	public Double getMeanSalary() {
		return meanSalary;
	}
	public void setMeanSalary(Double meanSalary) {
		this.meanSalary = meanSalary;
	}
	@Override
	public String toString() {
		return "PositionSalary [id=" + id + ", position=" + position
				+ ", lowRangeSalary=" + lowRangeSalary + ", highRangeSalary="
				+ highRangeSalary + ", numSamples=" + numSamples
				+ ", meanSalary=" + meanSalary + ", companyId=" + companyId
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionSalary other = (PositionSalary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
