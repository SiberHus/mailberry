package com.siberhus.mailberry.controller.pojo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.FieldValidator;

public class FieldValidatorFormBean {

	@NotNull
	private Long listId;
	
	private List<FieldValidator> fieldValidators;
	
	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<FieldValidator> getFieldValidators() {
		return fieldValidators;
	}

	public void setFieldValidators(List<FieldValidator> fieldValidators) {
		this.fieldValidators = fieldValidators;
	}
	
	
}
