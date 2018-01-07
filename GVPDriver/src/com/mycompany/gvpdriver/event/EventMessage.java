package com.mycompany.gvpdriver.event;

/** @copyright   2009-2013 mycompany */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

//import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;
import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;

/** 
 *
 * @description  This servlet plays message(s)
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */
public class EventMessage  extends BaseCallFlow {
	
	private static final long serialVersionUID = 337357044404620015L;
	private static final Logger logger = Logger.getLogger(EventMessage.class);

	/**
	 * @param callID	 	--call id
	 * @param ci		 	--The CallInfo object
	 * @param vxml			--VXML object
	 * @param session      	--HTTP session
	 * @param request      	--HTTP request
	 * @param response      --HTTP response
	 * 
	 * @return String		--The VXML response
	 */
	public String doResponse(String callID,							
				ICallInfo         ci,
				VXML 				vxml,
				HttpSession         session,
				HttpServletRequest  request,
				HttpServletResponse response
	) throws ServletException,IOException, Exception 
	{		
		addMDC(callID,ci);
		String result = "";
		logger.info("started");
		String submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKOUT;

		try {
			result = buildDocument(vxml,
						ci,
						submitNext,
						request);		
		}
		catch(Exception e) {
			logger.error("Error building vxml response: " + e.getMessage());
		}
		finally {
			if(logger.isTraceEnabled()) logger.trace(result);
			removeMDC(ci);
		}
			
		return result;
	}	

	/** 
	* Builds custom part of the VXML response document body.
	*
	* @param ci		-- BaseCallInfo object (for Driver it shoudl be an instance of CallInfo)
	* @return vxml  -- Part of VXML response
	*/
	public VXML addDocumentBody(VXML vxml, ICallInfo ci, String submitNext, HttpServletRequest request) {
		
			//String promptpath  		= null;
			//String nodeID 			= null;		
			boolean isLangSelected	= false;
			String[] arrLangCfg 	= null;
			String langCode		 	= null;
			FastList<Audio> audios 	= null;
			FastMap<String, FastMap<String, FastList<Audio>>> audioLangMap = null;
			String bargein	 		= VXMLValues.VAL_FALSE;
			String langMode 		= null;
			Audio a 				= null;	
			String event			= null;
			NodeInfo ni				= null;

			try {
				//event name corresponds to prompt type
				event 			= ci.getEvent();
				ni 				= ci.getNode(ci.getGlobalNodeID());
				//nodeID     		= ni.getId();
				arrLangCfg		= ci.getLangArray(); 
				isLangSelected	= ci.isLangSelected();	
				logger.info("isLangSelected: "+isLangSelected);	
				langCode		= ci.getLangCode();
				//promptpath      = ci.getPromptpath();
			}
			catch(Exception e) {
				logger.error("error getting call info: " + e.getMessage());
			}
			
			try {			
				audioLangMap = ni.getAudioLangMap();
				if(audioLangMap==null||audioLangMap.size()<=0){
					//logger.info("audioLangMap is null or empty");
					vxml = getLastForm(vxml, submitNext);
					return vxml;
				}
		
				audios = this.buildAudioList(audioLangMap,arrLangCfg, //promptpath, nodeID, 
						isLangSelected, langCode, event);		
				/*if(logger.isTraceEnabled()){
					logger.trace("audios: " + audios);
				}*/
				
			}
			catch(Exception e) {
				logger.error("error getting audios: " + e.getMessage());
			}
			
			ni.appendAudiosToHistory(audios);
			
			//logger.trace("start building vxml ");
			vxml.FormStart("main", "dialog", "true");
	
			vxml.BlockStart("blkPlayMessage");	

			//append each audio wrapped with vxml prompt tag
			if(audios!=null) {
				//logger.trace("appending prompts");
			//append all available audios to vxml
			for (FastList.Node<Audio> n = audios.head(), end = audios.tail(); (n = n.getNext()) != end;) {
				try{
					a = n.getValue();
					//logger.trace("a: " +a);
					if(a==null)
						continue;
				
					langMode = BaseGlobalConfig.langPropMap.get(BaseConstants.LANG_MODE__KEY + a.getLanguage());
					//logger.debug("langMode: " +langMode);
					appendPrompt(vxml, a, bargein, langMode);
				}
				catch(Exception e){
					logger.error("Error wrapping audio: " + e.getMessage());
				}
			}	//end of the loop		
			}	//end of audios != null			
			
			vxml.Goto("#keepGoing");
			vxml.BlockEnd();
			vxml.FormEnd();
			
			/////////////////////////////////////////////////	
			vxml = getLastForm(vxml, submitNext);
			//all error handling is in the vxml app root
			//to override, add error/catch blocks here

			return vxml;
		}		 
		
		private VXML getLastForm(VXML vxml, String submitNext) {
			
			vxml.FormStart("keepGoing");
			vxml.BlockStart("submitBlock");
			vxml.Submit(submitNext, null, 
					null);  //namelist
			vxml.BlockEnd();
			vxml.FormEnd();
			return vxml;
		}
		
		private void appendPrompt(VXML vxml, Audio a, String bargein, String langMode) {
			if(a==null)		
				return;
		
			String tts = null;
			String src = a.getSrc();
			String text = a.getText();
			if(logger.isTraceEnabled()){
				logger.trace("appending prompt for audio " + a);
				//logger.trace("src: " + src + ", text: " + text);
			}

			vxml.PromptStart(
						bargein, 	//bargein
						null,		//bargeintype 
						null, 		//cond
						null, 		//count
						null, 		//timeout	//default 10s
						null, 		//xml:base
						langMode, 	//xml:lang
						null, 		//gvp:langexpr
						tts		//gvp:ttsengine
						);	
				
				vxml.AudioStart(src); 
				vxml.Text(text);
				vxml.AudioEnd();
				
				vxml.PromptEnd();
		}
}	// end of class
