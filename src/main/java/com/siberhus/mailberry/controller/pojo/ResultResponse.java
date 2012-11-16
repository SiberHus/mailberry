package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static transient final ResultResponse OK = new ResultResponse("OK"){
		private static final long serialVersionUID = 1L;

		@Override
		public ResultResponse setValue(String value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResultResponse setError(boolean error) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResultResponse setErrorDetail(String errorDetail) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResultResponse setFieldErrors(Map<String, String> fieldErrors) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResultResponse addFieldError(String fieldName, String error) {
			throw new UnsupportedOperationException();
		}
		
	};
	
	private String value;
	private boolean error;
	private String errorDetail;
	private Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
	
	public ResultResponse(String value){
		this.value = value;
		this.error = false;
	}
	
	public ResultResponse(){}
	
	public String getValue() {
		return value;
	}
	public ResultResponse setValue(String value) {
		this.value = value;
		this.error = false;
		return this;
	}
	public boolean isError() {
		return error;
	}
	public ResultResponse setError(boolean error) {
		this.error = error;
		return this;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public ResultResponse setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
		this.error = true;
		return this;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}
	
	public ResultResponse setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
		return this;
	}
	
	public ResultResponse addFieldError(String fieldName, String error) {
		this.fieldErrors.put(fieldName, error);
		return this;
	}
}
