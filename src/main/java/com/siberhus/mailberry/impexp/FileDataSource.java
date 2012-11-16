package com.siberhus.mailberry.impexp;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.FileImport;
import com.siberhus.mailberry.model.SubscriberList;

public class FileDataSource {

	@NotNull
	private SubscriberList list;
	
	private List<String> fieldNames;
	
	private FileImport fileImport;
	
	@NotNull
	private FileType fileType = null;
	
	private boolean update = false;
	
	private ImportCsvConfig csv;
	
	private ImportExcelConfig excel;
	
	private ImportXmlConfig xml;
	
	private boolean deleteSourceFile = true;
	
	private boolean createErrorFile = false;
	
	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public FileImport getFileImport() {
		return fileImport;
	}
	
	public void setFileImport(FileImport fileImport) {
		this.fileImport = fileImport;
	}
	
	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		if(fileImport!=null){
			this.fileImport.setFileType(fileType);
		}
		this.fileType = fileType;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public ImportCsvConfig getCsv() {
		return csv;
	}
	
	public void setCsv(ImportCsvConfig csv) {
		this.csv = csv;
	}

	public ImportExcelConfig getExcel() {
		return excel;
	}

	public void setExcel(ImportExcelConfig excel) {
		this.excel = excel;
	}

	public ImportXmlConfig getXml() {
		return xml;
	}

	public void setXml(ImportXmlConfig xml) {
		this.xml = xml;
	}

	public boolean isDeleteSourceFile() {
		return deleteSourceFile;
	}

	public void setDeleteSourceFile(boolean deleteSourceFile) {
		this.deleteSourceFile = deleteSourceFile;
	}

	public boolean isCreateErrorFile() {
		return createErrorFile;
	}

	public void setCreateErrorFile(boolean createErrorFile) {
		this.createErrorFile = createErrorFile;
	}
	
	
}
