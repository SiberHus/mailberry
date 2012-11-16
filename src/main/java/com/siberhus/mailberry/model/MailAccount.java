package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="mail_accounts")
public class MailAccount extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull @ManyToOne
	@JoinColumn(name="mail_server_id", referencedColumnName="id", nullable=false)
	private MailServer mailServer;
	
	@NotNull @Size(max=256)
	@Column(name="display_name", length=256, unique=true, nullable=false)
	private String displayName;
	
	@NotNull @Email @Size(max=256)
	@Column(name="email", length=256, nullable=false)
	private String email;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
	@NotNull
	@Column(name="status", length=8, nullable=false)
	private String status = Status.ACTIVE;
	
	@Column(name="pop3_authen", nullable=false)
	private boolean pop3Authen = false;
	
	@Size(max=64)
	@Column(name="pop3_username", length=64)
	private String pop3Username;
	
	@Size(max=128)
	@Column(name="pop3_password", length=512)
	private String pop3Password;
	
	@Column(name="smtp_authen", nullable=false)
	private boolean smtpAuthen = false;
	
	@Size(max=64)
	@Column(name="smtp_username", length=64)
	private String smtpUsername;
	
	@Size(max=128)
	@Column(name="smtp_password", length=512)
	private String smtpPassword;
	
	@Min(0) @Max(Integer.MAX_VALUE)
	@Column(name="total_sent_mails", nullable=false)
	private int totalSentMails = 0;
	
	@Min(0) @Max(Integer.MAX_VALUE)
	@Column(name="successful_mails", nullable=false)
	private int successfulMails = 0;
	
	@Min(0) @Max(Integer.MAX_VALUE)
	@Column(name="failed_mails", nullable=false)
	private int failedMails = 0;
	
	@Override
	public String toString(){
		return email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MailServer getMailServer() {
		return mailServer;
	}

	public void setMailServer(MailServer mailServer) {
		this.mailServer = mailServer;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		this.status = status;
	}

	public boolean isPop3Authen() {
		return pop3Authen;
	}

	public void setPop3Authen(boolean pop3Authen) {
		this.pop3Authen = pop3Authen;
	}

	public String getPop3Username() {
		return pop3Username;
	}

	public void setPop3Username(String pop3Username) {
		this.pop3Username = pop3Username;
	}

	public String getPop3Password() {
		return pop3Password;
	}

	public void setPop3Password(String pop3Password) {
		this.pop3Password = pop3Password;
	}

	public boolean isSmtpAuthen() {
		return smtpAuthen;
	}

	public void setSmtpAuthen(boolean smtpAuthen) {
		this.smtpAuthen = smtpAuthen;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public int getTotalSentMails() {
		return totalSentMails;
	}

	public void setTotalSentMails(int totalSentMails) {
		this.totalSentMails = totalSentMails;
	}

	public int getSuccessfulMails() {
		return successfulMails;
	}

	public void setSuccessfulMails(int successfulMails) {
		this.successfulMails = successfulMails;
	}

	public int getFailedMails() {
		return failedMails;
	}

	public void setFailedMails(int failedMails) {
		this.failedMails = failedMails;
	}
	
}
