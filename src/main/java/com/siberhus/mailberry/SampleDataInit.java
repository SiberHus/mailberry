package com.siberhus.mailberry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.model.Attachment;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.model.TemplateChunk;
import com.siberhus.mailberry.model.TemplateVariable;
import com.siberhus.mailberry.model.Tracking;
import com.siberhus.mailberry.model.Tracking.RsvpStatus;
import com.siberhus.mailberry.service.CampaignService;

public class SampleDataInit implements InitializingBean{
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	@Inject
	private TrackingDao trackingDao;
	
	@Inject
	private CampaignService campaignService;
	
	private SubscriberList sampleList = null;
	
	private MailAccount defaultMailAccount = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		createSampleTemplateVariables();
		em.flush();
		createSampleTemplateChunks();
		em.flush();
		createSampleSubscriberList();
		em.flush();
		
		
		try{
			sampleList = (SubscriberList)em.createQuery("from SubscriberList where listName='sample list'").getSingleResult();
		}catch(NoResultException e){
			throw new NoResultException("Sample SubscriberList has not created yet");
		}
		MailServer mailServer = (MailServer)em.createQuery("from MailServer where serverName='localhost'").getSingleResult();
		try{
			defaultMailAccount = (MailAccount)em.createQuery("from MailAccount where displayName='Admin'").getSingleResult();
		}catch(NoResultException e){
			MailAccount account = new MailAccount();
			account.setMailServer(mailServer);
			account.setDisplayName("Admin");
			account.setEmail("admin@siberhus.com");
			account.setSmtpAuthen(true);
			account.setSmtpUsername("admin@siberhus.com");
			account.setSmtpPassword("password");
			em.persist(account);
			defaultMailAccount = account;
		}
		
		createSampleCampaign();
		em.flush();
		
		createSampleDataForAnalytics();
		
		transactionManager.commit(status);
	}
	
	public void createSampleTemplateVariables(){
		Number number = (Number)em.createQuery("select count(*) from TemplateVariable").getSingleResult();
		if(number.intValue()==0){
			TemplateVariable tv = new TemplateVariable("senderEmail", "admin@siberhus.com");
			em.persist(tv);
			tv = new TemplateVariable("replyToEmail", "no-reply@siberhus.com");
			em.persist(tv);
			tv = new TemplateVariable("currentYear", "2011");
			em.persist(tv);
			tv = new TemplateVariable("website", "http://www.siberhus.com");
			em.persist(tv);
			for(int i=1;i<=20;i++){
				tv = new TemplateVariable("variableName"+i, "variableValue"+i);
				em.persist(tv);
			}
			em.flush();
		}
	}
	
	public void createSampleTemplateChunks(){
		Number number = (Number)em.createQuery("select count(*) from TemplateChunk").getSingleResult();
		if(number.intValue()==0){
			TemplateChunk tc = new TemplateChunk("copyright", "SiberHus, LLC Copyright $(currentYear)");
			em.persist(tc);
			for(int i=1;i<=5;i++){
				tc = new TemplateChunk("chunkName"+i, "chunkValue"+i);
				em.persist(tc);
			}
			em.flush();
		}
	}
	
	public void createSampleSubscriberList(){
		Number number = (Number)em.createQuery("select count(*) from SubscriberList").getSingleResult();
		if(number.intValue()==0){
			SubscriberList list = new SubscriberList();
			list.setListName("sample list");
			list.setStatus(SubscriberList.Status.ACTIVE);
			list.setFieldCount(3);
			list.setField1Name("firstName");
			list.setField2Name("lastName");
			list.setField3Name("party");
			em.persist(list);
			String[][] leads = new String[][]{
				new String[]{"washington@siberhus.com","George", "Washington", "", Subscriber.Status.ACTIVE},
				new String[]{"adams@siberhus.com","John", "Adams", "Federalist", Subscriber.Status.ACTIVE},
				new String[]{"jefferson@siberhus.com","Thomas", "Jefferson", "Democratic-Republican", Subscriber.Status.ACTIVE},
				
				//below is in blacklist
				new String[]{"roosevelt@siberhus.com","Theodore", "Roosevelt", "Republican", Subscriber.Status.ACTIVE},
				new String[]{"wilson@siberhus.com","Woodrow", "Wilson", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"truman@siberhus.com","Harry S.", "Truman", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"eisenhower@siberhus.com","Dwight D.", "Eisenhower", "Republican", Subscriber.Status.ACTIVE},
				//============================
				
				new String[]{"kennedy@siberhus.com","John F.", "Kennedy", "Democratic", Subscriber.Status.UNSUBSCRIBED},
				new String[]{"carter@siberhus.com","Jimmy", "Carter", "Democratic", Subscriber.Status.UNSUBSCRIBED},
				new String[]{"reagan@siberhus.com","Ronald", "Reagan", "Republican", Subscriber.Status.UNSUBSCRIBED},
				
				new String[]{"bush@siberhus.com","George H. W.", "Bush", "Republican", Subscriber.Status.INACTIVE},
				new String[]{"clinton@siberhus.com","Bill", "Clinton", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"bush.jr@siberhus.com","George W.", "Bush", "Republican", Subscriber.Status.BLOCKED},
				new String[]{"obama@siberhus.com","Barack", "Obama", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"hussachai@siberhus.com","Hussachai", "Puripunpinyo", "SiberHus", Subscriber.Status.TEST},
			};
			/*
			 * Summary
			 * ===================================
			 * 1 Test subscriber
			 * 12 Active subscribers
			 * 	- 4 subscribers in global block list
			 *  - 3 subscribers in local block list
			 *  + 5 subscribers will be in campaign
			 * 1 Inactive subscriber
			 * 1 Blocked subscriber
			 */
			for(String[] data : leads){
				Subscriber subscriber = new Subscriber();
				subscriber.setList(list);
				subscriber.setEmail(data[0]);
				subscriber.setField1Value(data[1]);
				subscriber.setField2Value(data[2]);
				subscriber.setField3Value(data[3]);
				subscriber.setStatus(data[4]);
				em.persist(subscriber);
			}
			em.flush();
		}
	}
	
	public void createSampleCampaign(){
		Number number = (Number)em.createQuery("select count(*) from Campaign where campaignName='Campaign1'").getSingleResult();
		if(number.intValue()==1){
			return;
		}
		Campaign campaign = new Campaign();
//		campaign.setVelocity(true);
		campaign.setList(sampleList);
		campaign.setMailAccount(defaultMailAccount);
		campaign.setCampaignName("Campaign1");
		campaign.setStatus(Campaign.Status.DRAFT);
		campaign.setBlacklistEnabled(true);
		campaign.setFromEmail("$(senderEmail)");
		campaign.setFromName("SiberHus LLC");
		campaign.setReplyToEmail("$(replyToEmail)");
		campaign.setMailSubject("Hello World Mix Message");
//		campaign.setMailPriority(5);
		campaign.setMessageType(Campaign.MessageType.MIX);
		campaign.setMessageBodyText(
			"Dear ${firstName} ${lastName}\n"+
			"Your party is ${party}\n"+
			"$(website)\n"+
			"$[copyright]"
		);
		campaign.setMessageBodyHtml("<html><body>"+
			"<b>Dear</b> ${firstName} ${lastName}<br/>"+
			"Your party is ${party}<br/>\n"+
			"<a href=\"$(website)\">website</a><br/>"+
			"$[copyright]"+
			"</body></html>"
		);
		campaign.setTrackable(true);
		campaign.setClickstream(true);
//		campaign.addAttachment(new Attachment("hello.txt","$(fileStore)/hello.txt"));
		campaign.addAttachment(new Attachment("secret.zip","$(fileStore)/secret.txt",true,"password"));
		campaignService.save(campaign);
		em.flush();
	}
	
	public void createSampleDataForAnalytics() throws ParseException{
		Number number = (Number)em.createQuery("select count(*) from Campaign where campaignName='Happy new year'").getSingleResult();
		if(number.intValue()==1){
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Long> campaignIds = new ArrayList<Long>();
		Campaign campaign = new Campaign();
		campaign.setList(sampleList);
		campaign.setMailAccount(defaultMailAccount);
		campaign.setStartDate(sdf.parse("01/10/2011"));
		campaign.setEndDate(sdf.parse("31/10/2011"));
		campaign.setStatus(Campaign.Status.SENT);
		campaign.setFinishTime(new Date());
		campaign.setSendTime(new Date());
		campaign.setFromEmail("$(senderEmail)");
		campaign.setFromName("SiberHus LLC");
		campaign.setReplyToEmail("$(replyToEmail)");
		campaign.setMessageType(Campaign.MessageType.MIX);
		campaign.setMessageBodyText("Hello");
		campaign.setMessageBodyHtml("<html><body>Hello</body></html>");
		
		String campaignNames[] = new String[]{"Halloween festival", "Happy new year", "20th anniversary"};
		String subjects[] = new String[]{"Trick or Treat", "Happy new year 2011", "Let's cerebrate by getting 50% discount"};
		for(int i=0;i<3;i++){
			campaign.setId(null);
			campaign.setCampaignName(campaignNames[i]);
			campaign.setMailSubject(subjects[i]);
			campaign.setDescription("Lorem Ipsum "+i);
			Long campaignId = campaignService.save(campaign).getId();
			trackingDao.createTable(campaignId);
			campaignIds.add(campaignId);
		}
		
		for(Long campaignId: campaignIds){
			@SuppressWarnings("unchecked")
			List<Subscriber> subscribers = em.createQuery("from Subscriber").getResultList();
			int total = RandomUtils.nextInt(10)+5; //Pob.=[5,15)
			for(int i=0;i<total;i++){
				Subscriber subscriber = subscribers.get(i);
				Long subscriberId = subscriber.getId();
				Tracking tracking = createTrackingTemplate(subscriberId);
				int randN = RandomUtils.nextInt(10);
				if(randN==1||randN==2||randN==3){
					tracking.setSuccess(false);
					tracking.setHardBounce(true);
					tracking.setErrorMessage("hard bounce error");
				}else if(randN==4){
					tracking.setSuccess(false);
					tracking.setSoftBounce(true);
					tracking.setErrorMessage("soft bounce error");
				}else if(randN==5 || randN==6){
					tracking.setOptOut(true);
					tracking.setOpenTime(new Date());
				}else if(randN==7 || randN==8 || randN==9){
					if(randN==7)tracking.setRsvpStatus(RsvpStatus.ATTENDING);
					if(randN==8)tracking.setRsvpStatus(RsvpStatus.MAYBE);
					if(randN==9)tracking.setRsvpStatus(RsvpStatus.DECLINED);
					tracking.setRsvpTime(new Date());
					tracking.setOpenTime(new Date());
				}else if(randN==10 || randN==11){
					tracking.setForwarded(true);
					tracking.setOpenTime(new Date());
				}
				trackingDao.save(campaignId, tracking);
			}
		}
		
	}

	private Tracking createTrackingTemplate(Long subscriberId){
		Tracking tracking = new Tracking();
		tracking.setSubscriberId(subscriberId);//NOT NULL
		tracking.setEmailDomain("siberhus.com");//NOT NULL
		tracking.setSecurityToken(RandomStringUtils.randomAlphanumeric(8));//NOT NULL
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
		return tracking;
	}
}
