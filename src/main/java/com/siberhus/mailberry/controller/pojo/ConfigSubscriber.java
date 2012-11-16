package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;

public class ConfigSubscriber implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Valid
	private FileImport fileImport;
	@Valid
	private OptOut optOut;
	@Valid
	private Rsvp rsvp;
	
	public ConfigSubscriber(){}
	public ConfigSubscriber(ConfigurationService config){
		this.fileImport = new FileImport(config);
		this.optOut = new OptOut(config);
		this.rsvp = new Rsvp(config);
	}
	
	public FileImport getFileImport() {
		return fileImport;
	}

	public void setFileImport(FileImport fileImport) {
		this.fileImport = fileImport;
	}

	public OptOut getOptOut() {
		return optOut;
	}

	public void setOptOut(OptOut optOut) {
		this.optOut = optOut;
	}

	public Rsvp getRsvp() {
		return rsvp;
	}

	public void setRsvp(Rsvp rsvp) {
		this.rsvp = rsvp;
	}
	
	public static class FileImport implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		@NotNull @Size(min=2, max=2048)
		private String sourceDir;
		@Size(min=2, max=2048)
		private String errorDir;
		
		public FileImport(){}
		public FileImport(ConfigurationService config){
			this.sourceDir = config.getValueAsString(Config.Subscriber.Import.SOURCE_DIR);
			this.errorDir = config.getValueAsString(Config.Subscriber.Import.ERROR_DIR);
		}
		public String getSourceDir() {
			return sourceDir;
		}
		public void setSourceDir(String sourceDir) {
			this.sourceDir = sourceDir;
		}
		public String getErrorDir() {
			return errorDir;
		}
		public void setErrorDir(String errorDir) {
			this.errorDir = errorDir;
		}
	}
	
	public static class OptOut implements Serializable{
		
		private static final long serialVersionUID = 1L;
		@Size(min=2, max=1024)
		private String pageSuccess;
		@Size(min=2, max=1024)
		private String pageConfig;
		@Size(min=1, max=256)
		private String linkName;
		public OptOut(){}
		public OptOut(ConfigurationService config){
			this.pageSuccess = config.getValueAsString(Config.Subscriber.OptOut.PAGE_SUCCESS);
			this.pageConfig = config.getValueAsString(Config.Subscriber.OptOut.PAGE_CONFIG);
			this.linkName = config.getValueAsString(Config.Subscriber.OptOut.LINK_NAME, "unsubscribe");
		}
		public String getPageSuccess() {
			return pageSuccess;
		}
		public void setPageSuccess(String pageSuccess) {
			this.pageSuccess = pageSuccess;
		}
		public String getPageConfig() {
			return pageConfig;
		}
		public void setPageConfig(String pageConfig) {
			this.pageConfig = pageConfig;
		}
		public String getLinkName() {
			return linkName;
		}
		public void setLinkName(String linkName) {
			this.linkName = linkName;
		}
	}
	
	public static class Rsvp implements Serializable{
		
		private static final long serialVersionUID = 1L;
		@Size(min=2, max=1024)
		private String pageSuccess;
		@Size(min=2, max=1024)
		private String pageConfig;
		@Size(min=1, max=256)
		private String linkNameAtt;
		@Size(min=1, max=256)
		private String linkNameMay;
		@Size(min=1, max=256)
		private String linkNameDec;
		public Rsvp(){}
		public Rsvp(ConfigurationService config){
			this.pageSuccess = config.getValueAsString(Config.Subscriber.RSVP.PAGE_SUCCESS);
			this.pageConfig = config.getValueAsString(Config.Subscriber.RSVP.PAGE_CONFIG);
			this.linkNameAtt = config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_ATT, "Attending");
			this.linkNameMay = config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_MAY, "Maybe");
			this.linkNameDec = config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_DEC, "Declined");
		}
		public String getPageSuccess() {
			return pageSuccess;
		}
		public void setPageSuccess(String pageSuccess) {
			this.pageSuccess = pageSuccess;
		}
		public String getPageConfig() {
			return pageConfig;
		}
		public void setPageConfig(String pageConfig) {
			this.pageConfig = pageConfig;
		}
		public String getLinkNameAtt() {
			return linkNameAtt;
		}
		public void setLinkNameAtt(String linkNameAtt) {
			this.linkNameAtt = linkNameAtt;
		}
		public String getLinkNameMay() {
			return linkNameMay;
		}
		public void setLinkNameMay(String linkNameMay) {
			this.linkNameMay = linkNameMay;
		}
		public String getLinkNameDec() {
			return linkNameDec;
		}
		public void setLinkNameDec(String linkNameDec) {
			this.linkNameDec = linkNameDec;
		}
	}
}
