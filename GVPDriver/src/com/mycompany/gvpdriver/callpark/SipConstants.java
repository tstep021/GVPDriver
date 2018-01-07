package com.mycompany.gvpdriver.callpark;

public class SipConstants {
	//public static final String AD_MESSAGE_DELIMITER 	= "::";
	public static final String CRLF                 	= "\r\n";
	// public static final int DEFAULT_SERVER_PORT = 9100; //used by AD
    public static final String PARAM_BRANCH     = "branch";
    public static final String PARAM_EXPIRES    = "expires";
    public static final String PARAM_MADDR      = "maddr";
    public static final String PARAM_RECEIVED   = "received";
    public static final String PARAM_RPORT      = "rport";
    public static final String PARAM_SENTBY     = "sent-by";
    public static final String PARAM_TAG        = "tag";
    public static final String PARAM_TRANSPORT  = "transport";
    public static final String PARAM_TTL        = "ttl";

    public static final String PARAM_SEPARATOR  = ";";
    public static final String PARAM_ASSIGNMENT = "=";
   /*
	public static final String KEY_DATA_LOCAL_PORT 		= "data_local_port";
	public static final String KEY_LOCALHOST 			= "localhost";
	public static final String KEY_PROXY_LOCAL_PORT 	= "proxy_local_port";
	public static final String KEY_SIP_LOCAL_PORT 		= "sip_local_port";
	public static final String KEY_SIP_TIMEOUT			= "sip_timeout";
	public static final String KEY_SIP_TRIES			= "sip_tries";
	public static final String KEY_SIP_SLEEP			= "sip_sleep";
	
	//TODO remove 2 lines
	public static final String MY_ADDRESS = "10.76.222.11"; //"192.168.0.51";
	public static final int MY_PORT = 5060;
	
	public static final String KEY_SIP_REMOTE_HOST 		= "sip_remote_host";
	public static final String KEY_SIP_REMOTE_PORT 		= "sip_remote_port";
	*/
	public static final String SIP_PARAM_DELIM			= ";";
	/*
	public static final String MAIL_SMTP_HOST_KEY		= "mail_smtp_host";
	// Get sender address
	public static final String MAIL_FROM_KEY			= "mail_from"; 
	// get recipient address list
	public static final String MAIL_TO_KEY				= "mail_to";
	*/
    public static final String TRANSPORT_UDP                = "UDP";
    public static final String TRANSPORT_TCP                = "TCP";
    public static final String TRANSPORT_SCTP               = "SCTP";
    public static final String TRANSPORT_TLS                = "TLS";
    public static final int    TRANSPORT_UDP_USUAL_MAX_SIZE = 1300;
    public static final int    TRANSPORT_UDP_MAX_SIZE       = 65535;
    public static final char   TRANSPORT_VIA_SEP            = '/';
    public static final char   TRANSPORT_VIA_SEP2           = ' ';
    public static final int    TRANSPORT_DEFAULT_PORT       = 5060;
    public static final int    TRANSPORT_TLS_PORT           = 5061;
    public static final char   TRANSPORT_PORT_SEP           = ':';
    
    /////////to check
        //Parameters

        //Miscellaneous

    public static final char   FIELD_NAME_SEPARATOR = ':';
    public static final String DEFAULT_SIP_VERSION  = "SIP/2.0";

    public static final String IPV4_TTL             = "1";
    public static final char   AT                   = '@';
    public static final String LOOSE_ROUTING        = "lr";
    public static final char   LEFT_ANGLE_BRACKET   = '<';
    public static final char   RIGHT_ANGLE_BRACKET  = '>';
    public static final String HEADER_SEPARATOR     = ",";
    
      //STATUS CODES
    public static final int CODE_MIN_PROV                            = 100;
    public static final int CODE_MIN_SUCCESS                         = 200;
    public static final int CODE_MIN_REDIR                           = 300;
    public static final int CODE_MAX                                 = 699;

    public static final int CODE_100_TRYING                          = 100;
    public static final int CODE_180_RINGING                         = 180;
    public static final int CODE_200_OK                              = 200;
    public static final int CODE_401_UNAUTHORIZED                    = 401;
    public static final int CODE_405_METHOD_NOT_ALLOWED              = 405;
    public static final int CODE_407_PROXY_AUTHENTICATION_REQUIRED   = 407;
    public static final int CODE_481_CALL_TRANSACTION_DOES_NOT_EXIST = 481;
    public static final int CODE_486_BUSYHERE                        = 486;
    public static final int CODE_487_REQUEST_TERMINATED              = 487;
    public static final int CODE_500_SERVER_INTERNAL_ERROR           = 500;
    
      //REASON PHRASES
    public static final String REASON_180_RINGING  = "Ringing";
    public static final String REASON_200_OK       = "OK";
    public static final String REASON_405_METHOD_NOT_ALLOWED =
        "Method Not Allowed";
    public static final String REASON_481_CALL_TRANSACTION_DOES_NOT_EXIST =
        "Call/Transaction Does Not Exist";
    public static final String REASON_486_BUSYHERE = "Busy Here";
    public static final String REASON_487_REQUEST_TERMINATED =
        "Request Terminated";
    public static final String REASON_500_SERVER_INTERNAL_ERROR =
        "Server Internal Error";
    

    //TRANSACTION
    
    
    //TRANSACTION USER
    
    public static final int    DEFAULT_MAXFORWARDS   = 70;
    public static final String BRANCHID_MAGIC_COOKIE = "z9hG4bK";
    public static final String SIP_SCHEME            = "sip";
    public static final char   SCHEME_SEPARATOR      = ':';
    
    //TIMERS (in milliseconds)
    
    public static final int TIMER_T1 = 500;
    public static final int TIMER_T2 = 4000;
    public static final int TIMER_T4 = 5000;
    public static final int TIMER_INVITE_CLIENT_TRANSACTION = 32000;
 
    
    //TRANSACTION USER
    
    
    //CORE
    
    public static final String CONTENT_TYPE_SDP = "application/sdp";
    
        //Methods
    
    public static final String METHOD_INVITE   = "INVITE";
    public static final String METHOD_ACK      = "ACK";
    public static final String METHOD_REGISTER = "REGISTER";
    public static final String METHOD_BYE      = "BYE";
    public static final String METHOD_OPTIONS  = "OPTIONS";
    public static final String METHOD_CANCEL   = "CANCEL";
    
        //Classical form
    
    public static final String HDR_ALLOW               = "Allow";
    public static final String HDR_AUTHORIZATION       = "Authorization";
    public static final String HDR_CALLID              = "Call-ID";
    public static final String HDR_CONTACT             = "Contact";
    public static final String HDR_CONTENT_ENCODING    = "Content-Encoding";
    public static final String HDR_CONTENT_LENGTH      = "Content-Length";
    public static final String HDR_CONTENT_TYPE        = "Content-Type";
    public static final String HDR_CSEQ                = "CSeq";
    public static final String HDR_FROM                = "From";
    public static final String HDR_MAX_FORWARDS        = "Max-Forwards";
    public static final String HDR_RECORD_ROUTE        = "Record-Route";
    public static final String HDR_PROXY_AUTHENTICATE  = "Proxy-Authenticate";
    public static final String HDR_PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String HDR_ROUTE               = "Route";
    public static final String HDR_SUBJECT             = "Subject";
    public static final String HDR_SUPPORTED           = "Supported";
    public static final String HDR_TO                  = "To";
    public static final String HDR_VIA                 = "Via";
    public static final String HDR_WWW_AUTHENTICATE    = "WWW-Authenticate";
    
        //Compact form
    
    public static final char COMPACT_HDR_CALLID           = 'i';
    public static final char COMPACT_HDR_CONTACT          = 'm';
    public static final char COMPACT_HDR_CONTENT_ENCODING = 'e';
    public static final char COMPACT_HDR_CONTENT_LENGTH   = 'l';
    public static final char COMPACT_HDR_CONTENT_TYPE     = 'c';
    public static final char COMPACT_HDR_FROM             = 'f';
    public static final char COMPACT_HDR_SUBJECT          = 's';
    public static final char COMPACT_HDR_SUPPORTED        = 'k';
    public static final char COMPACT_HDR_TO               = 't';
    public static final char COMPACT_HDR_VIA              = 'v';
    
 
}
