package com.mycompany.gvpdriver.entity;

//import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.ecs.cdr.CDR;

public class CallInfoBase  extends CDR implements ICallInfo {

	private static final long serialVersionUID = -1160223153276772434L;
	private static final Logger lo = Logger.getLogger(CallInfoBase.class);
	private long backUpDnis 					= 0;
	private FastMap<String, NodeInfo> callflow 	= null;
	private int countAttempts 					= 0;
	private NodeInfo currentNode 				= null;
	private String event						= null;
	private String firstNodeID 					= null;
	private String globalNodeID					= null;
	private FastList<NodeInfo> gobackHistory 	= null;
	private String grammarpath 					= null;
	private String[] langArray					= null;
	/** Variable indicates which languages might participate in the session */
	private String langConfig  					= null;
	private String langMode             		= BaseConstants.DEFAULT_LANGUAGE_MODE;
	/** 
	 * Used for playback of the prompts, mainly wihtin the simplemessage 
	 * Only if app is bilingual AND language has been selected by the 
	 * caller, flag is flipped to true => prompts are played in 
	 * the selected language. Otherwise, when flag is false,
	 * prompts are played in all languages and in the order provided 
	 * in the langConfig
	 */
	private boolean langSelected  				= false;	
	private int maxnoinput 						= BaseGlobalConfig.maxnoinput;
	private int maxnomatch 						= BaseGlobalConfig.maxnomatch;
	private int maxtries	    				= BaseGlobalConfig.maxtries;
	private String[] mdcKeys	= null;
	private String[] mdcValues	= null;
	/**
	 * Structure that keeps information updated by nodes 
	 * for other nodes to use. This information is not available 
	 * when call starts, but is set based on a caller chices or other 
	 * 
	 */
	private FastMap<String, String> messageBoard 	= null;
	private String promptpath  					= null;
	//used for call parking
	private HashMap<String, String> sipInfo = null;
	
	public CallInfoBase() {
		super(); 
		this.gobackHistory = new FastList<NodeInfo>();
	}
	
	public long getBackUpDnis() {
		return this.backUpDnis;
	}

	public void setBackUpDnis(long b) {
		this.backUpDnis = b;
	}
	
	public void setCallflow(FastMap<String, NodeInfo> cf) {
		this.callflow = cf;
	}
	
	public FastMap<String, NodeInfo> getCallflow() {
		return this.callflow;
	}	

	public void incrementCountAttempts() {
		countAttempts++;
	}

	public int getCountAttempts() {
		return this.countAttempts;
	}

	public void resetCountAttempts() {
		this.countAttempts = 0;
	}

	public void setCurrentNode(NodeInfo fi) {
		this.currentNode = fi;
	}
	
	public NodeInfo getCurrentNode() {
		return this.currentNode;
	}
	
	public String getEvent(){
		return this.event;
	}
	
	public void setEvent(String e){
		this.event = e;
	}

	public String getFirstNodeID(){
		return this.firstNodeID;
	}
	
	public void setFirstNodeID(String s) {
		this.firstNodeID = s;
	}
 
	public void setGlobalEventExitOptions(FastMap<String, ExitOption> m) throws Exception  {
		//this.globalEventsExitOptions = m;
		this.callflow.get(this.getGlobalNodeID()).setExitOptions(m);
	}
	
	public FastMap<String, ExitOption> getGlobalEventExitOptions()  throws Exception {
		//return this.globalEventsExitOptions;
		return this.callflow.get(this.getGlobalNodeID()).getExitOptions();
	}

	public void setGlobalPrompts(FastMap<String, FastMap<String, FastList<Audio>>> am) 
	throws Exception {
		this.callflow.get(this.getGlobalNodeID()).setAudioLangMap(am);
	}

	public FastMap<String, FastMap<String, FastList<Audio>>> getGlobalPrompts() 
	throws Exception {
		return this.callflow.get(this.getGlobalNodeID()).getAudioLangMap();
	}
	
	public void setGlobalNodeID(String s) {
		this.globalNodeID = s;
	}
	
	public String getGlobalNodeID() {
		return this.globalNodeID;
	}
		
	/**
	 * Sets global properties to global node
	 * !Use with caution, not to override properties loaded from database!
	 */
	public void setGlobalProperties(FastMap<String, String> m) throws Exception {
		this.callflow.get(this.getGlobalNodeID()).setProperties(m);
	}
	
	/**
	 * Returns properties of the global node
	 */
	public FastMap<String, String> getGlobalProperties() throws Exception {
		return this.callflow.get(this.getGlobalNodeID()).getProperties();
	}
	
	/**
	 * Sets global property to global node
	 * !Use with caution, not to override property loaded from database!
	 */
	public void setGlobalProperty(String key, String value)  throws Exception {
		//this.globalProperties = m;
		this.callflow.get(this.getGlobalNodeID()).setProperty(key, value);
	}
	
	/**
	 * Returns properties of the global node
	 */
	public String getGlobalProperty(String key)  throws Exception {
		return this.callflow.get(this.getGlobalNodeID()).getProperty(key);
	}

	public FastList<NodeInfo> getGobackHistory(){
		return this.gobackHistory;
	}
	
	public void setGobackHistory(FastList<NodeInfo> li){
		this.gobackHistory = li;
	}
	
	/**
	 * Adds the last visited input node to the goback 
	 * history, keeps the size up to max
	 * @param n
	 */
	public void addGobackHistoryNode(NodeInfo n) {
		this.gobackHistory.add(n);
		
		//if(this.gobackHistory.size()>AppBaseConstants.MAX_CALL_HISTORY)
		//	this.gobackHistory.removeFirst();
		
		//return this.gobackHistory.size();
	}
	
	public boolean removeLastGobackHistoryNode(){
		if(this.gobackHistory.size()<=0)
			return false;
		
		try {
			this.gobackHistory.removeLast();
			return true;
		}
		catch(Exception e){
			lo.error("Error removing last history item: " + e.getMessage());
			return false;
		}
	}
		
	public NodeInfo getGobackNode(){
		NodeInfo gobackNode = null;
		try {
			gobackNode = this.gobackHistory.getLast();
		}
		catch(Exception e){
			lo.error("Error getting last history node: " + e.getMessage());
			return null;
		}
		return gobackNode;
	}

	/**
	* Sets the grammarpath for the current profile   
	* 
	* @param gp
	*/
	public void setGrammarpath(String gp) {
		this.grammarpath = gp;
	}  

	/**
	* Returns the grammarpath for the current profile
	* 
	* @return  String  grammarpath
	*/
	public String getGrammarpath() {
		return this.grammarpath;
	}
	
	public CallInfoBase getInstance(){
		//CallInfoBase.class
		return this;
	}
	
	/**
	 * Get the languages in order
	 * 
	 * @return int langArray
	 */
	 public String[] getLangArray()  {
		 return this.langArray;
	 }  

	 /**
	  * Set the languages in order
	  *    	   
	  * @param  lg
	  */
	 public void setLangArray(String[] lg)  {
		 this.langArray = lg;
	 }	  	
	 
	 /**
	  * Set the langConfig property
	  *    	   
	  * @param  lg
	  */
	 public void setLangConfig(String lg)  {
		 this.langConfig = lg;
	 }	  	

	/**
	* Get the language config property
	* 
	* @return int langConfig
	*/
	public String getLangConfig()  {
		return this.langConfig;
	}  
	
	/**
	 * Sets all properties that rely on a language. Either 
	 * langCfg or currentLanguage has to be not null
	 * Called from CallStart (langCfg is not null, currentLang==null),
	 *  or from language selection node (langCfg==null, currentLang is not null)
	 * 
	 * @param langCfg
	 */
	public void setLanguageFromConfig(String langCfg) {
		String fp = "setLanguageFromConfig: ";
		
		lo.info(fp + "langCfg: " + langCfg);
		lo.info(fp + "this.langConfig: " + this.langConfig);
		
		try {
			
		if(langCfg!=null && langCfg.trim().length()>0) {
			this.langConfig = langCfg;
			//if(lo.isDebugEnabled())
			//lo.debug(fp + "reset langConfig from " + this.langConfig + " to " + langCfg);
		
			int index = langCfg.indexOf(',');
			if(lo.isDebugEnabled())
			lo.debug(fp + "index of comma in langConfig: " + index);
		
			if(index>0) {
			//bilingual, needs caller language selection
			//setting initial properties for the first language in a list to start with
			try {
				String lgCode = langCfg.substring(0,index);
				if(lo.isDebugEnabled())
				lo.debug(fp + "lgCode: " + lgCode);
				if(lgCode==null || lgCode.trim().length()<=0) {
					lgCode = "en";
				}
				this.setLangCode(lgCode);
				if(lo.isDebugEnabled())
				lo.debug(fp + "bilingual: Set this.langCode to lgCode: " + this.getLangCode());
			}
			catch(Exception ex) {
				lo.error(fp + "Error parsing language config: " + ex.getMessage());
			}	
			
			//setting array of languages
			try {
				this.langArray 	= this.langConfig.split(BaseConstants.LIST_SEPARATOR);
			}
			catch(Exception ex) {
				lo.error(fp + "Error setting langArray: " + ex.getMessage());
			}
			
			}
			else {
				//unilingual
				this.setLangCode(langCfg);
				this.langArray = new String[] {this.getLangCode()};
				//lo.trace(fp + "unilingual: set this.langCode to langCfg: " + this.getLangCode());
			}
			
			this.setLanguageProperties(this.getLangCode());
		}		
		}
		catch(Exception e) {
			lo.error(fp + "Error parsing languages: " + e.getMessage());			
		}
	}
	
	/**
	* Gets the language mode for grammars 
	* 
	* @return String
	*/
	public String getLangMode() {
		return this.langMode;
	}  

	/**
	* Sets the language code for grammars
	*    	   
	* @param lg
	*/
	public void setLangMode(String lg) {
		this.langMode = lg;
	}		
	
	/**
	 * Sets all properties that rely on a language. Either 
	 * langCfg or currentLanguage has to be not null
	 * Called from CallStart (langCfg is not null, currentLang==null),
	 *  or from language selection node (langCfg==null, currentLang is not null)
	 * 
	 * @param lg
	 */
	public void setLanguageProperties(String languageToSet) {
		String fp = "setLanguageProperties: ";
		String localLangC = null;
		
		lo.info(fp + "languageToSet: " + languageToSet);
		
		//GlobalConfig.langPropMap = new FastMap<String, String>();
		
		try {		
			if(languageToSet==null || languageToSet.trim().length()<=0) {
				languageToSet = "en";
			}
			this.setLangCode(languageToSet);
			
			localLangC = this.getLangCode();
			//if(lo.isDebugEnabled())
			//lo.debug(fp + "Set this.langCode to languageToSet: localLangC: " + localLangC);			
		
			//set session properties from the property map by language
			String languageMode = BaseGlobalConfig.langPropMap.get(BaseConstants.LANG_MODE__KEY 	+ localLangC);
			//if(lo.isDebugEnabled())
			//lo.debug(fp + "languageMode : " + languageMode);
			if(languageMode!=null)
				this.setLangMode(languageMode);
			//if(lo.isTraceEnabled())
			lo.info(fp + "set langMode to " + this.getLangMode());
		}
		catch(Exception e) {
			lo.error("Error setting language properties: " + e.getMessage());
			/*
StackTraceElement[] trace = e.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					lo.error(trace[y]);
				}
			}*/
		}
	}
 	
	/**
	* Returns the langSelected property
	*  
	* @return boolean
	*/
	public boolean isLangSelected() {
		return this.langSelected;
	}  

	/**
	* Sets the langSelected
	*    	   
	* @param lg
	*/
	public void setLangSelected(boolean lg) {
		this.langSelected = lg;
	}	

	/**
	 * Sets max number of tries on no input
	 * 
	 * @param c
	 */
	public void setMaxnoinput(int c)	{
		this.maxnoinput = c;
	}
	
	/**
	 * Gets max number of tries on noinput
	 * 
	 * @return int
	 */
	public int getMaxnoinput()	{
		return this.maxnoinput;
	}	

	/**
	 * Sets max number of tries on nomatch
	 * 
	 * @param c
	 */
	public void setMaxnomatch(int c){
		this.maxnomatch = c;
	}

	/**
	 * Gets max number of tries on nomatch
	 * 
	 * @return int
	 */
	public int getMaxnomatch()	{
		return this.maxnomatch;
	}	


	/**
	 * Sets max number of tries on error
	 * 
	 * @param c
	 */
	public void setMaxtries(int c)	{
		this.maxtries = c;
	}

	/**
	 * Returns max number of tries on error
	 * 
	 * @return int
	 */
	public int getMaxtries()	{
		return this.maxtries;
	}	
	
	public String[] getMDCKeys(){
		return this.mdcKeys;
	}
	public String[] getMDCValues(){
		return this.mdcValues;
	}
	public void setMDCKeys(String[] kk){
		this.mdcKeys = kk;
	}
	public void setMDCValues(String[] vv){
		this.mdcValues = vv;
	}
	
	/**
	 * Adds the key / value pair, or, if the key already exists,
	 * overrides it
	 * @param n
	 */
	public void addMessage(String key, String value) {
		if(this.messageBoard==null )
			this.messageBoard = new FastMap<String, String>();
		
		try{
			this.messageBoard.put(key, value);
		}
		catch(Exception e){
			lo.error("Error adding message item "+key+" with value "+value+": " + e.getMessage());
		}
	}
	
	/**
	 * Removes the value for specified key from the messageBoard
	 * @param key
	 */
	public void removeMessage(String key){
		if(this.messageBoard==null || this.messageBoard.size()<=0)
			return;
		
		try {
			this.messageBoard.remove(key);
		}
		catch(Exception e){
			lo.error("Error removing message item "+key+": " + e.getMessage());
		}
	}
	
	public String getMessage(String key){
		String msg = null;
		if(this.messageBoard==null || this.messageBoard.size()<=0)
			return null;
		
		try {
			msg = this.messageBoard.get(key);
		}
		catch(Exception e){
			lo.error("Error removing message item "+key+": " + e.getMessage());
			msg = null;
		}
		return msg;
	}
	
	public FastMap<String, String> getMessageBoard(){
		return this.messageBoard;
	}
	
	public void setMessageBoard(FastMap<String, String> m){
		this.messageBoard = m;
	}
 
	public NodeInfo getNode(String id) throws Exception {
    	if(id==null)
    		return null;

    	return this.callflow.get(id);
	}

	/**
	* Returns the promptpath for the current profile
	* 
	* @return  String  inputmodes
	*/
	public String getPromptpath() {
		return this.promptpath;
	}	

	/**
	 * Sets the promptpath for the current profile   
	 * 
	 * @param pp
	 */
	public void setPromptpath(String pp) {
		this.promptpath = pp;
	}  
	
	public HashMap<String, String> getSipInfo() {
		return this.sipInfo;
	}

	public void setSipInfo(HashMap<String, String> m) {
		this.sipInfo = m;
	}

	public String toHeaderString() {
		//String result;  //super.toString();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append('\n').append(" ==== Call Info Header ========== ");
		
		sb.append("\nDNIS	 		: ").append(this.getDNIS());
		sb.append("\nCLID	 		: ").append(this.getCLID());
		sb.append("\nserviceID 		: ").append(this.getServiceID());
		sb.append("\npromptpath  	: ").append(promptpath);	
		if(this.grammarpath!=null)
		sb.append("\ngrammarpath	: ").append(this.grammarpath);
		sb.append("\nlangMode		: ").append(langMode);
		sb.append("\nlangCode		: ").append(this.getLangCode());
		sb.append("\nlangConfig		: ").append(langConfig);
		sb.append("\nfirstNodeID	: ").append(this.getFirstNodeID());
		sb.append("\nmaxtries		: ").append(this.getMaxtries());
		sb.append("\nmaxnoinput		: ").append(this.getMaxnoinput());
		sb.append("\nmaxnomatch		: ").append(this.getMaxnomatch());
		
		return sb.toString();
	}
	
	public String toStringShort() {
		return super.toStringShort();
	}
	
	public String toString() {
		String result = super.toString();
		
		StringBuffer sb = new StringBuffer(result);
		
		sb.append('\n').append(" ==== Call Info =================== ");
		
		//if( callState!=null)
		//sb.append("\ncallState		: " + callState);	
		//sb.append("\ncountRetry		: " + countRetry);
		//sb.append("\ncurrentNode	: ").append(currentNode);
		sb.append("\nserviceID 		: ").append(this.getServiceID());
		sb.append("\npromptpath  	: ").append(promptpath);	
		sb.append("\ngrammarpath	: ").append(this.getGrammarpath());
		sb.append("\nlangMode		: ").append(langMode);
		sb.append("\nlangCode		: ").append(this.getLangCode());
		sb.append("\nlangConfig		: ").append(langConfig);
		sb.append("\nfirstNodeID	: ").append(this.getFirstNodeID());
		sb.append("\nmaxtries		: ").append(this.getMaxtries());
		sb.append("\nmaxnoinput		: ").append(this.getMaxnoinput());
		sb.append("\nmaxnomatch		: ").append(this.getMaxnomatch());
		
		//sb.append("\nsubmitBase  	: " + submitBase);
		
		if(callflow !=null){
		sb.append('\n').append("Callflow: ");
		for (FastMap.Entry<String, NodeInfo> e = callflow.head(), end = callflow.tail(); (e = e.getNext()) != end;) {
			sb.append('\n').append("callflow entry: " + e.toText());
		}
		}
		
		return result + sb.toString();
	}

}
