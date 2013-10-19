package edu.sjsu.cmpe295b.planhercareer.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JobPosition {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


	private String positionId;
	
	private String position;
	
	private String locationId;
	
	private String location;
	
	private String startDate;
	
	private String endDate;

	private String companyId;
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

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
	
	public long compareDates(JobPosition p)
	{
		String thisEndDateStr = getEndDate();
		String thisStartDateStr = getStartDate();
		
		String otherStartDateStr = p.getStartDate();
		String otherEndDateStr = p.getEndDate();
		
		Date thisEndDate = null;
		long endDateMs = -1;
		Date thisStartDate = null;
		long startDateMs = -1;

		Date otherEndDate = null;
		long otherEndDateMs = -1;

		Date otherStartDate = null;
		long otherStartDateMs = -1;

		try
		{
			
			if ( null != thisEndDateStr)
			{
				thisEndDate = format.parse(thisEndDateStr);
				endDateMs = thisEndDate.getTime();
			}
			if ( null != thisStartDateStr)
			{
				thisStartDate = format.parse(thisStartDateStr);
				startDateMs = thisStartDate.getTime();
			}
			if ( null != otherEndDateStr)
			{
				otherEndDate = format.parse(otherEndDateStr);
				otherEndDateMs = otherEndDate.getTime();
			}
			
			if ( null != otherStartDateStr)
			{
				otherStartDate = format.parse(otherStartDateStr);
				otherStartDateMs = otherStartDate.getTime();
			}
			if ( (null == thisEndDate) && (null == thisStartDate))
				return 0;

			if ( (null == otherEndDate) && (null == otherStartDate))
				return 0;
			
			long otherSelectedDateMs = otherEndDateMs;
			if ( null == otherEndDate)
				otherSelectedDateMs = otherStartDateMs;
			
			long selectedDateMs = endDateMs;
			if ( null == endDate)
				selectedDateMs = startDateMs;
			
			return (selectedDateMs - otherSelectedDateMs);
		} catch (ParseException pe) {
			return 0;
		}		
	}
}
