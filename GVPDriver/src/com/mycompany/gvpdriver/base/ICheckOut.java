package com.mycompany.gvpdriver.base;

/** @copyright   2010-2013 mycompany */

//import java.io.File;
//import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.gvpdriver.entity.*;

/** 
 * 
 * 
 * Description   * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public interface ICheckOut 
{	
	public String findNextUrl(ICallInfo ci, HttpServletRequest request, HttpServletResponse response) throws Exception;
}	// end of class