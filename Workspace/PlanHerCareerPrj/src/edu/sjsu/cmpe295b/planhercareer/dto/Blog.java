package edu.sjsu.cmpe295b.planhercareer.dto;

import java.sql.Timestamp;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object representation of Blog
 * 
 * @author Team 5
 */
@XmlRootElement(name = "##default")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Blog 
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
	 * list of articles in a blog
	 */
	private List<Article> articles;
	
	/**
	 * current time-stamp
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
	 * @return the blog title
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
	 * @return the list of articles of a blog
	 */
	public List<Article> getArticles() {
		return articles;
	}

	/**
	 * @param the list of articles of a blog to set
	 */
	public void setArticles(List<Article> articles) {
		this.articles = articles;
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

	@Override
	public String toString() {
		return "Blog [user_id=" + user_id + ", blog_id=" + blog_id + ", title="
				+ title + ", articles=" + articles + ", timestamp=" + timestamp
				+ ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((articles == null) ? 0 : articles.hashCode());
		result = prime * result + ((blog_id == null) ? 0 : blog_id.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
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
		Blog other = (Blog) obj;
		if (articles == null) {
			if (other.articles != null)
				return false;
		} else if (!articles.equals(other.articles))
			return false;
		if (blog_id == null) {
			if (other.blog_id != null)
				return false;
		} else if (!blog_id.equals(other.blog_id))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}
}
