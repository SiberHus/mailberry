package com.siberhus.mailberry.service.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.siberhus.mailberry.dao.pojo.CountingStats;

public class CountingAndRateStats extends CountingStats{
	
	private static final long serialVersionUID = 1L;
	
	private double successRate;
	private double hardBounceRate;
	private double softBounceRate;
	private double openRate;
	private double forwardRate;
	private double optOutRate;
	private double rsvpRate;
	
	public CountingAndRateStats(CountingStats countingStats){
		double multiplier = 0;
		if(countingStats.getEmails()>0){
			multiplier = 100.0/countingStats.getEmails();
		}
		setEmails(countingStats.getEmails());
		setSuccesses(countingStats.getSuccesses());
		this.successRate = roundDouble(countingStats.getSuccesses()*multiplier);
		setHardBounces(countingStats.getHardBounces());
		this.hardBounceRate = roundDouble(countingStats.getHardBounces()*multiplier);
		setSoftBounces(countingStats.getSoftBounces());
		this.softBounceRate = roundDouble(countingStats.getSoftBounces()*multiplier);
		setOpens(countingStats.getOpens());
		this.openRate = roundDouble(countingStats.getOpens()*multiplier);
		setClicks(countingStats.getClicks());
		setForwards(countingStats.getForwards());
		this.forwardRate = roundDouble(countingStats.getForwards()*multiplier);
		setOptOuts(countingStats.getOptOuts());
		this.optOutRate = roundDouble(countingStats.getOptOuts()*multiplier);
		setRsvps(countingStats.getRsvps());
		this.rsvpRate = roundDouble(countingStats.getRsvps()*multiplier);
	}
	
	private static double roundDouble(double val){
		return new BigDecimal(val)
			.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	public double getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(double successRate) {
		this.successRate = successRate;
	}

	public double getHardBounceRate() {
		return hardBounceRate;
	}

	public void setHardBounceRate(double hardBounceRate) {
		this.hardBounceRate = hardBounceRate;
	}

	public double getSoftBounceRate() {
		return softBounceRate;
	}

	public void setSoftBounceRate(double softBounceRate) {
		this.softBounceRate = softBounceRate;
	}

	public double getOpenRate() {
		return openRate;
	}

	public void setOpenRate(double openRate) {
		this.openRate = openRate;
	}

	public double getForwardRate() {
		return forwardRate;
	}

	public void setForwardRate(double forwardRate) {
		this.forwardRate = forwardRate;
	}

	public double getOptOutRate() {
		return optOutRate;
	}

	public void setOptOutRate(double optOutRate) {
		this.optOutRate = optOutRate;
	}

	public double getRsvpRate() {
		return rsvpRate;
	}

	public void setRsvpRate(double rsvpRate) {
		this.rsvpRate = rsvpRate;
	}
	
}
