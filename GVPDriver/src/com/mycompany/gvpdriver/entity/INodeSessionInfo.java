package com.mycompany.gvpdriver.entity;

/** 
 * Placeholder for node specific session objects that contain 
 * node variables needed for the node execution
 * This object must be recycled (set to null) on each node CheckOut
 */
public interface INodeSessionInfo {
	public static String KEY = "node_session_info";
	
	/** 
	 * Should be implemented to clean up or reset 
	 * all variables, and used by nodes on CheckOut
	 */
	public void recycle();
}
