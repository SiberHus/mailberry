package com.siberhus.mailberry.impexp;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.SubscriberList;


public class ExportBean {
	
	@NotNull
	private SubscriberList list;
	
	private String status;
	
	private List<Integer> fieldNumbers;
	
	@NotNull
	private FileType fileType;
	
	private ExportCsvConfig csv;
	
	private ExportExcelConfig excel;
	
	private ExportXmlConfig xml;
	
	public SubscriberList getList() {
		return list;
	}
	
	public void setList(SubscriberList list) {
		this.list = list;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<Integer> getFieldNumbers() {
		return fieldNumbers;
	}

	public void setFieldNumbers(List<Integer> fieldNumbers) {
		this.fieldNumbers = fieldNumbers;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
	
	public ExportCsvConfig getCsv() {
		return csv;
	}

	public void setCsv(ExportCsvConfig csv) {
		this.csv = csv;
	}

	public ExportExcelConfig getExcel() {
		return excel;
	}
	
	public void setExcel(ExportExcelConfig excel) {
		this.excel = excel;
	}

	public ExportXmlConfig getXml() {
		return xml;
	}

	public void setXml(ExportXmlConfig xml) {
		this.xml = xml;
	}
	
}

