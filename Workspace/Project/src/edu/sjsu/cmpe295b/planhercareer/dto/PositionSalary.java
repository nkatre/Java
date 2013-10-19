package edu.sjsu.cmpe295b.planhercareer.dto;

public class PositionSalary {
	public String position;
	
	public Double lowRangeSalary;
	public Double highRangeSalary;
	public Long numSamples;
	public Double meanSalary;
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
		return "PositionSalary [position=" + position + ", lowRangeSalary="
				+ lowRangeSalary + ", highRangeSalary=" + highRangeSalary
				+ ", numSamples=" + numSamples + ", meanSalary=" + meanSalary
				+ "]";
	}
}
