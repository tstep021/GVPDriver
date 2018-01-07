package com.mycompany.gvpdriver.callpark.sipbeans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//import java.util.Timer;
//import java.util.TimerTask;
import org.apache.log4j.Logger;

public class SipMessageSender {
	private static Logger logger = Logger.getLogger(SipMessageSender.class);
    private Socket socket;
    
    public SipMessageSender(Socket s) {
    	this.socket = s;
    }

    public synchronized String sendMessage(SipMessage sipMessage) throws Exception {
        logger.debug("sendMessage: " + sipMessage);
        if (sipMessage == null) 
            return null;
    
        return sendBytes(sipMessage.toString().getBytes());
    }

    public synchronized String sendBytes(byte[] bytes) throws IOException {
        DataOutputStream os = null;
        BufferedReader reader = null;
        String responseString = null;
        //DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
        //        inetAddress, port);
        if(logger.isTraceEnabled()){
        logger.trace("sendBytes: sending " + bytes.length + " bytes");
        }
        try {
        	
        	os = new DataOutputStream(socket.getOutputStream());//.send(packet);
        	os.write(bytes);
        	if(logger.isTraceEnabled())
            logger.trace("sendBytes: packet sent");
        	reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//("got response from server: " + reader);

			responseString = reader.readLine();
			logger.info("sendBytes: responseString: " + responseString);    	
        } catch (Throwable t) {
            logger.error("throwable", new Exception(t));
        }
        finally{
        	try {
        		os.flush();
            	os.close();
        	}
        	catch(Exception e){
        		logger.error("sendBytes: Error closing stream: " + e.getMessage());
        	}
        }
        return responseString;
    }
}