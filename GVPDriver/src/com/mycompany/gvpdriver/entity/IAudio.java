package com.mycompany.gvpdriver.entity;

import com.mycompany.vxml.facade.*;

public interface IAudio 
{	
	public void setSrc(String fn);	
	public String getSrc(); 
	public void setText(String t);
	public String getText();
	public void setType(String t);
	public String getType();
	public void setLanguage(String t);
	public String getLanguage();
	public void setSequenceNumber(int n);	
	public int getSequenceNumber();
	public void append(VXML vxml, String bargein, String langMode, String cond);	
	public void appendPrompt(VXML vxml, String bargein, String langMode, String cond);
}
