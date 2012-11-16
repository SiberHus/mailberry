package com.siberhus.mailberry.service.pojo;

import java.util.List;

public class TemplateEmailMessage extends EmailMessage {
	
	protected boolean multipart = false;
	protected String userAgent;
	protected List<?> blacklist;
	
	public boolean isMultipart() {
		return multipart;
	}
	
	public void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public List<?> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<?> blacklist) {
		this.blacklist = blacklist;
	}
	
}
