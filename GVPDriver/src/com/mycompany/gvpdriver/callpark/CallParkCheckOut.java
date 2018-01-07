package com.mycompany.gvpdriver.callpark;

/** @copyright   2013 mycompany. */
/*
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;*/
import java.net.Socket;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.server.*;

/**
 * @file CallParkCheckOut.java
 * 
 * @description .
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public class CallParkCheckOut extends ServerBase
{
	private final static Logger logger = Logger.getLogger(CallParkCheckOut.class);
	  public CallParkCheckOut(int port) {
		  super(port);
		  setName("CallParkCheckOut");
	  }
	  
	  public void launchHandler(Socket s){
		  CheckOutHandlerT h = new CheckOutHandlerT(s);
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
	   * This method handles any messages received from the client.
	   *
	   * @param msg The message received from the client.
	   * @param client The connection from which the message originated.
	   */
	  /* 	  public void handleMessageFromClient(Object msg, ConnectionToClient client)
	  { 
		 // String fp = "handleMessageFromClient: ";
		  
		String output = null;
		  
		  if(msg==null)
			  return;
		  else if(msg.toString().equalsIgnoreCase("q")) {
			  //output = "Client " + client.getName() + " is quitting";
			  try {
				  logger.info("Client " + client.getName() + " is quitting");
				  client.sendToClient("Thank you for visiting. Bye");
				  client.close();
			  }
			  catch(Exception e) {
				  logger.error(fp + "Error closing client: " + e.getMessage());
			  }
			  return;
		  }
		  else {
			  output = client.getName() + "> " + msg;
		  }	  

		  logger.info(fp +output);
		 // this.sendToAllClients(output);
	  } 
		  */  

} // end of class