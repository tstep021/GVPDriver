package com.mycompany.gvpdriver.event;

/** @copyright   2010-2013 mycompany. */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;
import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;

/** 
 * @file         NIS.java
 * 
 * @description  This servlet plays error message and hangs up
 * 
 * @author       Tatiana Stepourska
 * updates		 Tripthi Theethimada
 * @version      1.0
 */

public class NIS extends BaseCallFlow
{	
	private static final long serialVersionUID = 2765155167172832379L;
	private static final Logger logger = Logger.getLogger(NIS.class);
	
	/**
	 * Executes the application logic and calls the method 
	 * that generates VXML response document.  
	 * 
	 * @param callID	   --IVR call ID
	 * @param si		   --SessionInfo object
	 * @param vxml         --VXML object
	 * @param info         --The CDR object
	 * @param session      --HTTP session object
	 * @param request      --HTTP request
	 * @param response     --HTTP response
	 * 
	 * @return String      The VXML response
	 */
	public String doResponse(String 			callID,
							ICallInfo 			ci,
							VXML                vxml,
							HttpSession         session,
							HttpServletRequest  request,
							HttpServletResponse response
							) throws ServletException,IOException, Exception	
	{
		addMDC(callID,ci);
		String result = null;
		try {		
			result = buildDocument(vxml,
				ci,
				null, 
				request);
		}
		catch(Exception e) {
			logger.error("Error displaying NIS document: " + e.getMessage());
			logger.info("trying to create error exit document: ");
			result = this.silentExit(vxml, callID); 
		}
		finally{
			if(logger.isTraceEnabled()) logger.trace(result);
			removeMDC(ci);
		}
		return result;
	}
 	 
	public VXML addDocumentBody(VXML vxml, ICallInfo ci, String submitNext, HttpServletRequest request) {
		
		String promptpath  = BaseGlobalConfig.resource_repository_url + "prompts/system/";		
		int len =BaseGlobalConfig.languages.length; //ci.getLangArray().length; //
		
		for(int i=0;i<len;i++) {
			vxml.FormStart("mainNIS"+BaseGlobalConfig.languages[i], "dialog", "true"); //id, scope, cleardtmf
		
			vxml.BlockStart("playNISMsg" + BaseGlobalConfig.languages[i]);
				
			vxml.Assign(CDRConstants.KEY_TERM, ""+CDRConstants.TERM_SYSTEM_HANGUP_NORMAL);

			vxml.AudioStart(promptpath + BaseGlobalConfig.languages[i] + "/nis.wav");
				vxml.Text(BaseUtils.getStringFromFile(BaseGlobalConfig.resource_repository_basepath + promptpath + BaseGlobalConfig.languages[i] + "/nis.txt"));
			vxml.AudioEnd();							

			if(i>=len-1)
				vxml.Disconnect();
			else
				vxml.Goto("#mainNIS"+BaseGlobalConfig.languages[i+1]);
			vxml.BlockEnd();
			
			//in any case goto call end, overrides the vxml header handling
			vxml.ErrorStart();
			if(BaseGlobalConfig.debug) {
				vxml.Text("form Error in n i s, disconnecting");
				}
			vxml.Disconnect();
			vxml.ErrorEnd();
		
			vxml.CatchStart(VXMLEvents.EVENT_ERROR);
			if(BaseGlobalConfig.debug) {
				vxml.Text("caught form event Error in n i s, disconnecting");
				}
			vxml.Disconnect();
			vxml.CatchEnd();
			vxml.FormEnd();
		}
					
		return vxml;
	}		 
	
		public final void addMDC(String callID, ICallInfo ci){
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
	
	public final void removeMDC(ICallInfo ci){
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
}  // end of class