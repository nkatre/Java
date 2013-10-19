package edu.sjsu.cmpe295b.planhercareer.blog.ws;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.ObjectMapper;
import org.omg.CORBA.NO_RESOURCES;

import edu.sjsu.cmpe295b.planhercareer.blog.dao.BlogDAO;
import edu.sjsu.cmpe295b.planhercareer.blog.dao.MongoBlogDAOImpl;
import edu.sjsu.cmpe295b.planhercareer.blog.util.UniqueIdGenerator;
import edu.sjsu.cmpe295b.planhercareer.blog.util.UniqueIdGeneratorImpl;
import edu.sjsu.cmpe295b.planhercareer.blog.dto.Article;
import edu.sjsu.cmpe295b.planhercareer.blog.dto.Blog;
import edu.sjsu.cmpe295b.planhercareer.blog.dto.GroupWithoutEvent;

import edu.sjsu.cmpe295b.planhercareer.blog.exception.DAOException;
import edu.sjsu.cmpe275.strand.zkclient.ZkStrandNodeData;

/**
 * BlogResource - web-service 
 * 
 * @author Team 5
 */
@Path("/")
public class BlogResource 
	extends StrandResourceBase
{
	public static final String STRAND_NAME = "blog";
	
	/**
	 * Blog data access object
	 */
	private BlogDAO _dao = null;
	
	/**
	 * Unique Id generator
	 */
	private UniqueIdGenerator _idGen = null;

	/**
	 * Initializes 
	 * 		Blog DTO to an instance of Mongo Blog DTO object
	 * 		UniqueIdGenerator instance
	 */
	public BlogResource()
	{
		super(STRAND_NAME);
		
		try
		{
			_dao = new MongoBlogDAOImpl();
			_idGen = new UniqueIdGeneratorImpl();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates Blog from the request
	 * @param blog Blog to be created
	 * @return BlogId of the blog created using the passed values
	 */
	@Path("/blog")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createBlog(@Context UriInfo uriInfo, Blog blog)
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		String blogId = null;
		try {
			if ((null == blog.getUser_id()) || (blog.getUser_id().equals("")) ) {
				throw new WebApplicationException(new
						Exception(),Response.Status.UNAUTHORIZED);
			}
			blogId = _idGen.getUniqueId(null);
			blog.setBlog_id(blogId);
			_dao.createBlog(blog);
		} catch (DAOException daoex) {
			throw new WebApplicationException(daoex,Response.Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok("{\"blog_id\" : \"" + blogId + "\"}").build();
	}

	/**
	 * Fetches all Blogs in the system
	 * @return returns a JSON representation of the list of Blogs
	 * 			(does not include article information)
	 */
	@Path("/blog")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllBlogs(@Context UriInfo uriInfo) 
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		List<Blog> blogs = new ArrayList<Blog>();
		List<GroupWithoutEvent> blogsWithoutArticles = new ArrayList<GroupWithoutEvent>();
		try
		{
			blogs = _dao.getAllBlogs();

			if ( null != blogs)
			{
				for (Blog b : blogs)
				{
					GroupWithoutEvent ba = new GroupWithoutEvent();
					ba.copyFrom(b);
					blogsWithoutArticles.add(ba);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(ex,Response.Status.INTERNAL_SERVER_ERROR);
		}
		ObjectMapper objMapper = new ObjectMapper();
		String result = "";

		try
		{
			result = objMapper.writeValueAsString(blogsWithoutArticles); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
		return Response.ok(result).build();
	}

	/**
	 * Fetches either blog or article depending on the query parameters passed with the request
	 * @param uriInfo URI information
	 * @param blogId Unique id of the blog passed as a path parameter
	 * @return 
	 * 		if article id is provided as part of the query parameter, 
	 * 			the article having the given article id in blog id in the request will be fetched,
	 * 			if article not found, WebApplicationException is thrown;
	 * 		if article id is not provided as part of the query parameter, 
	 * 			the blog having the blog id in the request will be fetched,
	 * 			if blog not found, WebApplicationException is thrown
	 */
	@Path("/blog/{blogid}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getBlogOrArticle(@Context UriInfo uriInfo, @PathParam("blogid") String blogId ) 
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		String article_id = uriInfo.getQueryParameters().getFirst("article_id");

		if (null == blogId) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		Blog blog = null;
		GroupWithoutEvent ba = null;

		Article article = null;

		ObjectMapper objMapper = new ObjectMapper();
		String result = "";

		if (null==article_id){
			try
			{
				blog = _dao.getBlog(blogId);
				if ( null != blog)
				{
					ba = new GroupWithoutEvent();
					ba.copyFrom(blog);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new WebApplicationException(ex,Response.Status.NOT_FOUND);
			}

			if ( null == blog)
				throw new WebApplicationException(Response.Status.NOT_FOUND);

			try
			{
				result = objMapper.writeValueAsString(ba); 
				return Response.ok(result).build();
			} catch (Exception e) {
				e.printStackTrace();
				throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
			}
		} else {	
			try
			{
				article = _dao.getArticle(blogId,article_id);
				if ( null == article)
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				result = objMapper.writeValueAsString(article);
				return  Response.ok(result).build();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new WebApplicationException(ex,Response.Status.NOT_FOUND);
			}			
		}
	}

	/**
	 * Creates an article in the given blog
	 * @param blogId unique id of the blog in which the article needs to be created
	 * @param article Article data transfer object extracted from the request
	 * @return the unique id of the article created in the given blog
	 */
	@Path("/blog/{blogid}/article")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createArticle(@Context UriInfo uriInfo, @PathParam("blogid") String blogId, Article article) 
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		article.setBlog_id(blogId);
		String articleId = null;
		try
		{
			if ((null == article.getUser_id()) || (article.getUser_id().equals("")) ) {
				throw new WebApplicationException(new Exception(),Response.Status.UNAUTHORIZED);
			}
			//articleId = _idGen.getUniqueId(blogId);
			articleId = _idGen.getUniqueId(null); // unique id across all blogs
			article.setArticle_id(articleId);
			_dao.createArticle(article);
		} catch (DAOException daoex) {
			throw new WebApplicationException(daoex,Response.Status.INTERNAL_SERVER_ERROR);   
		}
		return Response.ok("{\"article_id\" : \"" + articleId + "\"}").build(); 
	}

	/**
	 * Fetches the list of articles in a given blog
	 * @param blogId unique id of the blog for which article list is requested
	 * @return JSON representation of the list of articles in a given blog
	 */
	@Path("/blog/{blogid}/article")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllArticlesForBlog(@Context UriInfo uriInfo, @PathParam("blogid") String blogId) 
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		List<Article> articles = new ArrayList<Article>();

		try
		{
			articles = _dao.getArticlesForBlog(blogId);
		} catch (NO_RESOURCES nr) {
			nr.printStackTrace();
			throw new WebApplicationException(nr,Response.Status.NOT_FOUND);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(ex,Response.Status.NOT_FOUND);
		}
		ObjectMapper objMapper = new ObjectMapper();
		String result = "";

		try
		{
			result = objMapper.writeValueAsString(articles); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok(result).build();
	}

	/**
	 * Deletes either blog or article depending on the query parameters passed with the request
	 * @param uriInfo URI information
	 * @param blogId Unique id of the blog passed as a path parameter
	 * @return Response object having the status of the delete operation
	 */
	@Path("/blog/{blogid}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteBlogOrArticle(@Context UriInfo uriInfo, @PathParam("blogid") String blogId ) 
	{
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		String article_id = queryParams.getFirst("article_id");
		String user_id = queryParams.getFirst("user_id");
		
		System.out.println("article_id: " + article_id + " user_id: " + user_id + " blogId: " + blogId);

		if ((null == blogId) || (null == user_id)) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		if (null==article_id) 
		{
			try
			{
				_dao.deleteBlog(user_id, blogId);
				return Response.status(200).entity("{\"message\" : \"Delete Blog successful\"}").build();
				//return Response.status(200).entity(uriInfo.getAbsolutePath().toString()).build();
			} catch (Exception ex) {
				ex.printStackTrace();	
				//throw new WebApplicationException(ex,Response.Status.NOT_FOUND);   
				throw new WebApplicationException(Response.status(400).entity("{\"message\" : \"Delete Blog Unsuccessful\"}").build());
			}			
		} else {	
			try {
				_dao.deleteArticle(user_id, blogId, article_id);
				return Response.status(200).entity("{\"message\" : \"Delete Article successful\"}").build();
			} catch (DAOException e) {
				throw new WebApplicationException(e, Response.Status.FORBIDDEN );
			} catch (NO_RESOURCES nr) {
				throw new WebApplicationException(nr,Response.Status.NOT_FOUND);
			} catch (NullPointerException npe) {
				throw new WebApplicationException(npe,Response.Status.BAD_REQUEST); 
			} catch (Exception ex) {
				throw new WebApplicationException();
			}
		}
	}

	
	/**
	 * Updates article with the values passed in the request
	 * @param blogId unique id of the Blog
	 * @param article Article data transfer object extracted from the request
	 * @return Response object having the status of the update operation
	 */
	@Path("/blog/{blogid}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateArticle(@Context UriInfo uriInfo, @PathParam("blogid") String blogId, Article article) 
	{		
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		article.setBlog_id(blogId);
		try
		{
			_dao.updateArticle(article);
			return Response.status(200).entity("{\"message\" : \"Update Article successful\"}").build();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException();//ex,Response.Status.NOT_FOUND);   
		}		
	}		
	
	/**
	 * Updates article with the values passed in the request
	 * @param blogId unique id of the Blog
	 * @param blog Article data transfer object extracted from the request
	 * @return Response object having the status of the update operation
	 */
	@Path("/blog/{blogid}/update")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateBlog(@Context UriInfo uriInfo, @PathParam("blogid") String blogId, Blog blog) 
	{		
		// Get LoginId query Params - If not found, then will be serviced by this instance
		String loginId = uriInfo.getQueryParameters().getFirst(LOGIN_ID_PARAM_KEY);
		
		// Find Candidate node
		ZkStrandNodeData node = selectNodeForService(loginId);
		
		// Redirect if candidate node is not this instance
		if (! isLocalNode(node))
		{
			URI redirectUri = getRedirectUrl(uriInfo, node);
			return Response.temporaryRedirect(redirectUri).build(); 
		}
		
		blog.setBlog_id(blogId);
		try
		{
			_dao.updateBlog(blog);
			return Response.status(200).entity("{\"message\" : \"Update Blog successful\"}").build();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException();//ex,Response.Status.NOT_FOUND);   
		}		
	}		
}
