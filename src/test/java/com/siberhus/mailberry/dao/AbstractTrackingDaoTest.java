package com.siberhus.mailberry.dao;

import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.siberhus.mailberry.dao.pojo.CountingStats;
import com.siberhus.mailberry.model.Tracking;
import com.siberhus.mailberry.model.Tracking.RsvpStatus;


public abstract class AbstractTrackingDaoTest {
	
	private TrackingDao trackingDao;
	
	private Long campaignId = 123456L;
	
	public AbstractTrackingDaoTest() {
		this.trackingDao = getTrackingDao();
	}
	
	public abstract TrackingDao getTrackingDao();
	
	@Before
	public void init(){
		trackingDao.createTable(campaignId);
	}
	
	@After
	public void destroy(){
		trackingDao.dropTable(campaignId);
	}
	
	@Test
	public void testAll(){
		//template object
		Tracking tracking = new Tracking();
		tracking.setSubscriberId(null);//NOT NULL
		tracking.setEmailDomain("siberhus.com");//NOT NULL
		tracking.setSecurityToken(null);//NOT NULL
		tracking.setSuccess(true);
		tracking.setSentTime(new Date());
		tracking.setHardBounce(false);
		tracking.setSoftBounce(false);
		tracking.setErrorMessage(null);
		tracking.setOpenTime(null);
		tracking.setForwarded(false);
		tracking.setOptOut(false);
		tracking.setRsvpStatus(null);
		tracking.setRsvpTime(null);
		tracking.setUaRaw(null);
		tracking.setUaType(null);
		tracking.setUaName(null);
		tracking.setUaVersion(null);
		tracking.setOsName(null);
		tracking.setOsFamily(null);
		tracking.setIpAddress(null);
		
		//Create first 10 success subscribers
		for(int i=1;i<=10;i++){
			tracking.setSubscriberId((long)i);
			tracking.setSecurityToken(RandomStringUtils.randomAlphanumeric(8));
			tracking.setSuccess(true);
			trackingDao.save(campaignId, tracking);
		}
		
		tracking = trackingDao.findBySubscriberId(campaignId, 1L);
		Assert.assertTrue(tracking.isSuccess());
		Assert.assertEquals(8, tracking.getSecurityToken().length());
		Assert.assertEquals("siberhus.com", tracking.getEmailDomain());
		Assert.assertFalse(tracking.isHardBounce());
		
		//Set Error
		trackingDao.setError(campaignId, 1L, "hard bounce error");//1 Hard bounce error
		tracking = trackingDao.findBySubscriberId(campaignId, 1L);
		Assert.assertTrue(tracking.isHardBounce());
		Assert.assertEquals("hard bounce error", tracking.getErrorMessage());
		
		tracking = trackingDao.findBySubscriberId(campaignId, 2L);//1 Soft bounce error
		tracking.setSuccess(false);
		tracking.setSoftBounce(true);
		tracking.setErrorMessage("soft bounce error");
		trackingDao.save(campaignId, tracking);
		tracking = trackingDao.findBySubscriberId(campaignId, 2L);
		Assert.assertTrue(tracking.isSoftBounce());
		Assert.assertEquals("soft bounce error", tracking.getErrorMessage());
		
		//Set Opt-Out
		trackingDao.setOptOut(campaignId, 3L);
		tracking = trackingDao.findBySubscriberId(campaignId, 3L);
		tracking.setOpenTime(new Date());
		tracking = trackingDao.save(campaignId, tracking);
		Assert.assertTrue(tracking.isOptOut());
		
		//Set RSVP
		trackingDao.setRsvpStatus(campaignId, 4L, RsvpStatus.ATTENDING);
		tracking = trackingDao.findBySubscriberId(campaignId, 4L);
		tracking.setOpenTime(new Date());
		tracking = trackingDao.save(campaignId, tracking);
		Assert.assertEquals(RsvpStatus.ATTENDING, tracking.getRsvpStatus());
		Assert.assertNotNull(tracking.getRsvpTime());
		
		trackingDao.setRsvpStatus(campaignId, 5L, RsvpStatus.MAYBE);
		tracking = trackingDao.findBySubscriberId(campaignId, 5L);
		tracking.setOpenTime(new Date());
		tracking = trackingDao.save(campaignId, tracking);
		
		//Set forwarded
		tracking = trackingDao.findBySubscriberId(campaignId, 6L);
		tracking.setForwarded(true);
		tracking.setOpenTime(new Date());
		trackingDao.save(campaignId, tracking);
		
		
		CountingStats countingStat = trackingDao.getCoutingStats(campaignId);
		Assert.assertEquals(10, countingStat.getEmails());
		Assert.assertEquals(8, countingStat.getSuccesses());
		Assert.assertEquals(1, countingStat.getHardBounces());
		Assert.assertEquals(1, countingStat.getSoftBounces());
		Assert.assertEquals(1, countingStat.getForwards());
		Assert.assertEquals(1, countingStat.getOptOuts());
		Assert.assertEquals(4, countingStat.getOpens());
	}
}
