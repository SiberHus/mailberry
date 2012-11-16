package com.siberhus.mailberry.impexp;

public class ExportExcelConfig {
	
	private boolean labeled = true;
	
	private boolean reserveDataType = false;//TODO: postpone to next release
	
	private int itemsPerSheet = 5000;
	
	private int sheetsPerWorkbook = 1;//TODO: postpone to next release
	
	private FileType fileType;
	
	public boolean isLabeled() {
		return labeled;
	}

	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
	}

	public boolean isReserveDataType() {
		return reserveDataType;
	}

	public void setReserveDataType(boolean reserveDataType) {
		this.reserveDataType = reserveDataType;
	}

	public int getItemsPerSheet() {
		return itemsPerSheet;
	}

	public void setItemsPerSheet(int itemsPerSheet) {
		this.itemsPerSheet = itemsPerSheet;
	}

	public int getSheetsPerWorkbook() {
		return sheetsPerWorkbook;
	}

	public void setSheetsPerWorkbook(int sheetsPerWorkbook) {
		this.sheetsPerWorkbook = sheetsPerWorkbook;
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