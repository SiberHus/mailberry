package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="blacklist", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "email"}))
public class Blacklist extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Size(min=0, max=256)
	@Column(name="full_name", length=256)
	private String fullName;
	
	@Email
	@Column(name="email", length=256, nullable=false)
	private String email;
	
	@Transient
	private String emails;
	
	@Size(min=0, max=512)
	@Column(name="reason", length=512)
	private String reason;
	
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
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
