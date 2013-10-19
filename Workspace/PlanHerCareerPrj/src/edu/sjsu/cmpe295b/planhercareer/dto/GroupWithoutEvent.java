package edu.sjsu.cmpe295b.planhercareer.dto;

import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object representation of Blog without Article
 * Used to send response for services that needs blog data without the associated article information
 * 
 * @author Team 5
 */
@XmlRootElement(name = "##default")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupWithoutEvent 
{	
	/**
	 * user id
	 */
	private String user_id;
	
	/**
	 * blog id
	 */
	private String blog_id;
	
	/**
	 * title of the blog
	 */
	private String title;
	
	/**
	 * Default value taking current Timestamp
	 */
	private Timestamp currentTimestamp = new java.sql.Timestamp(new java.util.Date().getTime());
	
	/**
	 * timeStamp of the blog
	 */
	private String timestamp = currentTimestamp.toString();

	/**
	 * description of the blog
	 */
	private String description;

	/**
	 * @return the user id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the blog id
	 */
	public String getBlog_id() {
		return blog_id;
	}

	/**
	 * @param blog_id to set
	 */
	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	/**
	 * @return the title of the blog
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title of the blog to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the timestamp of the blog
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp of the blog to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the description of the blog
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description of the blog to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Copy values from blog
	 * @param b Blog DTO
	 */
	public void copyFrom(Blog b)
	{
		description = b.getDescription();
		timestamp = b.getTimestamp();
		title = b.getTitle();
		user_id = b.getUser_id();
		blog_id = b.getBlog_id();
	}
}
