package com.siberhus.mailberry.exception;

public class SqlErrorException extends MailBerryException {

	private static final long serialVersionUID = 1L;

	public SqlErrorException() {
		super();
	}

	public SqlErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlErrorException(String message) {
		super(message);
	}

	public SqlErrorException(Throwable cause) {
		super(cause);
	}
	
}
