package com.mycompany.gvpdriver.callpark;

/**
 * 
 * 
 */
public class DuplicateRequestException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DuplicateRequestException() {
		super();
	}
	/**
	 * Constructor
	 * @param s java.lang.String
	 */
	public DuplicateRequestException(String s) {
		super(s);
	}
	/**
	 * Constructor for the case that the exception is raised by the other exception.
	 * 
	 * @param msg java.lang.String
	 * @param ex java.lang.Exception
	 */
	public DuplicateRequestException(String msg, Exception e) 
	{
		super(msg, e);
	}
}
