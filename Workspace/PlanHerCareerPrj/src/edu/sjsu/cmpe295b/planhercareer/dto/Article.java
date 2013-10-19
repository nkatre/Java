package edu.sjsu.cmpe295b.planhercareer.dto;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object representation of Article
 * 
 * @author Team 5
 *
 */
@XmlRootElement(name = "##default")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Article 
{		
	/**
	 * article title
	 */
	private String title;
	
	/**
	 * article id
	 */
	private String article_id;
	
	/**
	 * current time-stamp
	 */
	private Timestamp currentTimestamp = new java.sql.Timestamp(new java.util.Date().getTime());
	
	/**
	 * timeStamp of the article
	 */
	private String timestamp = currentTimestamp.toString();
	
	/**
	 * user id
	 */
	private String user_id;
	
	/**
	 * blog id
	 */
	private String blog_id;
	
	/**
	 * content of the article
	 */
	private String content;

	/**
	 * @return the title of the article
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title of the article to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the timeStamp of the article
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp of the article to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the content of the article
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param the content of the article to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the article id
	 */
	public String getArticle_id() {
		return article_id;
	}

	/**
	 * @param article id to set
	 */
	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

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
	 * @return the blog_id
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

	@Override
	public String toString() {
		return "Article [title=" + title + ", article_id=" + article_id
				+ ", timestamp=" + timestamp + ", user_id=" + user_id
				+ ", blog_id=" + blog_id + ", content=" + content + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((article_id == null) ? 0 : article_id.hashCode());
		result = prime * result + ((blog_id == null) ? 0 : blog_id.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		Article other = (Article) obj;
		if (article_id == null) {
			if (other.article_id != null)
				return false;
		} else if (!article_id.equals(other.article_id))
			return false;
		if (blog_id == null) {
			if (other.blog_id != null)
				return false;
		} else if (!blog_id.equals(other.blog_id))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
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
