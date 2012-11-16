package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="campaigns", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "campaign_name"}))
public class Campaign extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static class Status{
		public static final String DRAFT = "DRA";
		public static final String IN_PROGRESS = "INP";
		public static final String SENT = "SEN";
		public static final String CANCELLED = "CAN";
		public static final String SCHEDULED = "SCH";
		public static final String PAUSED = "PAU";//TODO: unused
		private static final String VALUES[] = new String[]{DRAFT,IN_PROGRESS,SENT,CANCELLED,SCHEDULED,PAUSED}; 
	}
	
	public static enum MessageType {
		TEXT, HTML, MIX
	}
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="list_id", referencedColumnName="id", nullable=false)
	private SubscriberList list;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="mail_account_id", referencedColumnName="id", nullable=false)
	private MailAccount mailAccount;
	
	@NotNull @Size(min=1, max=256)
	@Column(name="campaign_name", length=256, nullable=false)
	private String campaignName;
	
	@Size(max=16)
	@Column(name="campaign_color", length=16)
	private String campaignColor;
	
	@Size(max=256)
	@Column(name="capaign_image", length=256)
	private String campaignImage;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
	@NotNull
	@Column(name="status", length=16, nullable=false)
	private String status = Status.DRAFT;
	
	@DateTimeFormat(iso=ISO.DATE)
	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date startDate;
	
	@DateTimeFormat(iso=ISO.DATE)
	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="blacklist_enabled")
	private boolean blacklistEnabled = true;
	
	@NotNull @Size(max=256)
	@Column(name="from_email", length=256, nullable=false)
	private String fromEmail;
	
	@Size(max=128)
	@Column(name="from_name", length=128)
	private String fromName; //nullable
	
	@Size(max=256)
	@Column(name="reply_to_email", length=256)
	private String replyToEmail;
	
	@NotNull @Size(max=512)
	@Column(name="mail_subject", length=512, nullable=false)
	private String mailSubject;
	
	@Min(value=1) @Max(value=5)
	@Column(name="mail_priority")
	private Integer mailPriority;
	
	@NotNull @Size(max=16)
	@Column(name="message_charset", length=16, nullable=false)
	private String messageCharset = "UTF-8";
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="message_type", length=16, nullable=false)
	private MessageType messageType;	
	
//	@Lob
	@Basic(fetch=FetchType.LAZY)
//	@Column(name="message_body_text", nullable=false, columnDefinition="CLOB NOT NULL")
	@Column(name="message_body_text", nullable=false, columnDefinition="TEXT")
	private String messageBodyText;
	
//	@Lob
	@Basic(fetch=FetchType.LAZY)
//	@Column(name="message_body_html", nullable=false, columnDefinition="CLOB NOT NULL")
	@Column(name="message_body_html", nullable=false, columnDefinition="TEXT")
	private String messageBodyHtml;
	
	@Column(name="velocity", nullable=false)
	private boolean velocity = false;
	
	@Column(name="message_file", length=128)
	private String messageFile;
	
	@OneToMany(mappedBy="campaign", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	private List<Attachment> attachments;
	
	@Column(name="inline_resource", nullable=false)
	private boolean inlineResource = false;
	
	@Column(name="trackable", nullable=false)
	private boolean trackable = true;
	
	@Column(name="clickstream", nullable=false)
	private boolean clickstream = true;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="scheduled_time")
	private Date scheduledTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_time")
	private Date sendTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="finish_time")
	private Date finishTime;
	
	@Column(name="emails")
	private int emails;
	
	@OneToMany(mappedBy="campaign", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	@OrderBy("clickCount DESC")
	private List<Clickstream> clickstreams;
	
	public void addAttachment(Attachment attachment){
		if(this.attachments==null){
			this.attachments = new ArrayList<Attachment>();
		}
		this.attachments.add(attachment);
		attachment.setCampaign(this);
	}
	
	public void addClickstream(Clickstream clickstream){
		if(this.clickstreams==null){
			this.clickstreams = new ArrayList<Clickstream>();
		}
		this.clickstreams.add(clickstream);
		clickstream.setCampaign(this);
	}
	
	@Override
	public String toString(){
		return campaignName;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignColor() {
		return campaignColor;
	}

	public void setCampaignColor(String campaignColor) {
		this.campaignColor = campaignColor;
	}

	public String getCampaignImage() {
		return campaignImage;
	}

	public void setCampaignImage(String campaignImage) {
		this.campaignImage = campaignImage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(!ArrayUtils.contains(Status.VALUES, status)){
			throw new IllegalArgumentException("status must be any of "
					+Arrays.toString(Status.VALUES));
		}
		this.status = status;
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

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
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

	public String getMessageFile() {
		return messageFile;
	}

	public void setMessageFile(String messageFile) {
		this.messageFile = messageFile;
	}
	
	public boolean isVelocity() {
		return velocity;
	}

	public void setVelocity(boolean velocity) {
		this.velocity = velocity;
	}

	public boolean isInlineResource() {
		return inlineResource;
	}

	public void setInlineResource(boolean inlineResource) {
		this.inlineResource = inlineResource;
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

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		if(attachments!=null){
			for(Attachment attachment: attachments){
				attachment.setCampaign(this);
			}
		}
		this.attachments = attachments;
	}
	
	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public int getEmails() {
		return emails;
	}

	public void setEmails(int emails) {
		this.emails = emails;
	}
	
	public List<Clickstream> getClickstreams() {
		return clickstreams;
	}

	public void setClickstreams(List<Clickstream> clickstreams) {
		this.clickstreams = clickstreams;
	}
	
	
}
