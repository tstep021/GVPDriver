package com.mycompany.gvpdriver.callpark;

/** @copyright   2013 mycompany. */

import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.ecs.cdr.*;

/**
 * @file CheckInHandlerT.java
 * 
 * @description
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public class CheckInHandlerT extends Thread {
	private final static Logger logger = Logger.getLogger(CheckInHandlerT.class);
	private Socket socket = null;
	
	/**
	 * Initializes socket
	 * 
	 * @param socket
	 */
	public CheckInHandlerT(Socket skt) {
		super("Park_IN_" + new Timestamp(System.currentTimeMillis()));
		logger.info(this.getName() + " created.");
		this.socket = skt;
	}

	/**
	 * Implements the superclass run() method
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		logger.info(this.getName() + " started.");

		PrintWriter out 		= null;
		ObjectInputStream oin 	= null;
		int status 				= BaseConstants.STATUS_ERROR;
		HashMap<String,String> sipInfo = null;
		InputStream is			= null;
		
		try {
			is = this.socket.getInputStream();
			oin = new ObjectInputStream(is);
			out = new PrintWriter(this.socket.getOutputStream(), true);
			sipInfo = (HashMap<String,String>)oin.readObject();
			logger.info("received sipInfo: " + sipInfo);
			Register.getInstance().add(sipInfo.get(CDRConstants.CALL_ID_KEY), sipInfo);
			//	throw new Exception("Message format is invalid!");
			status = BaseConstants.STATUS_SUCCESS;			
		} catch (Exception e) {
			logger.error("Error reading object input stream: " + e.getMessage());
			/*StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}*/
			
			//trying to handle different input stream - maybe used for 
			//heartbeat or command
			DataInputStream reader = null;
			try {
				reader = new DataInputStream(is);
				int available = reader.available();
				logger.trace("alternate stream: available " + available + " bytes to read");
				if(available<=0)
					throw new Exception("No bytes available to read from the alternate stream");
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<available;i++){
					sb.append(reader.readByte());
				}
				logger.info("alternate stream content: " + sb.toString());
				status = BaseConstants.STATUS_SUCCESS;
			}
			catch(Exception ex){
				logger.error("Error reading alternate stream: " + ex);
			}
			finally{
				try{
					reader.close();
				}
				catch(Exception exx){}
			}
		}
		finally {
			//send the response
			try {				
				out.println(status);
				out.flush();
			}
			catch(Exception e){
				logger.error("error sending response: "+e.getMessage());
			}
			
			// close all connections and streams
			try {
				oin.close();
				logger.debug("reader closed");
			} catch (Exception ex) {
				logger.error("Error closing ObjectInputStream: "
						+ ex.getMessage());
				StackTraceElement[] trace = ex.getStackTrace();
				if (trace != null) {
					for (int i = 0; i < trace.length; i++) {
						logger.error(trace[i]);
					}
				}
			}
			try {
				is.close();
				logger.debug("is closed");
			} catch (Exception ex) {
				logger.error("Error closing InputStream: "
						+ ex.getMessage());
			}
			
			try {
				out.close();
				logger.debug("writer closed");
			} catch (Exception ex) {
				logger.error("Error closing PrintWriter: "
						+ ex.getMessage());
				/*StackTraceElement[] trace = ex.getStackTrace();
				if (trace != null) {
					for (int i = 0; i < trace.length; i++) {
						logger.error(trace[i]);
					}
				}*/
			}
			
			try {
				this.socket.close();
				logger.debug("input socket closed");
			} catch (Exception ex) {
				logger.error("Error closing socket: " + ex.getMessage());
				/*StackTraceElement[] trace = ex.getStackTrace();
				if (trace != null) {
					for (int i = 0; i < trace.length; i++) {
						logger.error(trace[i]);
					}
				}*/
			}

			out = null;
			oin = null;
			this.socket = null;
		}
	} // end of run
	
	public void readAlternamteStream(InputStream is){
		int status = BaseConstants.STATUS_ERROR;//TODO
		//trying to handle different input stream - maybe used for 
		//heartbeat or command
		DataInputStream reader = null;
		try {
			reader = new DataInputStream(is);
			int available = reader.available();
			logger.trace("alternate stream: available " + available + " bytes to read");
			if(available<=0)
				throw new Exception("No bytes available to read from the alternate stream");
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<available;i++){
				sb.append(reader.readByte());
			}
			logger.info("alternate stream content: " + sb.toString());
			status = BaseConstants.STATUS_SUCCESS;
		}
		catch(Exception ex){
			logger.error("Error reading alternate stream: " + ex);
		}
		finally{
			try{
				reader.close();
			}
			catch(Exception exx){}
		}
		logger.info("status: " + status);
	}
} // end of class