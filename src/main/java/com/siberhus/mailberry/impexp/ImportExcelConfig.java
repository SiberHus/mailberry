package com.siberhus.mailberry.impexp;

import java.io.Serializable;

public class ImportExcelConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean labeled = true;
	
	private boolean multipleSheets = false;
	
	private FileType fileType;
	
	public boolean isLabeled() {
		return labeled;
	}

	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
	}

	public boolean isMultipleSheets() {
		return multipleSheets;
	}

	public void setMultipleSheets(boolean multipleSheets) {
		this.multipleSheets = multipleSheets;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		if(fileType!=FileType.XLS && fileType!=FileType.XLSX){
			throw new IllegalArgumentException("Unsupported file type: "+fileType);
		}
		this.fileType = fileType;
	}
}
