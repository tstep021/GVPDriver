package com.mycompany.gvpdriver.base; 

/** Copyright 2008-2013, mycompany. All rights reserved */

/** 
 * @file         BaseBaseConstants.java	
 * 
 * @description Class contains a list of BaseConstants.
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
public class BaseConstants
{ 		
	/////////////////////////////////////////////////////////////
	/// GLOBAL CONFIGURATION KEYS
	/////////////////////////////////////////////////////////////
	public static final String AUDIO_REPOSITORY_URL_KEY      	= "audio_repository_url";
	public static final String CONFIDENCE_NBEST_MIN_KEY			= "confidence_nbest_min";
	//public static final String CONFIDENCE_NBEST_MIN_EN_KEY	= "confidence_nbest_min_en";
	//public static final String CONFIDENCE_NBEST_MIN_FR_KEY	= "confidence_nbest_min_fr";	

	public static final String TTSENGINE_PREFIX_KEY				= "ttsengine_";

	//public static final String __ROOT_NODE_NAME				= "callflow";
	public static final String CTX_PARAM_NAME_KEY			= "name";
	public static final String CALL_INFO_KEY				= "call_info";
	
	/**
	 * Key for accessing configuration file names - comma separated string - 
	 * filenames with extentions, e.g.: "app.properties,config.txt"
	 * All properties are being read as key-value pairs and fill the 
	 * default configuration hastable at SessionStartBase03 init
	 */
	public static final String KEY_APP_PROPERTIES    				= "app_properties";
	/**Name of the history logger appender*/
	public static final String KEY_CALL_HISTORY						= "CallHistory";
	public static final String KEY_CONFIRM			= "confirm";
	public static final String KEY_CONFIRM_INPUT_NO = "confirm_input_no";
	public static final String KEY_CONFIRM_INPUT_YES = "confirm_input_yes";
	/** The language configuration - for values see integer BaseConstants below */
	public final static String KEY_LANG_CONFIG                      = "lang_config";
	
	public final static String KEY_MDC_NAMES                      = "mdc_names";	
	public final static String KEY_MDC_VALUES                     = "mdc_values";	
	
	/** Email from address key */
	public static final String KEY_MAIL_FROM	            		= "mail_from";
	//for mail_to use VXMLProperties.PROP_MAINTAINER
	/** SMTP host address key */
	public static final String KEY_MAIL_SMTP_HOST	        		= "mail_smtp_host";
	/** Mail session key */
	public static final String KEY_MAIL_SESSION             		= "mail/Session";
	/** Maximum number of times to retry in case of error, no match, no input etc. */ 
	public static final String KEY_MAXTRIES         				= "maxtries"; 
	/** Node property telling to record it as a specified billing feature in CDR */
	public static final String KEY_RECORD_FEATURE      				= "record_feature";
	public static final String KEY_TEST								= "test";
	/** Key for accessing VXML builder object */
	public final static String KEY_SESSION_VXML             		= "session_vxml";
	
	public static final String DEBUG_KEY						= "debug";
	/** Default exit option key */
	public static final String DEFAULT_KEY						= "_default_";
	
	public static final String EVENT_KEY						= "aEvent";
	public static final String LANG_MODE__KEY					= "lang_mode_";
	public static final String LOADTEST_KEY                 	= "loadtest";
	public static final String MAXNOINPUT_KEY					= "maxnoinput";
	public static final String MAXNOMATCH_KEY					= "maxnomatch";
	public static final String MAXDIGITS_KEY					= "maxdigits";
	public static final String MINDIGITS_KEY					= "mindigits";
	public static final String NPA_LOC_KEY						= "npa_loc";
	public static final String RESOURCE_REPOSITORY_BASEPATH_KEY = "resource_repository_basepath";
	public static final String RESOURCE_REPOSITORY_URL_KEY  	= "resource_repository_url";
	
	public static final String REPEAT_KEY						= "repeat_key";
	public static final String GOBACK_KEY						= "goback_key";
	public static final String OPERATOR_KEY						= "operator_key";
	
	public static final String KEY_VXML_VERSION  				= "vxml_version";
	//////////////////////////////////////////////////////////////
	// END OF GLOBAL CONFIGURATION KEYS
	//////////////////////////////////////////////////////////////
		
	/* ================================================================ */
	/* ========== HTTP methods ======================================== */
	/* ================================================================ */
	/** Http request method POST */
	public static final String METHOD_POST           				= "post";
	/** Http request method GET */
	public static final String METHOD_GET            				= "get";
	/* ================================================================ */
	/* ========== End of HTTP methods ================================= */
	/* ================================================================ */
	
	public static final String EXIT_OPTION_TOKEN			= "_";

	public static final String FLAG_N						= "N";
	public static final String FLAG_Y						= "Y";
	/** Numeric value of On */
	public static final int  ON                         	= 1;
	/** Numeric value of Off */
	public static final int  OFF                        	= 0;

	//public static final String OPERATOR_YNFLAG				= "operator_ynflag";
	//public static final String SESSION_ID_KEY				= "sessionID";	
	//public static final int MAX_NUM_PROMPTS					= 4;
	//public static final int MAX_LENGTH_PADDING				= 5;
	//public static final int MAX_CALL_HISTORY				= 5;
	
	public static final String LIST_SEPARATOR           	= ",";

	public static final String MSG_TIMEOUT					= "2s";
	public static final String MAINFEATURE					= "mainfeature";
	public static final String GLOBAL_NODE_TYPE		= "global";
	public static final String NODE_TYPE_LANGUAGE			= "language";
	public static final String NODE_TYPE_MENU				= "menu";
	public static final String NODE_TYPE_SIMPLEMESSAGE		= "simplemessage";

	public static final String PARAM_ERR_URL				= "errURL";
	/* ==========Prompts related BaseConstants =========================== */
	public static final String PROMPT_TYPE_BULLETIN			= "bulletin";
	public static final String PROMPT_TYPE_CONFIRM			= "comfirm";
	public static final String PROMPT_TYPE_DYNAMIC 			= "dynamic";
	public static final String PROMPT_TYPE_ERROR			= "error";
	//public static final String PROMPT_TYPE_GOBACK			= "goback";
	public static final String PROMPT_TYPE_LANG				= "lang";
	public static final String PROMPT_TYPE_MAIN				= "main";
	public static final String PROMPT_TYPE_MAXNOINPUT		= "maxnoinput";
	public static final String PROMPT_TYPE_MAXNOMATCH		= "maxnomatch";
	public static final String PROMPT_TYPE_NIS				= "nis";
	public static final String PROMPT_TYPE_NOINPUT			= "noinput";
	public static final String PROMPT_TYPE_NOMATCH			= "nomatch";	
	public static final String PROMPT_TYPE_NOTACTIVATED		= "notactivated";	

	public static final String DIR_GRAMMAR                  = "grammars/";
	public static final String DIR_PROMPT               	= "prompts/";
	public static final String FETCHAUDIO					= "fetchaudio.wav";

	/* ========= End of prompt related BaseConstants ================ */

	// /SIP headers
	public static final String SIP_HDR_CALLID 			= "Call-ID";
	public static final String SIP_HDR_CONTENT_LENGTH 	= "Content-Length";
	public static final String SIP_HDR_CONTENT_TYPE 	= "Content-Type";
	public static final String SIP_HDR_CSEQ 			= "CSeq";
	public static final String SIP_HDR_FROM 			= "From";
	public static final String SIP_HDR_MAX_FORWARDS 	= "Max-Forwards";
	public static final String SIP_HDR_TO 				= "To";
	public static final String SIP_HDR_TXNUMBER 		= "piglet";
	public static final String SIP_HDR_VIA 				= "Via";
	// ///end of SIP headers
	public static final String SIP_INFO_KEY_CLID 		= "clid";
	public static final String SIP_INFO_KEY_DNIS 		= "dnis";
	public static final String SIP_INFO_KEY_TRUNKGROUP 	= "trunkGroup";
	// SIP Method
	public static final String SIP_METHOD_INFO 			= "INFO";
	public static final String SIP_PARAM_DELIMITER 		= ";";
	public static final String SIP_TRACE_DELIMITER 		= "~";
	
	public static final int STATUS_SUCCESS					= 0;
	public static final int STATUS_ERROR					= 9;
	public static final int ORA_ERR_NO_DATA_FOUND			= 1403;
	public static final int STATUS_DB_ACCESS_NODATA			= -3;
	public static final int STATUS_NO_DNIS_PROVISIONED		= -1;
	public static final int STATUS_NO_CONFIG_FOUND			= -2;
	public static final int STATUS_CONFIG_NOT_ACTIVE		= -3;	
	
	/* ================================================== */
	/* === file extensions values ======================= */
	/* ================================================== */
	/** File extention wav */
 	public static final String FILE_EXT_WAV                = "wav";
 	/** File extention vox */
	public static final String FILE_EXT_VOX                = "vox";
	/** File extention xml */
	public static final String FILE_EXT_XML                = "xml";
	/** File extention txt */
	public static final String FILE_EXT_TXT                = "txt";
	/** File extention ulaw */
	public static final String FILE_EXT_ULAW               = "ulaw";
	/* ================================================== */
	/* === end of file extensions values ================ */
	/* ================================================== */

	
	/////////////////////////////////////////////////////////////
	////////////////// DEFAULTS /////////////////////////////////
	/////////////////////////////////////////////////////////////

	public static final String DEFAULT_CONFIDENCELEVEL_EN 		= "0.50";
	public static final String DEFAULT_CONFIDENCELEVEL_FR 		= "0.50";

	public static final String DEFAULT_CONFIDENCE_CONFIRM_EN 	= "0.92";
	public static final String DEFAULT_CONFIDENCE_CONFIRM_FR 	= "0.70";

	public static final String DEFAULT_CONFIDENCE_NBEST_MIN_EN 	= "0.70";
	public static final String DEFAULT_CONFIDENCE_NBEST_MIN_FR 	= "0.50";

	/** Default interdigit timeout */
	public static final String DEFAULT_INTERDIGITTIMEOUT		= "3s";

	public static final int DEFAULT_MAXTRIES					= 3;
	//public static final int    DEFAULT_MENU_NAMES_RETRIES       = 3;
	public static final int    DEFAULT_NOMAXIMUM		        = -1;
	public static final int    DEFAULT_DIGIT_MINIMUM		    = 1;
	
	public static final String DEFAULT_LANGUAGE_MODE			= "en-US";
	/** Default size of max nbest list */
	public static final int DEFAULT_MAXNBEST					= 3;
	
	public static final String DEFAULT_GOBACK_KEY				= "9";
	public static final String DEFAULT_OPERATOR_KEY				= "0";
	public static final String DEFAULT_REPEAT_KEY				= "*";
	
	public static final int DEFAULT_REQUEST_TIMEOUT        		= 120000;//2min //180000; //3 min
	public static final String DEFAULT_TIMEOUT		           	= "4s";
	
	/* ================== CALL FLOW HEADER DEFAULTS ================= */
/*	public static final String SERVICE_ID 			= "service_id";
	public static final String ROOT_NODE_ID 		= "root_node_id";
	public static final String LANGCODE 			= "langcode";
*/	
	/* ================== END OF CALL FLOW HEADER DEFAULTS  ================= */
	
	/* ================== NODES KEYS ================= */
	
/*	
	public static final String ID 					= "id";
	public static final String NODE_NAME 			= "node_name";
	public static final String NODE_TYPE_CODE 		= "node_type_code";
	public static final String TRANSFER_PN			= "transfer_pn";
	public static final String IS_ROOT_YNFLAG 		= "is_root_ynflag";
	public static final String SP_NAME 				= "sp_name";
	public static final String TRANSFER_TYPE 		= "transfer_type_code";
	public static final String ENVIRONMENT			= "ENVIRONMENT";
	
	public static final String NODE_ID 				= "node_id";
	public static final String PROMPT_TYPE_CODE 	= "prompt_type_code";
	public static final String PROMPT_NAME 			= "prompt_name";
	public static final String LANGUAGE_CODE 		= "language_code";
	public static final String SEQUENCE_NUMBER		= "sequence_number";
	public static final String PROMPT_TEXT 			= "prompt_text";
	public static final String EXTENSION 			= "extension";
	
	public static final String PROP_TYPE_NAME 		= "prop_type_name";
	public static final String PROP_VALUE 			= "prop_value";
	*/
	//public static final String REPEAT_YNFLAG		= "repeat_ynflag";
	
	public static final String TXPHONE_KEY						= "txphone";	
	public static final String TRANSFER_TYPE_KEY            	= "txtype";
	
	/* ================== END OF NODES KEYS  ================= */
	
	/////////////////////////////////////////////////////////////
	/** Underscore. Also used as a call history node body 
	 * start token, for example, "@123456_main"
	 */
	public static final String UNDERSCORE               = "_";
	/** 
	 * Configuration delimiter, also used as a call history 
	 * key / value delimiter, for example, input=1
	 */
	public static final String CONFIG_DELIM             = "=";
	public static final String REQUEST_PARAM_DELIM      = "?";
	//public static final String HISTORY_DELIMITER		= ":";
	/** Pound. Also used as an event end token */
	public static final String POUND		            = "#";
	//public static final String _HISTORY_NODE_START       = "@";
	//public static final String _HISTORY_EVENT_BODY_START  = ":";
	//////////////////////////////////////////////////////////////
	
	public static final String REJECTCALL					= "rejectcall";
	public static final String TOKEN_CANCEL_X				= "x";
	
	public static final String VAR_DOC_HISTORY				= "doc_history";
	/** Recognition confidence of the result */
	public static final String VAR_RECRESULT_CONFIDENCE		= "rec_confidence";
	public static final String VAR_RECRESULT_INPUTMODE		= "rec_inputmode";
	
	public static final String VXML_VERSION					= "2.1";
}//end of IBaseConstants class