package com.mycompany.gvpdriver.event;

/** @copyright   2010-2013 mycompany. */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;
import com.mycompany.ecs.cdr.CDRConstants;

import com.mycompany.vxml.facade.*;

/** 
 * @file         TechDiff.java
 * 
 * @description  This servlet plays error message and hangs up
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public class TechDiff extends BaseCallFlow
{	
	private static final long serialVersionUID = 2765155167172832379L;
	private static final Logger logger = Logger.getLogger(TechDiff.class);
	
	/**
	* Implements abstract method specified in the base servlet to produce VXML response
	* 
	* @param info     --ApplicationCDR object
	* @param logger   --Logger object
	* @param sMan     --SessionManager object
	* @param session  --HttpSession object
	* @param request  --HttpServletRequest object
	* @param response --HttpServletResponse object
	* 
	* @return Full VoiceXML document
	 */
	public String doResponse(String 			callID,
			ICallInfo			ci, 
			VXML 				vxml,
			HttpSession 		session, 
			HttpServletRequest  request,
			HttpServletResponse response
	)	throws ServletException,IOException, Exception
	{
		addMDC(callID, ci);
		String result = null;
		try {
			
			String term = request.getParameter(CDRConstants.KEY_TERM);
			if(term!=null) {
				try {
					int iTerm = Integer.parseInt(term);
					ci.setTermCode(iTerm);
				}
				catch(Exception e) {}
			}
			
			//get document history
			String docHistory = request.getParameter(BaseConstants.VAR_DOC_HISTORY);
			logger.debug("docHistory: " + docHistory);
			NodeInfo ni = ci.getCurrentNode();
			if(docHistory!=null){
				try {
					//ci.appendCallHistory(docHistory);
					
					if(ni!=null){
						ni.appendHistory(docHistory);
						ni.endHistory(ci);
					}
					else{
						ci.setCallHistory(docHistory);
					}
				}
				catch(Exception e) {}
			}
			
			if(ni!=null){
				ni.appendHistory("event=error");
				ni.endHistory(ci);
			}
			else{
				ci.setCallHistory(ci.getCallHistory()+";event=error");
			}

			result = buildDocument(vxml,
					ci,
					null, 
					request);
			
		}
		catch(Exception e){
			logger.error("Error displaying error document: " + e.getMessage());
			logger.info("trying to create error exit document: ");
			result = this.silentExit(vxml, callID); 
		}
		finally {
			if(logger.isTraceEnabled())  logger.trace(result);
			
			removeMDC(ci);
		}
	
		return result;
	}
 
	public VXML addDocumentBody(VXML vxml, ICallInfo ci, String submitNext, HttpServletRequest request) {

		String[] arrLangCfg = null; 
		String term = null;
		String promptpath  = BaseGlobalConfig.resource_repository_url + "prompts/system/";
		logger.info("promptpath: " + promptpath);
		int len = 1;// BaseGlobalConfig.languages.length;
		
		try {
			arrLangCfg = ci.getLangArray();			
			len = arrLangCfg.length;		
			logger.info("number of languages: " + len);
			if(len<=0)
				len = BaseGlobalConfig.languages.length;
			
			
			term = ""+ci.getTermCode();
		}
		catch(Exception e) {
			logger.error("error getting session language array, using default");
			arrLangCfg = BaseGlobalConfig.languages;
			if(arrLangCfg !=null)
			len = BaseGlobalConfig.languages.length;
			else
				len =1;
		}

		for(int i=0;i<len;i++) {
			vxml.FormStart("mainTechDiff"+arrLangCfg[i], "dialog", "true"); //id, scope, cleardtmf
				
			//if(GlobalConfig.langPropMap.get(AppBaseConstants.TTSENGINE_PREFIX_KEY + GlobalConfig.languages[i])!=null)
			//vxml.Property(VXMLProperties.PROP_TTSENGINE, GlobalConfig.langPropMap.get(AppBaseConstants.TTSENGINE_PREFIX_KEY + GlobalConfig.languages[i]));// "speechify_jill");
				
			vxml.BlockStart("playTechDiffMsg" + arrLangCfg[i]);
				
			if(term!=null)
			vxml.Assign(CDRConstants.KEY_TERM, term);

			vxml.AudioStart(promptpath + arrLangCfg[i] + "/techDiff.wav");
				vxml.Text(BaseUtils.getStringFromFile(BaseGlobalConfig.resource_repository_basepath + promptpath + arrLangCfg[i] + "/techDiff.txt"));
			vxml.AudioEnd();							

			if(i>=len-1)
				vxml.Disconnect();
			else
				vxml.Goto("#mainTechDiff"+arrLangCfg[i+1]);
			vxml.BlockEnd();
			
			//in any case goto call end, overrides the vxml header handling
			vxml.ErrorStart();
			if(BaseGlobalConfig.debug) {
			vxml.Text("form Error in technical difficulties, disconnecting");
			}
			vxml.Disconnect();
			vxml.ErrorEnd();
		
			vxml.CatchStart(VXMLEvents.EVENT_ERROR);
			if(BaseGlobalConfig.debug) {
				vxml.Text("Caught form event Error in technical difficulties, disconnecting");
				}
			vxml.Disconnect();
			vxml.CatchEnd();
			vxml.FormEnd();
		}
		
		vxml.CatchStart(VXMLEvents.EVENT_ERROR);
		if(BaseGlobalConfig.debug) {
			vxml.Text("Caught document event Error in technical difficulties");//, redirecting to call end");
			}
		//vxml.Submit(ICallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.Disconnect();
		vxml.CatchEnd();

		vxml.ErrorStart();		
		if(BaseGlobalConfig.debug) {
			vxml.Text("document Error in technical difficulties");//, redirecting  to call end");
			}
		//vxml.Submit(ICallStates.callend, null, CDRConstants.KEY_TERM);
		vxml.Disconnect();
		vxml.ErrorEnd();
					
		return vxml;
	}		 
}  // end of TechDiff