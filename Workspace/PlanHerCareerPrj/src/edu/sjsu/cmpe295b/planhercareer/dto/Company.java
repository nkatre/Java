package edu.sjsu.cmpe295b.planhercareer.dto;

import java.util.List;

public class Company {

	private String id;
	
	private String name;

	private List<PositionSalary> roleSalaryData;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PositionSalary> getRoleSalaryData() {
		return roleSalaryData;
	}

	public void setRoleSalaryData(List<PositionSalary> roleSalaryData) {
		this.roleSalaryData = roleSalaryData;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", roleSalaryData="
				+ roleSalaryData + "]";
	}
}
