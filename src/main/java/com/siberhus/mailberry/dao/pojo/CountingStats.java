package com.siberhus.mailberry.dao.pojo;

import java.io.Serializable;

public class CountingStats implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int emails;
	
	private int successes;
	
	private int hardBounces;
	
	private int softBounces;
	
	private int opens;
	
	private int clicks;
	
	private int forwards;
	
	private int optOuts;
	
	private int rsvps;
	
	public int getEmails() {
		return emails;
	}
	public void setEmails(int emails) {
		this.emails = emails;
	}
	public int getSuccesses() {
		return successes;
	}
	public void setSuccesses(int successes) {
		this.successes = successes;
	}
	public int getHardBounces() {
		return hardBounces;
	}
	public void setHardBounces(int hardBounces) {
		this.hardBounces = hardBounces;
	}
	public int getSoftBounces() {
		return softBounces;
	}
	public void setSoftBounces(int softBounces) {
		this.softBounces = softBounces;
	}
	public int getOpens() {
		return opens;
	}
	public void setOpens(int opens) {
		this.opens = opens;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getForwards() {
		return forwards;
	}
	public void setForwards(int forwards) {
		this.forwards = forwards;
	}
	public int getOptOuts() {
		return optOuts;
	}
	public void setOptOuts(int optOuts) {
		this.optOuts = optOuts;
	}
	public int getRsvps() {
		return rsvps;
	}
	public void setRsvps(int rsvps) {
		this.rsvps = rsvps;
	}
	
}
