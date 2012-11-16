package com.siberhus.mailberry.exception;

public class DuplicateRequestException extends MailBerryException {

	private static final long serialVersionUID = 1L;

	public DuplicateRequestException() {
		super();
	}

	public DuplicateRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateRequestException(String message) {
		super(message);
	}

	public DuplicateRequestException(Throwable cause) {
		super(cause);
	}
	
	
}
