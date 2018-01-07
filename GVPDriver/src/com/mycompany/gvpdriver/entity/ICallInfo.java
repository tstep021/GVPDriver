package com.mycompany.gvpdriver.entity;

/** copyright 2012, mycompany Inc. */ 

import java.util.HashMap;

import javolution.util.FastMap;
import javolution.util.FastList;
import com.mycompany.ecs.cdr.transaction.IRecord;

/** 
 * @file         ICallInfo.java
 * 
 * @description  
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.0
 */

public interface ICallInfo extends IRecord
{ 	
	public long getBackUpDnis();
	public void setBackUpDnis(long b);
	/**
	* Gets the language mode for grammars 
	* 
	* @return String
	*/
	public String getLangMode();
	/**
	* Sets the language code for grammars
	*    	   
	* @param lg
	*/
	public void setLangMode(String lg);
	public void setCallflow(FastMap<String, NodeInfo> cf);	
	public FastMap<String, NodeInfo> getCallflow();	
	public void incrementCountAttempts();
	public int getCountAttempts();
	public void resetCountAttempts();
	public void setGlobalProperties(FastMap<String, String> m) throws Exception;	
	public FastMap<String, String> getGlobalProperties() throws Exception;
	public void setGlobalProperty(String key, String value)  throws Exception;
	public String getGlobalProperty(String key)  throws Exception;
	public void setGlobalPrompts(FastMap<String, FastMap<String, FastList<Audio>>> am) throws Exception;
	public FastMap<String, FastMap<String, FastList<Audio>>> getGlobalPrompts() throws Exception;	
	public void setGlobalEventExitOptions(FastMap<String, ExitOption> m) throws Exception;
	public FastMap<String, ExitOption> getGlobalEventExitOptions() throws Exception;	
	public void setGlobalNodeID(String s);	
	public String getGlobalNodeID();
	public FastList<NodeInfo> getGobackHistory();
	public void setGobackHistory(FastList<NodeInfo> li);
	/**
	 * Adds the last visited input node to the goback 
	 * history, keeps the size up to max
	 * @param n
	 */
	public void addGobackHistoryNode(NodeInfo n);
	public boolean removeLastGobackHistoryNode();	
	public NodeInfo getGobackNode();
	public ICallInfo getInstance();
	/**
	 * Get the languages in order
	 * 
	 * @return int langArray
	 */
	 public String[] getLangArray();

	 /**
	  * Set the languages in order
	  *    	   
	  * @param  lg
	  */
	 public void setLangArray(String[] lg);

	/**
	 * Get the language config property
	 * 
	 * @return int langConfig
	 */
	 public String getLangConfig();

	 /**
	  * Set the langConfig property
	  *    	   
	  * @param  lg
	  */
	 public void setLangConfig(String lg);
	  	
	/**
	* Returns the langSelected property
	*  
	* @return boolean
	*/
	public boolean isLangSelected();

	/**
	* Sets the langSelected
	*    	   
	* @param lg
	*/
	public void setLangSelected(boolean lg);

	/**
	 * Sets max number of tries on error
	 * 
	 * @param c
	 */
	public void setMaxtries(int c);

	/**
	 * Gets max number of tries on error
	 * 
	 * @return int
	 */
	public int getMaxtries();
  
	/**
	 * Sets max number of tries on no input
	 * 
	 * @param c
	 */
	public void setMaxnoinput(int c);
	
	/**
	 * Gets max number of tries on noinput
	 * 
	 * @return int
	 */
	public int getMaxnoinput();

	/**
	 * Sets max number of tries on nomatch
	 * 
	 * @param c
	 */
	public void setMaxnomatch(int c);

	/**
	 * Gets max number of tries on nomatch
	 * 
	 * @return int
	 */
	public int getMaxnomatch();

	/**
	* Sets the promptpath for the current profile   
	* 
	* @param pp
	*/
	public void setPromptpath(String pp);

	/**
	* Returns the promptpath for the current profile
	* 
	* @return  String  inputmodes
	*/
	public String getPromptpath();
	
	/**
	* Sets the grammarpath for the current profile   
	* 
	* @param gp
	*/
	public void setGrammarpath(String gp);

	/**
	* Returns the grammarpath for the current profile
	* 
	* @return  String  grammarpath
	*/
	public String getGrammarpath();
	
	/**
	* Method for getting countRetry current value   	 
	* 
	* @return  int countRetry
	*/
	//public int getCountRetry();
		   
	/**
	* Method for resetting countRetry   	 
	* 
	* @return  int countRetry
	*/
	//public int resetCountRetry();

	/**
	* Method to increment countRetry when error happened
	*/
	//public void incrementCountRetry();
	public String getEvent();
	public void setEvent(String e);
	public String getFirstNodeID();
	public void setFirstNodeID(String s);
	public NodeInfo getNode(String id) throws Exception;
	public void setCurrentNode(NodeInfo fi);
	public NodeInfo getCurrentNode();
	
	/**
	 * Sets all properties that rely on a language. Either 
	 * langCfg or currentLanguage has to be not null
	 * Called from CallStart (langCfg is not null, currentLang==null),
	 *  or from language selection node (langCfg==null, currentLang is not null)
	 * 
	 * @param lg
	 */
	public void setLanguageProperties(String languageToSet);
	
	/**
	 * Sets all properties that rely on a language. Either 
	 * langCfg or currentLanguage has to be not null
	 * Called from CallStart (langCfg is not null, currentLang==null),
	 *  or from language selection node (langCfg==null, currentLang is not null)
	 * 
	 * @param langCfg
	 */
	public void setLanguageFromConfig(String langCfg);
	/**
	 * Adds the key / value pair, or, if the key already exists,
	 * overrides it
	 * @param n
	 */
	public void addMessage(String key, String value);
	/**
	 * Removes the value for specified key from the messageBoard
	 * @param key
	 */
	public void removeMessage(String key);
	public String getMessage(String key);
	public FastMap<String, String> getMessageBoard();
	public void setMessageBoard(FastMap<String, String> m);
	public String[] getMDCKeys();
	public String[] getMDCValues();
	public void setMDCKeys(String[] kk);
	public void setMDCValues(String[] vv);
	public HashMap<String, String> getSipInfo();
	public void setSipInfo(HashMap<String, String> m);

	public String toHeaderString();
	public String toString();	
	public String toStringShort();
}//end of class