package com.siberhus.mailberry.exception;


public class DataMismatchException extends MailBerryException{
	
	private static final long serialVersionUID = 1L;

	public DataMismatchException() {
		super();
	}

	public DataMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataMismatchException(String message) {
		super(message);
	}

	public DataMismatchException(Throwable cause) {
		super(cause);
	}
	
	
}
