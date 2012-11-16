package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.dao.pojo.ClientInfoStats;
import com.siberhus.mailberry.dao.pojo.CountingStats;
import com.siberhus.mailberry.model.Tracking;

public interface TrackingDao {
	
	public void createTable(Long campaignId);
	
	public void dropTable(Long campaignId);
	
	public Tracking save(Long campaignId, Tracking tracking);
	
	public Tracking findBySubscriberId(Long campaignId, Long subscriberId);
	
	public void setSuccess(Long campaignId, Long subscriberId);
	
	public void setError(Long campaignId, Long subscriberId, String errorMessage);
	
	public String getSecurityToken(Long campaignId, Long subscriberId);
	
	public void setRsvpStatus(Long campaignId, Long subscriberId, String rsvpStatus);
	
	public void setOptOut(Long campaignId, Long subscriberId);
	
	//******************* REPORT ************************//
	
	public CountingStats getCoutingStats(Long campaignId);
	
	public ClientInfoStats getClientInfoStats(Long campaignId);
	
	
}
