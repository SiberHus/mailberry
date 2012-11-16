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
@Table(name="template_variables", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "variable_name"}))
public class TemplateVariable extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull @Size(min=1, max=128)
	@Column(name="variable_name", length=128, nullable=false)
	private String name;
	
	@NotNull @Size(min=1, max=1024)
	@Column(name="variable_value", length=1024, nullable=false)
	private String value;
	
	@NotNull
	@Column(name="status", length=4, nullable=false)
	private String status = Status.ACTIVE;
	
	public TemplateVariable(){}
	
	public TemplateVariable(String name, String value){
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
