package com.siberhus.mailberry.exception;

import org.springframework.validation.ObjectError;

public class FieldValidationException extends MailBerryException {

	private static final long serialVersionUID = 1L;
	
	private int fieldNumber;
	
	private ObjectError objectError;
	
	public FieldValidationException(ObjectError objectError){
		this(-1, objectError);
	}
	
	public FieldValidationException(int fieldNumber, ObjectError objectError) {
		super(objectError.getDefaultMessage());
		this.fieldNumber = fieldNumber;
		this.objectError = objectError;
	}
	
	public int getFieldNumber() {
		return fieldNumber;
	}
	
	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
	}
	
	public ObjectError getObjectError() {
		return objectError;
	}

	public void setObjectError(ObjectError objectError) {
		this.objectError = objectError;
	}

	
}
