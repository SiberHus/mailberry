package com.siberhus.mailberry.impexp;

import java.io.Serializable;

public class Progress implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int success;
	private int error;
	private int updated;
	private int created;
	private boolean finish = false;
	
	public void incSuccess() {
		success++;
	}

	public void incError() {
		error++;
	}

	public void incUpdated() {
		updated++;
	}

	public void incCreated() {
		created++;
	}
	
	public void setSuccess(int success) {
		this.success = success;
	}

	public void setError(int error) {
		this.error = error;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getSuccess() {
		return success;
	}

	public int getError() {
		return error;
	}

	public int getUpdated() {
		return updated;
	}

	public int getCreated() {
		return created;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	
}
