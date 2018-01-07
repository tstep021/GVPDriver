package com.mycompany.gvpdriver.event;

/**
 * @author tatiana.stepourska
 * 2012-10-11
 */
public class RejectCallEvent extends Exception {

	private static final long serialVersionUID = -7721450494318019995L;

	public RejectCallEvent(){
		super();
	}
	
	public RejectCallEvent(String msg){
		super(msg);
	}
}
