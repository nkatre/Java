package edu.sjsu.cmpe295b.planhercareer.dto;

/**
 * 
 * Similarity metrics between User1 and User2
 */
public class ConnectionSimilarityMetrics 
{
	private String  user1;
	
	private String user2;
	
	private double friendshipStrength;
	
	private double jobPositionSimilarity;
	
	private double jobFieldSimilarity;
	
	private double salarySimilarity;
	
	private double locationSimilarity;
		
	private double companySimilarity;
	
	private double gradDegreeSimilarity;

	private double cumulativeScore;

	public double getFriendshipStrength() {
		return friendshipStrength;
	}

	public void setFriendshipStrength(double friendshipStrength) {
		this.friendshipStrength = friendshipStrength;
	}

	public double getJobPositionSimilarity() {
		return jobPositionSimilarity;
	}

	public void setJobPositionSimilarity(double jobPositionSimilarity) {
		this.jobPositionSimilarity = jobPositionSimilarity;
	}

	public double getJobFieldSimilarity() {
		return jobFieldSimilarity;
	}

	public void setJobFieldSimilarity(double jobFieldSimilarity) {
		this.jobFieldSimilarity = jobFieldSimilarity;
	}

	public double getSalarySimilarity() {
		return salarySimilarity;
	}

	public void setSalarySimilarity(double salarySimilarity) {
		this.salarySimilarity = salarySimilarity;
	}

	public double getLocationSimilarity() {
		return locationSimilarity;
	}

	public void setLocationSimilarity(double locationSimilarity) {
		this.locationSimilarity = locationSimilarity;
	}

	public double getCompanySimilarity() {
		return companySimilarity;
	}

	public void setCompanySimilarity(double companySimilarity) {
		this.companySimilarity = companySimilarity;
	}

	public double getGradDegreeSimilarity() {
		return gradDegreeSimilarity;
	}

	public void setGradDegreeSimilarity(double gradDegreeSimilarity) {
		this.gradDegreeSimilarity = gradDegreeSimilarity;
	}

	public double getCumulativeScore() {
		return cumulativeScore;
	}

	public void setCumulativeScore(double cumulativeScore) {
		this.cumulativeScore = cumulativeScore;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}
}
