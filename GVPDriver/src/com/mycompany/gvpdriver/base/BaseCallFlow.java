package com.mycompany.gvpdriver.base;

/** copyright 2009-2012, mycompany  */

//import javolution.util.FastMap;
//import org.apache.log4j.Logger;

//import com.mycompany.ecs.cdr.CDRConstants;
import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.entity.*;
import com.mycompany.vxml.facade.*;

/** 
 * File         BaseCallFlow.java
 * 
 * Description  
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
public abstract class BaseCallFlow extends BaseServletExtension
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BaseCallFlow.class);

	/**
	 * Creates document footer which includes all mandatory 
	 * error handling
	 * 
	 * @param vxml
	 * @return
	 */
	public final VXML addDocumentFooter(VXML vxml, ICallInfo ci) throws Exception {			
		//log.trace("Adding document footer");
		vxml.VxmlEnd();
		
		return vxml;
	}

	/**
	 * Called from the node to generate vxml when enabled
	 * 
	 * @param vxml
	 */
	public void operator(VXML vxml){
		vxml.CatchStart(VXMLEvents.EVENT_OPERATOR);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_OPERATOR+"'");
		if(BaseGlobalConfig.debug) {
			vxml.Text("caught document event operator in base call flow, redirecting to global event handler");
		}
		vxml.Submit(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKIN, null, BaseConstants.EVENT_KEY + " " + BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}
	
	/**
	 * Called from the node to generate vxml when enabled
	 * 
	 * @param vxml
	 */
	public void goback(VXML vxml){
		vxml.CatchStart(VXMLEvents.EVENT_GOBACK);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_GOBACK+"'");
		if(BaseGlobalConfig.debug) {
			vxml.Text("caught document event goback in base call flow, redirecting to global event handler");
		}
		vxml.Submit(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKIN, null, BaseConstants.EVENT_KEY + " " + BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}
	
	public FastList<Audio> buildAudioList(FastMap<String, FastMap<String,FastList<Audio>>> audioLangMap,
			String[] langs,
			boolean langSelected,
			String langCode,
			String promptType
			) {
		FastList<Audio> audios 		= null;
		FastList<Audio> audioList 	= new FastList<Audio>();
		FastMap<String, FastList<Audio>> audioTypeMap = null;
		Audio a = null;

		//all messages for this promptType in the current language only
		if(langSelected){
			//get types map for current language
			audioTypeMap = audioLangMap.get(langCode);
			if(log.isTraceEnabled())
			log.trace("got audioTypeMap for "+ langCode);//+": " + audioTypeMap);
			if(audioTypeMap==null)
				return null;
				
			//get list for prompt type for selected lang
			audioList = audioTypeMap.get(promptType);
			if(log.isTraceEnabled())
			log.trace("got audioList for "+ promptType);// + ": " + audioList);
				
			if(audioList==null||audioList.size()<=0)
				return null;
				
			return audioList;
		}
		//language is not selected, must play one audio at a time in every language 
		int size = 0;
		int tmpSize = 0;
			
		//find out list sizes for this prompt type in each language and set the largest
		for(int k=0;k<langs.length;k++) {
			//get types map for current language
			audioTypeMap = audioLangMap.get(langs[k]);
			if(audioTypeMap==null)
				continue;
				
			//get list for prompt type 
			audioList = audioTypeMap.get(promptType);
			if(audioList==null)
				continue;
				
			tmpSize = audioList.size();
			//set size to the larger value
			if(tmpSize>size)
				size = tmpSize;
		}
		if(size<=0)
			return null;
		
		audios = new FastList<Audio>();
		
		//loop over the larger size list
		for(int j=0;j<size;j++){
			//play one audio at a time in each language 
			for(int k=0;k<langs.length;k++) {
				//reset audio value
				a = null;
				//get types map for language: k=0 is the current session language
				audioTypeMap = audioLangMap.get(langs[k]);
				if(log.isTraceEnabled())
					log.trace("got audioTypeMap for "+ langs[k]);//+": " + audioTypeMap);		
				if(audioTypeMap==null)
					continue;
				
				//get list for prompt type 
				audioList = audioTypeMap.get(promptType);
				if(log.isTraceEnabled())
					log.trace("got audioList for "+ promptType);// + ": " + audioList);
				if(audioList==null)
					continue;
				
				try {
					a = audioList.get(j);
				}
				catch(Exception e){
					a = null;	
				}
				if(a!=null)
					audios.add(a);
			}
		}

		if(audios.size()<=0)
			return null;
		
		return audios;
	}

}//end of class