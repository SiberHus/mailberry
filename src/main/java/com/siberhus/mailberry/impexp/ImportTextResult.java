package com.siberhus.mailberry.impexp;

public class ImportTextResult {
	
	private int successCount;
	
	private int errorCount;
	
	private StringBuilder rejectedText = new StringBuilder();
	
	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	
	public void incrementSuccessCount(){
		this.successCount++;
	}
	
	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
	public void incrementErrorCount(){
		this.errorCount++;
	}
	
	public String getRejectedText() {
		return rejectedText.toString();
	}
	
	public void appendRejectText(String text, char lb){
		rejectedText.append(text).append(lb);
	}
	
	public void appendRejectText(String text){
		rejectedText.append(text);
	}
	
}
