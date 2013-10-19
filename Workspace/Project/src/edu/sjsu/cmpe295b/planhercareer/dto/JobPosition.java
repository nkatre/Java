package edu.sjsu.cmpe295b.planhercareer.dto;

public class JobPosition {

	private String positionId;
	
	private String position;
	
	private String locationId;
	
	private String location;
	
	private String startDate;
	
	private String endDate;

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "JobPosition [positionId=" + positionId + ", position="
				+ position + ", locationId=" + locationId + ", location="
				+ location + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}	
}
