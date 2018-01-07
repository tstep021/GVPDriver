package com.mycompany.gvpdriver.callpark;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.*;

public class VoiceAppClient {
	private static Logger logger = Logger.getLogger(VoiceAppClient.class);

	protected int timeout = BaseConstants.DEFAULT_REQUEST_TIMEOUT;
	protected String responseString = "";
	private int status = -99; // ICommonConstants.STATUS_UNKNOWN;

	public VoiceAppClient() {
		this.remoteHost = BaseGlobalConfig.globalConfigMap.get(Constants.KEY_PARK_CHECKIN_IP);
		this.remotePort = Integer.parseInt(BaseGlobalConfig.globalConfigMap.get(Constants.KEY_PARK_CHECKIN_PORT));
	}

	public VoiceAppClient(String h, int p) {
		this.remoteHost = h;
		this.remotePort = p;
	}

	/**
	 * IP address of the remote host
	 */
	protected String remoteHost = null;

	/**
	 * Port on to remote host to connect to
	 */
	protected int remotePort = -1;

	/**
	 * Sets the remote host IP address
	 * 
	 * @param h
	 */
	public void setRemoteHost(String h) {
		this.remoteHost = h;
	}

	/**
	 * Returns the remote host IP address
	 * 
	 * @return String
	 */
	public String getRemoteHost() {
		return this.remoteHost;
	}

	/**
	 * Sets the remote port
	 * 
	 * @param p
	 */
	public void setRemotePort(int p) {
		this.remotePort = p;
	}

	/**
	 * Returns the remote port
	 * 
	 * @return int
	 */
	public int getRemotePort() {
		return this.remotePort;
	}

	/**
	 * Sets connection timeout
	 * 
	 * @param t
	 */
	public void setTimeout(int t) {
		this.timeout = t;
	}

	/**
	 * Returns connection timeout
	 * 
	 * @return int
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets request message string
	 * 
	 * @param req
	 */
	/*
	 * public void setRequestString(String req) { this.requestString = req; }
	 */

	/**
	 * Returns request message string
	 * 
	 * @return String
	 */
	/*
	 * public String getRequestString() { return this.requestString; }
	 */

	/**
	 * Sets response message string
	 * 
	 * @param res
	 */
	public void setResponseString(String res) {
		this.responseString = res;
	}

	/**
	 * Returns response message string
	 * 
	 * @return String
	 */
	public String getResponseString() {
		return this.responseString;
	}

	/**
	 * Sets status
	 * 
	 * @param st
	 */
	public void setStatus(int st) {
		this.status = st;
	}

	/**
	 * Returns status
	 * 
	 * @return int
	 */
	public int getStatus() {
		return this.status;
	}

	public int invoke(HashMap<String, String> info) throws Exception {
		Socket socket = null;
		BufferedReader bufferedreader = null;
		ObjectOutputStream writer = null;
		if (info == null)
			throw new Exception("cmd / info is null");

		try {
			socket = new Socket(remoteHost, remotePort);
			socket.setSoTimeout(this.timeout);
			pl("socket created");
			writer = new ObjectOutputStream(socket.getOutputStream());
			pl("output stream created, writing info: " + info);
			writer.writeObject(info);
			writer.flush();
			pl("message sent");

			bufferedreader = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			pl("got response from ACT: " + bufferedreader);

			responseString = bufferedreader.readLine();
			pl("responseString: " + responseString);
			
			setStatus(BaseConstants.STATUS_SUCCESS);
		} catch (UnknownHostException unknownhostexception) {
			pe("Don't know about host:" + remoteHost);
			unknownhostexception.printStackTrace();
			setStatus(BaseConstants.STATUS_ERROR);
		} catch (IOException ioexception) {
			pe("IOException: " + ioexception.getMessage());
			ioexception.printStackTrace();
			setStatus(BaseConstants.STATUS_ERROR);
		} catch (Exception exception) {
			pe("Error: " + exception.getMessage());
			exception.printStackTrace();
			setStatus(BaseConstants.STATUS_ERROR);
		} finally {
			try {
				if (writer != null) {
					writer.close();
					pl("writer closed");
				} else {
					pl("writer is null, nothing to close");
				}
			} catch (Exception exception2) {
				pe("Error closing output stream: " + exception2.getMessage());
			}
			try {
				if (bufferedreader != null) {
					bufferedreader.close();
					pl("bufferedreader closed");
				} else {
					pl("bufferedreader is null, nothing to close");
				}
			} catch (Exception exception3) {
				pe("Error closing input stream: " + exception3.getMessage());
			}
			try {
				if (socket != null) {
					socket.close();
					pl("socket closed");
				} else {
					pl("socket is null, nothing to close");
				}
			} catch (Exception exception4) {
				pe("Error closing socket: " + exception4.getMessage());
			}
			pl("Request complete. Bye");
		}

		return this.status;
	}

	protected static void pl(String msg) {
		logger.info(msg);
	}

	protected static void pe(String msg) {
		System.err.println(msg);
	}
	/*
	 * protected void init(String s) throws Exception { pl("Waking up..."); //
	 * String s1 = null; props = Utils.getConfigFromFile(s); this.remoteHost =
	 * props.getProperty("remote_host"); this.remotePort =
	 * Integer.parseInt(props.getProperty("remote_port"));
	 * 
	 * // System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
	 * props.list(System.out); pl("Init completed."); }
	 */

}
