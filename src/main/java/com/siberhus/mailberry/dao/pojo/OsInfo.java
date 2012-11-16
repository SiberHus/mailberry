package com.siberhus.mailberry.dao.pojo;

import java.io.Serializable;

public class OsInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String family;
	private String name;
	private String icon;
	private int count;
	
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
