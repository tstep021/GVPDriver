package com.mycompany.gvpdriver.event;

import com.mycompany.gvpdriver.base.*;

/**
 * This exception is thrown if configuration is not found for the DNIS
 * 
 * @author tatiana.stepourska
 *
 */
public class ConfigNotFoundException extends Exception {
	private static final long serialVersionUID = -4348836601520804372L;
	
	public int getErrcode() {
		return BaseConstants.STATUS_NO_CONFIG_FOUND;
	}

	public ConfigNotFoundException(){}

	public ConfigNotFoundException(String msg){	super(msg);}
}
