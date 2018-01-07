package com.mycompany.gvpdriver.callpark;

/** @copyright   2013 mycompany. */

//import java.util.*;

import org.apache.log4j.Logger;
//import com.mycompany.gvpdriver.server.*;

/**
 * @file 
 * 
 * @description .
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public class callparkd {
	private final static Logger logger = Logger.getLogger(callparkd.class);

	/**
	 * Starts the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CallParkCheckOut checkOutPoint = null;
		CallParkCheckIn checkInPoint = null;
		
		String propFile = null;
		// /----- initializing the server-----
		try {
			propFile = args[0];
			if (propFile != null) {
				Config.initialize(propFile);
			} else
				throw new Exception("Server properties file is unknown");
		} catch (Exception e) {
			logger.error("Exiting... " + e.getMessage());
			StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}
			System.exit(1);
		} finally {

		}
		
		try {
			try {
			checkOutPoint = new CallParkCheckOut(Config.checkout_local_port);
			//checkOutPoint.setName("CallParkCheckOut");
			checkOutPoint.setTimeout(Config.checkout_timeout);
			checkOutPoint.listen();
			//logger.info("CallParkCheckOut started");
			
			checkInPoint = new CallParkCheckIn(Config.checkin_local_port);
			//checkOutPoint.setName("CallParkCheckIn");
			checkInPoint.setTimeout(Config.checkin_timeout);
			checkInPoint.listen();
			//logger.info("CallParkCheckIn started");
			}
			catch(Exception ee){
				logger.error("Could not start CallParkCheckIn or CallParkCheckOut: " + ee.getMessage());
			}

		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}
			System.exit(-1);
		}
	}
} // end of class