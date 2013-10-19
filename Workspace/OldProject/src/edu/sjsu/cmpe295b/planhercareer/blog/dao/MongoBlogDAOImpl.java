package edu.sjsu.cmpe295b.planhercareer.blog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.omg.CORBA.NO_RESOURCES;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

import edu.sjsu.cmpe295b.planhercareer.blog.dto.Article;
import edu.sjsu.cmpe295b.planhercareer.blog.dto.Blog;
import edu.sjsu.cmpe295b.planhercareer.blog.exception.DAOException;

/** 
 * MongoBlogDAOImpl is a implementation for Mongo DB data access object  
 * Implements: BlogDAO
 * 
 * @author Team 5
 */
public class MongoBlogDAOImpl implements BlogDAO 
{
	/**
	 * Mongo Database Collection
	 */
	protected static final String sCollection = "mongo.collection";
	
	/**
	 * Mongo Database
	 */
	protected static final String sDB = "mongo.db";
	
	/**
	 * Mongo host
	 */
	protected static final String sHost = "mongo.host";
	
	/**
	 * DBCollection object
	 */
	private DBCollection collection;
	
	/**
	 * Properties object for setting Database configuration and properties
	 */
	private Properties props;
	
	/**
	 * Initializes Properties object and sets properties for Database setup
	 * @throws Exception
	 */
	public MongoBlogDAOImpl() throws Exception 
	{
		props = new Properties();
		props.setProperty(sHost, "localhost");
		props.setProperty(sDB, "blog");
		props.setProperty(sCollection, "blog");
	}
	
	/**
	 * Create a Blog
	 * @param blog - Blog DTO containing blog details to be created
	 * @throws DAOException if the blog id already exists
	 */
	@Override
	public void createBlog(Blog blog) throws DAOException 
	{
		if ( null == blog)
			throw new DAOException("Trying to create a null blog !!");
		
		Blog b = getBlog(blog.getBlog_id());
		
		if ( null != b )
			throw new DAOException("Blog with blogId (" + blog.getBlog_id() + ") already exists !!");
		
		BasicDBObject r = createBlogDBObject(blog);
		try
		{
			connect();
			collection.insert(r,WriteConcern.FSYNC_SAFE);
			
		} catch (Exception ex) {
			throw new DAOException(ex);
		}		
	}

	/**
	 * Get all blogs
	 * @return list of blogs in the system
	 */
	@Override
	public List<Blog> getAllBlogs() 
	{
		try 
		{
			connect();
			DBCursor cursor = collection.find();
			List<Blog> blogs = materializeBlogs(cursor);
			
			return blogs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Gets the Blog with the specified blogId
	 * @param blogId unique id for the blog
	 * @return Blog with the id specified
	 */
	@Override
	public Blog getBlog(String blogId) 
	{
		if ( null == blogId)
			return null;
				
		BasicDBObject query = new BasicDBObject(BLOG_DBFIELD_BLOGID,
										Pattern.compile(blogId, Pattern.CASE_INSENSITIVE));
		
		try 
		{
			connect();
			DBCursor cursor = collection.find(query);
			List<Blog> blogs = materializeBlogs(cursor);
			
			if ( (null != blogs) && (! blogs.isEmpty()))
				return blogs.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void updateBlog(Blog blog) throws DAOException {
		
		String blogId = blog.getBlog_id();
		String userId = blog.getUser_id();

		if ( (null == userId) || (null == blogId))
			throw new DAOException("UserId or articleId is null !!");
		
		Blog b = getBlog(blogId);

		if ( null == b)
			throw new DAOException("Blog (" + blogId + ") does not exist !!");
		
		if ( ! userId.equalsIgnoreCase(blog.getUser_id()))
			throw new DAOException("Blog cannot be updated by userId (" +
					userId + ")");

		if ((null != blog.getDescription()) && (!blog.getDescription().trim().isEmpty()))
			b.setDescription(blog.getDescription());
		
		if ((null != blog.getTitle()) && (!blog.getTitle().trim().isEmpty()))
			b.setTitle(blog.getTitle());
		
		if ((null != blog.getTimestamp()) && (!blog.getTimestamp().trim().isEmpty()))
			b.setTimestamp(blog.getTimestamp());
				
		try
			{
				connect();
				// First delete the blog and recreate to update the blog
				deleteBlog(b.getUser_id(), blogId);
				createBlog(b);
			} catch (Exception ex) {
				throw new DAOException(ex);
			}
    }

	/**
	 * Delete the blog with the specified Id
	 * @param userId id of the user requesting for blog deletion
	 * @param blogId unique id of the blog
	 * @return true if deletion was successful; else, return false
	 * @throws DAOException if the user is not the creator or if blogId or userId does not exist 
	 */
	@Override
	public void deleteBlog(String userId, String blogId) throws DAOException 
	{				
		Blog b = getBlog(blogId);
		
		if ((null == userId) || ( null == blogId))
			throw new DAOException("BlogId or UserId is null !!");
		
		if ( null == b)
		{
			throw new DAOException("BlogId (" + blogId + ") does not exist !!");
		}
		
		if (! userId.equalsIgnoreCase(b.getUser_id()))
		{
			throw new DAOException("UserId + (" + userId + ") is not the owner of this blog !!");			
		}
		
		try
		{
			connect();
			BasicDBObject obj = createBlogDBObject(b);
			collection.remove(obj,WriteConcern.FSYNC_SAFE);
		} catch (Exception ex) {
			throw new DAOException(ex);
		}

	}

	/**
	 * Creates a new article for a given blog id
	 * @param article data transfer object to create the article in the system
	 * @return article id of the article created
	 * @throws DAOException if it is not able to create the Blog or if the blog does not exist
	 */
	@Override
	public void createArticle(Article article) throws DAOException 
	{
		if ( null == article)
			throw new DAOException("Trying to create a null article !!");
		
		
		Blog b = getBlog(article.getBlog_id());
		
		if ( null == b)
			throw new DAOException("Blog with blogId (" + article.getBlog_id() + ") does not exist !!");
		
		List<Article> articles = b.getArticles();
		
		if ( null == articles)
		{
			articles = new ArrayList<Article>();
			b.setArticles(articles);
		}
		
		for (Article a : articles)
		{
			if ( article.getArticle_id().equals(a.getArticle_id()))
				throw new DAOException("Article with id (" + article.getArticle_id() + ") already exists !!");
		}

		articles.add(article);
		b.setArticles(articles);
		
		BasicDBObject r = createBlogDBObject(b);
		try
		{
			connect();
			// delete and create blog to update it
			deleteBlog(b.getUser_id(),b.getBlog_id());
			collection.insert(r,WriteConcern.FSYNC_SAFE);
		} catch (Exception ex) {
			throw new DAOException(ex);
		}	
	}

	/**
	 * Retrieve all Articles for a given blog id
	 * @param blogId blog for which the article list is requested
	 * @return list of article for a given blog id
	 */
	@Override
	public List<Article> getArticlesForBlog(String blogId) 
	{
		if ( null == blogId)
			return null;
		
		Blog b = getBlog(blogId);
		
		if (null == b)
			throw new NO_RESOURCES("Blog " + blogId + " does not exist !!"); 
			
				
		BasicDBObject query = new BasicDBObject(BLOG_DBFIELD_BLOGID,
										Pattern.compile(blogId, Pattern.CASE_INSENSITIVE));
		
		try 
		{
			connect();
			DBCursor cursor = collection.find(query);
			List<Blog> blogs = materializeBlogs(cursor);
			List<Article> articles = null;
			if ( (null != blogs) && (! blogs.isEmpty())) {
				
				articles =  blogs.get(0).getArticles();	
				
				if(null==articles) {
					throw new NO_RESOURCES("No articles for Blog " + blogId + " exist !!"); 
				}
				
				return articles;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NO_RESOURCES("No articles for Blog " + blogId + " exist !!");
		}
		
		return null;
	}

	/**
	 * Retrieve the article corresponding to a articleId
	 * @param blogId blog to which the article belongs
	 * @param articleId the unique id of the article requested 
	 * @return article with the given blog id and article id
	 */
	@Override
	public Article getArticle(String blogId, String articleId) 
	{
		if ( null == articleId)
			return null;
				
		List<Article> allArticles = getArticlesForBlog(blogId);
		
		if ( null == allArticles)
			return null;
		
		for (Article a : allArticles)
		{
			if (articleId.equalsIgnoreCase(a.getArticle_id()))
				return a;
		}
		
		return null;
	}

	/**
	 * Delete the article with the given article id and blog id
	 * @param userId id of the user requesting for blog deletion
	 * @param blogId unique id of the blog to which the article belongs
	 * @param articleId unique id of the article
	 * @return true if deletion was successful; else, return false
	 * @throws DAOException if the user is not the creator or if blogId and/or articleId does not exist 
	 */
	@Override
	public void deleteArticle(String userId, String blogId, String articleId)
	throws DAOException 
	{
		Blog b = getBlog(blogId);
		List<Article> articles = getArticlesForBlog(blogId);

		boolean articleFound = false;

		if ( null == articles)
			throw new NO_RESOURCES("Articles for blogId (" + blogId + ") does not exist !!"); 

		if ( (null == userId) || (null == articleId))
			throw new NullPointerException("UserId or articleId is null !!");

		Article article = null;

		// Iterate and remove the article if it exists.
		for(int i = 0 ; i < articles.size(); i++)
		{
			if (articleId.equalsIgnoreCase(articles.get(i).getArticle_id()) )
			{
				article = articles.get(i);
				// UserId check
				if ( ! userId.equalsIgnoreCase(article.getUser_id()))
					throw new DAOException("Article cannot be deleted by userId (" + userId + ")");

				articleFound = true;
				articles.remove(i);
				break;
			}
		}

		b.setArticles(articles);

		if (!articleFound) {
			throw new NO_RESOURCES("The Article:" + articleId +" for blogId (" + blogId + ") does not exist !!");
		}
		try
		{
			connect();
			// First delete the blog and recreate to update the blog
			deleteBlog(b.getUser_id(), blogId);
			createBlog(b);
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}
		
	/**
	 * Update the article with the new data
	 * @param article Article that needs to be updated
	 * @throws DAOException if user is not the creator or if the blogId and/or articleId does not exist
	 */
	@Override
    public void updateArticle(Article article) throws DAOException
    {
		String blogId = article.getBlog_id();
		String articleId = article.getArticle_id();
		String userId = article.getUser_id();

		Blog b = getBlog(blogId);
		List<Article> a = getArticlesForBlog(blogId);

		if ( null == a)
			throw new DAOException("Articles for blogId (" + blogId + ") does not exist !!");

					if ( (null == userId) || (null == articleId))
						throw new DAOException("UserId or articleId is null !!");

			Article a2 = null;
			// Iterate and remove the article if it exists.
			for(int i = 0 ; i < a.size(); i++)
			{
				if (articleId.equalsIgnoreCase(a.get(i).getArticle_id()) )
				{
					a2 = a.get(i);
					// UserId check
					if ( ! userId.equalsIgnoreCase(article.getUser_id()))
						throw new DAOException("Article cannot be updated by userId (" +
								userId + ")");

					a.remove(i);
					a.add(article);
					break;
				}
			}
			b.setArticles(a);
			if ( null == a2)
			{
				throw new DAOException("Article with id (" + articleId + ") not found in the blog !!");
			}

			// UserId check
			if ( ! userId.equalsIgnoreCase(article.getUser_id()))
				throw new DAOException("Article cannot be deleted by userId (" +
						userId + ")");

			try
			{
				connect();
				// First delete the blog and recreate to update the blog
				deleteBlog(b.getUser_id(), blogId);
				createBlog(b);
			} catch (Exception ex) {
				throw new DAOException(ex);
			}
    }
		
	/**
	 * Create the BlogDBObject
	 * @param blog - Blog DTO containing blog details to be created
	 * @return the BlogDBObject
	 */
	private BasicDBObject createBlogDBObject(Blog b)
	{
		BasicDBObject r = new BasicDBObject();

		if ( null != b.getBlog_id())
			r.append(BLOG_DBFIELD_BLOGID,b.getBlog_id());
		
		if ( null != b.getTimestamp())
			r.append(BLOG_DBFIELD_TIMESTAMP,b.getTimestamp());
		
		if ( null != b.getTitle())
			r.append(BLOG_DBFIELD_TITLE,b.getTitle());
	
		if ( null != b.getUser_id())
			r.append(BLOG_DBFIELD_USERID,b.getUser_id());
		
		if ( null != b.getDescription())
			r.append(BLOG_DBFIELD_DESCRIPTION,b.getDescription());
		
		List l = createArticlesDBObjects(b.getArticles());
		
		if ( null != l)
			r.append(BLOG_DBFIELD_ARTICLES, l);
		
		return r;
	}
	
	/**
	 * Create the ArticlesDBObject
	 * @param List<Article> - List of articles to be created
	 * @return the ArticlesDBObject
	 */
	private List createArticlesDBObjects(List<Article> articles)
	{
		if ( null == articles)
			return null;
		
		List l = new ArrayList();
		
		for (Article a: articles)
		{
			l.add(createArticleDBObject(a));
		}
		
		return l;
	}
	
	/**
	 * Create the ArticleDBObject
	 * @param article - Article DTO containing article details to be created
	 * @return the ArticleDBObject
	 */
	private BasicDBObject createArticleDBObject(Article a)
	{
		BasicDBObject r = new BasicDBObject();

		if ( null != a.getArticle_id())
			r.append(ARTICLE_DBFIELD_ARTICLEID, a.getArticle_id());
		
		if ( null != a.getBlog_id())
			r.append(BLOG_DBFIELD_BLOGID, a.getBlog_id());
		
		if ( null != a.getContent())
			r.append(ARTICLE_DBFIELD_CONTENT, a.getContent());
		
		if ( null != a.getTimestamp())
			r.append(ARTICLE_DBFIELD_TIMESTAMP, a.getTimestamp());
		
		if ( null != a.getTitle())
			r.append(ARTICLE_DBFIELD_TITLE, a.getTitle());
		
		if ( null != a.getUser_id())
			r.append(ARTICLE_DBFIELD_USERID, a.getUser_id());
		
		return r;
	}
	
	/**
	 * Utility function to materialize Blogs from resultset.
	 */
	private List<Blog> materializeBlogs(DBCursor cursor) 
	{
		ArrayList<Blog> r = new ArrayList<Blog>();

		for (int n = 0, N = cursor.size(); n < N; n++) 
		{
			DBObject data = cursor.next();

			Blog b = new Blog();
			
			String v = (String) data.get(BLOG_DBFIELD_BLOGID);
			if (v != null)
				b.setBlog_id(v);

			v = (String) data.get(BLOG_DBFIELD_TITLE);
			if (v != null)
				b.setTitle(v);

			v = (String) data.get(BLOG_DBFIELD_TIMESTAMP);
			if (v != null)
				b.setTimestamp(v);

			v = (String) data.get(BLOG_DBFIELD_USERID);
			if (v != null)
				b.setUser_id(v);
			
			v = (String) data.get(BLOG_DBFIELD_DESCRIPTION);
			if (v != null)
				b.setDescription(v);
			
			Object obj = (Object) data.get(BLOG_DBFIELD_ARTICLES);
						
			if ( null != obj)
			{
				if ( obj instanceof List)
				{
					List<BasicDBObject> l = (List<BasicDBObject>)obj;
					b.setArticles(materializeArticles(l));
				}
			}
			
			r.add(b);
		}
		return r;
	}

	/**
	 * Get List of Articles materialized from MongoDB
	 */
	private List<Article> materializeArticles(List articles)
	{
		ArrayList<Article> r = new ArrayList<Article>();
		
		for(Object obj : articles)
		{
			BasicDBObject o = (BasicDBObject) obj;
			Article a = new Article();
			String v = (String) o.get(ARTICLE_DBFIELD_ARTICLEID);
			a.setArticle_id(v);
			
			v = (String) o.get(ARTICLE_DBFIELD_BLOGID);
			a.setBlog_id(v);
			
			v = (String) o.get(ARTICLE_DBFIELD_CONTENT);
			a.setContent(v);
			
			v = (String) o.get(ARTICLE_DBFIELD_TIMESTAMP);
			a.setTimestamp(v);
			
			v = (String) o.getString(ARTICLE_DBFIELD_TITLE);
			a.setTitle(v);
			
			v = (String) o.get(ARTICLE_DBFIELD_USERID);
			a.setUser_id(v);
			
			r.add(a);			
		}
		
		return r;
	}

	/**
	 * Get connection to the data
	 */
	private void connect() 
	{
		try 
		{
			if (collection != null && collection.getName() != null)
				return ;
		} catch (Exception ex) {
			collection = null;
		}

		try {
			Mongo m = new Mongo(props.getProperty(sHost));
			DB db = m.getDB(props.getProperty(sDB));
			
			collection = db.getCollection(props.getProperty(sCollection));
			
			if (collection == null)
				throw new RuntimeException("Missing collection: " + props.getProperty(sCollection));

			return;
		} catch (Exception ex) {
			// should never get here unless no directory is available
			throw new RuntimeException("Unable to connect to mongodb on " + props.getProperty(sHost));
		}
	}
	
	/**
	 * To close the connection 
	 */
	public void release() 
	{
		collection = null;
	}

	@Override
	public void resetDB() throws DAOException 
	{
		try
		{
			connect();
			collection.drop();
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}

}
