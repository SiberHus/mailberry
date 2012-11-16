package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

public class FakeSmtpStatus implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean stopped;
	
	private int successes;
	
	private int errors;

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public int getSuccesses() {
		return successes;
	}

	public void setSuccesses(int successes) {
		this.successes = successes;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}
	
	
}
