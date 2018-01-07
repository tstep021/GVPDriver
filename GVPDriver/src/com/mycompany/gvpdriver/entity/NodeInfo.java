package com.mycompany.gvpdriver.entity;

/** Copyright 2010-2013, mycompany. All rights reserved */

//import javolution.util.FastList;

//import java.io.Serializable;

import java.io.Serializable;
//import org.apache.log4j.Appender;
//import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
//import org.apache.log4j.*;
import java.util.Iterator;
//import java.util.Enumeration;

import javolution.util.FastMap;
import javolution.util.FastList;

import com.mycompany.gvpdriver.base.BaseConstants;
import com.mycompany.ecs.cdr.CDRConstants;

/** 
 *      
 *
 * @author  Tatiana Stepourska
 * @version 1.0
*/
public class NodeInfo implements //Cloneable, 
INodeInfo, Serializable
{ 	
	private static final long serialVersionUID = 1971458104208020126L;
	private static Logger logger = Logger.getLogger(NodeInfo.class);
	private static Logger historyLogger = Logger.getLogger(BaseConstants.KEY_CALL_HISTORY);
	
	private FastMap<String, String> properties 		= null;
	private FastMap<String, ExitOption> exitOptions = null;	
	private FastMap<String, FastMap<String, FastList<Audio>>> audioLangMap = null;
	private String executionResult					= null;
	private String resultKey						= null;
	private String id 								= null;
	private String name 							= null;
	private String type 							= null;
	private String url 								= null;
	private boolean first 							= false;
	private String spName 							= null;
	private StringBuffer history					= null;
	
	/** Holds the custom object for node specific session variables */
	private INodeSessionInfo nodeSessionInfo = null;
		
	/*public NodeInfo clone(){
		NodeInfo ni = new NodeInfo();
		ni.audioLangMap
	}*/
	
	public INodeSessionInfo getNodeSessionInfo() { 
		return this.nodeSessionInfo;
	}
	
	public void setNodeSessionInfo(INodeSessionInfo o) {
		this.nodeSessionInfo = o;
	}

	public boolean isFirst() { 
		return this.first;
	}
	
	public void setFirst(boolean s) {
		this.first = s;
	}

	public void setSpName(String s) {
		this.spName = s;
	}
	
	public String getSpName() {
		return this.spName;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public String getName() {
		return this.name;
	}
	
	public FastMap<String, FastMap<String, FastList<Audio>>> getAudioLangMap() {
		return this.audioLangMap;
	}
	
	public void setAudioLangMap(FastMap<String, FastMap<String, FastList<Audio>>> am) {
		this.audioLangMap = am;
	}
	
	public void removeAllAudios() {
		this.audioLangMap = null;
	}
	
	/**
	 * Removes all audios of specific type for all languages 
	 * from this node
	 * @param langArr
	 * @param tp
	 */
	public void removeAllAudiosForType(String[] langArr, String tp) {
		if(this.audioLangMap==null || this.audioLangMap.size()<=0){
			logger.trace("removeAllAudiosForType: audioLangMap is null or empty, nothing to remove");
			return;
		}
		if(langArr==null || tp==null || langArr.length<=0){
			logger.trace("removeAllAudiosForType: invalid parameters, nothing to remove: langArr: " + langArr + ", tp: " + tp);
			return;
		}
		
		FastMap<String, FastList<Audio>> audioTypeMap = null;
		FastList<Audio> audioList = null;
		
		for(int i=0;i<langArr.length;i++){
			audioTypeMap = this.audioLangMap.get(langArr[i]);
			logger.trace("removeAllAudiosForType: audioTypeMap for " + langArr[i] + ": " + audioTypeMap);
			if(audioTypeMap==null)
				continue;
			
			audioList = audioTypeMap.get(tp);
			logger.trace("removeAllAudiosForType: audioList for tp " + tp + ": " + audioList);
			if(audioList!=null)
				this.audioLangMap.get(langArr[i]).put(tp, null);
		}
	}

	public void addAudio(Audio a) throws Exception {
		FastMap<String, FastList<Audio>> audioTypeMap = null;
		FastList<Audio> audioList = null;
		String auType = null;
		String lang = null;
		//String fp = "addAudio:";
		if(this.audioLangMap==null){
			//logger.trace(fp+"audioLangMap is null, creating new one");
			this.audioLangMap=new FastMap<String, FastMap<String, FastList<Audio>>>();			
		}

		lang = a.getLanguage();
		//logger.trace(fp+"audio lang: " + lang);
		audioTypeMap = this.audioLangMap.get(lang);

		if(audioTypeMap==null){
			//logger.trace(fp+"audioTypeMap for lang " + lang + " is null, creating a new one");
			audioTypeMap = new FastMap<String, FastList<Audio>> ();
		}

		auType = a.getType();
		//logger.trace(fp+"audio type: " + type);
		audioList = audioTypeMap.get(auType);
		if(audioList==null){
			//logger.trace(fp+"audio list for type " + type + " is null, creating a new one");
			audioList = new FastList<Audio>();
		}
		//logger.trace(fp+"setting audio sequence : " + audioList.size());
		a.setSequenceNumber(audioList.size());
		audioList.add(a);
		//logger.trace(fp+"added audio to the list");
		audioTypeMap.put(auType,audioList);
		//logger.trace(fp+"put list into the type map");
		this.audioLangMap.put(lang, audioTypeMap);
		//logger.trace(fp+"put type map into the lang map");
	}
	
	public FastMap<String, String> getProperties() {
		return this.properties;
	}
	
	public void setProperties(FastMap<String, String> pp) {
		this.properties = pp;
	}
	
	public String getProperty(String s) {
		if(this.properties==null)
			return null;
		return this.properties.get(s);
	}
	
	public void setProperty(String key, String value) {
		if(key==null||value==null)
			return;
		
		if(this.properties==null){
			this.properties = new FastMap<String, String>();
		}
		this.properties.put(key, value);
	}
	
	
	public FastMap<String, ExitOption> getExitOptions() {
		return this.exitOptions;
	}
	
	public void setExitOptions(FastMap<String, ExitOption> eo) {
		this.exitOptions = eo;
	}
	
	public ExitOption getExitOption(String key) {
		if(this.exitOptions==null||this.exitOptions.isEmpty())
			return null;
		
		return this.exitOptions.get(key);
	}
	
	public void setExitOption(String key, ExitOption value) {
		if(this.exitOptions==null)
			this.exitOptions=new FastMap<String, ExitOption>();
		
		this.exitOptions.put(key, value);
	}

	public String getExecutionResult() {
		return this.executionResult;
	}
	
	public void setExecutionResult(String r) {
		this.executionResult = r;
	}
	
	public String getResultKey() {
		return this.resultKey;
	}
	
	public void setResultKey(String r) {
		this.resultKey = r;
	}

	/**
	 * Returns node Id unique per call
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Sets node id unique per call
	 * 
	 * @param nid
	 */
	public void setId(String nid) {
		this.id = nid;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String t) {
		this.type = t;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String s) {
		this.url = s;
	}
		
	public String toString() {		
		StringBuffer sb = new StringBuffer();
		sb
		.append("\n").append(this.type)
		.append(", isFirst:")
		.append(this.first)
		.append(", ID ").append(this.id)
		.append(", url: ").append(this.url)
		;
		
		if(this.audioLangMap!=null) {
			sb.append(this.audioLangMap.toText());
			 /*for (int i=0;i<this.audioList.length;i++) {
			     a = this.audioList[i];
			     if(a!=null)
			    	 sb.append(a.toString());
			 }*/
		}
		
		if(this.properties!=null) {
			sb.append("\nproperties: ");
			sb.append(this.properties.toText());
		/*	for (FastMap.Entry<String, String> e = this.properties.head(), 
					end = this.properties.tail(); 
					(e = e.getNext()) != end;) 
			{
		        key = e.getKey(); // No typecast necessary.
		        value = e.getValue(); // No typecast necessary.
		        sb.append("\n" + key + "=" + value);
		    }*/
		}
		
		if(this.exitOptions!=null) {
			sb.append("\nexit options: ");
			sb.append(this.exitOptions.toText());
			/*
			for (FastMap.Entry<String, ExitOption> e = this.exitOptions.head(), 
					end = this.exitOptions.tail(); 
					(e = e.getNext()) != end;) 
			{
		        key = e.getKey(); // No typecast necessary.
		        eoValue = e.getValue(); // No typecast necessary.
		        sb.append("\n" + key + "=" + eoValue);
		    }*/
		}
		sb.append("\n**************************************");
		;
		
		return sb.toString();
	}
	
	public String getHistory(){
		return this.history.toString();
	}
	
	public void startHistory(ICallInfo ci){
		
		try {
		this.history
		//.append("\n").append(ci.getCallID())
		.append(CDRConstants.HISTORY_TOKEN_NODE_START)
		.append("name=").append(this.name).append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM)
		.append("type=").append(this.type)//.append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM)
		//.append("body=")
		;
		}
		catch(Exception e){
			logger.error("Error starting history for node " + this.id);
		}
	}
	
	public void endHistory(ICallInfo ci){
		this.history
		.append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM)
		.append("result=")
		.append(CDRConstants.HISTORY_TOKEN_VALUELIST_OPEN)
		.append(this.executionResult)
		.append(CDRConstants.HISTORY_TOKEN_VALUELIST_CLOSE)
		.append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM)
		.append("key=").append(this.resultKey)
		;		

		//String logFileName = ci.getCallID()+".log";
		try {
		////////////////////////////////////////
	     // setting up a FileAppender dynamically...
	    /*  SimpleLayout layout = new SimpleLayout();    
	      FileAppender appender = new FileAppender(layout,logFileName,false); 
	      appender.setFile(logFileName);
	      appender.activateOptions();
	      
	      historyLogger.removeAllAppenders();
	      historyLogger.addAppender(appender);
	      
	      historyLogger.setLevel(Level.INFO);*/

	     /* logger.debug("Here is some DEBUG");
	      logger.info("Here is some INFO");
	      logger.warn("Here is some WARN");
	      logger.error("Here is some ERROR");
	      logger.fatal("Here is some FATAL");*/
		////////////////////////////////////
		
		
		/*
			//Logger rootLogger = Logger.getRootLogger();
			Enumeration<Appender> appenders = Logger.getRootLogger().getAllAppenders();
			FileAppender fa = null;
			while(appenders.hasMoreElements())	{
				Appender currAppender = appenders.nextElement();
				if(currAppender instanceof FileAppender)	{
					fa = (FileAppender) currAppender;
				}
			}
			if(fa != null)	{
				fa.setFile(logFileName);
				fa.activateOptions();
			}
			else{
				logger.info("No File Appender found");
			}*/
	
			//
/*		    Appender appender = historyLogger.getAppender(BaseConstants.KEY_CALL_HISTORY);
		    if (appender != null && appender instanceof org.apache.log4j.FileAppender) {
		        FileAppender roll = (FileAppender) appender;
		        roll.setFile(logName.toString());
		        roll.activateOptions();
		    }
		    //MDC.put(CDRConstants.CALL_ID_KEY, ci.getCallID());*/
			historyLogger.info(this.history.toString());
			ci.appendCallHistory(this.history.toString());
			//historyLogger.removeAllAppenders();
			/*MDC.remove(CDRConstants.CALL_ID_KEY);
			 * 
			 */
		}
		catch (Exception e) {
		    logger.error("Error setting up history logger: " + e.getMessage());
		}
		/////////////////////////////////////
		
		
		this.history = new StringBuffer();
	}
	
	public void appendHistory(String s){
		if(s!=null){
			try {
			this.history.append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM).append(s);
			}
			catch(Exception e){
				logger.error("appendHistory: failed to append node history: " + e.getMessage());
			}
		}
	}
	
	public void appendAudiosToHistory(FastList<Audio> aa){
		if(aa!=null){
			try {
				int idx = "/playback".length();
				this.history
				.append(CDRConstants.HISTORY_TOKEN_UNIT_DELIM)
				.append("audios=")
				.append(CDRConstants.HISTORY_TOKEN_VALUELIST_OPEN);
				
				Iterator<Audio> it = aa.iterator();
				while(it.hasNext()){
					
					this.history.append((it.next()).toString().substring(idx)).append(BaseConstants.LIST_SEPARATOR);
				}
				
				this.history.append(CDRConstants.HISTORY_TOKEN_VALUELIST_CLOSE);
			}
			catch(Exception e){}
		}
	}
	
	/**
	 * Returns full history of execution for the specific node
	 * @param ci
	 * @return
	 */
/*	public String toHistory(ICallInfo ci){
		StringBuffer sb = new StringBuffer();
		sb.append("\n").append(ci.getCallID())
		.append("@id=").append(this.id).append(",")
		.append("type=").append(this.type).append(",")
		.append("history=").append(this.history).append(",")
		.append("result=").append(this.executionResult).append(",")
		.append("key=").append(this.resultKey)
		;

		return sb.toString();
	}*/
	
	public NodeInfo() {
		super();
		this.history = new StringBuffer();
	}
 }//end of class