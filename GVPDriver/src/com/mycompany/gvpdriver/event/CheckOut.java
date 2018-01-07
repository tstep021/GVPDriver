package com.mycompany.gvpdriver.event;

/** @copyright   2012-2013 mycompany */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import javolution.util.FastMap;
import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

//import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.*;
import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;

/** 
 * @file         CheckOut.java
 * 
 * @description  
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.1
 */
public class CheckOut extends BaseNodeController //implements IMenuBaseConstants
{
	private static final long serialVersionUID = -7288670000451594187L;
	private static final Logger logger = Logger.getLogger(CheckOut.class);
	
	/**
	 * Generates our servlet's response.  Should be overridden by each servlet.
	 *
	 * @return String      The VXML response
	 */
	 public String doResponse(String 			callID,							
							ICallInfo         	ci,
							VXML 				vxml,
							HttpSession         session,
							HttpServletRequest  request,
							HttpServletResponse response
							) throws ServletException,IOException, Exception 
	{		
		 addMDC(callID, ci); //(CDRConstants.CALL_ID_KEY, callID);
		logger.info("started");
		String result 			= null;
		//ExitOption exitOption 	= null;
		//String resultKey		= null;
		String event 			= null;

		try	{
			//get event to checkout
			event = ci.getEvent();
			logger.info("event: " + event);
			//get exit options map
		/*	FastMap<String, ExitOption> optsMap = ci.getCurrentNode().getExitOptions();
			
			if(optsMap!=null) {
				for (FastMap.Entry<String, ExitOption> e = optsMap.head(), end = optsMap.tail(); (e = e.getNext()) != end;) {
					resultKey = e.getKey(); 
					if(resultKey==null)
						continue;			
					else if(resultKey.compareTo(BaseConstants.EXIT_OPTION_TOKEN+event+BaseConstants.EXIT_OPTION_TOKEN)==0){			
						exitOption = e.getValue(); 
						logger.info("found exitOption for resultKey "+ resultKey +": " + exitOption);
						break;
					}
				}
			}*/

			//set result of the current node execution
			ci.getCurrentNode().setExecutionResult(event);
			ci.getCurrentNode().setResultKey(BaseConstants.UNDERSCORE + event + BaseConstants.UNDERSCORE);	
			ci.getCurrentNode().endHistory(ci);
		
			result = buildDocument(vxml,
					ci,
					BaseCallStates.checkin,
					request);
		}
		catch(Exception e)	{
			logger.error("ERROR : " + e.getMessage());
		}
		finally{
			if(logger.isTraceEnabled()) logger.trace(result);
			removeMDC(ci);
		}
		return result;
	}
	
	public VXML addDocumentBody(VXML vxml, ICallInfo ci, NodeInfo fi, String submitNext) throws Exception
	{
		vxml.FormStart("main");			
		vxml.BlockStart("pass");

			vxml.Submit(submitNext, 	//next,
						null, 					//expr
						null); //,	//namelist 
						//METHOD_POST);

		vxml.BlockEnd();	
		
		//all error handling is in the vxml header/footer of BaseCallFlow
		//to override, add error/catch blocks here
		
	vxml.FormEnd();	
	
	return vxml;
	}

	/*public final void addMDC(String callID, ICallInfo ci){
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
	}*/
	
}