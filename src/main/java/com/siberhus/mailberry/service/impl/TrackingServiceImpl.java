package com.siberhus.mailberry.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.CampaignDao;
import com.siberhus.mailberry.dao.ClickstreamDao;
import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.dao.pojo.ClientInfoStats;
import com.siberhus.mailberry.exception.MailBerryException;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Clickstream;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.Tracking;
import com.siberhus.mailberry.service.TrackingService;
import com.siberhus.mailberry.service.pojo.CampaignTrackingStats;
import com.siberhus.mailberry.service.pojo.GeneralizedClientInfoStats;
import com.siberhus.mailberry.service.pojo.CountingAndRateStats;

import cz.mallat.uasparser.CachingOnlineUpdateUASparser;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

@Service
public class TrackingServiceImpl implements TrackingService {
	
	private Logger log = LoggerFactory.getLogger(TrackingServiceImpl.class);
	
	@Inject
	private TrackingDao trackingDao;
	
	@Inject
	private CampaignDao campaignDao;
	
	@Inject
	private ClickstreamDao clickstreamDao;
	
	@Inject
	private SubscriberDao subscriberDao;
	
	
	private UASparser uaParser;
	
	public static class RsvpStatus{
		public static final String ATTENDING = "ATT";
		public static final String MAYBE = "MAY";
		public static final String DECLINED = "DEC";
		public static final String VALUES[] = new String[]{ATTENDING, MAYBE, DECLINED};
	}
	
	public TrackingServiceImpl() throws IOException{
		uaParser = new CachingOnlineUpdateUASparser();
	}
	
	@Transactional
	@Override
	public void createTrackingTable(Long campaignId) {
		log.debug("Creating tracking table of campaign: {}", campaignId);
		
		trackingDao.createTable(campaignId);
	}
	
	@Transactional
	@Override
	public void dropTrackingTable(Long campaignId) {
		log.debug("Dropping tracking table of campaign: {}", campaignId);
		trackingDao.dropTable(campaignId);
	}
	
	@Transactional
	@Override
	public String createTracking(Long campaignId, Subscriber subscriber) {
		
		log.debug("Creating tracking id:{} of campaign: {}", new Object[]{subscriber.getId(), campaignId});
		String securityToken = RandomStringUtils.randomAlphanumeric(8);
		Tracking tracking = new Tracking(subscriber);
		tracking.setSecurityToken(securityToken);
		tracking = trackingDao.save(campaignId, tracking);
		log.debug("Tracking: {} was created", tracking);
		return securityToken;
	}
	
	@Transactional
	@Override
	public void setSuccessResult(Long campaignId, Long subscriberId) {
		log.debug("Update success result of subscriber:{} for campaign: {}", 
				new Object[]{subscriberId, campaignId});
		trackingDao.setSuccess(campaignId, subscriberId);
	}
	
	@Transactional
	@Override
	public void setErrorResult(Long campaignId, Long subscriberId, Exception error) {
		log.debug("Update error result of subscriber:{} for campaign: {}", 
				new Object[]{subscriberId, campaignId});
		String truncatedErrMsg = StringUtils.abbreviate(error.getMessage(), 256);
		trackingDao.setError(campaignId, subscriberId, truncatedErrMsg);
	}
	
	//************************ USER RESPONSE *********************************//
	
	@Override
	public boolean verifySecurityToken(Long campaignId, Long subscriberId, String securityToken) {
		log.debug("Verifying security token:{} of subscriber:{} for campaign: {}", 
				new Object[]{securityToken, subscriberId, campaignId});
		String token = trackingDao.getSecurityToken(campaignId, subscriberId);
		if(securityToken!=null && securityToken.equals(token)){
			log.debug("Verification code matched");
			return true;
		}
		log.debug("Illegal verification code");
		return false;
	}
	
	@Transactional
	@Override
	public void setOpenTime(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request) {
		log.debug("Updating client information of subscriber:{} for campaign: {}",
				new Object[]{subscriberId, campaignId});
		
		if(verifySecurityToken(campaignId, subscriberId, securityToken)){
			setClientInfo(campaignId, subscriberId, request);
		}
	}
	
	@Transactional
	@Override
	public void setRsvpStatus(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request, String status) {
		log.debug("Updating RSVP Status:{} of subscriber:{} for campaign: {}", 
				new Object[]{status, subscriberId, campaignId});
		if(!verifySecurityToken(campaignId, subscriberId, securityToken)){
			throw new AccessDeniedException("Access is denied");
		}
		if(!ArrayUtils.contains(RsvpStatus.VALUES, status)){
			throw new IllegalArgumentException("status must be any of "
					+Arrays.toString(RsvpStatus.VALUES));
		}
		trackingDao.setRsvpStatus(campaignId, subscriberId, status);
		setClientInfo(campaignId, subscriberId, request);
	}
	
	
	@Transactional
	@Override
	public String updateClickstream(Long campaignId, Long subscriberId, String securityToken, HttpServletRequest request, String url) {
		log.debug("Updating clickstream for campaign: {}", campaignId);
		
		if(!verifySecurityToken(campaignId, subscriberId, securityToken)){
			throw new AccessDeniedException("Access is denied");
		}
		log.debug("URL before decoding: {}", url);
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);//should not happen
		}
		log.debug("URL after decoding: {}", url);
		
		Campaign campaign = campaignDao.get(null, campaignId);
		if(campaign==null){
			log.warn("Campaign id:{} not found, skip updating clickstream", campaignId);
			return url;
		}
		Clickstream clickstream = clickstreamDao.findByCampaignIdAndUrl(campaignId, url);
		if(clickstream==null){
			clickstream = new Clickstream();
			clickstream.setCampaign(campaign);
			clickstream.setClickedUrl(url);
		}
		clickstream.setClickCount(clickstream.getClickCount()+1);
		clickstreamDao.save(clickstream);
		setClientInfo(campaignId, subscriberId, request);
		return url;
	}
	
	@Transactional
	@Override
	public Subscriber setOptOut(Long campaignId, Long subscriberId,
			String securityToken, HttpServletRequest request) {
		log.debug("Updating subscriber:{} status to {}", 
			new Object[]{subscriberId, Subscriber.Status.UNSUBSCRIBED});
		if(!verifySecurityToken(campaignId, subscriberId, securityToken)){
			throw new AccessDeniedException("Access is denied");
		}
		Subscriber subscriber = subscriberDao.get(subscriberId);
		subscriber.setStatus(Subscriber.Status.UNSUBSCRIBED);
		subscriberDao.save(subscriber);
		trackingDao.setOptOut(campaignId, subscriberId);
		return subscriber;
	}
	
	private void setClientInfo(Long campaignId, Long subscriberId, HttpServletRequest request) {
		
		Tracking tracking = trackingDao.findBySubscriberId(campaignId, subscriberId);
		if(tracking==null){
			log.warn("Unable to find tracking for subscriberId:{}", subscriberId);
			return;
		}
		
		String uaString = request.getHeader("user-agent");
		String truncatedUaString = StringUtils.abbreviate(uaString, 256);
		if(tracking.getOpenTime()!=null){
			if(!tracking.isForwarded()){
				if(!truncatedUaString.equals(tracking.getUaRaw())){
					log.debug("Different User-Agent detected. \nOld one: {}\nNew one:{} ",
						new Object[]{tracking.getUaRaw(), uaString});
					tracking.setForwarded(true);
					trackingDao.save(campaignId, tracking);
					return;
				}
			}
			log.debug("This client info has already been recorded at:{}", tracking.getOpenTime());
			return;
		}
		tracking.setOpenTime(new Date());
		
		tracking.setIpAddress(request.getRemoteAddr());
		UserAgentInfo uaInfo = null;
		try {
			uaInfo = uaParser.parse(uaString);
		} catch (IOException e) {
			throw new MailBerryException("Unable to parse user-agent", e);
		}
		tracking.setUaRaw(truncatedUaString);
		tracking.setUaType(uaInfo.getTyp());
		String uaNames[] = StringUtils.split(uaInfo.getUaName(), ' ');
		tracking.setUaName(uaNames[0]);
		String uaMajorVersion = StringUtils.substringBefore(uaNames[uaNames.length-1], ".");
		tracking.setUaVersion(uaMajorVersion);
		tracking.setUaIcon(uaInfo.getUaIcon());
		tracking.setOsName(uaInfo.getOsName());
		tracking.setOsFamily(uaInfo.getOsFamily());
		tracking.setOsIcon(uaInfo.getOsIcon());
		log.debug("User-Agent Info: (uaType:{}, uaName:{}, uaVersion:{}, osName:{}, osFamily:{})",
				new Object[]{tracking.getUaType(), tracking.getUaName(), tracking.getUaVersion()
				, tracking.getOsName(), tracking.getOsFamily()});
		trackingDao.save(campaignId, tracking);
	}


	//************************ ANALYTICS *********************************//
	
	@Override
	public CountingAndRateStats getCountingAndRateStats(Long campaignId) {
		CountingAndRateStats countingStatRate = new CountingAndRateStats(
				trackingDao.getCoutingStats(campaignId));
		return countingStatRate;
	}
	
	
	@Override
	public CampaignTrackingStats getCampaignTrackingStats(Long campaignId){
		Campaign campaign = campaignDao.get(null, campaignId);
		CampaignTrackingStats result = new CampaignTrackingStats(
				trackingDao.getCoutingStats(campaignId), campaign);
		
		return result;
	}
	
	@Override
	public GeneralizedClientInfoStats getClientTrackingStats(Long campaignId){
		
		ClientInfoStats infoStats = trackingDao.getClientInfoStats(campaignId);
		GeneralizedClientInfoStats trackingStats = new GeneralizedClientInfoStats(infoStats);
		
		return trackingStats;
	}
}
