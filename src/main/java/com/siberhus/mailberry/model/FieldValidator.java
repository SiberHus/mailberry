package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.base.AbstractModel;

@Entity
@Table(name="field_validators", 
	uniqueConstraints=@UniqueConstraint(columnNames={"list_id", "field_number"}))
public class FieldValidator extends AbstractModel<Long> {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="list_id", referencedColumnName="id", nullable=false)
	private SubscriberList list;
	
	@NotNull @Min(1) @Max(20)
	@Column(name="field_number", nullable=false)
	private Integer fieldNumber;
	
	@NotNull
	@Column(name="data_type", length=256, nullable=false)
	private String dataType;
	
	@NotNull
	@Column(name="required", nullable=false)
	private boolean required;
	
	@Min(0) @Max(Integer.MAX_VALUE)
	@Column(name="min_size")
	private Integer minSize;
	
	@Min(0) @Max(Integer.MAX_VALUE)
	@Column(name="max_size")
	private Integer maxSize;
	
	@Column(name="reg_exp", length=256)
	private String regExp;
	
	@Column(name="enabled", nullable=false)
	private boolean enabled = false;
	
	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}

	public Integer getFieldNumber() {
		return fieldNumber;
	}

	public void setFieldNumber(Integer fieldNumber) {
		this.fieldNumber = fieldNumber;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Integer getMinSize() {
		return minSize;
	}

	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
