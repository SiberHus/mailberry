package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;

public class ConfigFormat implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String displayTimestamp;
	@NotNull
	private String displayDate;
	@NotNull
	private String displayTime;
	
	@NotNull
	private String inputTimestamp;
	@NotNull
	private String inputDate;
	@NotNull
	private String inputTime;
	
	public ConfigFormat(){}
	
	public ConfigFormat(ConfigurationService config){
		this.displayTimestamp = config.getValueAsString(Config.Format.DISPLAY_TIMESTAMP);
		this.displayDate = config.getValueAsString(Config.Format.DISPLAY_DATE);
		this.displayTime = config.getValueAsString(Config.Format.DISPLAY_TIME);
		
		this.inputTimestamp = config.getValueAsString(Config.Format.INPUT_TIMESTAMP);
		this.inputDate = config.getValueAsString(Config.Format.INPUT_DATE);
		this.inputTime = config.getValueAsString(Config.Format.INPUT_TIME);
	}
	
	public String getDisplayTimestamp() {
		return displayTimestamp;
	}
	public void setDisplayTimestamp(String displayTimestamp) {
		this.displayTimestamp = displayTimestamp;
	}
	public String getDisplayDate() {
		return displayDate;
	}
	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}
	public String getDisplayTime() {
		return displayTime;
	}
	public void setDisplayTime(String displayTime) {
		this.displayTime = displayTime;
	}
	public String getInputTimestamp() {
		return inputTimestamp;
	}
	public void setInputTimestamp(String inputTimestamp) {
		this.inputTimestamp = inputTimestamp;
	}
	public String getInputDate() {
		return inputDate;
	}
	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	
	
}
