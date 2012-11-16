package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="configurations")
public class Configuration extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static final List<String> SUPPORT_TYPES = Collections.unmodifiableList(new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("java.lang.String");
			add("java.lang.Boolean");
			add("java.lang.Integer");
			add("java.lang.Long");
			add("java.lang.Float");
			add("java.lang.Double");
		}
	});
	@NotNull
	@Column(name="config_name", unique=true, length=256, nullable=false)
	private String name;
	
	@Column(name="config_value", length=256)
	private String value;
	
	@NotNull
	@Column(name="config_type", length=128)
	private String type = "java.lang.String";
	
	public Configuration(){}
	
	public Configuration(String name, String value, String type){
		this.name = name;
		this.value = value;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
