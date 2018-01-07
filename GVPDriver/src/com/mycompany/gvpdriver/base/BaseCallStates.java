package com.mycompany.gvpdriver.base;

/** Copyright 2012-2013, mycompany. All rights reserved */

/**
 * @file BaseCallStates.java
 * 
 * @description
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */
public interface BaseCallStates {

	// !!states must be configured as servlets 
	// in the web.xml deployment descriptor
	// DO NOT USE DIFFERENT STATES NAMES !
	public static final String APP_ROOT 	= "ROOT"; 
	public static final String callstart_checkout = "callstart_checkout";
	public static String callend 			= "callend";
	
	/** main dispatcher */
	public static String checkin 			= "checkin";
	//end of preconfigured servlets

	// classes that use submitbase variable
	public static final String CLASS_NIS          			= "com.mycompany.gvpdriver.event.NIS";
	public static final String CLASS_NOT_ACTIVE    			= "com.mycompany.gvpdriver.event.ConfigNotActiveMsg";
	public static final String CLASS_TECH_DIFF 				= "com.mycompany.gvpdriver.event.TechDiff";
	public static final String CLASS_GLOBAL_EVENT_CHECKIN 	= "com.mycompany.gvpdriver.event.CheckIn"; 
	public static final String CLASS_GLOBAL_EVENT_MESSAGE 	= "com.mycompany.gvpdriver.event.EventMessage"; 
	
	/*public static final
	 * String CLASS_GLOBAL_EVENT_MSG_EACH_LANGUAGE =
	 * "com.mycompany.phoenix.driver.event.EventMsgEachLanguage"; public static
	 * final String CLASS_GLOBAL_EVENT_MSG_NAVIGATOR =
	 * "com.mycompany.phoenix.driver.event.EventMsgNavigator"; public static final
	 * */
	 String CLASS_GLOBAL_EVENT_CHECKOUT = "com.mycompany.gvpdriver.event.CheckOut";
}// end of class