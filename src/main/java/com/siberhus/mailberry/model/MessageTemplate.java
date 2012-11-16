package com.siberhus.mailberry.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="message_templates", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "template_name"}))
public class MessageTemplate extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull @Size(max=256)
	@Column(name="template_name", length=256, unique=true, nullable=false)
	private String templateName;
	
	@Column(name="template_image", length=256)
	private String templateImage;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
//	@Lob
	@Basic(fetch=FetchType.LAZY)
//	@Column(name="template_text_data", nullable=false, columnDefinition="CLOB NOT NULL")
	@Column(name="template_text_data", nullable=true, columnDefinition="TEXT")
	private String templateTextData;
	
	@NotNull
//	@Lob
	@Basic(fetch=FetchType.LAZY)
//	@Column(name="template_html_data", nullable=false, columnDefinition="CLOB NOT NULL")
	@Column(name="template_html_data", nullable=false, columnDefinition="TEXT")
	private String templateHtmlData;
	
	@NotNull
	@Column(name="status", length=4, nullable=false)
	private String status = Status.ACTIVE;
	
	public String toString(){
		return templateName;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}


	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateImage() {
		return templateImage;
	}

	public void setTemplateImage(String templateImage) {
		this.templateImage = templateImage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemplateTextData() {
		return templateTextData;
	}

	public void setTemplateTextData(String templateTextData) {
		this.templateTextData = templateTextData;
	}

	public String getTemplateHtmlData() {
		return templateHtmlData;
	}

	public void setTemplateHtmlData(String templateHtmlData) {
		this.templateHtmlData = templateHtmlData;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		Status.validate(status);
		this.status = status;
	}
}
