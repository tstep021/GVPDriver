package com.mycompany.gvpdriver.callpark;

import java.net.*;
import java.util.*;

import com.mycompany.gvpdriver.base.*;
import com.mycompany.gvpdriver.callpark.sipbeans.*;

import org.apache.log4j.Logger;

import com.mycompany.vxml.facade.*;

public class SipClient {
	private static Logger logger = Logger.getLogger(SipClient.class);
	// private static DatagramSocket socket = null;
	private static Socket socket = null;

	public SipClient() {
	}

	/**
	 * 
	 */
	public int invoke(String acSharedCallId, 
			String imsMessageTxNum, 
			HashMap<String, String> callSipInfo)
			throws Exception {
		String response = null;
		String sip_remote_host = callSipInfo.get(Constants.KEY_PARK_SIP_REMOTE_HOST);
		logger.info("sip_remote_host: " + sip_remote_host);
		
		SipMessage sipReqMain = this.createSipMessage(callSipInfo, imsMessageTxNum);
		int rCode = -1;
		
		for(int j=0;j<Config.sip_tries;j++){
		try {
			// socket = new DatagramSocket(Constants.MY_PORT, InetAddress.getByName(Constants.MY_ADDRESS));
			socket = new Socket(sip_remote_host, 
								Config.sip_remote_port,
								Config.localhost, 
								Config.sip_local_port);
			socket.setSoTimeout(Config.sip_timeout);

			SipMessageSender sender = new SipMessageSender(socket);
			response = sender.sendMessage(sipReqMain);
			logger.info("Response from message: " + response);
			try {
				rCode = Integer.parseInt(response.split(" ")[1].trim());
			}
			catch(Exception ex){
				logger.error("Error parsing response: " + ex.getMessage());
			}
			
			if(rCode==SipConstants.CODE_200_OK)
				break;		
		}
		catch(SocketException e){
			logger.error("SocketException: " + e.getMessage());
		}
		catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			/*StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}*/
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				logger.error("Error closing socket: " + e.getMessage());
			}
			socket = null;
		}
		}
		return rCode;
	}

	private SipMessage createSipMessage(HashMap<String, String> map,
			String imsMessageTxNum) throws Exception {
		//String dnis = map.get(AppConstants.SIP_INFO_KEY_DNIS);// "8554537893";
		//String clid = map.get(AppConstants.SIP_INFO_KEY_CLID); // "6137981516";
		SipMessage sipReqMain = null;
		String from = map.get(BaseConstants.SIP_HDR_FROM);
		// clid URL
		SipURI sipUri = new SipURI(from.substring(0,from.indexOf(SipConstants.SIP_PARAM_DELIM))); //"sip:" + clid + "@" + audiocodes); // 10.76.222.21:5060");
		SipHeaders sipHeaders = new SipHeaders();
		SipHeaderFieldName name;
		SipHeaderFieldValue value;
		
		String to = map.get(BaseConstants.SIP_HDR_TO);
		// MCP local tag
		String mcptag = map.get(VXMLVariables.VAR_SESSION_CONNECTION_SIPLOCALTAG); // null;
																				// //"23DEC11C-7E37-1373-D14E-E47E0D1506FD";//
																				// from.substring(from.indexOf(";"+1));

		// String via = null;
		// List<SipHeaderFieldValue> vias = new
		// ArrayList<SipHeaderFieldValue>();

		sipReqMain = new SipMessage("INFO", sipUri);
		logger.info("Created main SIP request: " + sipReqMain.toString());

		// setting To and From headers
		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_FROM);
		value = new SipHeaderFieldValue(from);
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_TO);
		value = new SipHeaderFieldValue(to + ";tag=" + mcptag);
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_VIA);
		value = new SipHeaderFieldValue(map.get(BaseConstants.SIP_HDR_VIA));
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_CALLID);
		value = new SipHeaderFieldValue(map.get(BaseConstants.SIP_HDR_CALLID));
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_MAX_FORWARDS);
		value = new SipHeaderFieldValue(map.get(BaseConstants.SIP_HDR_MAX_FORWARDS));
		sipHeaders.add(name, value);

		logger.trace("dynamic headers added");

		// sequence for main
		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_CSEQ);
		value = new SipHeaderFieldValue("1 INFO");
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_CONTENT_TYPE);
		value = new SipHeaderFieldValue("application/sdp"); // ("application/x-www-form-urlencoded;charset=utf-8");
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName(BaseConstants.SIP_HDR_TXNUMBER);
		value = new SipHeaderFieldValue(imsMessageTxNum);
		sipHeaders.add(name, value);

		name = new SipHeaderFieldName("Content-Length");
		value = new SipHeaderFieldValue("" + (imsMessageTxNum.getBytes()).length);
		sipHeaders.add(name, value);

		logger.trace("static headers added");

		sipReqMain.setSipHeaders(sipHeaders);
		logger.trace("main headers set: " + sipHeaders.toString());

		return sipReqMain;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// initialize remote properties
		// String callidref = null;
		// String dnis = null;
		// SipClient c = null;

		try {
			// remoteAddress = InetAddress.getByName(args[0]);
			// remotePort = Integer.parseInt(args[1]);
			// dnis = args[2];
			// logger.info("remoteAddress: " + remoteAddress + "; remotePort: "
			// + remotePort);
			// c = new
			// SipClient(InetAddress.getByName(args[0]),Integer.parseInt(args[1]),
			// InetAddress.getByName("10.76.222.11"), 5060);
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			System.exit(1);
		}

		// waiting for message to send
		// while (true) {
		try {
			// String msg = readLine("Enter message type: ");
			// /got the message, starting to process
			// String msg = "mytest";
			// c.invoke();
		} catch (Exception e) {
			logger.error("Error invoking: " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}