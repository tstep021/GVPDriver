package com.mycompany.gvpdriver.base;

/** copyright 2012-2013, mycompany  */

import java.io.IOException;
import java.io.PrintWriter;
//import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastMap;
import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.BaseGlobalConfig;
import com.mycompany.ecs.cdr.CDRConstants;
import com.mycompany.ecs.cdr.client.CDRConfig;

import com.mycompany.vxml.facade.*;

/** 
 * Description   generates VXML application root document
 * 
 * @author      Tatiana Stepourska
 * 
 * @version     1.0
 */
public class BaseROOT extends HttpServlet //BaseServletExtension
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BaseROOT.class);
	//private static final String TEST_VARIABLE	= "TATIANA";
	
	String ctxName 			= null;	
	public static int prefixLength = CDRConfig.prefix_length;
	//	initialize in init()
	FastMap<String,String> map;
		
	/** 
	 * Gets invoked once by the service method. InitializeS global 
	 * application configuration
	 *   
	 * @param  config 	The ServletConfig object that contains configutation 
	 *        			information for this servlet 
	 * @exception ServletException    thrown when the servlet's normal operation 
	 *								 is interrupted
	 */
	/**  */
	public void init(ServletConfig config) throws ServletException	{
		super.init(config);
		
		log.info("initializing");

		String fp 				= "init: ";
		ServletContext ctx 		= null;
		String propsFile  		= null;		

		try {
			ctx = config.getServletContext();
			log.trace(fp + "ctx: " + ctx);
			
			ctxName = ctx.getInitParameter(BaseConstants.CTX_PARAM_NAME_KEY);
			log.info(fp + "ctxName: " + ctxName);
			
			//String realPath = ctx.getRealPath(ctxName);
			//log.info(fp + "realPath: " + realPath);
			
			//load map from GlobalConfig
			
			// Look for properties file
			propsFile = ctx.getInitParameter(BaseConstants.KEY_APP_PROPERTIES);
			log.info(fp + "propsFile: " + propsFile);
			
			map = BaseUtils.getStringMapFromFile(propsFile);
			
			try {
				prefixLength = Integer.parseInt(map.get(CDRConstants.KEY_PREFIX_LENGTH));
				CDRConfig.prefix_length = prefixLength;
			}
			catch(Exception e) {
				log.info(fp + "Error getting prefix length: " + e.getMessage() + ", using default: " + CDRConfig.prefix_length);
			}
		}
		catch(Exception ex)	{
			log.error(fp + "ERROR loading properties file: ");
			log.error(ex.getMessage());
			StackTraceElement[] trace = ex.getStackTrace();			  
			if(trace!=null)  {
				for(int y=0;y<trace.length;y++)  {
					log.error(trace[y]);
				}
			}
		}
	}

	/** 
	 * Allows a servlet to handle a POST request. 
	 * 
	 * @param request    An HttpServletRequest object that contains the request the
	 *                   client has made of the servlet
	 *
	 * @param response   An HttpServletResponse object that contains the response 
	 *				     the servlet sends to the client 
     *
     * @exception ServletException  If the request for the GET could not be handled
	 * @exception IOException   If an input or output error is detected when the 
	 *						    servlet handles the GET request 	
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse  response)
    	throws ServletException, IOException 	{
		doGet(request, response);
  	}
  		 
	/**
	 * Handles both HTTP POST and GET operations.
	 * 
	 * @param request  --Client HTTP request object.
	 * @param response --Client HTTP response object.
	 * 
	 * @exception java.io.IOException
	 */
	protected void  doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException	{
		PrintWriter output 	= null;
		String result 		= null;
		
		try {
			// set the content type for the response
			response.setContentType("text/html");
			output = response.getWriter();
			result = buildDocument(new VXML(), request);		
			
			if(log.isTraceEnabled()){
				log.trace("writing out response for application root document");
				log.trace(result);		
			}
			output.println(result);
		}
		catch(Exception e) {
			log.error("Exception generating application root document: " + e.getMessage());
		}
		finally {		
			// release resources 
			output.flush();
			output.close();
		}
	}

	/**
	 * Adds global document properties 
	 * Can be overwritten, call super(vxml) first
	 */
	public VXML addGlobalApplicationProperties(VXML vxml) {

		//if(BaseGlobalConfig.debug) {
			//<meta name="MAINTAINER" content="yourname@yourserver.com"/> 
		//	vxml.Meta(VXMLProperties.PROP_MAINTAINER, BaseGlobalConfig.maintainer);
			//<property name="com.genesyslab.maintainer.sendwhen" value="always"/>
			//vxml.Property(VXMLProperties.PROP_COM_GENESYSLAB_MAINTAINER_SENDWHEN, "always");
		//}
		vxml.Property(VXMLProperties.PROP_FETCHAUDIO, BaseGlobalConfig.fetchaudio);
		
		if(map!=null){
		vxml.Property(VXMLProperties.PROP_INPUTMODES, map.get(VXMLProperties.PROP_INPUTMODES));

		//cache handling goes here
		vxml.Property(VXMLProperties.PROP_DOCUMENTMAXAGE  , map.get(VXMLProperties.PROP_DOCUMENTMAXAGE));
		vxml.Property(VXMLProperties.PROP_DOCUMENTMAXSTALE, map.get(VXMLProperties.PROP_DOCUMENTMAXSTALE));
		vxml.Property(VXMLProperties.PROP_AUDIOMAXAGE     , map.get(VXMLProperties.PROP_AUDIOMAXAGE));
		vxml.Property(VXMLProperties.PROP_AUDIOMAXSTALE   , map.get(VXMLProperties.PROP_AUDIOMAXSTALE));	
		
		//	vxml.Property("maxage"          , "0");
		//	vxml.Property("maxstale"        , "0");						
		//	vxml.Property("documentmaxage"  , "0");
		//	vxml.Property("documentmaxstale", "0");
		//	vxml.Property("audiomaxage"     , "0");
		//	vxml.Property("audiomaxstale"   , "0");
		// end of cache handling
		
		//////TTS ///////////////////
		//	vxml.Property(VXMLProperties.PROP_TTSENGINE, map.get(VXMLProperties.PROP_TTSENGINE));
		//vxml.Property(VXMLProperties.PROP_COM_GENESYSLAB_TTSENGINE, map.get(VXMLProperties.PROP_COM_GENESYSLAB_TTSENGINE));
		//TODo
		//vxml.Property(VXMLProperties.PROP_VOICE,   map.get(VXMLProperties.PROP_VOICE));	
		//only by language
		//vxml.Property(VXMLProperties.PROP_VOICE, GlobalConfig.langPropMap.get(VXMLProperties.PROP_COM_GENESYSLAB_TTSENGINE+ICommonBaseConstants.UNDERSCORE+a.getLanguage()));
		// end of TTS /////////////////////////	
		
		vxml.Property(VXMLProperties.PROP_TIMEOUT, map.get(VXMLProperties.PROP_TIMEOUT));
		vxml.Property(VXMLProperties.PROP_TERMCHAR, "");
		vxml.Property(VXMLProperties.PROP_TERMTIMOUT, "0s");
		}
		return vxml;
	}

	/**
	 * Can be overwritten, call super(vxml) first
	 * @param vxml
	 * @return
	 */
	public VXML addGlobalApplicationVariables(VXML vxml) {
		vxml.Var(CDRConstants.KEY_TERM			, ""+CDRConstants.TERM_CALLER_HANGUP);
		vxml.Var(BaseConstants.EVENT_KEY			, "none");
		vxml.Var(BaseConstants.VAR_DOC_HISTORY   , "");	
		
		return vxml;
	}

	/**
	 * Creates Voice XML response string
	 * To be called by all classes that generate 
	 * vxml response documents
	 * 
	 * @param vxml
	 * @return
	 */
	public final String buildDocument(VXML vxml, HttpServletRequest request) throws Exception {
		
		addDocumentHeader		(vxml);
		addDocumentBody	 		(vxml, request);					
		addDocumentFooter		(vxml, request);
		
		return vxml.toString();
	}

	/**
	 * 
	 * @param vxml
	 * @return
	 */
	private final VXML addDocumentHeader(VXML vxml) throws Exception {
		vxml.Header("1.0", "ISO-8859-1");
					//app		//vxml version			//lang
		vxml.VxmlStart(null, BaseGlobalConfig.vxml_version, null);

		addGlobalApplicationProperties(vxml);
		addGlobalApplicationVariables(vxml);

		return vxml;
	}

	/** 
	 * Builds custom part of the VXML response document body.
	 *
	 * To be implemented by all classes that generate 
	 * vxml response documents
	 * Do NOT include headers, footers, and document level 
	 * events
	 * 
	 * @param vxml	    -- VXML object
	 * @param ci		-- BaseCallInfo object (for Driver it shoudl be an instance of CallInfo)
	 * @return vxml     -- Part of VXML response
	 */
	public VXML addDocumentBody(VXML vxml, //String submitNext, 
			HttpServletRequest request) throws Exception {
		
		/*
		vxml.FormStart("globalForm");
		vxml.BlockStart();
		vxml.Value(TEST_VARIABLE);
		vxml.BlockEnd();
		vxml.FormEnd();*/
		
		return vxml;
	}
	
	/**
	 * Adds catch blocks for global application (common for any configuration) events, 
	 * i.e, errors, maxouts, links, etc.
	 * To be implemented in the subclass
	 *  
	 * @param vxml
	 * @return vxml
	 */
	public VXML catchGlobalApplicationEvents(VXML vxml, String globalErrUrl, HttpServletRequest request) throws Exception {
		String submitOnErr = BaseGlobalConfig.submitbase + BaseCallStates.CLASS_TECH_DIFF;
		if(globalErrUrl!=null)
			submitOnErr = globalErrUrl;
		
		if(log.isTraceEnabled())
			log.trace("catchGlobalApplicationEvents: submitOnErr: " + submitOnErr);
		/*
		vxml.CatchStart(AppEvents.ERROR_TTS_UNKNOWNENGINE);
		vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/jin_err.wav");
		vxml.Text("  ");
		vxml.AudioEnd();
		vxml.Submit(GlobalConfig.submitbase + ICallStates.CLASS_TECH_DIFF, null, CDRConstants.KEY_TERM);
		vxml.CatchEnd();
		
		vxml.CatchStart("error.tts");
		vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/jin_err.wav");
		vxml.Text("  ");
		vxml.AudioEnd();
		vxml.AudioStart("/GLOBAL_RESOURCES/prompts/system/jin_err.wav");
		vxml.Text("  ");
		vxml.AudioEnd();
		//vxml.Disconnect();
		vxml.Submit(GlobalConfig.submitbase + ICallStates.CLASS_TECH_DIFF, null, BaseConstants.KEY_TERM);
		vxml.CatchEnd();
		*/
		
		this.maxnoinput(vxml);
		this.maxnomatch(vxml);
		this.maxout(vxml);
		
		vxml.ErrorStart();
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_ERROR+"'");

		/*
		if(BaseGlobalConfig.debug) {

			vxml.Text("document error in base call flow, redirecting to technical difficulties");
			vxml.Text("Message:");
			vxml.Value(VXMLEvents.ERROR_SHADOW__MESSAGE); //  "_message"
			vxml.Text("Event:"); 
			vxml.Value(VXMLEvents.ERROR_SHADOW__EVENT); //"_event"
			
			//if(ci.getPlatform()!=null && ci.getPlatform().equalsIgnoreCase(CDRConstants.PLATFORM_GVP)) {
			//vxml.Text("U r l: ");
			//vxml.Value(AppEvents.ERROR_SHADOW__URL); //"_url"
			vxml.Text("Element:"); 
			vxml.Value(VXMLEvents.ERROR_SHADOW__ELEMENT); //"_element"
			vxml.Text("Line:");
			vxml.Value(VXMLEvents.ERROR_SHADOW__LINE); //"_line"
			//}
		}*/
		vxml.Submit(submitOnErr, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.ErrorEnd();
	
		vxml.CatchStart(VXMLEvents.EVENT_ERROR);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_ERROR+"'");
		/*if(BaseGlobalConfig.debug) {
			vxml.Text("caught document event error in base call flow, redirecting to technical difficulties");
			vxml.Text("Message:");
			vxml.Value(VXMLEvents.ERROR_SHADOW__MESSAGE); //  "_message"
			vxml.Text("Event:"); 
			vxml.Value(VXMLEvents.ERROR_SHADOW__EVENT); //"_event"
			//if(ci.getPlatform()!=null && ci.getPlatform().equalsIgnoreCase(CDRConstants.PLATFORM_GVP)) {
			
			//vxml.Text("U r l: ");
			//vxml.Value(AppEvents.ERROR_SHADOW__URL); //"_url"
			vxml.Text("Element:"); 
			vxml.Value(VXMLEvents.ERROR_SHADOW__ELEMENT); //"_element"
			vxml.Text("Line:");
			vxml.Value(VXMLEvents.ERROR_SHADOW__LINE); //"_line"
			//}
			
		}*/
		vxml.Submit(submitOnErr, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();

		return vxml;
	}

	/**
	 * Creates document footer which includes all mandatory 
	 * error handling
	 * 
	 * @param vxml
	 * @return
	 */
	private final VXML addDocumentFooter(VXML vxml, HttpServletRequest request) throws Exception {
		
		//custom url to submit in case of error is null here
		//overwrite catchGlobalApplicationEvents and call super(vxml,myURL)
		catchGlobalApplicationEvents(vxml,null, request);
		BaseUtils.catchDisconnectEvents(vxml);
		
		vxml.VxmlEnd();
		
		return vxml;
	}

	/**
	 * Adds catch blocks for document hang up events 
	 * 
	 * @param  vxml
	 * 
	 * @return vxml
	 */
/*	private final VXML catchDisconnectEvents(VXML vxml)
	{
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_HANGUP);	
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);		
		vxml.CatchEnd();
	
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_HANGUP);
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
		
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_TRANSFER);
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();	
		
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_TRANSFER);
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();	

		vxml.CatchStart(VXMLEvents.EVENT_TRANSFER_DISCONNECT_HANGUP);
		vxml.Submit(BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();

		return vxml;
	}*/

	/**
	 * Called from the node to generate vxml when needed
	 * 
	 * @param vxml
	 */
	public void maxnoinput(VXML vxml){
		vxml.CatchStart(VXMLEvents.EVENT_MAXNOINPUT);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_MAXNOINPUT+"'");
		/*if(BaseGlobalConfig.debug) {
			vxml.Text("caught global application event maxnoinput in root, redirecting to global event handler");
		}*/
		vxml.Submit(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKIN, null, BaseConstants.EVENT_KEY+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}
	
	/**
	 * Called from the node to generate vxml when needed
	 * 
	 * @param vxml
	 */
	public void maxnomatch(VXML vxml){
		vxml.CatchStart(VXMLEvents.EVENT_MAXNOMATCH);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_MAXNOMATCH+"'");
		/*if(BaseGlobalConfig.debug) {
			vxml.Text("caught global application event maxnomatch in root, redirecting to global event handler");
		}*/
		vxml.Submit(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKIN, null, BaseConstants.EVENT_KEY+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}
	
	/**
	 * Called from the node to generate vxml when needed
	 * 
	 * @param vxml
	 */
	public void maxout(VXML vxml){
		vxml.CatchStart(VXMLEvents.EVENT_MAXOUT);
		vxml.Assign(BaseConstants.EVENT_KEY, "'"+VXMLEvents.EVENT_MAXOUT+"'");
		/*if(BaseGlobalConfig.debug) {
			vxml.Text("caught global application event maxout in root, redirecting to global event handler");
		}*/
		vxml.Submit(BaseGlobalConfig.submitbase + BaseCallStates.CLASS_GLOBAL_EVENT_CHECKIN, null, BaseConstants.EVENT_KEY+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}

}//end of class