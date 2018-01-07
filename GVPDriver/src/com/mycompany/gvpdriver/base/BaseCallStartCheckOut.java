package com.mycompany.gvpdriver.base;

/** @copyright   2010-2013 mycompany */

//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
//import java.util.Enumeration;
//import java.util.Properties;

//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.entity.*;
import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;

/** 
 * File         CheckOut.java
 * 
 * Description  The purpose of this class 
 * 				1) to get GVP vars and set it to the CDRInfo after the 
 * 				beginning of the call
 * 				2) Find next place to go
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public abstract class BaseCallStartCheckOut extends BaseServletExtension implements ICheckOut
{	
	private static final long serialVersionUID = -3452017500570279522L;
	private static final Logger logger = Logger.getLogger(BaseCallStartCheckOut.class);
	
	/**
	* Overrides the abstract method specified in the base servlet to produce VXML response
	* 
	* @param info     --CDRInfo object
	* @param config   --Properties object
	* @param session  --HttpSession object
	* @param request  --HttpServletRequest object
	* @param response --HttpServletResponse object
	* 
	* @return Full VoiceXML document
	 */
	public final String doResponse(String 			callID,
							ICallInfo			ci,
							VXML                vxml,
	                        HttpSession         session,
							HttpServletRequest  request,
							HttpServletResponse response
							) throws ServletException,IOException, Exception
	{		
		addMDC(callID,ci);
		String result = "";
		String target = null;
		logger.info("started");
		//RequestDispatcher dispatcher = null;
		initGVPParams(ci, request);
		logger.trace("finished initGVPparams");
	 	
		try		{
			String errURL = request.getParameter(BaseConstants.PARAM_ERR_URL);
			logger.info("errURL: " + errURL);
			
			if(errURL!=null)
				target = errURL;
			else
				target = findNextUrl(ci, request, response);
			//logger.info("nextUrl: " + target);
			//if(!BaseUtils.isValidUrl(target))
			//	throw new Exception("Can't fetch URL: "+target);
			
			/*dispatcher = getServletContext().getRequestDispatcher(target);
			logger.info("got dispatcher");
    		dispatcher.forward(request, response);
    		*/
			//response.sendRedirect(target);
		}
		catch(Exception e)	{	
			target = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF;
		}
		finally{
			try		{
				//target = findNextUrl(ci, request, response);
				logger.info("nextUrl: " + target);
				result = buildDocument(vxml,
						ci,
						target,
						request);
			}
			catch(Exception e)	{
				logger.error("ERROR building vxml: " + e.getMessage());	
				if(logger.isTraceEnabled()){
					
				}
				logger.info("trying to create error exit document: ");
				result = this.silentExit(vxml, callID); 
				/*try {
					response.sendRedirect(BaseGlobalConfig.submitbase + BaseCallStates.CL)
				}*/
			}
				
			if(logger.isTraceEnabled())  logger.trace(result);
			removeMDC(ci);
			
		}
	
		return result;
	 }		
	

/*	public void addMDC(String callID, ICallInfo ci){
		try {
			MDC.put(CDRConstants.CALL_ID_KEY, callID);
		}
		catch(Exception e){}
		
		try {
			String[] keys = ci.getMDCKeys();
			String[] values = ci.getMDCValues();
			logger.info("MDC values: "+values);
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
	}*/
	
	private final void initGVPParams(ICallInfo ci, HttpServletRequest request) throws Exception {

		// 1. get GVP platform session id
		String gvpSessionID = request.getParameter(CDRConstants.KEY_SESSION_COM_GENESYSLAB_SESSIONID);//"session.com.genesyslab.sessionid");
		logger.info("GVP sessionID: " + gvpSessionID);
		if(gvpSessionID!=null){
			ci.setCallID(gvpSessionID);
			addMDC(gvpSessionID, ci);
		}

		try {	
			//extract telco port
			String sip_header_x_channel = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_X_CHANNEL);
			logger.info(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_X_CHANNEL + ": " + sip_header_x_channel);
			BaseUtils.extractChannel(sip_header_x_channel, ci);			
		}
		catch(Exception e){
			logger.error("initGVPParams: error getting request attributes: " + e.getMessage());
			/*StackTraceElement[] trace = e.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					logger.error(trace[y]);
				}
			}*/
		}
			
		//extract GVP tenant
		try {
			/*
			String sip_header_x_genesys_gvp_session_id = request.getParameter("session.connection.protocol.sip.headers['x-genesys-gvp-session-id']");
			logger.info("session.connection.protocol.sip.headers['x-genesys-gvp-session-id']=" + sip_header_x_genesys_gvp_session_id);
			BaseUtils.extractGVPTenant(sip_header_x_genesys_gvp_session_id, ci);
			*/
			String sip_header_x_genesys_gvp_session_data = request.getParameter("session.connection.protocol.sip.headers['x-genesys-gvp-session-data']");
			logger.info("session.connection.protocol.sip.headers['x-genesys-gvp-session-data']=" + sip_header_x_genesys_gvp_session_data);
			BaseUtils.extractGVPTenant(sip_header_x_genesys_gvp_session_data, ci);
		}
		catch(Exception e){
			logger.error("initGVPParams: error extracting tenant: " + e.getMessage());
		}	
	}

	public final VXML addDocumentFooter(VXML vxml, ICallInfo ci)throws Exception {

		vxml.ErrorStart();
		vxml.Disconnect();
		vxml.ErrorEnd();
	
		vxml.CatchStart(VXMLEvents.EVENT_ERROR);
		vxml.Disconnect();
		vxml.CatchEnd();
		
		vxml.VxmlEnd();
		
		return vxml;
	}
	
	public final VXML addDocumentBody(VXML vxml,ICallInfo ci, String submitNext, HttpServletRequest request) throws Exception { 
		
		vxml.FormStart("mainCheckout");			
			vxml.BlockStart("checkItOut");

				vxml.Submit(submitNext, 	//next,
							null, 					//expr
							BaseServletExtension.GVP_NAMELIST,	//namelist 
							BaseConstants.METHOD_POST,
							null,					//caching 
							BaseGlobalConfig.fetchaudio, 			//fetchAudio
							null, 					//fetchint
							null, 					//fetchTO 
							null);					//encType
			
			//vxml.Disconnect();
			vxml.BlockEnd();	

		vxml.FormEnd();
		return vxml;
	}
	
	public void extractSipInfo(ICallInfo ci, HttpServletRequest request){
		try {
		//sip info for possible call parking 
		HashMap<String,String> sipInfo = new HashMap<String,String>();
		//StringBuffer sb = new StringBuffer();
		//String dnis = ""+ci.getBackUpDnis();
		long clid = ci.getCLID();
		//gvp  call id - session identifyer, used by Call parking facility
		sipInfo.put(CDRConstants.CALL_ID_KEY, ""+ci.getCallID());
		sipInfo.put(BaseConstants.SIP_INFO_KEY_DNIS, ""+ci.getDNIS());//dnis);
		sipInfo.put(BaseConstants.SIP_INFO_KEY_CLID, ""+clid);
		sipInfo.put(BaseConstants.SIP_INFO_KEY_TRUNKGROUP,""+ci.getTrunkGroup());
		//sipInfo.put(BaseConstants.KEY_AC_SHARED_CALL_ID,acSharedCallID);
		
		String sip_header_from = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_FROM);
		//logger.trace("session.connection.protocol.sip.headers['from']=" + sip_header_from);
		//sb.append(BaseConstants.SIP_HDR_FROM).append(BaseConstants.SIP_TRACE_DELIMITER).append(sip_header_from).append("\n");	
		sipInfo.put(BaseConstants.SIP_HDR_FROM, sip_header_from);

		//"session.connection.protocol.sip.headers[�<sip header name>�]"
		String sip_header_to = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_TO);
		//logger.trace("session.connection.protocol.sip.headers['to']=" + sip_header_to);
		// SIPLOCALTAG					
		String session_connection_siplocaltag = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_SIPLOCALTAG);
		//logger.trace(VXMLVariables.SESSION_CONNECTION_SIPLOCALTAG + ": " + session_connection_siplocaltag);
		//sb.append("Siplocaltag").append(BaseConstants.SIP_TRACE_DELIMITER).append(session_connection_siplocaltag).append("\n");
		//sb.append(BaseConstants.SIP_HDR_TO).append(BaseConstants.SIP_TRACE_DELIMITER).append(sip_header_to).append("\n"); //sip:"+dnis+trunkGroup+"@10.76.222.21:5060 SIP/2.0\n")				
		//ci.setSiplocaltag(session_connection_siplocaltag);
		sipInfo.put(BaseConstants.SIP_HDR_TO, sip_header_to+";tag="+session_connection_siplocaltag);
		sipInfo.put(VXMLVariables.VAR_SESSION_CONNECTION_SIPLOCALTAG, session_connection_siplocaltag);
		
		String sip_header_via = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_VIA);
		//logger.trace("session.connection.protocol.sip.headers['via']=" + sip_header_via);
		//String[] arr = sip_header_via.split(",");
		//for(int i=0;i<arr.length;i++)
		//	sb.append(BaseConstants.SIP_HDR_VIA).append(BaseConstants.SIP_TRACE_DELIMITER).append(arr[i]).append("\n");
		sipInfo.put(BaseConstants.SIP_HDR_VIA, sip_header_via);
		
		String sip_header_callid = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_CALL_ID);
		//lo.trace("session.connection.protocol.sip.headers['call-id']=" + sip_header_callid);
		//sb.append(BaseConstants.SIP_HDR_CALLID).append(AppBaseConstants.SIP_TRACE_DELIMITER).append(sip_header_callid).append("\n");
		sipInfo.put(BaseConstants.SIP_HDR_CALLID, sip_header_callid);

		String sip_header_maxforwards = request.getParameter(VXMLVariables.VAR_SESSION_CONNECTION_PROTOCOL_SIP_HEADERS_MAX_FORWARDS);
		//logger.trace("session.connection.protocol.sip.headers['max-forwards']=" + sip_header_maxforwards);
		//sb.append(BaseConstants.SIP_HDR_MAX_FORWARDS).append(AppBaseConstants.SIP_TRACE_DELIMITER).append(sip_header_maxforwards).append("\n");
		sipInfo.put(BaseConstants.SIP_HDR_MAX_FORWARDS, sip_header_maxforwards);
		logger.info(sipInfo);

		ci.setSipInfo(sipInfo);
		
		}
		catch(Exception e){
			logger.error("extractSipInfo: error: " + e.getMessage());
		}
	}
 
	public abstract String findNextUrl(ICallInfo ci, HttpServletRequest request, HttpServletResponse response) throws Exception;
}	// end of class