package com.mycompany.gvpdriver.entity;

public interface MediaInfo {
	public String getTmpName();
	public String getTmpPath();
	public String getRecordID();
	public String getBlobID();
	
	public void setTmpName(String s);
	public void setTmpPath(String s);
	public void setRecordID(String s);
	public void setBlobID(String s);
}
