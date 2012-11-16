package com.siberhus.mailberry.controller.pojo;

public class SpamResult {
	
	private boolean spam;
	private String score = "?";
	private String requiredScore = "?";
	private String analysisDetails;
	
	private boolean error;
	private String errorDetail;
	
	public SpamResult(){}
	
	public SpamResult(boolean spam, String score, String requiredScore){
		this.spam = spam;
		this.score = score;
		this.requiredScore = requiredScore;
	}
	
	public SpamResult(String errorDetail){
		this.errorDetail = errorDetail;
		this.error = true;
	}
	
	
	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRequiredScore() {
		return requiredScore;
	}

	public void setRequiredScore(String requiredScore) {
		this.requiredScore = requiredScore;
	}

	public String getAnalysisDetails() {
		return analysisDetails;
	}

	public void setAnalysisDetails(String analysisDetails) {
		this.analysisDetails = analysisDetails;
	}

	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
		this.error = true;
	}
	
	
}
