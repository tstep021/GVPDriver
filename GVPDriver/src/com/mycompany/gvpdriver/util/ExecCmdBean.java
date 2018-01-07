package com.mycompany.gvpdriver.util;



/** @copyright  2007-2013 mycompany */

import java.io.*;

import org.apache.log4j.Logger;

import com.mycompany.gvpdriver.base.*;

/**
 * @file ExecCmdBean.java
 * 
 * @description This class executes the shell command
 * 
 * @author Tatiana Stepourska
 * 
 * @version 1.0
 */

public class ExecCmdBean {
	private static final Logger logger = Logger.getLogger(ExecCmdBean.class);

	private String command = null;
	private String result = null;
	private String error = null;

	/**
	 * Sets command string for execution
	 * 
	 * @param c
	 */
	public void setCommand(String c) {
		this.command = c;
	}

	/**
	 * Gets execution command string
	 * 
	 * @return String
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * Sets execution result string
	 * 
	 * @param c
	 */
	public void setResult(String c) {
		this.result = c;
	}

	/**
	 * Returns execution result string
	 * 
	 * @return String
	 */
	public String getResult() {
		return this.result;
	}

	/**
	 * Sets execution error string
	 * 
	 * @param c
	 */
	public void setError(String c) {
		this.error = c;
	}

	/**
	 * Returns execution error string
	 * 
	 * @return String
	 */
	public String getError() {
		return this.error;
	}

	/**
	 * Invokes the execution of the supplied command
	 * 
	 * @return int --Execution status
	 */
	public int invoke() {
		return invoke(this.command);
	}

	/**
	 * Invokes the execution of the supplied command
	 * 
	 * @param cmd
	 * @return int --Execution status
	 */
	public int invoke(String cmd) {
		Runtime rt = Runtime.getRuntime();
		Process process = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		BufferedReader err = null;
		String line = "";
		int read = 0;
		char[] errBuf = new char[1000];
		this.result = "";
		int status = BaseConstants.STATUS_ERROR;
		String tmpStr = null;

		try {
			logger.info("Command: " + cmd);
			process = rt.exec(cmd);

			out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			logger.trace("got process output buffer: " + out);
			// out.
			// out.write(cmd);

			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			logger.trace("got response from process: " + in);

			err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			logger.trace("got error stream from process: " + err);

			logger.debug("reading the result buffer...");
			while ((line = in.readLine()) != null) {
				logger.info("result buffer: " + line);
				this.result = this.result + line;
				// this.result = new String(sb);
			}

			status = BaseConstants.STATUS_SUCCESS;

			if (err != null) {
				logger.info("reading the error buffer...");
				read = err.read(errBuf);
				logger.debug("read the buffer: " + read);

				if (read == -1) {
					// TODO: not an error!!
					logger.debug("The Error stream contains -1, returning success");
					status = BaseConstants.STATUS_SUCCESS;
					return status;
				}

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < read; i++) {
					sb.append(errBuf[i]);
				}
				errBuf = null;

				// this.error = new String(sb);
				tmpStr = new String(sb);
				logger.debug("tmpStr: " + tmpStr);

				// in the error buffer zap script passes a success message,
				// handle it
				if (tmpStr != null ) {
					this.error = tmpStr;
					if (this.error != null && this.error.trim().length() > 0)
						logger.info("Received error from cmd error buffer: "+ this.error);

					status = BaseConstants.STATUS_ERROR;
				}
			}
		} catch (IOException ioe) {
			this.result = null;
			this.error = ioe.getMessage();
			status = BaseConstants.STATUS_ERROR;
			logger.error("Error: " + ioe.getMessage());
			// ioe.printStackTrace();
			StackTraceElement[] trace = ioe.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}
		} catch (Exception e) {
			this.result = null;
			this.error = e.getMessage();
			status = BaseConstants.STATUS_ERROR;
			logger.error("Error: " + e.getMessage());
			// e.printStackTrace();
			StackTraceElement[] trace = e.getStackTrace();
			if (trace != null) {
				for (int i = 0; i < trace.length; i++) {
					logger.error(trace[i]);
				}
			}
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception ioe) {
				logger.error("Error closing resources: " + ioe.getMessage());
			}

			try {
				err.close();
			} catch (Exception ioe) {
				logger.error("Error closing resources: " + ioe.getMessage());
			}

			try {
				in.close();
			} catch (Exception ioe) {
				logger.error("Error closing resources: " + ioe.getMessage());
			}

			try {
				process.destroy();
			} catch (Exception e) {
				logger.error("Error disposing the process: " + e.getMessage());
				e.printStackTrace();
			}

		}

		logger.debug("returning status: " + status);
		return status;
	} // end of method



	/**
	 * Reads the command output into array
	 * 
	 * @param cmd
	 * 
	 * @return ArrayList
	 */
/*	private ArrayList<String> getResultsArray(String cmd) {
		ArrayList<String> resultList = null;

		Runtime rt = Runtime.getRuntime();
		Process process = null;
		BufferedReader in = null;
		BufferedReader err = null;
		String line = "";
		int read = 0;
		char[] errBuf = new char[1000];

		try {
			logger.info("Command: " + cmd);
			process = rt.exec(cmd);

			in = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			logger.debug("got response from process: " + in);

			err = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));
			logger.debug("got error stream from process: " + err);

			if (in != null) {
				resultList = new ArrayList<String>();

				logger.debug("reading the result buffer...");
				while ((line = in.readLine()) != null) {
					logger.info("line: " + line);
					if (line == null)
						line = "";

					resultList.add(line);
					resultList.add("\r");
				}
			}

			if (err != null) {
				logger.debug("reading the error buffer...");
				read = err.read(errBuf);
				logger.debug("read the buffer: " + read);

				StringBuffer sb = new StringBuffer();

				for (int i = 0; i < read; i++) {
					sb.append(errBuf[i]);
				}
				errBuf = null;
				this.error = new String(sb);
				logger.info("error: " + this.error);
			} else {
				logger.info("The Error stream is empty");
			}

		} catch (IOException ioe) {

			this.error = ioe.getMessage();
			logger.error("Error: " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (Exception e) {

			this.error = e.getMessage();
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				err.close();
			} catch (Exception ioe) {
				logger.error("Error closing resources: " + ioe.getMessage());
			}

			try {
				in.close();
			} catch (Exception ioe) {
				logger.error("Error closing resources: " + ioe.getMessage());
			}

			try {
				process.destroy();
			} catch (Exception e) {
				logger.error("Error disposing the process: " + e.getMessage());
			}
		}

		return resultList;
	}*/
} // end of class