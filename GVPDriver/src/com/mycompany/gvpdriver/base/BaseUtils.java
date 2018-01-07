package com.mycompany.gvpdriver.base;

/** @copyright   2009-2013 mycompany. All rights reserved. */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.entity.ICallInfo;
import com.mycompany.ecs.cdr.CDRConstants;
//import com.mycompany.ecs.cdr.client.CDRConfig;
import com.mycompany.vxml.facade.*;

/** 
 * @file         Utils.java
 * 
 * @description  This class contains the set of convenience static methods            
 * 
 * @author       Tatiana Stepourska
 * 
 * @version      1.1
 */

public class BaseUtils 
{               
	private static final Logger logger = Logger.getLogger(BaseUtils.class);	

	/**
	 * Sends trap to alarm server.
	 * 
	 * @param  String taskName  --For the Web server monitoring taskName is the URL
	 * 
	 * @return boolean
	 */
/*	private static boolean raiseAlarm(String sev, String errMsg)
	{
		boolean success = false;		
		logger.info("Raising alarm...");
		
		logger.info("errMsg : " + errMsg);
		
		Severity severity = null;
		
		if(sev==null || sev.equalsIgnoreCase(SEVERITY_INFO))
			severity = Severity.INFO;
		else if(sev.equalsIgnoreCase(SEVERITY_WARNING))
			severity = Severity.WARNING;
		else if(sev.equalsIgnoreCase(SEVERITY_MAJOR))
			severity = Severity.MAJOR;
		else if(sev.equalsIgnoreCase(SEVERITY_CRITICAL))
			severity = Severity.CRITICAL;
		else
			severity = Severity.INFO;
		
		if(errMsg==null)
			errMsg = new String("UNKNOWN");
			
		try
		{		
			//TODo - uncomment for live
			Trap.generateTrap(severity, errMsg);
			
			logger.info("Alarm generated");			
			success = true;
		}
		catch(Exception e)
		{
			success = false;
			logger.error("Error generating alarm: " + e.getMessage());
		}
		finally
		{

		}
				
		return success;
	}*/

	public static boolean isValidUrl(String urlParam) {
		URL url;
		int responseCode = -1;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlParam);
			HttpURLConnection.setFollowRedirects(false);
			connection = (HttpURLConnection) url.openConnection();
			responseCode = connection.getResponseCode();
		} catch (Exception ex) {
			//logger.error("Exception caught establishing connection to: " + urlParam);
			responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		} finally {

			try {
				connection.disconnect();
			} catch (Exception e) {

			}
			// for garbage collector
			url = null;
			connection = null;
		}
		
		if(responseCode == HttpURLConnection.HTTP_OK)
			return true;
		
		String err = "URL DOWN: "+urlParam+", HTTP Code: " + String.valueOf(responseCode);
  		logger.error("validateUrl: " + err); 
  		// BaseUtils.generateTrap(AlarmKeys.ALARM_IVR_DRIVER_INTERNAL_FAILURE_KEY, AlarmKeys.SEVERITY_MAJOR, err);

		return false;
	}
	
	public static long getTestDNIS() {
		String dnis = getStringFromFile(BaseGlobalConfig.resource_repository_basepath
				+ BaseGlobalConfig.resource_repository_url
				+ "config/aircanada/testdnis");
		long result = 0;

		try {
			result = Long.parseLong(dnis.trim());
			if (result <= 1000000000L)
				result = 0;
		} catch (Exception e) {
			logger.error("getTestDNIS: error parsing dnis: " + e.getMessage());
			result = 0;
		}
		logger.trace("getTestDNIS: returning result: " + result);
		return result;
	}

	/**
	 * Extracts trunk and port numbers and sets it to CDR
	 * from the sip header of format ds/ds1-0/3;IP=10.76.222.30 
	 * where 0 is a trunk number unique per trunk group, 3 is the port
	 * 
	 * @param xchannel
	 * @param ci
	 */
	public static final void extractChannel(String xchannel, ICallInfo ci){
		if(logger.isTraceEnabled())
			logger.trace("extractChannel: xchannel: " + xchannel);
		if(xchannel==null||xchannel.length()<=0)
			return;
		
		try {
			String s = xchannel.substring(0,xchannel.indexOf(";"));
			//if(logger.isTraceEnabled())
			//logger.trace("extractChannel: s: "+s);
			String[] arr = s.split("/");
			
			int port=Integer.parseInt(arr[2]);
			ci.setPort(port);
			//if(logger.isTraceEnabled())
				logger.info("extractChannel: port: "+port);
			
			int trunk = Integer.parseInt(arr[1].substring(arr[1].indexOf("-")+1));
			ci.setComponent(trunk);
			//if(logger.isTraceEnabled())
				logger.info("extractChannel: trunk: "+trunk);
		}
		catch(Exception e){
			logger.error("extractChannel: Error parsing trunk or port: " + e.getMessage());
		}
	}
	
	//public static final void extractGVPTenant(String sip_header_x_genesys_gvp_session_id, ICallInfo ci) throws Exception {
	public static final void extractGVPTenant(String sip_header_x_genesys_gvp_session_data, ICallInfo ci) throws Exception {
		//session.connection.protocol.sip.headers['x-genesys-gvp-session-data']=
		//callsession=23DEC11C-C82E-D0EB-AAAC-D16C0EEB8C73;1;0;;;;Environment/Voxify/AirCanada;Tatiana (AC IVR application)
		
		//session.connection.protocol.sip.headers['x-genesys-gvp-session-id']=
		//23DEC11C-C82E-EA13-A3B3-2D3D635F26FF;gvp.rm.datanodes=1;gvp.rm.tenant-id=1.103.101_Tatiana (AC IVR application)

		
		//String[] arr = sip_header_x_genesys_gvp_session_id.split(BaseConstants.SIP_PARAM_DELIMITER);
		
		//extracting from session data
		String[] arr = sip_header_x_genesys_gvp_session_data.split(BaseConstants.SIP_PARAM_DELIMITER);
		int index = arr.length-2; //second last
		String tPar = arr[index];
		arr = tPar.split("/");
		index = arr.length-1;	//last
		String t = arr[index];
		if(logger.isTraceEnabled())
		logger.trace("extractGVPTenant: tenant: " + t);
		ci.setTenant(t);
	}

	public static boolean isDTMF(String s) {
		if(s==null || s.trim().length()==0)
			return false;

		char[] arr = s.toCharArray();
		for(int i=0;i<arr.length;i++) {
			if(!(Character.isDigit(arr[i]))&&(arr[i]!='*')&&(arr[i]!='#'))
				return false;
		}
		
		return true;
	}
	
   /**
    * Checks configuration property for long distance codes
    * @param list
    * @param pn
    * @return
    */
   public static boolean isLocal(String pn, String list)   {
   	if(list==null || pn==null)
   		return false;
   	
   	boolean local = false;
   	String item = null;
   	
   	//parse list of local area codes
   	StringTokenizer st = new StringTokenizer(list, BaseConstants.LIST_SEPARATOR);
   	
   	while(st.hasMoreElements())	{
   		item = (String)st.nextElement();
   		logger.trace("item: " + item);
   		
   		if(item!=null && pn.startsWith(item)) {
   			local = true;
   			logger.info("Match on local area code: " + item);
   			break;
   		}
   	}
   	
   	return local;   	
   }
		
/*	public static String initGrammarPath(Properties config, long serviceID, String lang)	{
		String sep = File.separator;
		
		StringBuffer sb = new StringBuffer();
		sb
		.append(config.getProperty(AppBaseConstants.RESOURCE_REPOSITORY_BASEPATH_KEY))
		.append(config.getProperty(AppBaseConstants.RESOURCE_REPOSITORY_URL_KEY))
		.append(serviceID)
		.append(sep)		   
		.append(AppBaseConstants.DIR_GRAMMAR)
		//.append(sep)
		.append(lang)
		.append(sep)
		;
			
		return sb.toString();
	}*/

	/** 
	 * Reads configuration from file into Properties object
	 * 
	 * @param filename
	 * 
	 * @return Properties
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getStringFromFile(String filename) {
		String fp = "getStringFromFile: ";
		BufferedReader reader   = null;	
		String line = null;
		String result = " ";
		
		try	{
			logger.trace(fp + "trying to create reader from filename: " + filename);
			reader = new BufferedReader(new FileReader(filename));
		}
		catch(Exception e) {
			logger.error(fp + "error creating file reader");
			return " ";
		}
		
		logger.trace(fp + "trying to read the file");
		try {
			while((line = reader.readLine())!=null) {
				result = result + line;
			}
		}
		catch(Exception exn){
			logger.info(fp + "error reading file: " + exn.getMessage());
		}
		finally	{
			try	{
				//fin.close();
				reader.close();
				logger.trace(fp + "property input closed");
			}
			catch(Exception ec){
				logger.error(fp + "Error closing file stream: " + ec.getMessage());
				StackTraceElement[] trace = ec.getStackTrace();			  
				if(trace!=null)
				{
					for(int i=0;i<trace.length;i++)
					{
						logger.error(trace[i]);
					}
				}
			}
			reader  = null;
			
		}
		logger.trace(fp + "returning result: " + result);
		return result;
	}

	public static String[] getDBs(String dblist) throws Exception {
		String[] dbs = dblist.split(",");
		return dbs;
	}

	public static String toDelimSeparatedString(String[] arr, String delim) {
		String fp = "toDelimSeparatedString: ";
		
		logger.trace(fp + "delim: " + delim);
		//String result = null;
		StringBuffer sb = new StringBuffer();
		
		if(arr==null) {
			logger.trace(fp + "supplied string array is null");
			return null;
		}
		logger.trace(fp + "supplied string array is NOT null, continue");
		
		int arrlen = arr.length;
		logger.trace(fp + "arrlen: " + arrlen);
		
		if(arrlen<=0)
			return null;
		
		if(delim==null)
			delim = "";
		
		for(int i=0;i<arrlen;i++) {
			sb.append(arr[i]);
			
			if(i<(arrlen-1))
				sb.append(delim);
		}
		
		return sb.toString();
	}
	
	/** 
	 * Reads configuration from file into Properties object
	 * 
	 * @param filename
	 * 
	 * @return Properties
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static Properties getConfigFromFile(String filename) throws FileNotFoundException, IOException, Exception
	{
		//if Properties file available, parse it for parameter settings
		File file               = new File(filename);
		FileInputStream fin     = null;
		Properties config = new Properties();
		Exception ex = null;
		logger.trace("getConfigFromFile: filename: " + filename);
		
		try	{
			if(!file.exists())
				throw new FileNotFoundException("getConfigFromFile: No property file [" + filename + "] found.");

			logger.trace("getConfigFromFile: loading properties");
			fin = new FileInputStream(file);
			config.load(fin);
			//config.list(System.out);
		
			logger.trace("getConfigFromFile: Properties loaded");
			
			if(logger.isTraceEnabled())
			{
			Enumeration<Object> en = config.keys();
			while(en.hasMoreElements())
			{
				String key = (String)en.nextElement();
				logger.trace(key + "=" + config.getProperty(key));
			}
			}
		}
		catch(FileNotFoundException fnfe)	{
			logger.info("getConfigFromFile: throwing FileNotFoundException");
			ex = new FileNotFoundException(fnfe.getMessage());
		}
		catch(IOException ioe)	{
			logger.info("getConfigFromFile: throwing IOException");
			ex = new IOException(ioe.getMessage());
		}
		catch(Exception exn){
			logger.info("getConfigFromFile: throwing Exception");
			ex = new Exception(ex.getMessage());
		}
		finally	{
			try		{
				fin.close();
				logger.trace("getConfigFromFile: property input closed");
			}
			catch(Exception ec)	{
				logger.error("Error closing file stream: " + ec.getMessage());
				StackTraceElement[] trace = ec.getStackTrace();			  
				if(trace!=null)
				{
					for(int i=0;i<trace.length;i++)
					{
						logger.error(trace[i]);
					}
				}
			}
			fin  = null;
			file = null;
			
		}
		
		if(ex!=null)
			throw ex;
		
		return config;
	}
	
	/** 
	 * Reads configuration from file into Properties object
	 * 
	 * @param filename
	 * 
	 * @return Properties
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static FastMap<String,String> getStringMapFromFile(String filename) throws FileNotFoundException, IOException, Exception
	{
		BufferedReader reader	= null;
		FastMap<String,String> config = new FastMap<String,String>();
		Exception ex = null;
		String line = null;
		String[] tmp = null;
		logger.info("getStringMapFromFile: filename: " + filename);
		
		try	{
			//logger.trace("getStringMapFromFile: loading properties");
			
			reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine())!=null) {
				if(!line.startsWith(BaseConstants.POUND)){
				try {
					tmp = line.split("=");
					config.put(tmp[0], tmp[1]);
					if(logger.isTraceEnabled()){
						logger.trace("getStringMapFromFile: added "+tmp[0]+"="+tmp[1]);
					}
				}
				catch(Exception e) {
					
				}}
			}
		}
		catch(FileNotFoundException fnfe)	{
			logger.info("getStringMapFromFile: throwing FileNotFoundException");
			ex = new FileNotFoundException(fnfe.getMessage());
		}
		catch(IOException ioe)	{
			logger.info("getStringMapFromFile: throwing IOException");
			ex = new IOException(ioe.getMessage());
		}
		catch(Exception exn) {
			logger.info("getStringMapFromFile: throwing Exception");
			ex = new Exception(ex.getMessage());
		}
		finally	{
			try	{
				reader.close();
				logger.trace("getStringMapFromFile: reader closed");
			}
			catch(Exception ec)	{
				logger.error("getStringMapFromFile: Error closing reader: " + ec.getMessage());
				StackTraceElement[] trace = ec.getStackTrace();			  
				if(trace!=null)	{
					for(int i=0;i<trace.length;i++)		{
						logger.error(trace[i]);
					}
				}
			}
			reader = null;
		}
		
		if(ex!=null)
			throw ex;
		
		return config;
	}

	/**
	 * Splits a string into String array, each element of it is a 
	 * character. Intended to use for playback of prerecorded files 
	 * (digits, characters etc.)
	 * 
	 * @param s
	 * 
	 * @return String[]
	 */
	public static String[] getWavesForCharacters(String s)	{
		if(s==null)
			s = "";

		String[] list = new String[s.length()];
		
		for(int i=0;i<s.length();i++)	{
			list[i] = Character.toString(s.charAt(i));
		}
		
		return list;
	}
	
	public static boolean isValidPhone(String p){
		if(p==null||p.trim().length()!=10)
			return false;
		
		for(int i=0;i<p.trim().length();i++){
			if(!(Character.isDigit(p.charAt(i))))
				return false;
		}
		return true;
	}
	
	public static boolean isDigitString(String p){
		if(p==null||p.trim().length()<=0)
			return false;
		
		for(int i=0;i<p.trim().length();i++){
			if(!(Character.isDigit(p.charAt(i))))
				return false;
		}
		return true;
	}
	

	/**
	 * Adds catch blocks for document hang up events 
	 * 
	 * @param  vxml
	 * 
	 * @return vxml
	 */
	public static final void catchDisconnectEvents(VXML vxml) {
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_HANGUP);	
		vxml.Submit(BaseGlobalConfig.apppath + BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);		
		vxml.CatchEnd();
	
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_HANGUP);
		vxml.Submit(BaseGlobalConfig.apppath + BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
		
		vxml.CatchStart(VXMLEvents.EVENT_TELEPHONE_DISCONNECT_TRANSFER);
		vxml.Submit(BaseGlobalConfig.apppath + BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();	
		
		vxml.CatchStart(VXMLEvents.EVENT_CONNECTION_DISCONNECT_TRANSFER);
		vxml.Submit(BaseGlobalConfig.apppath +  BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();	

		vxml.CatchStart(VXMLEvents.EVENT_TRANSFER_DISCONNECT_HANGUP);
		vxml.Submit(BaseGlobalConfig.apppath + BaseCallStates.callend, null, CDRConstants.KEY_TERM+" "+BaseConstants.VAR_DOC_HISTORY);
		vxml.CatchEnd();
	}
	
	public static void generateTrap(String id, String sev, String trapDtls)  {
		 try {
		     // Class.forName("com.timeicr.monitor.snmp.Trap");
		      
		      //or
		      //Try the method forName(String name, boolean initialize, ClassLoader loader) 
		      //of Class and set the param "initialize" to false.
		     // ClassLoader loader = ClassLoader.getSystemClassLoader();
		    //  Class.forName("com.timeicr.monitor.snmp.Trap", false, loader);
		      
			    //GlobalConfig gConfig = GlobalConfig.getInstance();
			    //String trapMsg       = gConfig.getAlarmText(id);
			//    String fullTrapMsg   = null;      
				//Severity severity = null;
					
			/*	try {
					severity = GlobalConfigBase.severityMap.get(sev);
				}
				catch(Exception e){
					logger.error("Error parsing alarm message: " + e.getMessage());
				}
				if(sev==null)
					severity = Severity.INFO;
			      */
			   // if(trapDtls == null)
			   // 	trapDtls = new String("UNKNOWN");;
		     
			  //  fullTrapMsg = id + AlarmKeys.ALARM_DELIM_KEY +// trapMsg + 
			   // AlarmKeys.ALARM_DELIM_KEY + trapDtls;         
			            //TODO
			    //Trap.generateTrap(id, sev, trapDtls);  
		      // it exists on the classpath
		 }/* catch(ClassNotFoundException e) {
		      // it does not exist on the classpath
		 }*/
		 catch(Exception e) {
		      // it does not exist on the classpath
		 }
	}
	
	/**
	 * Records specified feature into CDR
	 * @param feature	--4-digit string with all zeroes except the position of the required feature
	 * @param ci
	 */
/*	public static void recordFeature(String feature, ICallInfo ci){
		try{
			for(int i=0;i<feature.length();i++){
				if(feature.charAt(i)=='1'){
					ci.addFeature(CDRConfig.recordableFeatures[i]);
					break;
				}
			}
		}
		catch(Exception e){
			logger.error("Error recording feature: " + feature);
		}
	}*/
}  // end of Utils