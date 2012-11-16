package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FakeSmtpParam implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static enum Command{
		Stop, Start
	}
	
	@NotNull
	private Command command;
	
	@Min(1) @Max(Integer.MAX_VALUE)
	private int port = 25;
	
	@Min(0) @Max(100)
	private int chanceOfError = 0;
	
	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getChanceOfError() {
		return chanceOfError;
	}

	public void setChanceOfError(int chanceOfError) {
		this.chanceOfError = chanceOfError;
	}
	
}

