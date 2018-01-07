package com.mycompany.gvpdriver.callpark;

public class Constants {
	public static final String MESSAGE_DELIMITER 				= "::";
	//public static final int DEFAULT_CHECKOUT_LOCAL_PORT = 9100; //used by AD
	/** For server */
	public static final String KEY_PARK_CHECKIN_LOCAL_PORT 		= "callpark_checkin_local_port";
	/** For server */
	public static final String KEY_PARK_CHECKOUT_LOCAL_PORT 	= "callpark_checkout_local_port";
	/** For server */
	public static final String KEY_PARK_CHECKIN_TIMEOUT			= "callpark_checkin_timeout";
	/** For server */
	public static final String KEY_PARK_CHECKOUT_TIMEOUT		= "callpark_checkout_timeout";
	/** For client */
	public static final String KEY_PARK_CHECKIN_IP			  	= "callpark_checkin_ip";
	/** For client */
	public static final String KEY_PARK_CHECKIN_PORT  			= "callpark_checkin_port";
	
	//sip message is sent to MCP gateway, available at runtime ci.getGatewayIP()
	//this key only for accessing IP in the call sip info object
	public static final String KEY_PARK_SIP_REMOTE_HOST 		= "callpark_sip_remote_host";
	public static final String KEY_PARK_SIP_REMOTE_PORT 		= "callpark_sip_remote_port";
	public static final String KEY_PARK_SIP_LOCAL_PORT 			= "callpark_sip_local_port";
	public static final String KEY_PARK_SIP_TIMEOUT				= "callpark_sip_timeout";
	public static final String KEY_PARK_SIP_TRIES				= "callpark_sip_tries";
	public static final String KEY_PARK_SIP_SLEEP				= "callpark_sip_sleep";
	public static final String KEY_PARK_VOICECLIENT_TIMEOUT		= "callpark_voiceclient_timeout";
	
	//TOdo remove 2 lines
	//public static final String MY_ADDRESS = "10.76.222.11"; //"192.168.0.51";
	//public static final int MY_PORT = 5060;
	
}
