package com.mycompany.gvpdriver.base;

/** copyright 2009-2013, mycompany  */

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.entity.*;
import com.mycompany.vxml.facade.*;

/** 
 * File         BaseNodeController.java
 * 
 * Description   
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
public abstract class BaseNodeController extends BaseCallFlow
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BaseNodeController.class);
		
	/** 
	 * Builds custom part of the VXML response document body.
	 *
	 * To be implemented by all classes that generate 
	 * vxml response documents
	 * Do NOT include headers, footers, and document level 
	 * events
	 * 
	 * @param vxml	    -- VXML object
	 * @param ci		-- BaseCallInfo object (for Driver it shoudl be an instance of CallInfo)
	 * @return vxml     -- Part of VXML response
	 */
	public abstract VXML addDocumentBody(VXML vxml,ICallInfo ci, NodeInfo fi, String submitNext) throws Exception;// {
			
	public VXML addDocumentBody(VXML vxml,ICallInfo ci, String submitNext, HttpServletRequest req) throws Exception {
		
		NodeInfo nodeInfo = null;
	
		try {
			
			// get config associated with the current node
			nodeInfo = ci.getCurrentNode(); 
			
			//moved to checkin for nodes that require feature usage
			//ci.addFeature(nodeInfo.getType());
		}
		catch(Exception e) {
			logger.error("Error getting node info: " + e.getMessage());
		}
		vxml = addDocumentBody(vxml,ci, nodeInfo, submitNext);		

		return vxml;
	}
	
}//end of class