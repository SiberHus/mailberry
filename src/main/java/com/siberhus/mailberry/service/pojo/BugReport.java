package com.siberhus.mailberry.service.pojo;

import java.io.Serializable;
import java.util.Date;

public class BugReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String stackTrace;//error detail
	
	//Step to Reproduce Bugs
	private String description;
	
	//application.getServerInfo()
	private String serverInfo; 
	
	//System.getProperty("java.version")
	private String javaVersion;
	
	//application.getMajorVersion() %>.<%= application.getMinorVersion()
	private String servletVersion;
	
	//JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion()
	private String jspVersion;
	
	/*
	 * os.name = Windows XP
	 * os.version = 5.1
	 * os.arch = x86
	 */
	private String osName;
	
	private String osVersion;
	
	private String osArch;
	
	/*
	 * user.language = th
	 * user.country = TH
	 * user.timezone = Asia/Bangkok
	 */
	private String userLanguage;
	
	private String userCountry;
	
	private String userTimezone;
	
	//java.io.tmpdir = C:\DOCUME~1\HUSSAC~1\LOCALS~1\Temp\
	private String tmpDir;
	
	//file.encoding = MS874
	private String fileEncoding;
	
	private Integer maxMemory; //MB
	
	private Date reportDate;
	
	private String reporter;//firstName + lastName
	
}
