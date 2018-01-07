package com.mycompany.gvpdriver.base;

/** copyright 2009-2013, mycompany  */

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.entity.*;
import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;


/** 
 * File         BaseServletExtension.java
 * 
 * Description  This is a base abstract servlet. 
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
 public abstract class BaseServletExtension extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static final Logger lo = Logger.getLogger(BaseServletExtension.class);
	/**
	 * To, From, CSeq, Call-ID, Max-Forwards,  and Via
	 */
	public static String GVP_NAMELIST = null;/* CDRConstants.KEY_SESSION_COM_GENESYSLAB_SESSIONID +" "+ 
	 CDRConstants.KEY_SESSION_COM_GENESYSLAB_USERDATA + " " + 
		// "session.connection.protocol.sip.headers" + " " + 
		 "session.connection.protocol.sip.headers['x-channel']" + " " + 
		 "session.connection.protocol.sip.headers['from']" + " " + 
		 "session.connection.protocol.sip.headers['to']" + " " + 
		 "session.connection.protocol.sip.headers['via']" + " " + 
		 "session.connection.protocol.sip.headers['call-id']" + " " + 
		 "session.connection.protocol.sip.headers['cseq']" + " " +
		 "session.connection.protocol.sip.headers['max-forwards']" + " " +
		 "session.connection.protocol.sip.headers['x-genesys-gvp-session-id']" + " " + 
		 "session.connection.protocol.sip.headers['x-genesys-gvp-session-data']" + " " +
		 "session.connection.protocol.sip.headers['x-iscc-cofid']" + " " + 
		 "session.connection.protocol.sip.headers['contact']" + " " + 
		 "session.connection.callidref" + " " + 
		 "session.connection.uuid" + " " + 
		 "session.connection.siplocaltag" + " " +
		 "session.com.genesyslab" + " " +
		 "session.com.genesyslab.userdata" + " " +
		 "session.com.genesyslab.sessionid" + " " + 
		 "session.com.genesyslab.userdata.calluuid";*/
	
	/** 
	 * Gets invoked once by the service method 
	 *   
	 * @param  config 	The ServletConfig object that contains configutation 
	 *        			information for this servlet 
	 * @exception ServletException    thrown when the servlet's normal operation 
	 *								 is interrupted
	 */
	public void init(ServletConfig config) throws ServletException 	{
		super.init(config);
	}
	 
	/** 
	 * Allows a servlet to handle a POST request. 
	 * 
	 * @param request    An HttpServletRequest object that contains the request the
	 *                   client has made of the servlet
	 *
	 * @param response   An HttpServletResponse object that contains the response 
	 *				     the servlet sends to the client 
     *
     * @exception ServletException  If the request for the GET could not be handled
	 * @exception IOException   If an input or output error is detected when the 
	 *						    servlet handles the GET request 	
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse  response)
    	throws ServletException, IOException 	{
		doGet(request, response);
  	}
  		 
	/**
	 * Handles both HTTP POST and GET operations.
	 * 
	 * @param request  --Client HTTP request object.
	 * @param response --Client HTTP response object.
	 * 
	 * @exception java.io.IOException
	 */
	protected void  doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException	{
		String callID		= null;
		PrintWriter output 	= null;
		HttpSession session = null;
		ICallInfo ci			= null;
		VXML vxml       	= null;
		String result 		= null;
		
		try {
			// set the content type for the response
			response.setContentType("text/html");
			//response.setContentType("application/voicexml+xml");//;charset=UTF-8")
			output = response.getWriter();
    	
			// get current session
			session = request.getSession();

			// get objects stored in the current session
			ci		= (ICallInfo)    session.getAttribute(BaseConstants.CALL_INFO_KEY);
			//get call ID for logging
			if(ci!=null)
				callID	= ci.getCallID();
			vxml       = (VXML)       session.getAttribute(BaseConstants.KEY_SESSION_VXML);
			if(vxml==null)
				vxml = new VXML();
			else
				vxml.resetDoc();

		//	if(lo.isTraceEnabled()) 
			//	trace(request, callID, ci);
		
			result = doResponse(callID,
								ci,
								vxml,
								session, 
								request, 
								response
			);
		
			output.println(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			// release resources 
			output.flush();
			output.close();
		}
	}
			
	/**
	 * Creates Voice XML response string
	 * To be called by all classes that generate 
	 * vxml response documents
	 * 
	 * @param vxml
	 * @return
	 */
	public final String buildDocument(VXML vxml, 
									ICallInfo ci, 
									String submitNext,
									HttpServletRequest request) throws Exception {
		
		addDocumentHeader		(vxml, ci);
		addDocumentBody	 		(vxml, ci, submitNext, request);
		addDocumentFooter		(vxml, ci);
		
		return vxml.toString();
	}

	/** 
	* Creates VXML response document to close everything and exit 
	* quietly and gracefully in case of fatal error.
	* 
	* @param vxml
	* @param callID
	* 
	* @return results   The VXML response
	*/
	public final String silentExit(VXML vxml, String callID) {
		vxml.Header();		 	
			vxml.VxmlStart(null, BaseConstants.VXML_VERSION, BaseConstants.DEFAULT_LANGUAGE_MODE);
			 	
				vxml.FormStart("mainSilentExit");
					vxml.BlockStart("blockSilentExit");
					if(BaseGlobalConfig.debug) {
						vxml.Text("executing silent exit, disconnecting, exiting");
						}
						vxml.Log("[" + callID + "]: executing silentExit");
						vxml.Disconnect();
						//vxml.Exit();
					vxml.BlockEnd();
				vxml.FormEnd();
			 		
				vxml.CatchStart(VXMLEvents.EVENT_ERROR);
				if(BaseGlobalConfig.debug) {
					vxml.Text("caught document event Error in silent exit, exiting");
					}
				vxml.Log("[" + callID + "]: caught error in silentExit");
				vxml.Disconnect();
					//vxml.Exit();
				vxml.CatchEnd();

				//for 6.4.5+
				vxml.ErrorStart();
				if(BaseGlobalConfig.debug) {
					vxml.Text("document Error in silent exit, disconncting");
					}
				vxml.Log("[" + callID + "]: executing error in silentExit");
					//vxml.Exit();
				vxml.Disconnect();
				vxml.ErrorEnd();
				
				BaseUtils.catchDisconnectEvents(vxml);
			 	
		vxml.VxmlEnd();
			 	
		//if(lo.isTraceEnabled()) lo.trace(vxml.getBuffer()); 	
		return vxml.toString();
	}	

	//////////////////////////////////////////////////////////
	// ABSTRACT METHODS
	/////////////////////////////////////////////////////////
	/** 
	 * Builds custom part of the VXML response document body.
	 *
	 * To be implemented by all classes that generate 
	 * vxml response documents
	 * Do NOT include headers, footers, and document level 
	 * events
	 * 
	 * @param vxml	    -- VXML object
	 * @param ci		-- BaseCallInfo object (for Driver it shoudl be an instance of CallInfo)
	 * @return vxml     -- Part of VXML response
	 */
	public abstract VXML addDocumentBody(VXML vxml,ICallInfo ci, String submitNext, HttpServletRequest request) throws Exception;
	
	/**
	 * Creates document footer which includes all mandatory 
	 * error handling
	 * 
	 * @param vxml
	 * @return
	 */
	public abstract VXML addDocumentFooter(VXML vxml, ICallInfo ci) throws Exception;

	/**
	 * Executes the application logic and calls the method 
	 * that generates VXML response document.  
	 * To be implemented by a subclass.
	 * 
	 * @param callID	   --IVR call ID
	 * @param vxml         --VXML object
	 * @param info         --The CDR object
	 * @param session      --HTTP session object
	 * @param request      --HTTP request
	 * @param response     --HTTP response
	 * 
	 * @return String      The VXML response
	 */
	public abstract String doResponse(String 			callID,
										ICallInfo 		ci,
										VXML                vxml,
										HttpSession         session,
										HttpServletRequest  request,
										HttpServletResponse response
										) throws ServletException,IOException, Exception;
	
	//public abstract void addMDC(String callID, ICallInfo ci);
	//public abstract void removeMDC(ICallInfo ci);
	
	public void addMDC(String callID, ICallInfo ci){
		try {
			MDC.put(CDRConstants.CALL_ID_KEY, callID);
		}
		catch(Exception e){}
		
		try {
			String[] keys = ci.getMDCKeys();
			String[] values = ci.getMDCValues();
			lo.info("MDC values length: "+values.length+": 1st: "+values[0]);
			for(int i=0;i<keys.length;i++){
				MDC.put(keys[i],values[i]);
			}
		} catch (Exception e2) {
		}
	}
	
	public void removeMDC(ICallInfo ci){
		try {
			MDC.remove(CDRConstants.CALL_ID_KEY);
		}
		catch(Exception e){}
		
		try {
			String[] keys = ci.getMDCKeys();
			for(int i=0;i<keys.length;i++){
				MDC.remove(keys[i]);
			}
		} catch (Exception e2) {}
	}
	
	
	///////////////////////////////////////////////////////////
	// Private methods
	//////////////////////////////////////////////////////////
	/**
	 * 
	 * @param vxml
	 * @return
	 */
	private final VXML addDocumentHeader(VXML vxml, 
										ICallInfo ci
										) throws Exception {
		vxml.Header();//"1.0", "ISO-8859-1");
		//vxml.Header("1.0", "UTF-8");
		vxml.VxmlStart(BaseGlobalConfig.apppath + BaseCallStates.APP_ROOT, BaseGlobalConfig.vxml_version, ci.getLangMode());
		return vxml;
	}

	/**
	 * Adds catch blocks for document hang up events 
	 * 
	 * @param  vxml
	 * 
	 * @return vxml
	 */
/*	private final VXML catchDisconnectEvents(VXML vxml)
	{
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_HANGUP);	
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM);		
		vxml.CatchEnd();
	
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_HANGUP);
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.CatchEnd();
		
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_TRANSFER);
		vxml.Submit(ICallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.CatchEnd();	
		
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_TRANSFER);
		vxml.Submit(ICallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.CatchEnd();	

		vxml.CatchStart(VXMLEvents.EVENT_TRANSFER_DISCONNECT_HANGUP);
		vxml.Submit(ICallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.CatchEnd();
		
		return vxml;
	}*/
	
/*	private final void trace(HttpServletRequest request, String callID, ICallInfo ci){
		try {
			MDC.put(CDRConstants.CALL_ID_KEY, callID);		
		}
		catch(Exception e){
			
		}
		try{
			String url = request.getRequestURI();
			lo.trace("requestURI: " + url);
			//String dnis = ""+ci.getBackUpDnis();
			//StringBuffer sb = new StringBuffer();
			//long clid = ci.getCLID();

				//String session_connection_dn = request.getParameter("session.connection.dn");//"session.com.genesyslab.sessionid");
				//lo.trace("session_connection_dn: " + session_connection_dn);
				
				//session.connection.local.uri|LOCALURI|1|
				String session_connection_local_uri = request.getParameter("session.connection.local.uri");
				lo.trace("session_connection_local_uri: " + session_connection_local_uri);
				//session.connection.remote.uri|REMOTEURI|1|
				String session_connection_remote_uri = request.getParameter("session.connection.remote.uri");
				lo.trace("session_connection_remote_uri: " + session_connection_remote_uri);
				//session.connection.originator|ORIGIN|1|
				String session_connection_originator = request.getParameter("session.connection.originator");
				lo.trace("session.connection.originator=" + session_connection_originator);
				
				// SIPLOCALTAG						
				String session_connection_siplocaltag = request.getParameter("session.connection.siplocaltag");
				lo.trace("session.connection.siplocaltag=" + session_connection_siplocaltag);
			//	sb.append("Siplocaltag~").append(session_connection_siplocaltag).append("\n");
				
				//session.connection.protocol.name|PROTOCOLNAME|0|
				//String session_connection_protocol_name = request.getParameter("session.connection.protocol.name");
				//lo.trace("session.connection.protocol.name=" + session_connection_protocol_name);
				//session.connection.protocol.version|PROTOCOLVERSION|0|
				//String session_connection_protocol_version = request.getParameter("session.connection.protocol.version"); 
				//lo.trace("session.connection.protocol.version=" + session_connection_protocol_version);
				//session.connection.protocol.sip.headers|Sip.Invite|6|
				//String session_connection_protocol_sip_headers = request.getParameter("session.connection.protocol.sip.headers");
				//lo.trace("session.connection.protocol.sip.headers=" + session_connection_protocol_sip_headers);
				
				//"session.connection.protocol.sip.headers[�<sip header name>�]"
				String sip_header_to = request.getParameter("session.connection.protocol.sip.headers['to']");
				lo.trace("session.connection.protocol.sip.headers['to']=" + sip_header_to);
			//	if(sip_header_to!=null)
			//	sb.append("To~"+ sip_header_to + "\n"); //sip:"+dnis+trunkGroup+"@10.76.222.21:5060 SIP/2.0\n")
				
				String sip_header_from = request.getParameter("session.connection.protocol.sip.headers['from']");
				lo.trace("session.connection.protocol.sip.headers['from']=" + sip_header_from);
				//if(sip_header_from!=null)
				//sb.append("From~"+sip_header_from + "\n");	
				
				String sip_header_via = request.getParameter("session.connection.protocol.sip.headers['via']");
				lo.trace("session.connection.protocol.sip.headers['via']=" + sip_header_via);
				//String[] arr = sip_header_via.split(",");
				//for(int i=0;i<arr.length;i++)
				//	sb.append("Via~" + arr[i] + "\n");
				
				String sip_header_callid = request.getParameter("session.connection.protocol.sip.headers['call-id']");
				lo.trace("session.connection.protocol.sip.headers['call-id']=" + sip_header_callid);
				//if(sip_header_callid!=null)
				//sb.append("Call-ID~"+sip_header_callid + "\n");
				
				String sip_header_cseq = request.getParameter("session.connection.protocol.sip.headers['cseq']");
				lo.trace("session.connection.protocol.sip.headers['cseq']=" + sip_header_cseq);
				//if(sip_header_cseq!=null)
				
				String sip_header_maxforwards = request.getParameter("session.connection.protocol.sip.headers['max-forwards']");
				lo.trace("session.connection.protocol.sip.headers['max-forwards']=" + sip_header_maxforwards);
				//if(sip_header_maxforwards!=null)
				//sb.append("Max-Forwards~"+sip_header_maxforwards + "\n");
				
				//Contact: <sip:Genesys@10.76.222.22:5070>
				String sip_header_contact = request.getParameter("session.connection.protocol.sip.headers['contact']");
				lo.trace("session.connection.protocol.sip.headers['contact']=" + sip_header_contact);
				//sb.append("Contact~" + sip_header_contact + "\n");
				 * 
				 */
				/*
				//Content-Length: 10
				String sip_header_content_length = request.getParameter("session.connection.protocol.sip.headers['content-length']");
				//lo.trace("session.connection.protocol.sip.headers['content-length']=" + sip_header_content_length);
				sb.append("Content-Length: " + sip_header_content_length + "\n");
				
				//Content-Type: application/sdp
				String sip_header_content_type = request.getParameter("session.connection.protocol.sip.headers['content-type']");
				//lo.trace("session.connection.protocol.sip.headers['content-type']=" + sip_header_content_type);
				sb.append("Content-Type: " + sip_header_content_type + "\n");
				
				//User-Agent: Audiocodes-Sip-Gateway-Mediant 3000/v.6.20A.046.005
				String sip_header_user_agent = request.getParameter("session.connection.protocol.sip.headers['user-agent']");
				//lo.trace("session.connection.protocol.sip.headers['user-agent']=" + sip_header_user_agent);
				sb.append("User-Agent: " + sip_header_user_agent + "\n");
				
				//Content-Disposition: session
				String sip_header_content_disposition = request.getParameter("session.connection.protocol.sip.headers['content-disposition']");
				//lo.trace("session.connection.protocol.sip.headers['content-disposition']=" + sip_header_content_disposition);
				sb.append("Content-Disposition: " + sip_header_content_disposition + "\n");
				*/
	/*
				//X-ISCC-CofId: location=sip;cofid=2138
				String sip_header_x_iscc_cofid = request.getParameter("session.connection.protocol.sip.headers['x-iscc-cofid']");
				lo.trace("session.connection.protocol.sip.headers['x-iscc-cofid']=" + sip_header_x_iscc_cofid);
				*/
				/*
				//Min-SE: 90
				String sip_header_min_se = request.getParameter("session.connection.protocol.sip.headers['min-se']");
				//lo.trace("session.connection.protocol.sip.headers['min-se']=" + sip_header_min_se);
				sb.append("Min-SE: " + sip_header_min_se + "\n");
				*/
	/*			
				//X-Genesys-GVP-Session-ID: 23DEC11C-C82E-47CF-DE06-DC18A177922A;gvp.rm.tenant-id=1.103.101_Air Canada Test;gvp.rm.datanodes=1
				String sip_header_x_genesys_gvp_session_id = request.getParameter("session.connection.protocol.sip.headers['x-genesys-gvp-session-id']");
				lo.trace("session.connection.protocol.sip.headers['x-genesys-gvp-session-id']=" + sip_header_x_genesys_gvp_session_id);
				//sb.append("X-Genesys-GVP-Session-ID~" + sip_header_x_genesys_gvp_session_id + "\n");
				
				//X-Genesys-GVP-Session-Data: callsession=23DEC11C-C82E-7A26-F264-62C27D7561B8;1;0;;;;Environment/Voxify/AirCanada;Air Canada Test^M
				String sip_header_x_genesys_gvp_session_data = request.getParameter("session.connection.protocol.sip.headers['x-genesys-gvp-session-data']");
				lo.trace("session.connection.protocol.sip.headers['x-genesys-gvp-session-data']=" + sip_header_x_genesys_gvp_session_data);
				//sb.append("X-Genesys-GVP-Session-Data~" + sip_header_x_genesys_gvp_session_data + "\n");
	*/			
				/*
				//Supported: timer, uui
				String sip_header_supported = request.getParameter("session.connection.protocol.sip.headers['supported']");
				//lo.trace("session.connection.protocol.sip.headers['supported']=" + sip_header_supported);
				sb.append("Supported: " + sip_header_supported + "\n");
				
				//Session-Expires: 1800
				String sip_header_session_expires = request.getParameter("session.connection.protocol.sip.headers['session-expires']");
				//lo.trace("session.connection.protocol.sip.headers['session-expires']=" + sip_header_session_expires);
				sb.append("Session-Expires: " + sip_header_session_expires + "\n");
				
				//X-Genesys-RM-Application-dbid: 174
				String sip_header_x_genesys_rm_application_dbid = request.getParameter("session.connection.protocol.sip.headers['x-genesys-rm-application-dbid']");
				//lo.trace("session.connection.protocol.sip.headers['x-genesys-rm-application-dbid']=" + sip_header_x_genesys_rm_application_dbid);
				sb.append("X-Genesys-RM-Application-dbid: " + sip_header_x_genesys_rm_application_dbid + "\n");
						*/
				//"session.connection.protocol.sip.headers['x-channel']"
/*				String sip_header_x_channel = request.getParameter("session.connection.protocol.sip.headers['x-channel']");
				lo.trace("session.connection.protocol.sip.headers['x-channel']=" + sip_header_x_channel);
	*/
				//if(sip_header_x_channel!=null)
				//sb.append("X-channel~" + sip_header_x_channel + "\n");
					
				//session.com.genesyslab.sessionid
				//String session_com_genesyslab_sessionid = request.getParameter("session.com.genesyslab.sessionid");
				//lo.trace("session.com.genesyslab.sessionid: " + session_com_genesyslab_sessionid);
				//sb.append("session.com.genesyslab.sessionid~" + session_com_genesyslab_sessionid + "\n");
	
				//session.connection.redirect|REDIRECTHEADER|7|
/*				String session_connection_redirect = request.getParameter("session.connection.redirect");
				lo.trace("session.connection.redirect=" + session_connection_redirect);
				
				String session_connection_callidref = request.getParameter("session.connection.callidref");
				lo.trace("session.connection.callidref=" + session_connection_callidref);
				//session.vendor_name.instance.parent|PARENT|1|
				//session.connection.ocn|OCN|1|
				//session.connection.rdnis|RDNIS|1|
				//session.connection.rreason|RREASON|1|
				//session.connection.uuid|XGENESYSCALLUUID|1|
				String session_connection_uuid = request.getParameter("session.connection.uuid");
				lo.trace("session.connection.uuid=" + session_connection_uuid);
				//sb.append("X-Genesys-CallUUID~").append(session_connection_uuid).append("\n");
				*/
				//lo.trace("\n" + sb.toString());
				//session.connection.dn|IVRPORT|1
				/*
				//"session.com.genesyslab.userdata.siplocaltag"
				String session_com_genesyslab_userdata_siplocaltag = request.getParameter("session.com.genesyslab.userdata.siplocaltag");
				lo.trace("session.com.genesyslab.userdata.siplocaltag=" + session_com_genesyslab_userdata_siplocaltag);
				// "session.com.genesyslab.userdata['x-genesys-gvp-siplocaltag']" 
				String genesyslab_userdata_x_genesys_gvp_siplocaltag = request.getParameter("session.com.genesyslab.userdata['x-genesys-gvp-siplocaltag']");
				lo.trace("session.com.genesyslab.userdata['x-genesys-gvp-siplocaltag']=" + genesyslab_userdata_x_genesys_gvp_siplocaltag);
				// "session.com.genesyslab.userdata['x-genesys-siplocaltag']" 
				String genesyslab_userdata_x_genesys_siplocaltag = request.getParameter("session.com.genesyslab.userdata['x-genesys-siplocaltag']");
				lo.trace("session.com.genesyslab.userdata['x-genesys-siplocaltag']=" + genesyslab_userdata_x_genesys_siplocaltag);
				 
				// "session.com.genesyslab['x-genesys-gvp-siplocaltag']" 
				String genesyslab_x_genesys_gvp_siplocaltag = request.getParameter("session.com.genesyslab['x-genesys-gvp-siplocaltag']");
				lo.trace("session.com.genesyslab['x-genesys-gvp-siplocaltag']=" + genesyslab_x_genesys_gvp_siplocaltag);
				// "session.com.genesyslab['x-genesys-siplocaltag']" 
				String genesyslab_x_genesys_siplocaltag = request.getParameter("session.com.genesyslab['x-genesys-siplocaltag']");
				lo.trace("session.com.genesyslab['x-genesys-siplocaltag']=" + genesyslab_x_genesys_siplocaltag);
				 */
				/*

				String LOGCALLDIR = request.getParameter("LOGCALLDIR");
				lo.trace("LOGCALLDIR=" + LOGCALLDIR);*/
				//String SIPLOCALTAG = request.getParameter("SIPLOCALTAG");
				//lo.trace("SIPLOCALTAG=" + SIPLOCALTAG);
				/*String  ALTER_URL = request.getParameter("ALTER_URL");
				lo.trace("ALTER_URL=" + ALTER_URL);
				String REDIRECT = request.getParameter("REDIRECT");
				lo.trace("REDIRECT=" + REDIRECT);
				String CALLIDREF = request.getParameter("CALLIDREF");
				lo.trace("CALLIDREF=" + CALLIDREF);
				String PROTOCOLNAME = request.getParameter("PROTOCOLNAME");
				lo.trace("PROTOCOLNAME=" + PROTOCOLNAME);
				String PROTOCOLVERSION = request.getParameter("PROTOCOLVERSION");
				lo.trace("PROTOCOLVERSION=" + PROTOCOLVERSION);
				String INITINFO = request.getParameter("INITINFO");
				lo.trace("INITINFO=" + INITINFO);
				String XGENESYSCALLUUID = request.getParameter("XGENESYSCALLUUID");
				lo.trace("XGENESYSCALLUUID=" +XGENESYSCALLUUID);
				String XGENESYSGVPSESSIONID = request.getParameter("XGENESYSGVPSESSIONID");
				lo.trace("XGENESYSGVPSESSIONID=" + XGENESYSGVPSESSIONID);
				String XGENESYSTENANTID = request.getParameter("XGENESYSTENANTID");
				lo.trace("XGENESYSTENANTID=" + XGENESYSTENANTID);
				 */
				
				//String userdata = request.getParameter(CDRConstants.KEY_SESSION_COM_GENESYSLAB_USERDATA);
				//logger.info("userdata: " + userdata);
	/*			 String session_com_genesyslab = request.getParameter("session.com.genesyslab");
				 lo.trace("session.com.genesyslab=" + session_com_genesyslab);
				 String session_com_genesyslab_sessionid = request.getParameter("session.com.genesyslab.sessionid");
				 lo.trace("session.com.genesyslab.sessionid=" + session_com_genesyslab_sessionid);
				 String session_com_genesyslab_userdata = request.getParameter("session.com.genesyslab.userdata");
				 lo.trace("session.com.genesyslab.userdata=" + session_com_genesyslab_userdata);
				 
				 //"session.com.genesyslab.userdata.calluuid"
				 String session_com_genesyslab_userdata_calluuid = request.getParameter("session.com.genesyslab.userdata.calluuid");
				 lo.trace("session.com.genesyslab.userdata.calluuid: " + session_com_genesyslab_userdata_calluuid);
				 
				 //String userdata_x_channel = request.getParameter("session.com.genesyslab.userdata['x-channel']");
				//logger.trace("userdata_x_channel: " + userdata_x_channel);
				//String userdata_from = request.getParameter("session.com.genesyslab.userdata['from']");
				//logger.trace("userdata_from: " + userdata_from);
				 
				//X-Genesys-GVP-Session-ID: 23DEC11C-C82E-47CF-DE06-DC18A177922A;gvp.rm.tenant-id=1.103.101_Air Canada Test;gvp.rm.datanodes=1
				 //String userdata_x_genesys_gvp_session_id = request.getParameter("session.com.genesyslab.userdata['x-genesys-gvp-session-id']");
				 //lo.trace("session.com.genesyslab.userdata['x-genesys-gvp-session-id']=" + userdata_x_genesys_gvp_session_id);
				// sb.append("X-Genesys-GVP-Session-ID~" + userdata_x_genesys_gvp_session_id + "\n");
					
				//X-Genesys-GVP-Session-Data: callsession=23DEC11C-C82E-7A26-F264-62C27D7561B8;1;0;;;;Environment/Voxify/AirCanada;Air Canada Test^M
				//String userdata_x_genesys_gvp_session_data = request.getParameter("session.com.genesyslab.userdata['x-genesys-gvp-session-data']");
				//lo.trace("session.com.genesyslab.userdata['x-genesys-gvp-session-data']=" + userdata_x_genesys_gvp_session_data);
				//sb.append("X-Genesys-GVP-Session-Data~" + sip_header_x_genesys_gvp_session_data + "\n");
				//logger.trace("userdata_gvp_session_id: " + userdata_gvp_session_id);

				 //"session.com.genesyslab.userdata[�gvp-session-id�]"
				String param = "session.com.genesyslab.userdata['"+session_com_genesyslab_sessionid+"']";
				//lo.trace("parameter session.com.genesyslab.userdata['gvp-session-id']=" + "session.com.genesyslab.userdata['"+session_com_genesyslab_sessionid+"']");
				lo.trace("parameter session.com.genesyslab.userdata['gvp-session-id'] : " + param);
				 
				// String customUserDataBySessionId = request.getParameter(param);
				// lo.trace("customUserDataBySessionId: " + customUserDataBySessionId);
				 
				// String customUserdata = request.getParameter("session.com.genesyslab.userdata[�"+session_com_genesyslab_sessionid+"�]");
				 //lo.trace("customUserdata:" + customUserdata);
			*/	 
			/*	 if(!ci.isTraceWritten()){
					 File file  =null;
					 FileWriter writer = null;
					 
					 try {
						file  =new File("/mycompanyapps/dev/aircanada/data/" + dnis+"_"+clid);
						writer = new FileWriter(file);
						writer.write(sb.toString());
						//ci.setTraceWritten(true);
					 }
					 catch(Exception e){
						 lo.error("Error writing sip trace: " + e.getMessage()); 
					 }
					 finally {
						 try {
							 writer.flush();
							 writer.close();
							 
						 }
						 catch(Exception e){
							 
						 }
						 writer = null;
						 file = null;
					 }
				 }
				 */
			//}				
/*		}
		catch(Exception e){
			lo.error("error getting request attributes: " + e.getMessage());
			StackTraceElement[] trace = e.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					lo.error(trace[y]);
				}
			}
		}
		
		try {
			MDC.remove(CDRConstants.CALL_ID_KEY);
			}
			catch(Exception e2) {
				lo.error("Error removing call ID form MDC : " + e2.getMessage());
			}
	}*/
 }//end of class