package com.siberhus.mailberry.service.pojo;

import java.util.List;

import com.siberhus.mailberry.model.Attachment;

public abstract class EmailMessage {
	
	protected String toEmail;
	protected String fromEmail;
	protected String fromName;
	protected String replyToEmail;
	protected String mailSubject;
	protected String messageBodyText;
	protected String messageBodyHtml;
	
	protected List<Attachment> attachments;
	
	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getReplyToEmail() {
		return replyToEmail;
	}

	public void setReplyToEmail(String replyToEmail) {
		this.replyToEmail = replyToEmail;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMessageBodyText() {
		return messageBodyText;
	}

	public void setMessageBodyText(String messageBodyText) {
		this.messageBodyText = messageBodyText;
	}

	public String getMessageBodyHtml() {
		return messageBodyHtml;
	}

	public void setMessageBodyHtml(String messageBodyHtml) {
		this.messageBodyHtml = messageBodyHtml;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
}
