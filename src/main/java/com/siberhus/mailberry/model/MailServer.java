package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="mail_server")
public class MailServer extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static enum ConnectionSecurity {
		NONE, SSL, STARTTLS
	}
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull @Size(max=256)
	@Column(name="server_name", length=256, nullable=false)
	private String serverName;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
	@NotNull
	@Column(name="status", length=8, nullable=false)
	private String status = Status.ACTIVE;
	
	//*************** SMTP *********************//
	@NotNull @Size(max=256)
	@Column(name="smtp_server", length=256, nullable=false)
	private String smtpServer;
	
	@NotNull @Min(1) @Max(Integer.MAX_VALUE)
	@Column(name="smtp_port", nullable=false)
	private int smtpPort = 25;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="smtp_conn_security", length=8, nullable=false)
	private ConnectionSecurity smtpConnectionSecurity = ConnectionSecurity.NONE;
	
	@Size(max=1024)
	@Column(name="smtp_properties", length=1024)
	private String smtpProperties;
	
	@Min(1) @Max(200) 
	@Column(name="smtp_timeout")
	private Integer smtpTimeout = 60;
	
	//*************** POP3 *********************//
	
	@Size(max=256)
	@Column(name="pop3_server", length=256)
	private String pop3Server;
	
	@Min(1) @Max(Integer.MAX_VALUE)
	@Column(name="pop3_port")
	private int pop3Port = 110;
	
	@Enumerated(EnumType.STRING)
	@Column(name="pop3_conn_security", length=8)
	private ConnectionSecurity pop3ConnectionSecurity = ConnectionSecurity.NONE;
	
	@Size(max=1024)
	@Column(name="pop3_properties", length=1024)
	private String pop3Properties;
	
	@Min(1) @Max(200) 
	@Column(name="pop3_timeout")
	private Integer pop3Timeout = 60;
	
	@Column(name="public_server")
	private boolean publicServer = false;
	
	@OneToMany(mappedBy="mailServer", cascade=CascadeType.ALL)
	private List<MailAccount> mailAccounts;
	
	@Override
	public String toString(){
		return serverName;
	}

	public void addMailAccount(MailAccount mailAccount){
		if(mailAccounts==null){
			mailAccounts = new ArrayList<MailAccount>();
		}
		mailAccounts.add(mailAccount);
		mailAccount.setMailServer(this);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
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

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public ConnectionSecurity getSmtpConnectionSecurity() {
		return smtpConnectionSecurity;
	}

	public void setSmtpConnectionSecurity(ConnectionSecurity smtpConnectionSecurity) {
		this.smtpConnectionSecurity = smtpConnectionSecurity;
	}

	public String getSmtpProperties() {
		return smtpProperties;
	}

	public void setSmtpProperties(String smtpProperties) {
		this.smtpProperties = smtpProperties;
	}
	
	public Integer getSmtpTimeout() {
		return smtpTimeout;
	}

	public void setSmtpTimeout(Integer smtpTimeout) {
		this.smtpTimeout = smtpTimeout;
	}

	public String getPop3Server() {
		return pop3Server;
	}

	public void setPop3Server(String pop3Server) {
		this.pop3Server = pop3Server;
	}

	public int getPop3Port() {
		return pop3Port;
	}

	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}

	public ConnectionSecurity getPop3ConnectionSecurity() {
		return pop3ConnectionSecurity;
	}

	public void setPop3ConnectionSecurity(ConnectionSecurity pop3ConnectionSecurity) {
		this.pop3ConnectionSecurity = pop3ConnectionSecurity;
	}

	public String getPop3Properties() {
		return pop3Properties;
	}

	public void setPop3Properties(String pop3Properties) {
		this.pop3Properties = pop3Properties;
	}
	
	public Integer getPop3Timeout() {
		return pop3Timeout;
	}

	public void setPop3Timeout(Integer pop3Timeout) {
		this.pop3Timeout = pop3Timeout;
	}
	
	public boolean isPublicServer() {
		return publicServer;
	}

	public void setPublicServer(boolean publicServer) {
		this.publicServer = publicServer;
	}

	public List<MailAccount> getMailAccounts() {
		return mailAccounts;
	}

	public void setMailAccounts(List<MailAccount> mailAccounts) {
		this.mailAccounts = mailAccounts;
	}
	
}
