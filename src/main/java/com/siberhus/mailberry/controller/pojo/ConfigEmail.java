package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;

public class ConfigEmail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Size(min=1, max=2048)
	private String mailUserAgent;
	
	@Email @Size(max=256)
	private String defaultFromEmail;
	@Size(max=128)
	private String defaultFromName;
	@Email @Size(max=256)
	private String defaultReplyToEmail;
	
	@Valid
	private Sending sending;
	@Valid
	private Template template;
	@Valid
	private SpamChecker spamChecker;
	
	public ConfigEmail(){}
	public ConfigEmail(ConfigurationService config){
		this.mailUserAgent = config.getValueAsString(Config.Email.MAIL_USER_AGENT);
		this.defaultFromEmail = config.getValueAsString(Config.Email.DEFAULT_FROM_EMAIL);
		this.defaultReplyToEmail = config.getValueAsString(Config.Email.DEFAULT_REPLY_TO_EMAIL);
		this.sending = new Sending(config);
		this.template = new Template(config);
		this.spamChecker = new SpamChecker(config);
	}
	
	public String getMailUserAgent() {
		return mailUserAgent;
	}
	public void setMailUserAgent(String mailUserAgent) {
		this.mailUserAgent = mailUserAgent;
	}
	public String getDefaultFromEmail() {
		return defaultFromEmail;
	}
	public void setDefaultFromEmail(String defaultFromEmail) {
		this.defaultFromEmail = defaultFromEmail;
	}
	public String getDefaultFromName() {
		return defaultFromName;
	}
	public void setDefaultFromName(String defaultFromName) {
		this.defaultFromName = defaultFromName;
	}
	public String getDefaultReplyToEmail() {
		return defaultReplyToEmail;
	}
	public void setDefaultReplyToEmail(String defaultReplyToEmail) {
		this.defaultReplyToEmail = defaultReplyToEmail;
	}
	public Sending getSending() {
		return sending;
	}
	public void setSending(Sending sending) {
		this.sending = sending;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public SpamChecker getSpamChecker() {
		return spamChecker;
	}
	public void setSpamChecker(SpamChecker spamChecker) {
		this.spamChecker = spamChecker;
	}

	
	public static class Sending implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		@Min(0) @Max(60)
		private int pauseBetweenMessages;//second
		@Min(1) @Max(100)
		private int numberOfThreads;
		@Min(1) @Max(2000)
		private int messagesPerConnection;
		@Min(0) @Max(10)
		private int numberOfAttempts;
		@Min(0) @Max(60)
		private int pauseBetweenAttempts;
		private boolean blockOnFail;
		@Min(0) @Max(60) 
		private int timeout;//unused - use SmtpProfile.timeout instead.
		
		public Sending(){}
		public Sending(ConfigurationService config){
			this.pauseBetweenMessages = config.getValue(Config.Email.Sending
					.PAUSE_BETWEEN_MESSAGES, Integer.class, 0);
			this.numberOfThreads = config.getValue(Config.Email.Sending
					.NUMBER_OF_THREADS, Integer.class, 4);
			this.messagesPerConnection = config.getValue(Config.Email.Sending
					.MESSAGES_PER_CONNECTION, Integer.class, 20);
			this.numberOfAttempts = config.getValue(Config.Email.Sending
					.NUMBER_OF_ATTEMPTS, Integer.class, 2);
			this.blockOnFail = config.getValue(Config.Email.Sending
					.BLOCK_ON_FAIL, Boolean.class, false);
			this.timeout = config.getValue(Config.Email.Sending
					.TIMEOUT, Integer.class, 30);
		}
		public int getPauseBetweenMessages() {
			return pauseBetweenMessages;
		}
		public void setPauseBetweenMessages(int pauseBetweenMessages) {
			this.pauseBetweenMessages = pauseBetweenMessages;
		}
		public int getNumberOfThreads() {
			return numberOfThreads;
		}
		public void setNumberOfThreads(int numberOfThreads) {
			this.numberOfThreads = numberOfThreads;
		}
		public int getMessagesPerConnection() {
			return messagesPerConnection;
		}
		public void setMessagesPerConnection(int messagesPerConnection) {
			this.messagesPerConnection = messagesPerConnection;
		}
		public int getNumberOfAttempts() {
			return numberOfAttempts;
		}
		public void setNumberOfAttempts(int numberOfAttempts) {
			this.numberOfAttempts = numberOfAttempts;
		}
		public int getPauseBetweenAttempts() {
			return pauseBetweenAttempts;
		}
		public void setPauseBetweenAttempts(int pauseBetweenAttempts) {
			this.pauseBetweenAttempts = pauseBetweenAttempts;
		}
		public boolean isBlockOnFail() {
			return blockOnFail;
		}
		public void setBlockOnFail(boolean blockOnFail) {
			this.blockOnFail = blockOnFail;
		}
		public int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
	}
	
	public static class Template implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private String velocityStorePath;
		private String velocityEncoding;
		public Template(){}
		public Template(ConfigurationService config){
			this.velocityStorePath = config.getValueAsString(
					Config.Email.Template.VELOCITY_STORE_PATH);
			this.velocityEncoding = config.getValueAsString(
					Config.Email.Template.VELOCITY_ENCODING, "UTF-8");
		}
		public String getVelocityStorePath() {
			return velocityStorePath;
		}
		public void setVelocityStorePath(String velocityStorePath) {
			this.velocityStorePath = velocityStorePath;
		}
		public String getVelocityEncoding() {
			return velocityEncoding;
		}
		public void setVelocityEncoding(String velocityEncoding) {
			this.velocityEncoding = velocityEncoding;
		}
	}
	
	public static class SpamChecker implements Serializable{
		
		private static final long serialVersionUID = 1L;
		@NotNull @Size(max=1024)
		private String host;
		@Min(1) @Max(Integer.MAX_VALUE)
		private int port;
		public SpamChecker(){}
		public SpamChecker(ConfigurationService config){
			this.host = config.getValueAsString(Config.Email.SpamChecker.HOST, "localhost");
			this.port = config.getValue(Config.Email.SpamChecker.PORT, Integer.class, 783);
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
	}
}
