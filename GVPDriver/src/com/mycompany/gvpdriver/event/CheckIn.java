package com.mycompany.gvpdriver.event;

/** @copyright   2011-2013 mycompany */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;
import com.mycompany.vxml.facade.*;
//import com.mycompany.gvpdriver.util.*;

/** 
 * @description  
 * 
 * @author      tatiana.stepourska
 * 
 * @version      1.1
 */
public class CheckIn extends BaseNodeController 
{
	private static final long serialVersionUID = -5982617032635973272L;
	private static final Logger logger = Logger.getLogger(CheckIn.class);

	/**
	 * Generates our servlet's response.  Should be overridden by each servlet.
	 *
	 * @return String      The VXML response
	 */
	 public String doResponse(String callID,							
							ICallInfo         ci,
							VXML 				vxml,
							HttpSession         session,
							HttpServletRequest  request,
							HttpServletResponse response
							) throws ServletException,IOException, Exception 
	{		
		addMDC( callID,ci);

		//logger.info("started");
		String result = null;
		String event = request.getParameter(BaseConstants.EVENT_KEY);
		logger.info("started global event handling - event: "+event);
		
		String submitNext = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_MESSAGE;

		//get history, most likely from previous node
		String docHistory = request.getParameter(BaseConstants.VAR_DOC_HISTORY);
		//logger.debug("docHistory: " + docHistory);
	
		try	{
			//assign current event
			ci.setEvent(event);		

			NodeInfo globalNodeInfo = ci.getNode(ci.getGlobalNodeID());
			NodeInfo currentNode = ci.getCurrentNode();
			if(docHistory!=null){
				//ci.appendCallHistory(docHistory);
				currentNode.appendHistory(docHistory);
				currentNode.endHistory(ci);
			}

			globalNodeInfo.startHistory(ci);

			//if(logger.isTraceEnabled()){
			//	logger.trace("Current node: " + currentNode);
			//}
						
			if(event==null){
				logger.info("event is null, checking out");
				submitNext = BaseGlobalConfig.submitbase+BaseCallStates.CLASS_GLOBAL_EVENT_CHECKOUT;
			}
			else if(event.equalsIgnoreCase(VXMLEvents.EVENT_MAXNOINPUT)){

			}
			else if(event.equalsIgnoreCase(VXMLEvents.EVENT_MAXNOMATCH)){

			}
			/*else if(event.equalsIgnoreCase(AppEvents.EVENT_ERROR)){
				
			}*/
			else if(event.equalsIgnoreCase(VXMLEvents.EVENT_OPERATOR)){
				
			}
			else if(event.equalsIgnoreCase(VXMLEvents.EVENT_GOBACK)){
				//get list of exit options of the global event node				
				FastMap<String, ExitOption>  exitOptions = globalNodeInfo.getExitOptions();
				if(exitOptions==null){
					exitOptions = new FastMap<String, ExitOption>();
				}
			
				try {
					//find out ID for node to go back to
					NodeInfo gobackNode = ci.getGobackNode();					
					
					//at the first level, no room to go back
					if(gobackNode==null){
						logger.info("go back node is null, no room to go back");
						
						//add current node info to the list of exit options for go back
						ExitOption gobackOption = new ExitOption();
						gobackOption.setKey(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN);
						//gobackOption.setLabel(langCode);
						//set the node ID where is came from
						gobackOption.setExitValue(currentNode.getId());
						exitOptions.put(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN, gobackOption);
						
						globalNodeInfo.setExitOptions(exitOptions);
					}
					else {
						//valid node to go to, remove it from history
						ci.removeLastGobackHistoryNode();
						
						String gobackID = gobackNode.getId();
						logger.info("gobackID: " + gobackID);			
						
						//add goback node info to the list of exit options
						ExitOption gobackOption = new ExitOption();
						gobackOption.setKey(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN);
						//gobackOption.setLabel(langCode);
						gobackOption.setExitValue(gobackID);
						exitOptions.put(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN, gobackOption);
						globalNodeInfo.setExitOptions(exitOptions);
						
						//no message, go directly to checkout
						submitNext = BaseGlobalConfig.submitbase+BaseCallStates.CLASS_GLOBAL_EVENT_CHECKOUT;						
					}				
				}
				catch(Exception e){
					logger.error("Error getting go back node: " + e.getMessage());
					
					//add current node info to the list of exit options for go back
					ExitOption gobackOption = new ExitOption();
					gobackOption.setKey(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN);
					//gobackOption.setLabel(langCode);
					//set the node ID where is came from
					gobackOption.setExitValue(currentNode.getId());
					exitOptions.put(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN, gobackOption);
					
					globalNodeInfo.setExitOptions(exitOptions);
				}
			}
			/*else if(event.equalsIgnoreCase(AppEvents.EVENT_EXIT)){
				
			}
			else if(event.equalsIgnoreCase(AppEvents.EVENT_CANCEL)){
				
			}
			else if(event.equalsIgnoreCase(AppEvents.EVENT_HELP)){
				
			}		
			else{
				
			}
		*/
			//set globalEventNode as a current node
			ci.setCurrentNode(globalNodeInfo);
			
			result = buildDocument(vxml,
						ci,
						submitNext,
						request);				

			if(logger.isTraceEnabled()) logger.trace(result);
		}
		catch(Exception e)	{
			logger.error("ERROR : " + e.getMessage());
			try {
				response.sendRedirect(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF);
			}
			catch(Exception ex) {
				result = this.silentExit(vxml, callID);
			}
		}
		finally {
			removeMDC(ci);
		}
		return result;
	}
	
	public VXML addDocumentBody(VXML vxml, ICallInfo ci, NodeInfo fi, String submitNext) throws Exception
	{
		/*String nodeID = null;
		try {
			nodeID = ci.getGlobalNodeID();
			ci.appendCallHistory(CDRConstants.HISTORY_TOKEN_NODE_START+nodeID + CDRConstants.HISTORY_TOKEN_NODE_BODY_START);
		}
		catch(Exception e){
			logger.error("Error getting node ID: " + e.getMessage());
		}*/

		vxml.FormStart("main");			
		vxml.BlockStart("pass");
		
	/*
	if(BaseGlobalConfig.loadtest){			
			try {
				String[] arr = Utils.getWavesForCharacters(nodeID);
				if(arr!=null){
				vxml.PromptStart(AppValues.VAL_FALSE);
				vxml.AudioStart(BaseGlobalConfig.resource_repository_url+"prompts/system/silence1s.wav");
				vxml.Text("	");
				vxml.AudioEnd();
				for(int i=0;i<arr.length;i++){			
					vxml.AudioStart(BaseGlobalConfig.resource_repository_url+"prompts/system/dtmf_"+arr[i]+".wav");
					vxml.Text(arr[i]);
					vxml.AudioEnd();
				}
				vxml.PromptEnd();
				}
			}
			catch(Exception e) {
				
			}		
		}*/

			vxml.Submit(submitNext, //next,
						null, 		//expr
						null		//namelist 
						);

		vxml.BlockEnd();	
		
		//all error handling is in the vxml header/footer of BaseCallFlow
		//to override, add error/catch blocks here
		
	vxml.FormEnd();	
	
	return vxml;
	}
}