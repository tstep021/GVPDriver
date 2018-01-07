package com.mycompany.gvpdriver.callend; 

/** copyright 2009-2013, mycompany. */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.entity.*;

/** 
 * File         CallEnd.java
 * 
 * Description  This class cleans up the session, 
 *                overrides abstract method doResponse 
 *               which has to call onSessionEnd
 * 					
 * @author       Tatiana Stepourska
 * 
 * @version      1.3
 */

 public class CallEnd  extends BaseCallEnd
 { 	    	
	 private static final long serialVersionUID = 1L;
	 

	 public void customCallEnd(ICallInfo ci, HttpServletRequest request, HttpServletResponse response){
		 
	 }

}//end of class