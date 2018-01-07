package com.mycompany.gvpdriver.callstart;

/** @copyright  2010-2013 mycompany. */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.gvpdriver.base.BaseCallStart;
import com.mycompany.gvpdriver.entity.*;
import com.mycompany.gvpdriver.event.RejectCallEvent;

/** 
 * File         CallStart.java
 * 
 * Description  This SessionStart class extends common @see SessionStartBase
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */

public class CallStart extends BaseCallStart
{
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes session object
	 * 
	 * @param request
	 * 
	 * @return SessionInfo
	 */
	public ICallInfo createCallInfo(){
		return new CallInfoBase();
	}

	@Override
	public ICallInfo initSessionInfo(
			ICallInfo ci, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws RejectCallEvent, Exception {
		// TODO Auto-generated method stub
		return ci;
	}

}