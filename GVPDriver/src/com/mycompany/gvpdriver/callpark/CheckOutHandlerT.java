package com.mycompany.gvpdriver.callpark;

/** @copyright   2013 mycompany. */

import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.Timestamp;
//import java.util.Properties;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.*;

/**
 * @file ClientHandlerThread.java
 * 
 * @description
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public class CheckOutHandlerT extends Thread {
	private final static Logger logger = Logger.getLogger(CheckOutHandlerT.class);
	private Socket socket = null;
	private String acSharedCallId = null;
	//private String vgInstanceId = null; //		// should be a SIPLOCALTAG
	private String imsMessageTxNumber = null;

	/**
	 * Initializes socket
	 * 
	 * @param socket
	 */
	public CheckOutHandlerT(Socket skt) {
		super("Park_OUT_" + new Timestamp(System.currentTimeMillis()));
		logger.info(this.getName() + " created.");
		this.socket = skt;
	}

	/**
	 * Implements the superclass run() method
	 */
	public void run() {
		logger.info(this.getName() + " started.");

		boolean success = false;
		String message = null;
		BufferedReader reader = null; // to send request to server
		InputStream is			= null;
		int status = BaseConstants.STATUS_ERROR;
		
		try {
			is = this.socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
			message = reader.readLine();
			logger.info("received message for call transfer: " + message);
			/*
			 * out = new PrintWriter(this.socket.getOutputStream(), true);
			 * //out.println("ERRORS:" + totalStatus + ",PROCESSED:" + status);
			 * out.println(status); //out.flush();
			 */
			success = true;
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			/*StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}*/
			//trying to handle different input stream - maybe used for 
			//heartbeat or command
			DataInputStream rdr = null;
			PrintWriter out = null;
			try {
				rdr = new DataInputStream(is);
				int available = rdr.available();
				logger.trace("alternate stream: available " + available + " bytes to read");
				if(available<=0)
					throw new Exception("No bytes available to read from the alternate stream");
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<available;i++){
					sb.append(rdr.readByte());
				}
				logger.info("alternate stream content: " + sb.toString());
				status = BaseConstants.STATUS_SUCCESS;
			}
			catch(Exception ex){
				logger.error("Error reading alternate stream: " + ex.getMessage());
			}
			finally{
				//send the response
				try {			
					out = new PrintWriter(this.socket.getOutputStream(), true);
					out.println(status);
					out.flush();
				}
				catch(Exception exx){
					logger.error("error sending response: "+exx.getMessage());
				}
				
				try{
					reader.close();
				}
				catch(Exception exx){}
			}
		}
		// close all connections and streams
		finally {
			try {
				reader.close();
				logger.debug("reader closed");
			} catch (Exception ex) {
				logger.error("Error closing ObjectInputStream: "
						+ ex.getMessage());
				/*StackTraceElement[] trace = ex.getStackTrace();
				if (trace != null) {
					for (int i = 0; i < trace.length; i++) {
						logger.error(trace[i]);
					}
				}*/
			}
			try {
				is.close();
				logger.debug("is closed");
			} catch (Exception ex) {
				logger.error("Error closing InputStream: "
						+ ex.getMessage());
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

			reader = null;
			this.socket = null;
		}

		if (!success) 
			return;

		// forward transfer number as a SIP message
		SipClient sipClient = null;
		HashMap<String,String> sipInfo = null;
		int responseCode = -1;

		try {
			if (!parseMessage(message))
				throw new Exception("Message format is invalid!");
			
			sipInfo = Register.getInstance().remove(acSharedCallId);
			if(sipInfo==null)
				throw new Exception("Call sip info is not available!");
			
			sipClient = new SipClient(); 	
			responseCode = sipClient.invoke(acSharedCallId,
							imsMessageTxNumber,
							sipInfo
							);
				
		} catch (Exception e) {
			responseCode = BaseConstants.STATUS_ERROR;
			logger.error("Error: " + e.getMessage());
			StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}
		}

		logger.info(this.getName() + ": done with status " + responseCode);
	} // end of run

	private boolean parseMessage(String message) throws Exception {
		String[] arr = message.split(Constants.MESSAGE_DELIMITER);
		acSharedCallId 		= arr[0].trim();
		//String vgInstanceId = arr[1].trim();
		imsMessageTxNumber 	= arr[2].trim();

		if (acSharedCallId.length() != 17 //|| vgInstanceId.length() <= 0
				|| imsMessageTxNumber.length() < 10)
			return false;

		return true;
	}
} // end of class