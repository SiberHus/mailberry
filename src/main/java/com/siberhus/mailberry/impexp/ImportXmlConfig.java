package com.siberhus.mailberry.impexp;

public class ImportXmlConfig {
	
	private String charset = "UTF-8";
	private String rootTagName = "list";
	private String itemTagName = "subscriber";
	private FileFormat fileFormat = FileFormat.WIN;
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getRootTagName() {
		return rootTagName;
	}
	public void setRootTagName(String rootTagName) {
		this.rootTagName = rootTagName;
	}
	public String getItemTagName() {
		return itemTagName;
	}
	public void setItemTagName(String itemTagName) {
		this.itemTagName = itemTagName;
	}
	public FileFormat getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	
}
