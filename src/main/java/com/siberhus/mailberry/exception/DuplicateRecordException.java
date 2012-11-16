package com.siberhus.mailberry.exception;

public class DuplicateRecordException extends MailBerryException {
	
	private static final long serialVersionUID = 1L;

	public DuplicateRecordException() {
		super();
	}

	public DuplicateRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateRecordException(String message) {
		super(message);
	}

	public DuplicateRecordException(Throwable cause) {
		super(cause);
	}
	
	
}
