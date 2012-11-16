package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class CampaignStep2FormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String campaignName;
	private String campaignImage;
	private String campaignColor;
	@NotNull
	private Long listId;
	private String listName;
	@NotNull
	private Long mailAccountId;
	private String description;
	
	@DateTimeFormat(iso=ISO.DATE)
	private Date startDate;
	@DateTimeFormat(iso=ISO.DATE)
	private Date endDate;
	private boolean blacklistEnabled = true;
	private boolean trackable = true;
	private boolean clickstream = true;
	
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getCampaignImage() {
		return campaignImage;
	}
	public void setCampaignImage(String campaignImage) {
		this.campaignImage = campaignImage;
	}
	public String getCampaignColor() {
		return campaignColor;
	}
	public void setCampaignColor(String campaignColor) {
		this.campaignColor = campaignColor;
	}
	public Long getListId() {
		return listId;
	}
	public void setListId(Long listId) {
		this.listId = listId;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public Long getMailAccountId() {
		return mailAccountId;
	}
	public void setMailAccountId(Long mailAccountId) {
		this.mailAccountId = mailAccountId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isBlacklistEnabled() {
		return blacklistEnabled;
	}
	public void setBlacklistEnabled(boolean blacklistEnabled) {
		this.blacklistEnabled = blacklistEnabled;
	}
	public boolean isTrackable() {
		return trackable;
	}
	public void setTrackable(boolean trackable) {
		this.trackable = trackable;
	}
	public boolean isClickstream() {
		return clickstream;
	}
	public void setClickstream(boolean clickstream) {
		this.clickstream = clickstream;
	}
	
	
	
}
