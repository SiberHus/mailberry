package com.siberhus.mailberry.service;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siberhus.fakesmtp.FakeSmtpServer;
import com.siberhus.fakesmtp.SmtpMessage;
import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.MailBerryTestBootstrap;
import com.siberhus.mailberry.MailBerryTestCase;
import com.siberhus.mailberry.controller.TrackingsController;
import com.siberhus.mailberry.model.Attachment;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.pojo.FilledEmailMessage;
import com.siberhus.mailberry.service.pojo.TemplateEmailMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/spring/dataSource-context.xml",
	"/spring/persistence-context.xml",
	"/spring/service-context.xml",
	"/spring/test-context.xml"
})
public class EmailServiceImplTest extends MailBerryTestCase implements ApplicationContextAware{
	
	private final static Logger log = LoggerFactory.getLogger(EmailServiceImplTest.class);
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private CampaignService campaignService;
	
	@Inject
	private ConfigurationService configService;
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private MailBerryTestBootstrap bootstrap;
	
	
	@AfterClass
	public static void clearTmpDirectory(){
		log.info("Deleting all files in src/test/tmp directory");
		File tmpDir = new File("src/test/tmp");
		for(File tmpFile: tmpDir.listFiles()){
			log.info("Deleting file: {}", tmpFile);
			tmpFile.delete();
		}
	}
	
//	@Ignore
	@Test
	public void testCreateEmailMessage() throws Exception{
		
		Campaign campaign = createDemoCampaign("Create Email Message");
		
		TemplateEmailMessage emailMessage = emailService.createEmailMessage(campaign);
		
		Assert.assertNull(emailMessage.getToEmail());
		Assert.assertEquals("admin@siberhus.com", emailMessage.getFromEmail());
		Assert.assertEquals("no-reply@siberhus.com", emailMessage.getReplyToEmail());
		Assert.assertEquals("Hello World Mix Message", emailMessage.getMailSubject());
		Assert.assertTrue(emailMessage.isMultipart());
		
		String bodyText = emailMessage.getMessageBodyText();
		Assert.assertTrue(bodyText.contains("${firstName}"));
		Assert.assertTrue(bodyText.contains("http://www.siberhus.com"));
		Assert.assertTrue(bodyText.contains("SiberHus Copyright 2011"));
		
		
		String bodyHtml = emailMessage.getMessageBodyHtml();
		Assert.assertTrue(bodyHtml.startsWith("<html>"));
		Assert.assertTrue(bodyHtml.contains("${firstName}"));
		Assert.assertTrue(bodyHtml.contains("${_clickstream_}http%3A%2F%2Fwww.siberhus.com"));
		Assert.assertTrue(bodyHtml.contains("SiberHus Copyright 2011"));
		
		Assert.assertEquals(4, emailMessage.getBlacklist().size());
		
		Assert.assertEquals(2, emailMessage.getAttachments().size());
		for(Attachment attachment: emailMessage.getAttachments()){
			Assert.assertTrue(attachment.getFilePath().startsWith("src/test/files"));
		}
		
	}
	
//	@Ignore
	@Test
	public void testFillUpEmailMessage() throws Exception{
		
		Campaign campaign = createDemoCampaign("FillUp Email Message");
		
		TemplateEmailMessage templateEmailMessage = emailService.createEmailMessage(campaign);
		Assert.assertTrue(templateEmailMessage.isMultipart());
		
		Subscriber subscriber = (Subscriber)em.createQuery(
				"from Subscriber where email='hussachai@siberhus.com'").getSingleResult();
		FilledEmailMessage filledEmailMessage = emailService.fillUpEmailMessage(campaign, templateEmailMessage, subscriber);
		
		Assert.assertEquals("hussachai@siberhus.com", filledEmailMessage.getToEmail());
		Assert.assertEquals("admin@siberhus.com", filledEmailMessage.getFromEmail());
		Assert.assertEquals("no-reply@siberhus.com", filledEmailMessage.getReplyToEmail());
		Assert.assertEquals("Hello World Mix Message", filledEmailMessage.getMailSubject());
		
		
		String bodyText = filledEmailMessage.getMessageBodyText();
		
		Assert.assertTrue(bodyText.contains("Dear Hussachai Puripunpinyo\n"));
		Assert.assertTrue(bodyText.contains("Your party is SiberHus\n"));
		Assert.assertTrue(bodyText.contains("http://www.siberhus.com"));
		Assert.assertTrue(bodyText.contains("SiberHus Copyright 2011"));
		
		String bodyHtml = filledEmailMessage.getMessageBodyHtml();
		Assert.assertTrue(bodyHtml.contains("<b>Dear</b> Hussachai Puripunpinyo<br/>"));
		Assert.assertTrue(bodyHtml.contains("Your party is SiberHus<br/>"));
//		System.out.println(bodyHtml);
		
		//Make sure we've enabled both trackable and clickstream feature.
		Assert.assertTrue(campaign.isTrackable()&&campaign.isClickstream());
		String serverUrl = configService.getValueAsString(Config.SERVER_URL);
		
		String clickstreamUrl = serverUrl+TrackingsController.PATH+"/redirect/"+
			campaign.getId()+"/"+subscriber.getId()+"/"+null+"?url="+URLEncoder.encode("http://www.siberhus.com","UTF-8");
		Assert.assertTrue(bodyHtml.contains(clickstreamUrl));
		
		String trackingUrl = serverUrl+TrackingsController.PATH+"/"+campaign.getId()+"/"+subscriber.getId()+"/"+null+"/open.png";
		Assert.assertTrue(bodyHtml.contains("<img src=\""+trackingUrl));
		Assert.assertTrue(bodyHtml.contains("SiberHus Copyright 2011"));
		
	}
	
//	@Ignore
	@Test
	public void testSendTest(){
		
		Campaign campaign = createDemoCampaign("Send Test");
		
		FakeSmtpServer server = new FakeSmtpServer();
		server.start();
		
		try {
			emailService.sendTest(null, campaign.getId(), "hussachai@siberhus.com");
		} catch (MessagingException e) {
			Assert.fail(e.getMessage());
		}
		
		server.stop();
		
		Assert.assertEquals(1, server.getReceivedEmails());
		
		Iterator<SmtpMessage> emailIter = server.getReceivedEmail();
		SmtpMessage email = (SmtpMessage)emailIter.next();
		
		Assert.assertEquals("Hello World Mix Message", email.getHeaderValue("Subject"));
		Assert.assertEquals("hussachai@siberhus.com", email.getHeaderValue("To"));
		Assert.assertEquals("5", email.getHeaderValue("X-Priority"));
		Assert.assertEquals("MailBerry", email.getHeaderValue("User-Agent"));
		Assert.assertTrue(email.getHeaderValue("Content-Type").startsWith("multipart/mixed"));
		Assert.assertEquals("no-reply@siberhus.com", email.getHeaderValue("Reply-To"));
		Assert.assertEquals("SiberHus LLC <admin@siberhus.com>", email.getHeaderValue("From"));
		String messageBody = email.getBody();
		//Check attachment
		Assert.assertTrue(messageBody.contains("Content-Type: text/plain; charset=us-ascii; name=hello.txt"));
		Assert.assertTrue(messageBody.contains("Content-Type: application/zip; name=secret.zip"));
		
		Iterator<String> headerNames = email.getHeaderNames();
		log.info("Email Headers ===================");
		while(headerNames.hasNext()){
			String headerName = headerNames.next();
			log.info("{}={}",new Object[]{headerName, email.getHeaderValue(headerName)});
		}
		log.info("End ===================");
		log.info("Email Body ================");
		log.info(email.getBody());
		log.info("End ===================");
	}
	
//	@Ignore
	@Test
	public void testSendNow(){
		
		Campaign campaign = createDemoCampaign("Send Now");
		
		FakeSmtpServer server = new FakeSmtpServer();
		server.start();
		
		Assert.assertEquals(Campaign.Status.DRAFT, campaign.getStatus());
		try {
			emailService.sendNow(null, campaign.getId());
		} catch (MessagingException e) {
			Assert.fail(e.getMessage());
		}
		
		server.stop();
		
		campaign = em.find(Campaign.class, campaign.getId());
		
		Assert.assertEquals(Campaign.Status.SENT, campaign.getStatus());
//		Assert.assertEquals(5, campaign.getSmtpProfile().getTotalSentMails());
//		Assert.assertEquals(5, server.getReceivedEmailSize());
		
		Iterator<SmtpMessage> messages = server.getReceivedEmail();
		while(messages.hasNext()){
			SmtpMessage message = messages.next();
			String toEmail = message.getHeaderValue("To");
			if("washington@siberhus.com".equals(toEmail)){
				Assert.assertTrue(message.getBody().contains("Dear George Washington"));
			}else if("adams@siberhus.com".equals(toEmail)){
				Assert.assertTrue(message.getBody().contains("Dear John Adams"));
			}else if("jefferson@siberhus.com".equals(toEmail)){
				Assert.assertTrue(message.getBody().contains("Dear Thomas Jefferson"));
			}
		}
		
	}
	
	@Ignore
	@Test
	public void testSendNow_LoadTest(){
		
		long startNanos = System.nanoTime();
		startTransaction();
		final int LOAD = 1000;
		SubscriberList listLoadTest = new SubscriberList();
		listLoadTest.setListName("XEmails");
		listLoadTest.setStatus(SubscriberList.Status.ACTIVE);
		listLoadTest.setFieldCount(2);
		listLoadTest.setField1Name("firstName");
		listLoadTest.setField2Name("lastName");
		em.persist(listLoadTest);
		
		List<String[]> leads = new ArrayList<String[]>(LOAD);
		for(int i=1;i<=LOAD;i++){
			leads.add(new String[]{"test"+i+"@siberhus.com","FirstName"+i, "LastName"+i});
		}
		for(String[] data : leads){
			Subscriber subscriber = new Subscriber();
			subscriber.setList(listLoadTest);
			subscriber.setEmail(data[0]);
			subscriber.setField1Value(data[1]);
			subscriber.setField2Value(data[2]);
			em.persist(subscriber);
		}
		
		Campaign campaignLoadTest = new Campaign();
		campaignLoadTest.setVelocity(true);
		campaignLoadTest.setList(listLoadTest);
		campaignLoadTest.setMailAccount(bootstrap.getDefaultMailAccount());
		campaignLoadTest.setCampaignName("Campaign_LoadTest");
		campaignLoadTest.setStatus(Campaign.Status.DRAFT);
		campaignLoadTest.setBlacklistEnabled(true);
		campaignLoadTest.setFromEmail("$(senderEmail)");
		campaignLoadTest.setFromName("SiberHus LLC");
		campaignLoadTest.setReplyToEmail("no-reply@siberhus.com");
		campaignLoadTest.setMailSubject("Hello World Mix Message");
		campaignLoadTest.setMailPriority(5);
		campaignLoadTest.setMessageType(Campaign.MessageType.MIX);
		campaignLoadTest.setMessageBodyText(
			"Dear ${firstName} ${lastName}\n"+
			"$(website)\n"+
			"$[copyright]"
		);
		campaignLoadTest.setMessageBodyHtml("<html><body>"+
			"<b>Dear</b> ${firstName} ${lastName}<br/>"+
			"<a href=\"$(website)\">website</a><br/>"+
			"$[copyright]"+
			"</body></html>"
		);
		campaignLoadTest.setTrackable(true);
		campaignLoadTest.setClickstream(true);
		//no attachment
		campaignLoadTest = campaignService.save(campaignLoadTest);
		em.flush();
		
		commitTransaction();
		
		FakeSmtpServer server = new FakeSmtpServer();
		server.start();
		
		try {
			emailService.sendNow(null, campaignLoadTest.getId());
		} catch (MessagingException e) {
			Assert.fail(e.getMessage());
		}
		server.stop();
		
		Assert.assertEquals(LOAD, server.getReceivedEmails());
		
		DecimalFormat df = new DecimalFormat("###,###.00");
		double elapse = (System.nanoTime()-startNanos)/1000000.00;//ms
		double throughput = LOAD/(elapse/1000);
		log.info("Elapsed time: {} ms", df.format(elapse));
		log.info("Throughput: {} email(s)/second", df.format(throughput));
	}
	
	@Test
	public void testScheduleDelivery(){
		Campaign campaign = createDemoCampaign("Scheduled");
		try{
			emailService.scheduleDelivery(null, campaign.getId(), new Date());
			Assert.fail();
		}catch(IllegalArgumentException e){
			Assert.assertEquals("time must be future", e.getMessage());
		}
		FakeSmtpServer server = new FakeSmtpServer();
		server.start();
		Date time = new Date(new Date().getTime()+6000);//next 7 seconds
		emailService.scheduleDelivery(null, campaign.getId(), time);
		server.stop();
		try{ Thread.sleep(15*1000);}catch(Exception e){} //stop current thread for 15 seconds
		campaign = em.find(Campaign.class, campaign.getId());
		Assert.assertEquals(Campaign.Status.SENT, campaign.getStatus());
		
	}
	
	private Campaign createDemoCampaign(String name){
//		startTransaction();
//		em.createQuery("delete from Attachment").executeUpdate();
//		em.createQuery("delete from Campaign").executeUpdate();
//		commitTransaction();
		
		//Create campaign
		Campaign campaign = new Campaign();
//		campaign.setVelocity(true);
		campaign.setList(bootstrap.getDefaultSubscriberList());
		campaign.setMailAccount(bootstrap.getDefaultMailAccount());
		campaign.setCampaignName(name);
		campaign.setStatus(Campaign.Status.DRAFT);
		campaign.setBlacklistEnabled(true);
		campaign.setFromEmail("$(senderEmail)");
		campaign.setFromName("SiberHus LLC");
		campaign.setReplyToEmail("no-reply@siberhus.com");
		campaign.setMailSubject("Hello World Mix Message");
		campaign.setMailPriority(5);
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
		campaign.addAttachment(new Attachment("hello.txt","$(fileStore)/hello.txt"));
		campaign.addAttachment(new Attachment("secret.zip","$(fileStore)/secret.txt",true,"password"));
		
		return campaignService.save(campaign);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
	}
	
	
}

