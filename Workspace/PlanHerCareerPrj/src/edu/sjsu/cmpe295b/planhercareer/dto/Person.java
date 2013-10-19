package edu.sjsu.cmpe295b.planhercareer.dto;

public class Person {
	public enum Gender
	{
		UNKNOWN,
		MALE,
		FEMALE
	};
	
	private String id;
	
	private String userName;
	
	private String name;
	
	private String dob;
	
	private Gender gender;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setGender(String gender) {
		String g = gender.toUpperCase().trim();
		try
		{
			this.gender = Gender.valueOf(g);
		} catch ( IllegalArgumentException iae) {
			this.gender = Gender.UNKNOWN;
		}
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", userName=" + userName + ", name=" + name
				+ ", dob=" + dob + ", gender=" + gender + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
