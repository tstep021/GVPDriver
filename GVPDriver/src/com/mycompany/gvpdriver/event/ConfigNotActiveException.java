package com.mycompany.gvpdriver.event;

import com.mycompany.gvpdriver.base.*;

/**
 * This exception is being thrown when something is not 
 * completed in the provisioning tables, for example, 
 * DNIS is not provisioned
 * 
 * @author tatiana.stepourska
 *
 */
public class ConfigNotActiveException extends Exception {
	private static final long serialVersionUID = -4348836601520804372L;

	public int getErrcode() {
		return BaseConstants.STATUS_CONFIG_NOT_ACTIVE;
	}

	public ConfigNotActiveException(){}

	public ConfigNotActiveException(String msg){super(msg);}
}
