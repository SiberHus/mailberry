package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Campaign.MessageType;

public class CampaignStep1FormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String CREATE_FROM_BLANK = "blank";
	public static final String CREATE_FROM_TEMPLATE = "template";
	public static final String CREATE_FROM_REPLICATION = "replicate";
	
	@NotNull
	private String createFrom = CREATE_FROM_BLANK;
	
	private Long templateId;
	
	private String templateName;
	
	private Long campaignId;
	
	private String campaignName;
	
	@NotNull
	private MessageType messageType = Campaign.MessageType.MIX;
	
	private boolean velocity = false;
	
	private String messageBodyText;
	
	private String messageBodyHtml;
	
	public String getCreateFrom() {
		return createFrom;
	}

	public void setCreateFrom(String createFrom) {
		this.createFrom = createFrom;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public boolean isVelocity() {
		return velocity;
	}

	public void setVelocity(boolean velocity) {
		this.velocity = velocity;
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
	
}
