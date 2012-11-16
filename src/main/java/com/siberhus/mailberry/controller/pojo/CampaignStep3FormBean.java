package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.Attachment;

public class CampaignStep3FormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull @Size(max=256)
	private String fromEmail;
	@Size(max=128)
	private String fromName;
	@Size(max=256)
	private String replyToEmail;
	@Min(1) @Max(5)
	private Integer mailPriority;
	@NotNull
	private String messageCharset = "UTF-8";
	@NotNull @Size(max=512)
	private String mailSubject;
	private String messageBodyText;
	private String messageBodyHtml;
	private boolean inlineResource = false;
	private List<Attachment> attachments;
	private String status;
	
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
	public Integer getMailPriority() {
		return mailPriority;
	}
	public void setMailPriority(Integer mailPriority) {
		this.mailPriority = mailPriority;
	}
	public String getMessageCharset() {
		return messageCharset;
	}
	public void setMessageCharset(String messageCharset) {
		this.messageCharset = messageCharset;
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
	public boolean isInlineResource() {
		return inlineResource;
	}
	public void setInlineResource(boolean inlineResource) {
		this.inlineResource = inlineResource;
	}
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
