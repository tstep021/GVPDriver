package com.mycompany.gvpdriver.dispatcher;

/** copyright 2010-2013, mycompany  */

import java.io.IOException;
//import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

//import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;
import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;

/** 
 * File        Dispatcher.java
 * 
 * Description  . 
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
 public class Dispatcher extends BaseCallFlow
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Dispatcher.class);
	
	/** 
	 * Gets invoked once by the service method 
	 *   
	 * @param  config 	The ServletConfig object that contains configuration 
	 *        			information for this servlet 
	 * @exception ServletException    thrown when the servlet's normal operation 
	 *								 is interrupted
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	 
	/**
	 * Generates our servlet's response.  Should be overridden by each servlet.
	 * 
	 * @param info         --The CDRInfo object
	 * @param config       --SessionInfo object
	 * @param vxml         --VXML object
	 * @param session      --HTTP session
	 * @param request      --HTTP request
	 * @param response      --HTTP response
	 *
	 * @return String      The VXML response
	 */
	public String doResponse(String 			callID,
			ICallInfo			ci, 
			VXML 				vxml,
			HttpSession 		session, 
			HttpServletRequest  request,
			HttpServletResponse response
			) throws ServletException,IOException, Exception
	{
		addMDC(callID, ci);
		logger.info("started checkin");
		String result = null;
		String target = null;

		try {
			target = resolveNext(ci);
		}
		catch(Exception e) {
			logger.error("Error checkin: " + e.getMessage());
			target = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF;
		}
		finally {
			//logger.info("next url: " + target);	
			try {
				result = buildDocument(vxml,
					ci,
					target,
					request
					);
				if(logger.isTraceEnabled()){
					logger.trace(result);
				}		
			}
			catch(Exception ex){
				logger.error("Error building checkin result: " + ex.getMessage());
			}			
			removeMDC(ci);
		}
	
		return result;
	}

	public VXML addDocumentBody(VXML vxml, ICallInfo ci, String submitNext, HttpServletRequest request) {  	 							
		
		vxml.FormStart("main");			
		vxml.BlockStart("pass");

		if(submitNext==null){
			vxml.Disconnect();
		}
		else {
			vxml.Submit(submitNext, 	//next,
				null, 				//expr
				null,				//namelist 
				null,
				null,					//caching 
				BaseGlobalConfig.fetchaudio, 	//fetchAudio
				null, 					//fetchint
				null, 					//fetchTO 
				null);					//encType
		}

		vxml.BlockEnd();	
		vxml.FormEnd();		
		
		return vxml;
	}		 
	
	/**
	 * Selects next node to go to
	 * 
	 * @param ci
	 * @return String  --Next node url
	 * @throws Exception
	 */
	private String resolveNext(ICallInfo ci) throws Exception {
		ExitOption exitOption 	= null;			
		String target 			= null;
		//String optKey 			= null;
		String nextNodeID 		= null;
		NodeInfo nextNode 		= null;
		
		NodeInfo currentNode					= ci.getCurrentNode();
		logger.info("resolveNext: from node: "+currentNode.getId());
		FastMap<String, ExitOption> exitoptions = currentNode.getExitOptions();
		//logger.trace("resolveNext: got exitoptions: "+exitoptions);
		if(exitoptions==null||exitoptions.isEmpty()){
			logger.info("resolveNext: no exit options found for node " + currentNode.getId());
			return null;
		}
		
		String resultKey 						= currentNode.getResultKey();		
		logger.info("resolveNext: resultKey: "+resultKey);

		exitOption = exitoptions.get(resultKey);

		//not found
		if(exitOption==null){
			logger.info("resolveNext: no exit option found for node " + currentNode.getId() + " with resultKey " + resultKey);
			return null;
		}
		
		logger.info("resolveNext: exit option: "+exitOption.toFullString());
		
		//exit option found, check if it has a single exit entry, or more than 1
		if(exitOption.hasSingleExit()){
			logger.info("resolveNext: exit option has a single exit");
			nextNodeID 	= exitOption.getExitValue();			
		}
		else {
			logger.info("resolveNext: exit option has multiple exits");
			String execResult = null;
			//FastMap<String, String> exitMap = exitOption.getExitMap();
			String routingNodeID = exitOption.getRoutingNodeID();
			logger.info("resolveNext: routingNodeID: "+routingNodeID);			
			
			//more than one language present - routing by language
			if(exitOption.getLangCount()>1){
				String langLabel = ci.getLangCode();
				logger.info("current session language: " + langLabel);
				//by language only
				if(routingNodeID==null){
					//exitMap.getEntry(ci.getLangCode());
					//get next node ID
					nextNodeID = exitOption.getExitValueFromMap(langLabel);
				}
				//by both language and routing node
				else {
					//(key is underscore separated)
					NodeInfo routingNode = ci.getNode(routingNodeID);			
					if(routingNode!=null){						
						execResult = routingNode.getExecutionResult();
						logger.info("resolveNext: execResult for node " +routingNodeID + ": " + execResult);
						if(execResult!=null)
							nextNodeID = exitOption.getExitValueFromMap(langLabel+BaseConstants.UNDERSCORE+execResult);
					}				
				}
			}
			//node result routing only
			else {
				NodeInfo routingNode = ci.getNode(routingNodeID);
				if(routingNode!=null){						
					execResult = routingNode.getExecutionResult();
					logger.info("resolveNext: execResult for node " +routingNodeID + ": " + execResult);
					if(execResult!=null)
						nextNodeID = exitOption.getExitValueFromMap(execResult);
			}
		}	//end of if multiple exits found

		}

		if(nextNodeID==null){
			logger.info("resolveNext: nextNodeID is NULL, ending the call");
			return null;
		}			
		logger.info("resolveNext: nextNodeID: " + nextNodeID);
		//get next node by ID
		nextNode = ci.getNode(nextNodeID);
		if(nextNode==null){
			logger.info("nextNode is NULL, ending the call");
			return null;
		}

		//get next node URL
		target = nextNode.getUrl();	
		logger.info("target: " + target);
			
		//set next node to the session
		if(target!=null)
			ci.setCurrentNode(nextNode);	
		else {
			logger.info("target url is null, ending the call");
		}
		return target;
	}
}//end of class