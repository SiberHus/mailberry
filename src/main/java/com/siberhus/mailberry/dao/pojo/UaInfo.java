package com.siberhus.mailberry.dao.pojo;

import java.io.Serializable;

public class UaInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String name;
	private String version;
	private String icon;
	private int count;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
