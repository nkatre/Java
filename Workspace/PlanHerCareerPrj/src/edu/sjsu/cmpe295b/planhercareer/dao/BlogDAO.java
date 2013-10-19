package edu.sjsu.cmpe295b.planhercareer.dao;

import java.util.List;

import edu.sjsu.cmpe295b.planhercareer.dto.Article;
import edu.sjsu.cmpe295b.planhercareer.dto.Blog;
import edu.sjsu.cmpe295b.planhercareer.exception.DAOException;

/** 
 * Interface: BlogDAO 
 * 		- data access object  that contains methods to access the database for fetching and updating data  
 * 
 * @author Team 5
 */
public interface BlogDAO 
{
	// Article DB Fields
	
	/**
	 * article title
	 */
	public static final String ARTICLE_DBFIELD_TITLE = "title";
	
	/**
	 * article id
	 */
	public static final String ARTICLE_DBFIELD_ARTICLEID = "articleid";
	
	/**
	 * timeStamp of the article
	 */
	public static final String ARTICLE_DBFIELD_TIMESTAMP = "timestamp";
	
	/**
	 * user id of the article
	 */
	public static final String ARTICLE_DBFIELD_USERID = "userid";
	
	/**
	 * blog id
	 */
	public static final String ARTICLE_DBFIELD_BLOGID = "blogid";
	
	/**
	 * content of the article
	 */
	public static final String ARTICLE_DBFIELD_CONTENT = "content";
	
	// BLOG DB Fields
	/**
	 * title of the blog
	 */
	public static final String BLOG_DBFIELD_TITLE = "title";
	
	/**
	 * timeStamp of the blog
	 */
	public static final String BLOG_DBFIELD_TIMESTAMP = "timestamp";
	
	/**
	 * user id of the blog
	 */
	public static final String BLOG_DBFIELD_USERID = "userid";
	
	/**
	 * blog id
	 */
	public static final String BLOG_DBFIELD_BLOGID = "blogid";
	
	/**
	 * articles of the blog
	 */
	public static final String BLOG_DBFIELD_ARTICLES = "articles";
	
	/**
	 * description of the blog
	 */
	public static final String BLOG_DBFIELD_DESCRIPTION = "description";

	/**
	 * Create a Blog
	 * @param blog - Blog DTO containing blog details to be created
	 * @throws DAOException
	 */
	void createBlog(Blog blog) throws DAOException;
	
	/**
	 * Get all blogs
	 * @return list of blogs in the system
	 */
	List<Blog> getAllBlogs();
	
	/**
	 * Gets the Blog with the specified blogId
	 * @param blogId unique id for the blog
	 * @return Blog with the id specified
	 */
	Blog getBlog(String blogId);
	
	/**
	 * Delete the blog with the specified Id
	 * @param userId id of the user requesting for blog deletion
	 * @param blogId unique id of the blog
	 * @return true if deletion was successful; else, return false
	 * @throws DAOException if the user is not the creator or if blogId does not exist 
	 */
	void deleteBlog(String userId, String blogId) throws DAOException;
		
	/**
	 * Update the Blog with the new data
	 * @param blog Blog that needs to be updated
	 * @throws DAOException if user is not the creator or if the blogId does not exist
	 */
	void updateBlog(Blog blog) throws DAOException;
	
	/**
	 * Creates a new article for a given blog id
	 * @param article data transfer object to create the article in the system
	 * @return article id of the article created
	 * @throws DAOException if it is not able to create the Blog or if the blog does not exist
	 */
	void createArticle(Article article) throws DAOException;
	
	/**
	 * Retrieve all Articles for a given blog id
	 * @param blogId blog for which the article list is requested
	 * @return list of article for a given blog id
	 */
	List<Article> getArticlesForBlog(String blogId);
	
	/**
	 * Retrieve the article corresponding to a articleId
	 * @param blogId blog to which the article belongs
	 * @param articleId the unique id of the article requested 
	 * @return article with the given blog id and article id
	 */
	Article getArticle(String blogId, String articleId);
	
	/**
	 * Delete the article with the given article id and blog id
	 * @param userId id of the user requesting for blog deletion
	 * @param blogId unique id of the blog to which the article belongs
	 * @param articleId unique id of the article
	 * @return true if deletion was successful; else, return false
	 * @throws DAOException if the user is not the creator or if blogId and/or articleId does not exist 
	 */
	void deleteArticle(String userId, String blogId, String articleId) throws DAOException;

	/**
	 * Update the article with the new data
	 * @param article Article that needs to be updated
	 * @throws DAOException if user is not the creator or if the blogId and/or articleId does not exist
	 */
	void updateArticle(Article article) throws DAOException;

	/**
	 * Drops the DB
	 */
	void resetDB() throws DAOException;
}
