package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

public class EventColor implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String backgroundColor;
	private String borderColor;
	private String textColor;

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = cleanColor(backgroundColor);
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = cleanColor(borderColor);
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = cleanColor(textColor);
	}
	
	private String cleanColor(String color){
		if(color!=null && color.length()>0){
			if(!color.startsWith("#")){
				return "#"+color;
			}else{
				return color;
			}
		}else{
			return "#000000";
		}
	}
}
