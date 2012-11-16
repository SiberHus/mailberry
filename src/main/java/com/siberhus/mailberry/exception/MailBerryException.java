package com.siberhus.mailberry.exception;

public class MailBerryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MailBerryException() {
		super();
	}

	public MailBerryException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailBerryException(String message) {
		super(message);
	}

	public MailBerryException(Throwable cause) {
		super(cause);
	}

	
}
