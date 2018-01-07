package com.mycompany.gvpdriver.entity;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.BaseConstants;

import javolution.util.FastMap;

/**
 * 
 * @author tatiana.stepourska
 *
 */
public class ExitOption implements Serializable {
	private static final Logger logger = Logger.getLogger(ExitOption.class);
	private static final long serialVersionUID = 263601557720007043L;

	/** Oracle generated id - used for accessing TOD path**/
	private int id = 0; 

	/** 
	 * Recognition (dtmf key or speech grammar) value
	 * Also can be _default_ , or global event identfier with 
	 * leading and trailing underscores attached, for example,
	 * exit option key for event "error" will be "_error_"
	 */
	private String key = null;
	
	//private boolean routingByLanguage = false;
	private int langCount = 0;
	
	/** Grammar tag assigned to the recognized input, f.ex, 
	 * for DTMF key 1 language assigned is 'en-US', or
	 * for digital input 516 assigned value 'N553326'
	 * Should be used by engine for grammar handling only!
	 */
	private String grammarTag = null;
	
	/**
	 * Field used for routing by language feature
	 */
	//private String languageLabel = null;
	
	/**
	 * Field used for routing by some previous caller input
	 */
	private String routingNodeID = null;
	
	/** Node unique ID to go next, if this label has been assigned 
	 * or this key recognized */
	private String exitValue = null;
	
	//private FastMap<String, Object> resultMap = null;
	private FastMap<String, String> exitMap = null;
	
	public ExitOption(){
	}
	
	public ExitOption(String k, String eValue){
		this.key = k;
		this.exitValue = eValue;
	}
	/*

	public ExitOption(String k, String lbl, String eValue){
		this.key = k;
		//this.languageLabel = lbl;
		this.exitValue = eValue;
	}*/
	
	public boolean languageExists(String lg){
		if(this.exitMap==null||this.exitMap.isEmpty())			
			return false;
		
		if(this.exitMap.containsKey(lg))
			return true;
		
		// for lang + routing value
		String ke;
		for(FastMap.Entry<String, String> entry = this.exitMap.head(), end = this.exitMap.tail(); (entry = entry.getNext()) != end;) {
			ke = entry.getKey();
			if(ke.startsWith(lg+BaseConstants.UNDERSCORE)){
				return true;		
			}
		}
		
		return false;
	}
	
	public boolean hasSingleExit(){
		if(this.langCount>1){
			if(logger.isTraceEnabled())
				logger.trace("hasSingleExit: this.langCount: " + this.langCount + ", returning false");
			return false;
		}
		/*
\		//case no lang routing, only node routing
		if(this.exitMap==null||this.exitMap.isEmpty()){
			if(logger.isTraceEnabled())
				logger.trace("hasSingleExit: exit map is null or empty: " + this.exitMap + ", returning true");
			return true;
		}
		
		if(routingNodeID!=null&&this.exitMap.size()==1){
			if(logger.isTraceEnabled())
				logger.trace("hasSingleExit: routingNodeID!=null&&this.exitMap.size()==1: " + routingNodeID + ", returning true");
			return true;
		}*/
		
		if(logger.isTraceEnabled())
			logger.trace("hasSingleExit: else, returning true");
		return true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int eId) {
		this.id = eId;
	}
	
	/**
	 * Sets the key to look up the exit value
	 * @param s
	 */
	public void setKey(String s) {
		this.key = s;
	}
	
	/**
	 * Returns the key to look up the exit value
	 * @param s
	 */
	public String getKey() {
		return this.key;
	}
	/*
	public void setRoutingByLanguage(boolean b) {
		this.routingByLanguage = b;
	}
	
	public boolean getRoutingByLanguage() {
		return this.routingByLanguage;
	}	*/
	
	/*
	public void setLanguageLabel(String s) {
		this.languageLabel= s;
	}
	
	public String getLanguageLabel() {
		return this.languageLabel;
	}*/
	
	public void setLangCount(int c) {
		this.langCount = c;
	}
	
	public void incrementLangCount() {
		this.langCount++;
	}
	
	public int getLangCount() {
		return this.langCount;
	}	
	
	public void setGrammarTag(String s) {
		this.grammarTag= s;
	}
	
	public String getGrammarTag() {
		return this.grammarTag;
	}
	
	public void setRoutingNodeID(String s) {
		this.routingNodeID= s;
	}
	
	public String getRoutingNodeID() {
		return this.routingNodeID;
	}
	
	/**
	 * Sets next node ID
	 * @param s
	 */
	public void setExitValue(String s) {
		this.exitValue = s;
	}
	
	/**
	 * Returns next node ID
	 * @return
	 */
	public String getExitValue() {
		return this.exitValue;
	}
	
	public String getExitValueFromMap(String eKey) {
		return this.exitMap.get(eKey);
	}
	
	
	public FastMap<String,String> getExitMap() {
		return this.exitMap;
	}
	
	public void addExit(String ke, String val){
		if(ke==null)
			return;
		
		if(this.exitMap==null)
			exitMap = new FastMap<String, String>();
		exitMap.put(ke, val);
	}
	
	/**
	 * Returns destination node ID
	 * @param ke
	 * @param val
	 * @return
	 */
	public String getExit(String ke, String val){
		if(this.exitMap==null||ke==null)
			return null;

		return exitMap.get(ke);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("\nExit Option: ")
		.append(" key: ").append(this.key)	
		.append(", next: ").append(this.exitValue)
		.append(", langCount: ").append(this.langCount)
		;
		
		
		
		return sb.toString();
	}
	
	public String toFullString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("\nExit Option: ")
		.append(" key: ").append(this.key)	
		.append(", next: ").append(this.exitValue)
		.append(", langCount: ").append(this.langCount)
		.append(", routingNodeID: ").append(this.routingNodeID)
		.append(", exitMap: ").append(this.exitMap)
		;
		
		
		
		return sb.toString();
	}
}
