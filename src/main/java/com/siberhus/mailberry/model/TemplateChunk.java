package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="template_chunks", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "chunk_name"}))
public class TemplateChunk extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull  @Size(max=128)
	@Column(name="chunk_name", length=128, nullable=false)
	private String name;
	
	@NotNull
//	@Basic(fetch=FetchType.LAZY)
//	@Lob
//	@Column(name="chunk_value", columnDefinition="CLOB NOT NULL", nullable=false)
	@Column(name="chunk_value", nullable=false, columnDefinition="TEXT")
	private String value;
	
	@NotNull
	@Column(name="status", length=4, nullable=false)
	private String status = Status.ACTIVE;

	public TemplateChunk(){}
	
	public TemplateChunk(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		Status.validate(status);
		this.status = status;
	}
	
}
