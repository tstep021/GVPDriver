package com.mycompany.gvpdriver.callpark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {
	private final static Logger logger = Logger.getLogger(Config.class);
	public static InetAddress 	localhost = null;

	public static int 			checkin_local_port = 0;
	public static int           checkin_timeout		= 60000;
	public static int 			checkout_local_port = 0;
	public static int           checkout_timeout	= 60000;
	public static int 			sip_local_port 	= 0;
	//public static InetAddress 	sip_remote_host = null;
	public static int 			sip_remote_port = 0;
	public static int           sip_timeout		= 60000;
	public static int           sip_tries		= 3;
	public static int           sip_sleep		= 200;
	public static int           voiceclient_timeout	= 60000;
	
	public static String audiocodes = "10.76.222.30:5060";
	public static String resmanip = "10.76.222.23";
	public static Properties props = null;
	
	/**
	 * Runs all the routines required to initialize 
	 * the CMRSpeechServer
	 * 
	 * @param filename  --Full name for configuration file
	 * 
	 * @exception FileNotFoundException
	 * @exception Exception
	 */
	public static void initialize(String filename) throws FileNotFoundException, Exception {
		//if Properties file available, parse it for parameter settings
		File file = new File(filename);
		FileInputStream fin = null;		
		
		try	{
			if(!file.exists())
				throw new FileNotFoundException("initialize: No property file [" + filename + "] found.");

			props = new Properties();

			logger.info("initialize: loading properties");
			fin = new FileInputStream(file);
			props.load(fin);
			//defaultConfig.list(System.out);
		
			if(props==null)
				throw new Exception("initialize: Default configuration has not been initialized");
			
			Enumeration<Object> en = props.keys();
			while(en.hasMoreElements())	{
				String key = (String)en.nextElement();
				logger.info(key + "=" + props.getProperty(key));
			}			
		}
		catch(FileNotFoundException fnfe)	{
			throw new FileNotFoundException(fnfe.getMessage());
		}
		catch(IOException ioe)	{
			throw new Exception(ioe.getMessage());
		}
		catch(Exception ex)	{
			throw new Exception(ex.getMessage());
		}
		finally	{
			try	{
				fin.close();
			}
			catch(Exception ec)	{
				logger.error("initialize: Error closing file stream: " + ec.getMessage());
				logger.error(ec.fillInStackTrace());
				//ec.printStackTrace();
			}
			fin  = null;
			file = null;
		}		
		
		try {
			localhost 			= InetAddress.getLocalHost(); // InetAddress.getByName(props.getProperty(Constants.KEY_LOCALHOST)); 
			checkin_local_port 	= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_CHECKIN_LOCAL_PORT));
			checkout_local_port = Integer.parseInt(props.getProperty 	 (Constants.KEY_PARK_CHECKOUT_LOCAL_PORT));
			checkin_timeout 	= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_CHECKIN_TIMEOUT));
			checkout_timeout	= Integer.parseInt(props.getProperty 	 (Constants.KEY_PARK_CHECKOUT_TIMEOUT));
			
			sip_local_port 		= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_SIP_LOCAL_PORT));
			//sip_remote_host 	= InetAddress.getByName(props.getProperty(Constants.KEY_PARK_SIP_REMOTE_HOST));
			sip_remote_port 	= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_SIP_REMOTE_PORT));
			sip_timeout			= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_SIP_TIMEOUT));
			sip_tries			= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_SIP_TRIES));
			sip_sleep			= Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_SIP_SLEEP));
			voiceclient_timeout = Integer.parseInt(props.getProperty	 (Constants.KEY_PARK_VOICECLIENT_TIMEOUT));
		}
		catch(Exception e){
			logger.info("Could not parse property: " + e.getMessage());
		}
		

		
		logger.info("initialize: configuration initialized");

		//get smtp host
		//this.smtpHost = defaultConfig.getProperty(Constants.MAIL_SMTP_HOST_KEY); 
		// Get sender address
		//this.from     = defaultConfig.getProperty(Constants.MAIL_FROM_KEY); 
		// get recipient address list
		//this.to       = defaultConfig.getProperty(Constants.MAIL_TO_KEY); 
	}	//end of initialize

	private Config(){}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
}
