package com.siberhus.mailberry.service;

import javax.servlet.http.HttpServletRequest;

import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.service.pojo.CampaignTrackingStats;
import com.siberhus.mailberry.service.pojo.GeneralizedClientInfoStats;
import com.siberhus.mailberry.service.pojo.CountingAndRateStats;

public interface TrackingService {
	
	public void createTrackingTable(Long campaignId);
	
	public void dropTrackingTable(Long campaignId);
	
	public String createTracking(Long campaignId, Subscriber subscriber);
	
	public void setSuccessResult(Long campaignId, Long subscriberId);
	
	public void setErrorResult(Long campaignId, Long subscriberId, Exception error);
	
	//************************ USER RESPONSE *********************************//
	public boolean verifySecurityToken(Long campaignId, Long subscriberId, String securityToken);
	
	public void setOpenTime(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request);
	
	public void setRsvpStatus(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request, String status);
	
	public String updateClickstream(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request, String url);
	
	public Subscriber setOptOut(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request);
	
	//************************ ANALYTICS *********************************//
	
	public CountingAndRateStats getCountingAndRateStats(Long campaignId);
	
	public CampaignTrackingStats getCampaignTrackingStats(Long campaignId);
	
	public GeneralizedClientInfoStats getClientTrackingStats(Long campaignId);
	
	
}
