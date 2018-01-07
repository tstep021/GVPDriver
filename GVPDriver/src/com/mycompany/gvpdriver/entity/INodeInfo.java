package com.mycompany.gvpdriver.entity;

/** Copyright 2010-2013, mycompany. All rights reserved */

//import javolution.util.FastList;

import javolution.util.FastMap;
import javolution.util.FastList;

/** 
 *      
 *
 * @author  Tatiana Stepourska
 * @version 1.0
*/
public interface INodeInfo 
{ 		
	public INodeSessionInfo getNodeSessionInfo();
	public void setNodeSessionInfo(INodeSessionInfo o);
	public boolean isFirst();
	public void setFirst(boolean s);
	public void setSpName(String s);
	public String getSpName();
	public void setName(String s);
	public String getName();	
	public FastMap<String, FastMap<String, FastList<Audio>>> getAudioLangMap();	
	public void setAudioLangMap(FastMap<String, FastMap<String, FastList<Audio>>> am);	
	public FastMap<String, String> getProperties();
	public void setProperties(FastMap<String, String> pp);
	public String getProperty(String s);
	public void setProperty(String key, String value);	
	public FastMap<String, ExitOption> getExitOptions();
	public void setExitOptions(FastMap<String, ExitOption> eo);
	public String getExecutionResult();	
	public void setExecutionResult(String r);
	public String getResultKey();	
	public void setResultKey(String r);
	/**
	 * Returns node Id unique per call
	 * 
	 * @return
	 */
	public String getId();
	/**
	 * Sets node id unique per call
	 * 
	 * @param nid
	 */
	public void setId(String nid);
	public String getType();
	public void setType(String t);
	public String getUrl();
	public void setUrl(String s);
	//public String toHistory(ICallInfo ci);
	public void startHistory(ICallInfo ci);	
	public void endHistory(ICallInfo ci);	
	public void appendHistory(String s);
 }//end of class