package com.mycompany.gvpdriver.callpark;

/** @copyright   2013 mycompany. */

import java.net.Socket;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.server.*;

/**
 * @file CallParkCheckIn.java
 * 
 * @description .
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public class CallParkCheckIn extends ServerBase {
	private final static Logger logger = Logger.getLogger(CallParkCheckIn.class);

	  public CallParkCheckIn(int port) {
		  super(port);
		  this.setName("CallParkCheckIn");
	  }
	  
	  public void launchHandler(Socket s){
		  CheckInHandlerT h = new CheckInHandlerT(s);
		  h.start();
	  }
	  
	  /**
	   * Overrides the hook method in the superclass.
	   * Called when the server stops accepting
	   * connections because an exception has been raised.
	   * The default implementation does nothing.
	   * This method may be overriden by subclasses.
	   *
	   * @param exception the exception raised.
	   */
	  protected void listeningException(Throwable exception) {
		  logger.error("listeningException: ");
	  }

	  /**
	   * Overrides the hook method in the superclass.
	   * Called when the server starts listening for
	   * connections.  The default implementation does nothing.
	   * The method may be overridden by subclasses.
	   */
	  protected void serverStarted() {
		  logger.info("serverStarted: listening on port " + getPort());
		  //logger.info("Listening on port " + getPort());
	  }

	  /**
	   * Overrides the hook method in the superclass.
	   * Called when the server stops accepting
	   * connections.  The default implementation
	   * does nothing. This method may be overriden by subclasses.
	   */
	  protected void serverStopped() {
		 logger.info("serverStopped: ");
	  }

	  /**
	   * Overrides the hook method in the superclass.
	   * Called when the server is closed.
	   * The default implementation does nothing. This method may be
	   * overriden by subclasses. When the server is closed while still
	   * listening, serverStopped() will also be called.
	   */
	  protected void serverClosed() {
		  logger.info("serverClosed: ");
	  }
	  
	 
	/**
	 * Raises an alarm in case of failure to accept connection
	 * 
	 * @param ex
	 */
	protected void handleListeningError(Exception ex, String subj, String name) {
		/*
		 * try { this.body = new StringBuffer(); this.body .append(new
		 * Timestamp(System.currentTimeMillis())) .append(": ") .append(name)
		 * .append(": ") .append(ex.getMessage()) .append("\n");
		 * 
		 * if(ex.fillInStackTrace()!=null)
		 * this.body.append(ex.fillInStackTrace());
		 * 
		 * EMailSend email = new EMailSend();
		 * 
		 * email.setSmtpHost(this.smtpHost); email.setFrom (this.from);
		 * email.setTo (this.to); email.setSubject (subj); email.setBody
		 * (this.body.toString()); //email.setAttFilename(attFilename);
		 * 
		 * email.start(); } catch(Exception exn){
		 * logger.error("Error sending email: " + exn); }
		 */
	}

} // end of class