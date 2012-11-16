package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="dashboard_widgets")
public class DashboardWidget extends SpringAuditableModel<Long>{

	private static final long serialVersionUID = 1L;
	
	public static enum Position{
		Center, Left, Right
	}
	
	@Column(name="widget_code", length=32, unique=true, nullable=false)
	private String code;//auto generated from name
	
	@NotNull @Size(max=32)
	@Column(name="widget_name", length=32, unique=true, nullable=false)
	private String name;
	
	@NotNull @Size(max=256)
	@Column(name="content_uri", length=256, nullable=false)
	private String contentUri;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name="default_position", length=8)
	private Position defaultPosition = Position.Center;
	
	@NotNull
	@Column(name="status", length=2048, nullable=false)
	private String status = Status.ACTIVE;
	
	@Column(name="admin_only", nullable=false)
	private boolean adminOnly = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentUri() {
		return contentUri;
	}

	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Position getDefaultPosition() {
		return defaultPosition;
	}

	public void setDefaultPosition(Position defaultPosition) {
		this.defaultPosition = defaultPosition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAdminOnly() {
		return adminOnly;
	}

	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	
	
}
