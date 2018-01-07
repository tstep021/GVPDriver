package com.mycompany.gvpdriver.callpark;

import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * singleton class
 * 
 * 
 */
public class Register {
	static private Logger logger	= Logger.getLogger(Register.class);		
	static private HashMap<String,HashMap<String,String>> callmap = new HashMap<String,HashMap<String,String>>();
	private static Register register = null;

	private Register() {}
	
	public static synchronized Register getInstance(){
		if ( register == null )
			register = new Register();
		return register;    
	}
	
	public HashMap<String,HashMap<String,String>> getCallList()	{
		return callmap;
	}
	
	public synchronized HashMap<String,String> remove(String acSharedCallId){
		logger.info("removing "+ acSharedCallId);
		return callmap.remove(acSharedCallId);
	}
	
	public synchronized void add(String acSharedCallId, HashMap<String,String> call) throws DuplicateRequestException{
		if(callmap.containsKey(acSharedCallId)){
			logger.error("failed to add duplicate call ID: " + acSharedCallId);
			throw new DuplicateRequestException("Failed to park: call with id "+acSharedCallId + " is already parked");
		}
		
		callmap.put(acSharedCallId, call);
		logger.info("added " + acSharedCallId);
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public String toString(){	
		Iterator<String> it;
		
		try {
			it = callmap.keySet().iterator();
		}
		catch(Exception e){
			logger.error("Error getting key set: " + e.getMessage());
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		while(it.hasNext()){
			sb.append("\n").append(it.next());
		}

		return sb.toString();
	}	
}