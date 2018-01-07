package com.mycompany.gvpdriver.callstart;

/** @copyright   2010-2013 mycompany */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;

/** 
 * File         CheckOut.java
 * 
 * Description  The purpose of this class 
 * 				1) to get GVP vars and set it to the CDRInfo after the 
 * 				beginning of the call
 * 				2) Find next place to go
 *  			This class can be extended or replaced
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public class CheckOut extends BaseCallStartCheckOut
{	
	private static final long serialVersionUID = -3452017500570279522L;
	private static final Logger logger = Logger.getLogger(CheckOut.class);
	
	/**
	 * 
	 * 
	 */
	public String findNextUrl(ICallInfo ci, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstNodeID  = ci.getFirstNodeID();
		if(logger.isDebugEnabled()) {
			logger.debug("firstNodeID: " + firstNodeID);
		}
		//getting first node of the callflow
		NodeInfo firstNode = ci.getNode(firstNodeID);
		
		if(firstNode==null){
		logger.info("First node is NULL!");
		}
		
		ci.setCurrentNode(firstNode);
		return firstNode.getUrl();		
	}
}	// end of class