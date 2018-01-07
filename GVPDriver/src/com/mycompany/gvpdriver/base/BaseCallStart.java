package com.mycompany.gvpdriver.base;

/** @copyright 2010-2013 mycompany. All rights reserved. */

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.mycompany.ecs.cdr.client.CDRConfig;
import com.mycompany.gvpdriver.base.BaseGlobalConfig;
import com.mycompany.gvpdriver.entity.*;
import com.mycompany.gvpdriver.event.*;
import com.mycompany.gvpdriver.util.*;
import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;
/** 
 * File         BaseCallStart.java
 * 
 * Description  This class initialises some properties 
 *               and session objects
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public abstract class BaseCallStart extends BaseServletExtension
{
	private static final long serialVersionUID = -3093489730927513027L;
	private static final Logger log = Logger.getLogger(BaseCallStart.class);
	
	public static final String COOKIE_JSESSIONID_KEY	 = "JSESSIONID";
	//default value means cookie persists until browser shutdown
	public static final int COOKIE_DEFAULT_MAX_AGE	 	 = -1;
	public String ctxName 			= null;	
	public ServletContext ctx 		= null;
	public Properties props		= null;
	
	/** Initialize default application configuration */
	public void init(ServletConfig config) throws ServletException	{
		super.init(config);
		
		String fp 				= "init: ";		
		String propsFile  		= null;		
		
		boolean err				= false;
		
		try {
			ctx = config.getServletContext();
			log.trace(fp + "ctx: " + ctx);
			
			ctxName = ctx.getInitParameter(BaseConstants.CTX_PARAM_NAME_KEY);
			log.info(fp + "ctxName: " + ctxName);

			// Look for properties file
			propsFile = ctx.getInitParameter(BaseConstants.KEY_APP_PROPERTIES);
			log.info(fp + "propsFile: " + propsFile);
			
			//load properties
			props = BaseUtils.getConfigFromFile(propsFile);
			if(props==null || props.size()<=0)
				throw new ServletException(fp + "Properties are empty!");
		}
		catch(Exception ex)	{
			log.error(fp + "ERROR loading properties file: ");
			log.error(ex.getMessage());
			StackTraceElement[] trace = ex.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					log.error(trace[y]);
				}
			}
			err = true;
		}
		
		if(err)
			throw new ServletException(fp + "could not load properties file");				

		//init global configuration
		try {		
			BaseGlobalConfig.load(props, ctxName);
		}
		catch(Exception ex) {
			log.error(fp + "ERROR initializing global configuration:");
			log.error(ex.getMessage());
			StackTraceElement[] trace = ex.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					log.error(trace[y]);
				}
			}
		}
		
		/*if(log.isTraceEnabled()){
			BaseServletExtension.GVP_NAMELIST = CDRConstants.KEY_SESSION_COM_GENESYSLAB_SESSIONID +" "+ 
			 CDRConstants.KEY_SESSION_COM_GENESYSLAB_USERDATA + " " + 
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_X_CHANNEL + " " + 
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_FROM + " " + 
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_TO + " " +
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_VIA + " " + 
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_CALL_ID + " " +
			   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_MAX_FORWARDS + " " + 
			   	VXMLVariables.VAR_SESSION_CONNECTION_SIPLOCALTAG + " " + 
				 "session.connection.protocol.sip.headers['cseq']" + " " +
				 "session.connection.protocol.sip.headers['x-genesys-gvp-session-id']" + " " + 
				 "session.connection.protocol.sip.headers['x-genesys-gvp-session-data']" + " " +
				 "session.connection.protocol.sip.headers['x-iscc-cofid']" + " " + 
				 "session.connection.protocol.sip.headers['contact']" + " " + 
				 "session.connection.callidref" + " " + 
				 "session.connection.uuid" + " " + 
				 "session.com.genesyslab" + " " +
				 "session.com.genesyslab.userdata" + " " +
				 "session.com.genesyslab.sessionid" + " " + 
				 "session.com.genesyslab.userdata.calluuid";
		}
		else{*/
			BaseServletExtension.GVP_NAMELIST = "";
		//}
		log.info("init: initialized GVP_NAMELIST: " + BaseServletExtension.GVP_NAMELIST);
	}	

	/**
	 * Implements the superclass abstract method
	 * 
	 * @param info
	 *            --SessionInfo object
	 * @param config
	 *            --Properties object
	 * @param session
	 *            --HttpSession object
	 * @param request
	 *            --HttpServletRequest object
	 * @param response
	 *            --HttpServletResponse object
	 * 
	 * @return String -- VoiceXML document
	 */
	public String doResponse(String callID, ICallInfo ci, VXML vxml,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = null;
		log.info("*** New Call Started ***");
		String submitNext = null;

		try {
			// 1) collect and initialize CDR info
			ci = initCDR(request, response, createCallInfo());
			callID = ci.getCallID();
			log.info("got GVP call ID: " + callID);
			initMDC(callID,ci);

			// 2)initialize session and call info
			this.initSessionInfo(ci, session, request,	response);
			log.trace("initSessionInfo completed with no errors");
			session.setAttribute(BaseConstants.CALL_INFO_KEY, ci);
			submitNext = BaseGlobalConfig.apppath + BaseCallStates.callstart_checkout;

		}
		catch(ProvisioningException e)	{
			log.error("ProvisioningException: " + e.getMessage());	
			submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_NIS;
		}
		catch(ConfigNotFoundException e)	{
			log.error("ConfigNotFoundException: " + e.getMessage());
			submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_NIS;
		}
		catch(ConfigNotActiveException e)	{
			log.error("ConfigurationException: " + e.getMessage());
			submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_NOT_ACTIVE;
		}
		catch (RejectCallEvent rcex) {
			log.info("Caught RejectCallEvent:"+rcex.getMessage());
			// setting it to token value to generate 
			//vxml code for rejecting call
			submitNext = BaseConstants.REJECTCALL;
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage());
			BaseUtils.generateTrap(AlarmKeys.ALARM_GVP_DRIVER_INTERNAL_FAILURE_KEY,
					AlarmKeys.SEVERITY_CRITICAL,
					"Error at call start: " + e.getMessage());
			submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF;
		} finally {
			log.trace("doResponse: finally");
			try {
				if (ci == null)
					ci = new CallInfoBase();
				// 3)initialize default language
				String langC = ci.getLangConfig();
				// logger.debug(fp + "langC: " + langC);
				if (langC == null)
					langC = "en";

				ci.setLanguageFromConfig(langC);

				session.setAttribute(BaseConstants.CALL_INFO_KEY, ci);
				log.info("Initialization completed: callInfo: "
						+ ci.toHeaderString());
			} catch (Exception e) {
				log.error("Error building result: " + e.getMessage());
				result = this.silentExit(vxml, callID);
				if (log.isTraceEnabled()) {
					log.trace(result);
				}
			}

			try {
				result = buildDocument(vxml, ci, submitNext, request);
				if (log.isTraceEnabled()) {
					log.trace(result);
				}
			} catch (Exception e) {
				log.error("Error building vxml document: " + e.getMessage());
				result = this.silentExit(vxml, callID);
				if (log.isTraceEnabled()) {
					log.trace(result);
				}
			}

			removeMDC(ci);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Initializes the CallInfo object for a session and collects initial data 
	 * required for CDR
	 * 
	 * @param request
	 * 
	 * @throws Exception
	 */
	public ICallInfo initCDR(HttpServletRequest request, HttpServletResponse response,ICallInfo ci ) throws Exception{
		String fp = "initCDR: ";

		Properties params 		= null;
		String serverName 		= null;
		String remoteAddress 	= null;
		
		try{
		String paramValue		= null;
		String paramName 		= null;		
		params 		= new Properties();
		Enumeration<String> en 	= request.getParameterNames();
		while(en.hasMoreElements()) {
			paramName = en.nextElement();
			paramValue = request.getParameter(paramName);
			params.setProperty(paramName, paramValue);
		}
		//serverName 		= request.getServerName();
		serverName = request.getLocalAddr();
		remoteAddress 	= request.getRemoteAddr();
		if(log.isTraceEnabled()) {
		log.trace(fp + "serverName: " + serverName);
		log.trace(fp + "remoteAddress: " + remoteAddress);
		}	
		}
		catch(Exception e){
		log.error("error getting request attributes: " + e.getMessage());
		}

		ci.init(params, serverName, remoteAddress, CDRConfig.prefix_length, ctxName);
		ci.setTrunkGroup(ci.getComponent());
		ci.setBackUpDnis(ci.getDNIS());
		if(log.isTraceEnabled()) {
			log.trace(fp + "appserverIP after CDR init: " + ci.getAppserverIP());
			log.trace(fp + "gatewayIP after CDR init: " + ci.getGatewayIP());
		}

		if(BaseGlobalConfig.test){
			log.info(fp + "test is ON");
			
			long testDnis=BaseUtils.getTestDNIS();
			if(testDnis>0){
				log.info(fp + "replacing DNIS with " + testDnis);
				ci.setDNIS(testDnis);			
			}
		}

		return ci;
	}

	public final VXML addDocumentBody(VXML vxml,ICallInfo ci, String submitNext, HttpServletRequest request) throws Exception { 
		log.trace("addDocumentBody: "+submitNext);
		String errURL = null;
		
		if(submitNext==null){
			throw new Exception("next URL on call start is null!");
		}
		else if(submitNext.equalsIgnoreCase(BaseConstants.REJECTCALL)){
			//<meta  name=\"callrequest\" content=\"decline\" />
			vxml.Meta("callrequest", "decline");
		}
		else {
			if(submitNext.equalsIgnoreCase(BaseGlobalConfig.apppath + BaseCallStates.callstart_checkout)){
				//normal case, going to checkout, do nothing
			}
			else {
				//finish CDR init then redirect to specified URL
				errURL = new String(submitNext);
				submitNext = BaseGlobalConfig.apppath + BaseCallStates.callstart_checkout;
			}
		vxml.FormStart();

		  vxml.Property("fetchtimeout", "60000ms");

		   vxml.BlockStart();
		   if(errURL!=null){
			   vxml.Var(BaseConstants.PARAM_ERR_URL, errURL);
		   }
		   else{
			   vxml.Var(BaseConstants.PARAM_ERR_URL, "");
		   }

		   try {
			   	if(ci.getPlatform().compareTo(CDRConstants.PLATFORM_GVP)==0){
					//0.8 sec
					vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence0-8s.wav");
					vxml.Text("	");
					vxml.AudioEnd();
					
					vxml.Submit(submitNext, 	//next
							   	null, 	//expr
							   	CDRConstants.KEY_SESSION_COM_GENESYSLAB_SESSIONID + " " + 
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_X_CHANNEL + " " + 
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_FROM + " " + 
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_TO + " " +
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_VIA + " " + 
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_CALL_ID + " " +
							   	VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_MAX_FORWARDS + " " + 
							   	VXMLVariables.VAR_SESSION_CONNECTION_SIPLOCALTAG + " " + 
							   	BaseConstants.PARAM_ERR_URL + " " + 
							   	BaseServletExtension.GVP_NAMELIST,	//namelist
							   	BaseConstants.METHOD_POST //"post"
			    	);
			   	}
			   	else {
			   		vxml.Submit(submitNext, 	//next
						   	null, 	//expr					  
						   	BaseConstants.PARAM_ERR_URL,
						   	BaseConstants.METHOD_POST //"post"
		    	);
			   	}
		   }
		   catch(Exception e){
			   submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF;
			   vxml.Assign(CDRConstants.KEY_TERM, ""+CDRConstants.TERM_ERROR);
			   vxml.Submit(submitNext, 	//next
			    		 null, 	//expr
			    		 CDRConstants.KEY_TERM
			    		 );
		   }
		   vxml.BlockEnd();
		  BaseUtils.catchDisconnectEvents(vxml);
		vxml.FormEnd();
		}
		//////////////////////////////////
		
		/*
		//prompts for workaround for first message cut off
		else {
			*/
			//1 sec
			/*
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence1s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/
		/*
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence1s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/
			/*
			//1.5 sec
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence1-5s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/				
			/*
			//1.3 sec
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence1-3s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/
			/*
			//1.2 sec
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence1-2s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/
			
			//0.8 sec
			/*
			vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/silence0-8s.wav");
			vxml.Text("	");
			vxml.AudioEnd();
			*/
		
		return vxml;
	}
			
	public final VXML addDocumentFooter(VXML vxml, ICallInfo ci)throws Exception {

		vxml.VxmlEnd();	
		return vxml;
	}	

	/**
	 * Can be overwritten to add custom key value pairs to trace in logger MDC
	 * Note: call super(ci)  first!
	 * @param ci
	 */
	public void initMDC(String callID, ICallInfo ci){
		MDC.put(CDRConstants.CALL_ID_KEY, callID);
	}
	
/*
	public void addMDC(String callID, ICallInfo ci){
		try {
			MDC.put(CDRConstants.CALL_ID_KEY, callID);
		}
		catch(Exception e){}
		
		try {
			String[] keys = ci.getMDCKeys();
			String[] values = ci.getMDCValues();
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
*/
	////////////////////////////////////////////////////////////////
	// Abstract methods
	////////////////////////////////////////////////////////////////
	/**
	 * Executes the application logic and calls the method 
	 * that generates VXML response document.  
	 * Has to be implemented by a subclass.
	 * 
	 * @param callID	   --IVR call ID
	 * @param si       	   --SessionInfo object
	 * @param vxml         --VXML object
	 * @param cdr          --The CDR object
	 * @param session      --HTTP session object
	 * @param request      --HTTP request
	 * @param response     --HTTP response
	 * 
	 * @return String      --The VXML response
	 */
/*	public abstract String doResponse(String 			callID,									
										ICallInfo			ci,
										VXML                vxml,
										HttpSession         session,
										HttpServletRequest  request,
										HttpServletResponse response
										) throws ServletException,IOException, Exception;
*/
	public abstract ICallInfo createCallInfo();
	public abstract ICallInfo initSessionInfo(
			ICallInfo ci, 
			HttpSession         session,
			HttpServletRequest  request,
			HttpServletResponse response) throws 
			ProvisioningException, 
			ConfigNotFoundException, 
			ConfigNotActiveException, 
			RejectCallEvent, Exception;
	////////////////////////////////////////////////////////////////
	// End of Abstract methods
	////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// Private methods
	////////////////////////////////////////////////////////////////
	
}  //end of class