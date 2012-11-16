package com.siberhus.mailberry.impexp;

public enum FileType {
	
	CSV, XLS, XLSX, XML, VCF;
	
	@Override
	public String toString(){
		return this.name().toLowerCase();
	}
	
}
