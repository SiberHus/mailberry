package com.siberhus.mailberry.impexp;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.SubscriberList;

public class TextDataSource {

	private SubscriberList list;
	
	@NotNull
	private String delimiter;
	
	@NotNull
	private String text;
	
	@NotNull
	private List<String> fieldNames; //this fieldnames is difference from list.fieldNames
									//because it's edited by user before importing
	
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
}
