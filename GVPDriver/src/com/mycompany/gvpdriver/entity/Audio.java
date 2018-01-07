package com.mycompany.gvpdriver.entity;

import java.io.Serializable;

import com.mycompany.gvpdriver.base.BaseConstants;
import com.mycompany.vxml.facade.*;

public class Audio implements IAudio, Serializable //XMLSerializable 
{
	private static final long serialVersionUID = 4616645274016051681L;
	//private String filename = null;
	private String text		= null;
	private String name		= null;
	private String type		= null;
	private String language = "en"; 
	private int sequenceNumber = -1;
	private String extension = BaseConstants.FILE_EXT_WAV;

	private String src	=  null; //uri
	
	public final void setSrc(String fn) {
		this.src = fn;
	}
	
	public final String getSrc() {
		return this.src;
	}
	
	public final void setText(String t) {
		this.text = t;
	}
	
	public final String getText() {
		return this.text;
	}

	public final void setType(String t) {
		this.type = t;
	}
	
	public final String getType() {
		return this.type;
	}
	
	public final void setLanguage(String t) {
		this.language = t;
	}
	
	public final String getLanguage() {
		return this.language;
	}

	public void setExtension(String t) {
		this.extension = t;
	}
	
	public String getExtension() {
		return this.extension;
	}
	
	public void setName(String t) {
		this.name = t;
	}
	
	public String getName() {
		return this.name;
	}


	public final void setSequenceNumber(int n) {
		this.sequenceNumber = n;
	}
	
	public final int getSequenceNumber() {
		return this.sequenceNumber;
	}

	public String toString() {
		//StringBuffer sb = new StringBuffer();		
		//sb.append(this.getSrc());// + ";tts::" + this.getText());		
		return this.getSrc();//sb.toString();
	}
	
	public final void append(VXML vxml, String bargein, String langMode, String cond) {	

		vxml.AudioStart(this.getSrc()); 
		if(this.getText()==null)
			vxml.Text("  ");
		else
			vxml.Text(this.getText());
		vxml.AudioEnd();
	}

	
	public final void appendPrompt(VXML vxml, String bargein, String langMode, String cond) {
		
		vxml.PromptStart(
					bargein, 	//bargein
					null,		//bargeintype 
					cond, 		//cond
					null, 		//count
					null, 		//timeout	//default 10s
					null, 		//xml:base
					langMode, 	//xml:lang
					null, 		//gvp:langexpr
					null		//gvp:ttsengine
					);	
			
			vxml.AudioStart(this.getSrc()); 
			if(this.getText()==null)
				vxml.Text("  ");
			else
				vxml.Text(this.getText());
			vxml.AudioEnd();
			
			vxml.PromptEnd();
	}
}