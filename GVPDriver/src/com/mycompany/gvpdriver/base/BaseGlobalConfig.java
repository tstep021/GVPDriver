package com.mycompany.gvpdriver.base;

/** Copyright 2012-2013, mycompany. All rights reserved */

import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import javolution.util.FastMap;

import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.vxml.facade.VXMLProperties;

/** 
 * This is a class for keeping global application defaults.     
 *
 * @author  Tatiana Stepourska
 * @version 1.0
*/
public abstract class BaseGlobalConfig
{ 	
	private static final Logger log = Logger.getLogger(BaseGlobalConfig.class);
	public static FastMap<String,String> globalConfigMap = null;
	//public static FastMap<String,Audio> globalMessageMap = null;
	public static FastMap<String, String> langPropMap 	= null;
	public static String[] languages 					= null;
	
	public static String audio_repository_url			= "/GLOBAL_RESOURCES/";	
	public static String connecttimeout					= "30s";
	public static String connectwhen					= "immediate";
	public static boolean debug							= false;
	public static String interdigittimeout				= BaseConstants.DEFAULT_INTERDIGITTIMEOUT;
	public static String lang_config					= "en";
	public static String lang_mode						= "en-US";
	public static boolean loadtest               		= false;
	public static String mail_from						= null;
	public static String mail_smtp_host					= null;
	public static int    maxnbest 						= BaseConstants.DEFAULT_MAXNBEST;	
	public static String maintainer						= null;
	public static int    maxtries						= BaseConstants.DEFAULT_MAXTRIES;
	public static int    maxnoinput						= 1;
	public static int    maxnomatch						= BaseConstants.DEFAULT_MAXTRIES;
	public static String npa_loc						= null;
	public static String resource_repository_basepath 	= "/mycompanyapps";
	public static String resource_repository_url 		= "/GLOBAL_RESOURCES/";
	public static boolean test               			= false;
	public static String vxml_version					= BaseConstants.VXML_VERSION;

	/*--- run time application globals */	
	public static String apppath		= "";
	public static String cdr_config		= resource_repository_basepath + resource_repository_url + "config/db/dbConfig.txt";
	public static String fetchaudio		= resource_repository_url + "prompts/system/fetchaudio.wav";
	public static String transferaudio	= resource_repository_url + "prompts/system/onhold.wav";
	public static String submitbase		= "";
	public static String grammarbase 	= resource_repository_url + BaseConstants.DIR_GRAMMAR;
	public static String promptbase 	= audio_repository_url + BaseConstants.DIR_PROMPT;
	/*--- end of run time application globals */
	
	
	/**
	 * Initializes GlobalConfig object 
	 * 
	 * @param props
	 */
	public static void load(Properties props, String ctxName) throws Exception {

		String fp 				= "loadGlobalConfig: ";
		String key 				= null;
		String value 			= null;
		int iTmp				= 0;
		
		log.info(fp + "Started global configuration load");
		apppath 	= "/" + ctxName + "/";
		submitbase  = apppath + "servlet/";
		//log.debug(fp + "IGlobalConfig.submitbase: " + GlobalConfigBase.submitbase);
		
		globalConfigMap = new FastMap<String,String>();
		//globalMessageMap = new FastMap<String,Audio>();
		
		Enumeration<Object> en = props.keys();
			
		while(en.hasMoreElements()) {
			key = (String)en.nextElement();
			value = props.getProperty(key);
			//if(log.isTraceEnabled())
			//	log.trace(fp + "key: " + key + ", value:" + value);
			
			try {
				/*
				* if(key.compareTo(IBaseConstants.CONFIDENCE_NBEST_MIN_KEY + "_" + GlobalConfig.languages[i])==0){
				GlobalConfig.langPropMap.put(IBaseConstants.CONFIDENCE_NBEST_MIN_KEY + "_" + GlobalConfig.languages[i],
									value); 
				}
				if(key.compareTo(ICommonBaseConstants.CONFIDENCE_CONFIRM_KEY + "_" + GlobalConfig.languages[i])==0)	{
					GlobalConfig.langPropMap.put(ICommonBaseConstants.CONFIDENCE_CONFIRM_KEY + "_" + GlobalConfig.languages[i],
									value); 
				}
				*/
				if(key.compareTo(BaseConstants.RESOURCE_REPOSITORY_BASEPATH_KEY)==0)	{
					resource_repository_basepath = value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.AUDIO_REPOSITORY_URL_KEY)==0){	
					audio_repository_url		= value; 
					props.remove(key);
				}
				else if(key.compareTo(CDRConstants.KEY_CDR_CONFIG)==0){
					log.info(fp + "setting cdr_config to " + value);
					cdr_config			= value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.DEBUG_KEY)==0)	{
					iTmp = 0;
					try {
					iTmp = Integer.parseInt(value);
					props.remove(key);
					}
					catch(Exception e) {}
					
					if(iTmp==1)
						debug = true;
				}
				else if(key.compareTo(VXMLProperties.PROP_INTERDIGITTIMEOUT)==0)	{
					interdigittimeout	= value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.RESOURCE_REPOSITORY_URL_KEY)==0)	{
					resource_repository_url		= value;		
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.KEY_LANG_CONFIG)==0){
					lang_config = value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.LOADTEST_KEY)==0)	{	
					try{
					iTmp = Integer.parseInt(value);
					if(iTmp==BaseConstants.ON)
						loadtest	= true; 
					else
						loadtest	= false;
					
					props.remove(key);
					}
					catch(Exception e){
						
					}
				}
				else if(key.compareTo(BaseConstants.KEY_TEST)==0)	{	
					try{
					iTmp = Integer.parseInt(value);
					if(iTmp==BaseConstants.ON)
						test	= true; 
					else
						test	= false;
					
					props.remove(key);
					}
					catch(Exception e){
						
					}
				}
				else if	(key.compareTo(VXMLProperties.ATTR_CONNECTTIMEOUT)==0)	{
					connecttimeout			= value;
					props.remove(key);
				}
				else if	(key.compareTo(VXMLProperties.ATTR_CONNECTWHEN)==0)	{
					connectwhen				= value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.KEY_MAIL_FROM)==0)	{
					mail_from				= value; 
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.KEY_MAIL_SMTP_HOST)==0)	{
					mail_smtp_host			= value; 
					props.remove(key);
				}
				else if(key.compareTo(VXMLProperties.PROP_MAINTAINER)==0)	{					
					maintainer = value; 		
					props.remove(key);
				}
				else if(key.compareTo(VXMLProperties.PROP_MAXNBEST)==0)	{					
					iTmp = Integer.parseInt(value);
					maxnbest = iTmp; 			
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.KEY_MAXTRIES)==0)	{			
					iTmp = Integer.parseInt(value);
					maxtries	= iTmp; 			
					props.remove(key);
				}
				else if	(key.compareTo(BaseConstants.MAXNOINPUT_KEY)==0)	{
					iTmp = Integer.parseInt(value);
					maxnoinput			= iTmp;
					props.remove(key);
				}
				else if	(key.compareTo(BaseConstants.MAXNOMATCH_KEY)==0)	{
					iTmp = Integer.parseInt(value);
					maxnomatch			= iTmp;
					props.remove(key);
				}
				else if	(key.compareTo(BaseConstants.NPA_LOC_KEY)==0){	
					npa_loc	= value;
					props.remove(key);
				}
				else if(key.compareTo(BaseConstants.KEY_VXML_VERSION)==0){
					try{
					vxml_version			= value.trim(); 
					props.remove(key);
					}
					catch(Exception e){
						
					}
				}
				else
					globalConfigMap.put(key, value);
			} catch(Exception e){
				log.error(fp + "Error getting property " + key + ": " + e.getMessage());
			}				
		}	
		
		//reset grammar and prompt paths without language and service ID to global resources
		// to a single string
		grammarbase = new String(resource_repository_url + BaseConstants.DIR_GRAMMAR);
		promptbase  = new String(audio_repository_url);
		//log.info(fp + "grammarbase: " + GlobalConfig.grammarbase);
		//log.info(fp + "promptbase: " + GlobalConfig.promptbase);
		
		initializeSupportedLanguages(props);

		log.info(fp + "Global configuration load completed:");
		log.info(fp + printGlobalConfig());
	}	
	
	public static void initAppSpecificLanguages() throws Exception {
		//no app specific in this class
		//subclass and overwrite this method to initialize array of languages
		throw new Exception();
	}
	
	/**
	 * Initializes language dependent global properties.
	 * 
	 * @param props
	 * @throws Exception
	 */
	private static final void initializeSupportedLanguages(Properties props) throws Exception {
		String fp = "initializedSupportedLanguages: ";
		String key 				= null;
		String value 			= null;

		try {
			initAppSpecificLanguages();
		}
		catch(Exception e){
			//no app specific initialization provided, continue with generic
			log.info(fp + "initializing default languages");
			languages = new String[] {"en", "fr"};
		}

		int numberOfLanguages = languages.length;
		log.debug(fp + "number of supported languages is " + numberOfLanguages);
		
		langPropMap = new FastMap<String, String>();
		log.debug(fp + "GlobalConfig.langPropMap created");
	
		///////////////////////////////////////////////////////////////////
		/// Speech Recognition confidence related language specific properties
		///////////////////////////////////////////////////////////////////
		/*
		for(int i=0;i<numberOfLanguages;i++) {
			try	{
				
			}
			catch(Exception e)	{
				//log.info("Error parsing confidence_confirm_en: " + e.getMessage());
			}			
		}
		*/
		////////////////////////////////////////////////////////////
		////// end of language specific speech rec confidence props
		////////////////////////////////////////////////////////////////

		//put ttsengines
	/*	for(int i=0;i<numberOfLanguages;i++) {
			try	{
				//key = AppConstants.TTSENGINE_PREFIX_KEY + languages[i];
				key = VXMLProperties.PROP_COM_GENESYSLAB_TTSENGINE + AppConstants.UNDERSCORE + languages[i];
				//log.trace(fp + "key: " + key);
				
				if(key!=null) {
				value = props.getProperty(key);
				if(log.isDebugEnabled()) {
				log.debug(fp + ": " + key + "=" + value);
				}
				if(value!=null)	{
					langPropMap.put(key,value); 
					props.remove(key);
					globalConfigMap.remove(key);
				}
				}
			}
			catch(Exception e)	{
				log.error(fp + "Could not parse " + VXMLProperties.PROP_COM_GENESYSLAB_TTSENGINE + AppConstants.UNDERSCORE + languages[i] + ": " + e.getMessage());
			}
			
			try	{
				//key = AppConstants.TTSENGINE_PREFIX_KEY + languages[i];
				key = VXMLProperties.PROP_VOICE + AppConstants.UNDERSCORE + languages[i];
				//log.trace(fp + "key: " + key);
				if(key!=null) {
				value = props.getProperty(key);
				if(log.isDebugEnabled()){
				log.debug(fp + ": " + key + "=" + value);
				}
				if(value!=null)	{
					langPropMap.put(key,value); 
					props.remove(key);
					globalConfigMap.remove(key);
				}
				}
			}
			catch(Exception e)	{
				log.error(fp + "Could not parse " + VXMLProperties.PROP_VOICE + AppConstants.UNDERSCORE + languages[i] + ": " + e.getMessage());
			}
		}		*/	
		
		//lang modes for vxml docs and grammars
		for(int i=0;i<numberOfLanguages;i++) {
			try	{
				key = BaseConstants.LANG_MODE__KEY + languages[i];
				//log.trace(fp + "key: " + key);
				value = props.getProperty(key);
				if(log.isDebugEnabled()){
				log.debug(fp + ": " + key + "=" + value);
				}
				if(value!=null)	{
					langPropMap.put(key, value); 
					props.remove(key);
					globalConfigMap.remove(key);
				}
				///else {
				//	log.info(fp + "Could not parse " + IConstants.LANG_MODE__KEY + GlobalConfig.languages[i] + ", using default en-US");
				//	GlobalConfig.langPropMap.put(IConstants.LANG_MODE__KEY + GlobalConfig.languages[i], ICommonConstants.ENGLISH_MODE);
				//}
			}
			catch(Exception e)	{
				log.error(fp + "Could not parse " + BaseConstants.LANG_MODE__KEY + languages[i] + ": " + e.getMessage() + ", using default en-US");
				langPropMap.put(BaseConstants.LANG_MODE__KEY + languages[i], BaseConstants.DEFAULT_LANGUAGE_MODE);
			}
		}
		log.info(fp + "languagePropertyMap loaded: " + langPropMap);
	}

	public static String printGlobalConfig(){
		StringBuffer sb = new StringBuffer();
		
		sb
		.append("\n**** GlobalConfig ******************")
		.append("\napppath				  	   : " + apppath)
		.append("\naudio_repository_url  	   : " + audio_repository_url)
		.append("\ncdr_config           	   : " + cdr_config)	
		.append("\ndebug				  	   : " + debug)	
		.append("\nfetchaudio				   : " + fetchaudio)
		.append("\ngrammarbase 				   : " + grammarbase)
		.append("\nlang_config             	   : " + lang_config)
		.append("\nloadtest               	   : " + loadtest)
		.append("\nmail_from               	   : " + mail_from)
		.append("\nmail_smtp_host              : " + mail_smtp_host)
		//.append("\nmail_to                 	   : " + mail_to)	
		.append("\nmaintainer                  : " + maintainer)
		.append("\nmaxnbest                	   : " + maxnbest)
		.append("\nmaxtries              	   : " + maxtries)
		.append("\nmaxnoinput			   	   : " + maxnoinput)
		.append("\nmaxnomatch			   	   : " + maxnomatch);
		if(npa_loc!=null)
		sb.append("\nnpa_loc                 	   : " + npa_loc);
		sb
		.append("\npromptbase 				   : " + promptbase)
		.append("\nresource_repository_basepath: " + resource_repository_basepath)
		.append("\nresource_repository_url     : " + resource_repository_url)
		.append("\nsubmitbase		           : " + submitbase)
		.append("\nvxml_version		           : " + vxml_version);

		sb.append("\nlanguages supported: "  + languages);
		sb.append("\nlanguage properties:\n" + langPropMap);
		sb.append("\nglobal properties:\n"   + globalConfigMap);
		//sb.append("\nglobal messages:\n"     + globalMessageMap);
		
		sb.append("\n**********************************")
		;
		
		return sb.toString();
	}	


 }//end of class