package edu.sjsu.cmpe295b.planhercareer.test;

import java.util.List;

import edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl;
import edu.sjsu.cmpe295b.planhercareer.dto.Article;
import edu.sjsu.cmpe295b.planhercareer.dto.Blog;
import edu.sjsu.cmpe295b.planhercareer.exception.DAOException;
import edu.sjsu.cmpe295b.planhercareer.util.UniqueIdGenerator;
import edu.sjsu.cmpe295b.planhercareer.util.UniqueIdGeneratorImpl;
import junit.framework.TestCase;

/**
 * JUnit test for Blog strand
 * 
 * Extends TestCase
 * @author Team 5
 *
 */
public class BlogTester extends TestCase{

	/**
	 * Mongo Blog DAO implementation instance
	 */
	MongoBlogDAOImpl blogDAO = null;
	
	/**
	 * Unique Id Generator instance
	 */
	UniqueIdGenerator idGen = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		try {
			blogDAO = new MongoBlogDAOImpl();
			idGen = new UniqueIdGeneratorImpl();
			idGen.reset();

			blogDAO.resetDB();

		} 
		catch (Exception e)
		{
			System.out.println("JUnit SetUp Error: Could not initiatlize Blog components"); 
		}
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		blogDAO.release();
		super.tearDown();
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#createBlog(edu.sjsu.cmpe295b.planhercareer.dto.Blog)}.
	 */
	public final void testCreateBlog()
	{
		System.out.println("\n#####JUNIT Test Case: testCreateBlog#####");
		try 
		{
			String userId = idGen.getUniqueId(null);
			String blogId = idGen.getUniqueId(null);
			

			Blog blog = new Blog();
			blog.setUser_id(userId);
			blog.setTitle("Blog 1a");
			blog.setDescription("Blog Desc 1a");
			blog.setBlog_id(blogId);
			blogDAO.createBlog(blog);

			blog.setBlog_id(blogId);
			System.out.println("Blog created: " + blog.toString() + "\n");
		}
		catch (Exception e)
		{
			System.out.println("JUNIT Test Error in Method: testInsertNewMovie\n");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#getAllBlogs()}.
	 */
	public final void testGetAllBlogs()
	{
		System.out.println("\n#####JUNIT Test Case: testGetAllBlogs#####");
		try {
			String userId1 = idGen.getUniqueId(null);
			String blogId1 = idGen.getUniqueId(null);

			Blog blog1 = new Blog();
			blog1.setUser_id(userId1);
			blog1.setBlog_id(blogId1);
			blog1.setTitle("Blog 1");
			blog1.setDescription("Blog Desc 1");

			blogDAO.createBlog(blog1);

			String userId2 = idGen.getUniqueId(null);
			String blogId2 = idGen.getUniqueId(null);

			Blog blog2 = new Blog();
			blog2.setUser_id(userId2);
			blog2.setBlog_id(blogId2);
			blog2.setTitle("Blog 2");
			blog2.setDescription("Blog Desc 2");

			blogDAO.createBlog(blog2);

			List<Blog> blogList = blogDAO.getAllBlogs();
			System.out.println("Blogs list: " + blogList.toString() + "\n");
		} catch (Exception e) {
			System.out.println("JUNIT Test Error in Method: testGetAllBlogs\n");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#getBlog(java.lang.String)}.
	 */
	public final void testGetBlog()
	{
		System.out.println("\n#####JUNIT Test Case: testGetBlog#####");
		
		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		try {
			blogDAO.createBlog(blog);
			Blog b = blogDAO.getBlog(blog.getBlog_id());
			System.out.println("Blog:" + b.toString() + "\n");	
		} catch (Exception e) {
			System.out.println("JUNIT Test Error in Method: getBlog" + blog.getBlog_id() + "\n");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#deleteBlog(java.lang.String, java.lang.String)}.
	 */
	public final void testDeleteBlog()
	{
		System.out.println("\n#####JUNIT Test Case: testDeleteBlog#####");

		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");
		try {
			blogDAO.createBlog(blog);
			blogDAO.deleteBlog(userId, blogId);
			System.out.println("Delete successful for Blog:" + blog.toString() + "\n");
		} catch (Exception e) {
			System.out.println("JUNIT Test Error in Method: deleteBlog" + blogId + "\n");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#createArticle(edu.sjsu.cmpe295b.planhercareer.dto.Article)}.
	 */
	public final void testCreateArticle()
	{
		System.out.println("\n#####JUNIT Test Case: testCreateArticle#####");

		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);
		String articleId = idGen.getUniqueId(blogId);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		Article article = new Article();
		article.setArticle_id(articleId);
		article.setBlog_id(blogId);
		article.setContent("a1aa");
		article.setUser_id(userId);
		article.setTitle("a1aa");

		try {
			blogDAO.createBlog(blog);
			blogDAO.createArticle(article);
			System.out.println("Created article:" + article.toString() + "\n");
		} catch (DAOException e) {
			System.out.println("JUNIT Test Error in Method: createArticle\n");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#getArticlesForBlog(java.lang.String)}.
	 */
	public final void testGetArticlesForBlog()
	{
		System.out.println("\n#####JUNIT Test Case: testGetArticlesForBlog#####");

		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);
		String articleId1 = idGen.getUniqueId(blogId);
		String articleId2 = idGen.getUniqueId(blogId);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		Article article1 = new Article();
		article1.setArticle_id(articleId1);
		article1.setBlog_id(blogId);
		article1.setContent("a1aa");
		article1.setUser_id(userId);
		article1.setTitle("a1aa");

		Article article2 = new Article();
		article2.setArticle_id(articleId2);
		article2.setBlog_id(blogId);
		article2.setContent("a1aa");
		article2.setUser_id(userId);
		article2.setTitle("a1aa");

		try {
			blogDAO.createBlog(blog);
			blogDAO.createArticle(article1);
			blogDAO.createArticle(article2);
			List<Article> articles = blogDAO.getArticlesForBlog(blogId);
			System.out.println("Articles for blog:" + blogId + ": " + articles.toString() + "\n");
		} catch (DAOException e) {
			System.out.println("JUNIT Test Error in Method: getArticlesForBlog\n");
			assertTrue(false);
		}	   
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#getArticle(java.lang.String, java.lang.String)}.
	 */
	public final void testGetArticle()
	{
		System.out.println("\n#####JUNIT Test Case: testGetArticle#####");
		
		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);
		String articleId1 = idGen.getUniqueId(blogId);
		String articleId2 = idGen.getUniqueId(blogId);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		Article article1 = new Article();
		article1.setArticle_id(articleId1);
		article1.setBlog_id(blogId);
		article1.setContent("a1aa");
		article1.setUser_id(userId);
		article1.setTitle("a1aa");

		Article article2 = new Article();
		article2.setArticle_id(articleId2);
		article2.setBlog_id(blogId);
		article2.setContent("a1aa");
		article2.setUser_id(userId);
		article2.setTitle("a1aa");

		try {
			blogDAO.createBlog(blog);
			blogDAO.createArticle(article1);
			blogDAO.createArticle(article2);
			Article article = blogDAO.getArticle(blogId, articleId1);
			System.out.println("Article: " + articleId1 + " for blog: " + blogId + " ::: " + article.toString() + "\n");
		} catch (DAOException e) {
			System.out.println("JUNIT Test Error in Method: getArticle\n");
			assertTrue(false);
		}	 
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#deleteArticle(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public final void testDeleteArticle()
	{
		System.out.println("\n#####JUNIT Test Case: testDeleteArticle#####");

		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);
		String articleId = idGen.getUniqueId(blogId);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		Article article = new Article();
		article.setArticle_id(articleId);
		article.setBlog_id(blogId);
		article.setContent("a1aa");
		article.setUser_id(userId);
		article.setTitle("a1aa");

		try {
			blogDAO.createBlog(blog);
			blogDAO.createArticle(article);
			blogDAO.deleteArticle(userId, blogId, articleId);
			System.out.println("Delete article: " + articleId + " for blog: " + blogId + " ::: " + article.toString());
		} catch (DAOException e) {
			System.out.println("JUNIT Test Error in Method: deleteArticle");
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link edu.sjsu.cmpe295b.planhercareer.dao.MongoBlogDAOImpl#updateArticle(edu.sjsu.cmpe295b.planhercareer.dto.Article)}.
	 */
	public final void testUpdateArticle()
	{
		System.out.println("\n#####JUNIT Test Case: testUpdateArticle#####");

		String userId = idGen.getUniqueId(null);
		String blogId = idGen.getUniqueId(null);
		String articleId = idGen.getUniqueId(blogId);

		Blog blog = new Blog();
		blog.setUser_id(userId);
		blog.setBlog_id(blogId);
		blog.setTitle("Blog1");
		blog.setDescription("Blog Desc 1");

		Article article = new Article();
		article.setArticle_id(articleId);
		article.setBlog_id(blogId);
		article.setContent("a1aa");
		article.setUser_id(userId);
		article.setTitle("a1aa");

		try {
			blogDAO.createBlog(blog);
			blogDAO.createArticle(article);

			article.setArticle_id(articleId);
			article.setBlog_id(blogId);
			article.setContent("bbb");
			article.setUser_id(userId);
			article.setTitle("bbb");

			blogDAO.updateArticle(article);

			System.out.println("Update article: " + articleId + " for blog: " + blogId + " ::: " + article.toString() + "\n");
		} catch (DAOException e) {
			System.out.println("JUNIT Test Error in Method: updateArticle\n");
			assertTrue(false);
		}
	}

}
