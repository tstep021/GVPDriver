/**
 * Copyright 2005-2013, mycompany Inc.
 */

/* 
 * Date				Name				  Desc
 * ---------------------------------------------------------------------
 * 2005-04-12		Stephen Tam		  Initial Version
 * 2013-03-06		Tatiana Stepourska	updates for migration
 */
package com.mycompany.gvpdriver.util; 

/** 
 * 
 * @author mycompany
 * @version 1.0
 */
public class AlarmKeys 
{
	public static final String ALARM_GVP_DRIVER_INTERNAL_FAILURE_KEY			= "1000";
	public static final String ALARM_GVP_GLOBAL_CONFIG_LOAD_FAILURE_KEY			= "1001";

    public static final String ALARM_DELIM_KEY                                 = "|";
    
    public static final String SEVERITY_INFO									= "INFO";
	public static final String SEVERITY_WARNING									= "WARNING";
	public static final String SEVERITY_MAJOR									= "MAJOR";
	public static final String SEVERITY_CRITICAL								= "CRITICAL";
}