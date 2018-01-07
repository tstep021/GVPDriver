package com.mycompany.gvpdriver.base; 

/** copyright 2009-2013, mycompany. */

import java.io.IOException;
import java.io.PrintWriter;

//import java.lang.reflect.Proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.entity.*;
import com.mycompany.ecs.cdr.*;

import com.mycompany.vxml.facade.*;

/** 
 * File         BaseCallEnd.java
 * 
 * Description  This class cleans up the session, 
 *                overrides abstract method doResponse 
 *               which has to call onSessionEnd
 * 					
 * @author       Tatiana Stepourska
 * 
 * @version      1.3
 */

 public abstract class BaseCallEnd  extends HttpServlet
 { 	    	
	 public static final Logger logger = Logger.getLogger(BaseCallEnd.class);
	 private static final long serialVersionUID = 1L;
	 
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
				logger.error("Error in call end doGet: " + e.getMessage());
			}
			finally {
				// release resources 
				if(output!=null) {
				
				output.flush();
				output.close();
				}
			}
		}

		/**
		 * Generates our servlet's response.  Should be overridden by each servlet.
		 * 
		 * @param info         --The ApplicationCDR object
		 * @param config       --Properties object
		 * @param vxml         --VXML object
		 * @param session      --HTTP session
		 * @param request      --HTTP request
		 * @param response      --HTTP response
		 *
		 * @return String      The VXML response
		 */
		public final String doResponse(String 			callID,								
								ICallInfo         	ci,
								VXML 				vxml,
								HttpSession         session,
								HttpServletRequest  request,
								HttpServletResponse response
								) throws ServletException,IOException, Exception
		{
			addMDC(callID, ci);
			String result = null;
	  		int tCode = 1;
	  		String termCode = null; 		

			try {
		  		///// 1. getting term code /////////////////////
				try {
					termCode = request.getParameter(CDRConstants.KEY_TERM);
					if(logger.isDebugEnabled())
						logger.debug("term code from request:" + termCode);
					
					try	{
						tCode = Integer.parseInt(termCode);
					}
					catch(Exception e)	{
						tCode = CDRConstants.TERM_UNKNOWN;
					}
				
					logger.info("term code:" + termCode);
					ci.setTermCode(tCode);
					logger.trace("term code set");
				}
				catch(Exception e){
					logger.error("ERror getting term code: " + e.getMessage());
				}
				/////////end of getting term code//////////////////
				//get document history
				String docHistory = request.getParameter(BaseConstants.VAR_DOC_HISTORY);
				//logger.debug("docHistory: " + docHistory);
				if(docHistory!=null){
					try {
						//ci.appendCallHistory(docHistory);
						ci.getCurrentNode().appendHistory(docHistory);
					}
					catch(Exception e) {}
				}
				try {
					ci.getCurrentNode().endHistory(ci);
				}
				catch(Exception e) {}
				
				/////////////////////////////////////////
				// 2. doing any additional application specific stuff
				//////////////////////////////////////
				try {
				customCallEnd(ci, request, response);
				}
				catch(Exception e){
					logger.error("ERror in customCallEnd: " + e.getMessage());
				}
				/////////////////////////////////////////
				//doing additional custom stuff
				//////////////////////////////////////
				
				
				/// 3. calling on call end routines - inserting CDR ////////
				try {
					//logger.trace("calling on call end");
					//this.onCallEnd(ci, callID);
					CDRUtils.onCallEnd(ci,callID,BaseGlobalConfig.cdr_config);
				}
				catch(Exception e) {
					logger.error("Error executing onCallEnd: " + e.getMessage());
				}
				/////// end of calling on call end routines ////////
				
				///// 3. creating exit vxml document /////////////////
				result = this.sessionExit(vxml, callID);
			}
			catch(Exception e) {
				logger.error("Error building vxml document: " + e.getMessage());
			}
			////// end of creating exit vxml document /////////
			finally{
				///// 4. disposing of the http session  /////////////////
				try {
					session.invalidate();
				}
				catch(Exception e) {
					logger.error("Error invalidating session: " + e.getMessage());
				}
				///// end of disposing of the http session  /////////////////
				
				removeMDC(ci);
			}
			return result;    
		}
	
	/** 
	 * Creates VXML response for the final document.
	 * @param vxml
	 * @param callID
	 * 
	 * @return results   The VXML response
	 */
	 public String sessionExit(VXML vxml, String callID)
	 {
		vxml.Header();
	 	
		vxml.VxmlStart(null, BaseConstants.VXML_VERSION, BaseConstants.DEFAULT_LANGUAGE_MODE);
		
			vxml.FormStart("main");
				vxml.BlockStart();
					vxml.Exit();
				vxml.BlockEnd();
			vxml.FormEnd();
	 		
			vxml.CatchStart(VXMLEvents.EVENT_ERROR);
				vxml.Exit();
			vxml.CatchEnd();
			
			vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_HANGUP);	
			vxml.Exit();		
			vxml.CatchEnd();
		
			vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_HANGUP);
			vxml.Exit();
			vxml.CatchEnd();
			
			vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_TRANSFER);
			vxml.Exit();
			vxml.CatchEnd();	
			
			vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_TRANSFER);
			vxml.Exit();
			vxml.CatchEnd();	
			
			vxml.CatchStart(VXMLEvents.EVENT_COM_VOICEGENIE_CALL_DISCONNECT);
			vxml.Exit();
			vxml.CatchEnd();
			
			vxml.CatchStart(VXMLEvents.EVENT_TRANSFER_DISCONNECT_HANGUP);
			vxml.Exit();
			vxml.CatchEnd();
			
			//for 6.4.5+
			vxml.ErrorStart();
				vxml.Exit();
			vxml.ErrorEnd();
	 	
		vxml.VxmlEnd();
	 	
		if(logger.isTraceEnabled())
			logger.trace(vxml.getBuffer().toString()); 	
		return vxml.getBuffer().toString();
	 } 
		
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
		
	public abstract void customCallEnd(ICallInfo ci, HttpServletRequest request, HttpServletResponse response) throws Exception;
		
}//end of class