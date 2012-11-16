package com.siberhus.mailberry.impexp;


public class ExportXmlConfig extends ImportXmlConfig {
	
	private boolean prettyPrint = true;
	//TODO: add validation rules to attribute is postponed to next release
	
	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}
	
}