package edu.sjsu.cmpe295b.planhercareer.blog.exception;

/**
 * DAOException class
 * 		extends Exception
 * 
 * @author Team 5
 */
public class DAOException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default no-argument constructor
	 */
	public DAOException()
	{}
	
	/**
	 * Constructor taking a Throwable instance as a parameter
	 * @param t Throwable instance
	 */
	public DAOException(Throwable t)
	{
		super(t);
	}
	
	/**
	 * Constructor taking a String as a parameter
	 * @param t exception message string
	 */
	public DAOException(String msg)
	{
		super(msg);
	}
}
