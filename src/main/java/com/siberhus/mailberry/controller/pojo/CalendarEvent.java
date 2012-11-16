package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String title;// required
	private boolean allDay;
	// IETF format, ISO8601 format, or UNIX timestamp
	private String start;// required
	private String end;
	private String url;
	private String className;
	private boolean editable;
	// private EventSource source;
	private String color;
	private String backgroundColor;
	private String borderColor;
	private String textColor;
	
	// custom field
	private String description;
	private String status;
	
	public CalendarEvent() {
	}

	public CalendarEvent(String title, Date start) {
		this.title = title;
		setStart(start);
	}

	public int getId() {
		return id;
	}

	public CalendarEvent setId(int id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public CalendarEvent setTitle(String title) {
		this.title = title;
		return this;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public CalendarEvent setAllDay(boolean allDay) {
		this.allDay = allDay;
		return this;
	}

	public String getStart() {
		return start;
	}

	public CalendarEvent setStart(String start) {
		this.start = start;
		return this;
	}
	
	public CalendarEvent setStart(Date start) {
		if(start!=null){
			this.start = new SimpleDateFormat("yyyy-MM-dd").format(start);
		}
		return this;
	}
	
	/**
	 * Atom (ISO 8601) format
	 * @param start
	 * @return
	 */
	public CalendarEvent setStartWithTime(Date start) {
		if(start!=null){
			this.start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
		}
		return this;
	}
	
	public String getEnd() {
		return end;
	}

	public CalendarEvent setEnd(String end) {
		this.end = end;
		return this;
	}

	public CalendarEvent setEnd(Date end) {
		if(end!=null){
			this.end = new SimpleDateFormat("yyyy-MM-dd").format(end);
		}
		return this;
	}
	
	/**
	 * Atom (ISO 8601) format
	 * @param end
	 * @return
	 */
	public CalendarEvent setEndWithTime(Date end) {
		if(end!=null){
			this.end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end);
		}
		return this;
	}
	
	
	public String getUrl() {
		return url;
	}

	public CalendarEvent setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getClassName() {
		return className;
	}

	public CalendarEvent setClassName(String className) {
		this.className = className;
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public CalendarEvent setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public String getColor() {
		return color;
	}

	public CalendarEvent setColor(String color) {
		this.color = color;
		return this;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public CalendarEvent setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public CalendarEvent setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public String getTextColor() {
		return textColor;
	}

	public CalendarEvent setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public CalendarEvent setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public CalendarEvent setStatus(String status) {
		this.status = status;
		return this;
	}
	
	
}
