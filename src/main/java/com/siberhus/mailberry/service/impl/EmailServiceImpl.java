package com.siberhus.mailberry.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.NoResultException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.velocity.app.VelocityEngine;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.controller.TrackingsController;
import com.siberhus.mailberry.dao.BlacklistDao;
import com.siberhus.mailberry.dao.CampaignDao;
import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.dao.TemplateChunkDao;
import com.siberhus.mailberry.dao.TemplateVariableDao;
import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.email.DataVar;
import com.siberhus.mailberry.email.HtmlMessageInterceptor;
import com.siberhus.mailberry.exception.DuplicateRequestException;
import com.siberhus.mailberry.exception.MailBerryException;
import com.siberhus.mailberry.model.Attachment;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Campaign.MessageType;
import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.model.TemplateVariable;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.ConfigurationService;
import com.siberhus.mailberry.service.EmailService;
import com.siberhus.mailberry.service.TrackingService;
import com.siberhus.mailberry.service.pojo.EmailMessage;
import com.siberhus.mailberry.service.pojo.FilledEmailMessage;
import com.siberhus.mailberry.service.pojo.TemplateEmailMessage;
import com.siberhus.mailberry.util.ZipUtils;

@Service
//@Scope("prototype")
public class EmailServiceImpl implements EmailService{

	private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	@Inject
	private VelocityEngine velocityEngine;
	
	@Inject
	private StringEncryptor encryptor;
	
	//******************* DAOs ***********************//
	@Inject
	private SubscriberDao subscriberDao;
	
	@Inject
	private TemplateChunkDao templateChunkDao;
	
	@Inject
	private TemplateVariableDao templateVarDao;
	
	@Inject
	private BlacklistDao blacklistDao;
	
	@Inject
	private CampaignDao campaignDao;
	
	@Inject
	private TrackingDao trackingDao;
	
	//******************* Services *********************//
	@Inject
	private TrackingService trackingService;
	
	@Inject
	private ConfigurationService configService;
	
	private static final ConcurrentHashMap<Long, Integer> PROGRESSES = new ConcurrentHashMap<Long, Integer>();
	private static final ConcurrentHashMap<Long, EmailTimer> SCHEDULERS = new ConcurrentHashMap<Long, EmailTimer>();
	private static final ConcurrentHashMap<Long, Boolean> STOP_SIGNALS = new ConcurrentHashMap<Long, Boolean>();
	
	@Override
	public int getProgress(Long userId, Long campaignId){
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		Integer progress = PROGRESSES.get(campaignId);
		if(progress==null){
			if(Campaign.Status.SENT.equals(campaign.getStatus())){
				return 100;
			}else{
				return 0;
			}
		}
		return progress;
	}
	
	@Override
	public void sendTest(Long userId, Long campaignId, String email) throws MessagingException {
		
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		
		JavaMailSender sender = createJavaMailSender(campaign);
		TemplateEmailMessage emailMessage = createEmailMessage(campaign);
		MimeMessage message = sender.createMimeMessage();
		if(emailMessage.getUserAgent()!=null){
			message.setHeader("User-Agent", emailMessage.getUserAgent());
		}
		MimeMessageHelper helper = new MimeMessageHelper(message, emailMessage.isMultipart());
		if(campaign.getMailPriority()!=null) helper.setPriority(campaign.getMailPriority());
		//Set Test Email
		helper.setTo(email);
		if(emailMessage.getFromName()!=null){
			helper.setFrom(emailMessage.getFromName()+" <"+emailMessage.getFromEmail()+">");
		}else{
			helper.setFrom(emailMessage.getFromEmail());
		}
		helper.setReplyTo(emailMessage.getReplyToEmail());
		helper.setSubject(emailMessage.getMailSubject());
		if(campaign.getMessageType()==MessageType.MIX){
			helper.setText(emailMessage.getMessageBodyText(), emailMessage.getMessageBodyHtml());
		}else if(campaign.getMessageType()==MessageType.TEXT){
			helper.setText(emailMessage.getMessageBodyText(), false);
		}else if(campaign.getMessageType()==MessageType.HTML){
			helper.setText(emailMessage.getMessageBodyHtml(), true);
		}
		
		addAttachments(helper, emailMessage);
		
		sender.send(message);
	}
	
	@Transactional
	@Override
	public void sendTest(Long userId, Long campaignId) throws MessagingException {
		log.info("Start sending email to Test subscribers");
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		
		JavaMailSender sender = createJavaMailSender(campaign);
		TemplateEmailMessage templateEmailMessage = createEmailMessage(campaign);
		SubscriberList list = campaign.getList();
		
		List<Subscriber> subscriberList = subscriberDao
			.findAllByStatusFromList(list, Subscriber.Status.TEST);
		if(subscriberList.size()==0){
			throw new NoResultException("No test data");
		}
		MimeMessage messages[] = new MimeMessage[subscriberList.size()];
		for(int i=0;i<messages.length;i++){
			Subscriber subscriber = subscriberList.get(i);
			trackingService.createTracking(campaign.getId(), subscriber);
			MimeMessage message = createPersonalizedEmail(sender, campaign, 
					templateEmailMessage, subscriber);
			messages[i] = message;
		}
		try{
			log.debug("Sending campaign:{} to subscribers:{}", 
				new Object[]{campaign, subscriberList});
			sender.send(messages);
			log.debug("Campaign: {} was sent successfully", campaign);
		}catch(MailSendException e){
			log.debug("Send failde: {}", e.toString());
			for(Map.Entry<Object, Exception> msgEntry: e.getFailedMessages().entrySet()){
				MimeMessage message = (MimeMessage)msgEntry.getKey();
				Exception error = msgEntry.getValue();
				String failedEmail = message.getRecipients(RecipientType.TO)[0].toString();
				Subscriber failedSubscriber = subscriberDao.findByEmailFromList(list, failedEmail);
				if(failedSubscriber!=null){
					trackingService.setErrorResult(campaign.getId(), failedSubscriber.getId(), error);
				}else{
					log.error("Cannot find failed subscriber:{}", failedEmail);
				}
			}
			throw e;
		}
		log.debug("SendTest finished");
	}
	
	/**
	 * This is long-running method, so it have to manages its own transaction and keep it as short
	 * as possible otherwise transaction timeout will be raised.
	 */
	@Override
	public void sendNow(Long userId, final Long campaignId) throws MessagingException {
		
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		
		if(PROGRESSES.contains(campaignId)){
			throw new DuplicateRequestException("Campaign: "+campaignId+" is in progress");
		}
		PROGRESSES.put(campaignId, 0);
		if(!Campaign.Status.DRAFT.equals(campaign.getStatus())
				&& !Campaign.Status.SCHEDULED.equals(campaign.getStatus())){
			throw new IllegalStateException("Campaign: "+campaignId+" was sent or scheduled ");
		}
		try{
			TransactionDefinition txDef = new DefaultTransactionDefinition();
			TransactionStatus txStatus = null;
			try{
				txStatus = transactionManager.getTransaction(txDef);
				campaign.setStatus(Campaign.Status.IN_PROGRESS);
				campaign.setSendTime(new Date());
				campaignDao.save(campaign);
				transactionManager.commit(txStatus);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				if(txStatus!=null && !txStatus.isCompleted()){
					transactionManager.rollback(txStatus);
				}
				throw new MailBerryException("Unexpected error", e);
			}
			
			Stat stat = _sendNow(campaign, new EmailSendingConfig(configService));
			
			txStatus = null;
			try{
				txStatus = transactionManager.getTransaction(txDef);
				campaign.setStatus(Campaign.Status.SENT);
				campaign.setFinishTime(new Date());
				//Update MailServer Profile Stat
				MailAccount mailAccount = campaign.getMailAccount();
				mailAccount.setTotalSentMails(mailAccount.getTotalSentMails()
						+stat.successCount.intValue()+stat.failureCount.intValue());
				mailAccount.setSuccessfulMails(mailAccount.getSuccessfulMails()
						+stat.successCount.intValue());
				mailAccount.setFailedMails(mailAccount.getFailedMails()
						+stat.failureCount.intValue());
				campaignDao.save(campaign);
				transactionManager.commit(txStatus);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				if(txStatus!=null && !txStatus.isCompleted()){
					transactionManager.rollback(txStatus);
				}
				throw new MailBerryException("Unexpected error", e);
			}
			
		}finally{
			PROGRESSES.remove(campaignId);
			STOP_SIGNALS.remove(campaignId);
		}
	}
	
	private Stat _sendNow(final Campaign campaign, final EmailSendingConfig config){
		
		final JavaMailSender sender = createJavaMailSender(campaign);
		final Stat stat = new Stat();
		final TemplateEmailMessage templateEmailMessage = createEmailMessage(campaign);
		final SubscriberList list = campaign.getList();
		
		List<Long> subscriberIdList = subscriberDao
			.getAllIdsByStatusFromList(list, Subscriber.Status.ACTIVE);
		
		final int subscriberCount = subscriberIdList.size();
		int roundCount = (int)Math.ceil(subscriberCount/new Double(config.messagesPerConnection));
		final CountDownLatch doneSignal = new CountDownLatch(roundCount);
		
		ExecutorService executor = Executors.newFixedThreadPool(config.numberOfThread);
		
		final TransactionDefinition txDef = new DefaultTransactionDefinition();
		
		for(int i=0; i<subscriberIdList.size(); i+=config.messagesPerConnection){
			
			final List<Long> subIdList = new ArrayList<Long>();
			for(int j=0;j<config.messagesPerConnection;j++){
				if( (i+j) < subscriberIdList.size()){
					subIdList.add(subscriberIdList.get(i+j));
				}
			}
			
			executor.execute(new Runnable(){
				@Override
				public void run() {
					if(STOP_SIGNALS.get(campaign.getId())!=null){
						return;
					}
					
					TransactionStatus txStatus = null;
					try{
						txStatus = transactionManager.getTransaction(txDef);
						Map<Object, Exception> failedMsgMap = null;
						Collection<MimeMessage> messages = new ArrayList<MimeMessage>();
						for(int i=0; i< subIdList.size(); i++){
							Long subscriberId = subIdList.get(i);
							Subscriber subscriber = subscriberDao.get(subscriberId);
							trackingService.createTracking(campaign.getId(), subscriber);
							
							if(templateEmailMessage.getBlacklist()!=null){
								if(templateEmailMessage.getBlacklist().contains(subscriber.getEmail())){
									log.debug("Skip email: {} because it's in global block list", subscriber.getEmail());
									continue;
								}
							}
							
							MimeMessage message = createPersonalizedEmail(sender, campaign, templateEmailMessage, subscriber);
							messages.add(message);
						}
						
						log.info("Sending campaign: {} to subscribers: {}"
							, new Object[]{campaign.getId(), subIdList});
						try{
							sender.send(messages.toArray(new MimeMessage[0]));
						}catch(MailSendException e){
							failedMsgMap = e.getFailedMessages();
							for(int i=0;i<config.numberOfAttempts;i++){
								Set<Object> failedMessages = failedMsgMap.keySet();
								try{
									sender.send(failedMessages.toArray(new MimeMessage[0]));
									failedMessages = null;
									break;
								}catch(MailSendException e2){
									failedMsgMap = e2.getFailedMessages();
									if(config.pauseBetweenAttemps>0){
										Thread.sleep(config.pauseBetweenAttemps*1000);
									}
									continue;
								}
							}
//							messages[0].getRecipients(RecipientType.TO).toString();
						}
						
						int total = messages.size();
						int failures = 0;
						if(failedMsgMap!=null){
							failures = failedMsgMap.size();
							for(Map.Entry<Object, Exception> msgEntry: failedMsgMap.entrySet()){
								MimeMessage message = (MimeMessage)msgEntry.getKey();
								Exception error = msgEntry.getValue();
								String failedEmail = message.getRecipients(RecipientType.TO)[0].toString();
								Subscriber failedSubscriber = subscriberDao.findByEmailFromList(list, failedEmail);
								if(failedSubscriber!=null){
									trackingService.setErrorResult(campaign.getId(), failedSubscriber.getId(), error);
									if(config.blockOnFail){
										failedSubscriber.setStatus(Subscriber.Status.BLOCKED);
									}
								}else{
									log.error("Cannot find failed subscriber:{}", failedEmail);
								}
							}
						}
						
						int successes = total-failures;
						int totalSuccess = stat.successCount.addAndGet(successes);
						int totalFailure = stat.failureCount.addAndGet(failures);
						int progress = (int)( ((totalSuccess+totalFailure) * 100)/ subscriberCount);
						PROGRESSES.put(campaign.getId(), progress);
						
						transactionManager.commit(txStatus);
						
					}catch(Exception e){
						log.error(e.getMessage(), e);
						if(txStatus!=null && !txStatus.isCompleted()){
							transactionManager.rollback(txStatus);
						}
					}finally{
						doneSignal.countDown();
					}
					if(config.pauseBetweenMessages>0){
						try{
							Thread.sleep(config.pauseBetweenMessages*1000);
						}catch(Exception e){log.error(e.getMessage(), e);}
					}
				}// END RUN
			});
		}//END FOR
		
		log.info("Waing for {} tasks be done.", subscriberCount);
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
		
		log.info("Shutdown email executor service");
		try{
			executor.shutdown();
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		
		return stat;
	}
	
	private JavaMailSender createJavaMailSender(Campaign campaign){
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		MailAccount mailAccount = campaign.getMailAccount();
		MailServer mailServer = mailAccount.getMailServer();
		
		Properties smtpProps = new Properties();
		sender.setHost(mailServer.getSmtpServer());
		sender.setProtocol("smtp");
		sender.setPort(mailServer.getSmtpPort());
		sender.setDefaultEncoding(campaign.getMessageCharset());
		if(mailAccount.isSmtpAuthen()){
			smtpProps.setProperty("mail.smtp.auth", "true");
			sender.setUsername(mailAccount.getSmtpUsername());
			//decrypt password
			String smtpPasswd = mailAccount.getSmtpPassword();
			smtpPasswd = encryptor.decrypt(smtpPasswd);
			sender.setPassword(smtpPasswd);
		}
		switch(mailServer.getSmtpConnectionSecurity()){
		case SSL:
			sender.setProtocol("smtps");
			smtpProps.setProperty("mail.smtp.socketFactory.port", 
				String.valueOf(mailServer.getSmtpPort()));
			smtpProps.setProperty("mail.smtp.socketFactory.class", 
				"javax.net.ssl.SSLSocketFactory");
			break;
		case STARTTLS:
			smtpProps.setProperty("mail.smtp.starttls.enable", "true");
		}
		if(mailServer.getSmtpTimeout()!=null){
			//Socket I/O timeout value in seconds. Default is infinite timeout.
			smtpProps.setProperty("mail.smtp.timeout", String.valueOf((mailServer.getSmtpTimeout()*1000)));//change from milliseconds to seconds
		}
		
		//Override the properties if need
		if(mailServer.getSmtpProperties()!=null){
			smtpProps = new Properties();
			try {
				smtpProps.load(new StringReader(mailServer.getSmtpProperties()));
			} catch (IOException e) {/*No IOException for StringReader*/}
		}
		sender.setJavaMailProperties(smtpProps);
		
		return sender;
	}
	
	
	private MimeMessage createPersonalizedEmail(JavaMailSender sender, Campaign campaign, 
			TemplateEmailMessage templateEmailMessage, Subscriber subscriber) throws MessagingException{
		
		MimeMessage message = sender.createMimeMessage();
		if(templateEmailMessage.getUserAgent()!=null){
			message.setHeader("User-Agent", templateEmailMessage.getUserAgent());
		}
		
		MimeMessageHelper helper = new MimeMessageHelper(message, templateEmailMessage.isMultipart());
		
		FilledEmailMessage emailMessage = fillUpEmailMessage(campaign, templateEmailMessage, subscriber);
		if(campaign.getMailPriority()!=null) helper.setPriority(campaign.getMailPriority());
		log.debug("Creating personalized email for: {}", emailMessage.getToEmail());
		helper.setTo(emailMessage.getToEmail());
		if(emailMessage.getFromName()!=null){
			helper.setFrom(emailMessage.getFromName()+" <"+emailMessage.getFromEmail()+">");
		}else{
			helper.setFrom(emailMessage.getFromEmail());
		}
		helper.setReplyTo(emailMessage.getReplyToEmail());
		helper.setSubject(emailMessage.getMailSubject());
		
		if(campaign.getMessageType()==MessageType.MIX){
			helper.setText(emailMessage.getMessageBodyText(), emailMessage.getMessageBodyHtml());
		}else if(campaign.getMessageType()==MessageType.TEXT){
			helper.setText(emailMessage.getMessageBodyText(), false);
		}else if(campaign.getMessageType()==MessageType.HTML){
			helper.setText(emailMessage.getMessageBodyHtml(), true);
		}
		
		addAttachments(helper, emailMessage);
		
		log.debug("Personalized email:{} was created successfully.", emailMessage.getToEmail());
		
		return message;
	}
	
	
	private void addAttachments(MimeMessageHelper helper, EmailMessage emailMessage) throws MessagingException{
		List<Attachment> attachments = emailMessage.getAttachments();
		if(attachments!=null && attachments.size()>0){
			log.debug("Adding attachments (total:{}) to email message", attachments.size());
			for(Attachment attachment: attachments){
				File attachedFile = new File(attachment.getFilePath());
				if(attachedFile.exists()){
					if(attachment.isCompressed()){
						String rand = RandomStringUtils.randomAlphanumeric(5);
						SimpleDateFormat sdf = new SimpleDateFormat("MMddmmHHssSSS");
						String tmpDirStr = configService.getValueAsString(Config.TMP_DIR);
						String generatedName = sdf.format(new Date())+"_"+rand+".zip";
						if(!tmpDirStr.endsWith("/")){
							tmpDirStr = tmpDirStr + "/";
						}
						File archivedFile = new File(tmpDirStr+generatedName);
						try{
							if(attachment.getArchivePasswd()!=null){
								log.debug("Compressing attachment file with password");
								ZipUtils.zipFile(attachedFile, archivedFile, attachment.getArchivePasswd());
							}else{
								log.debug("Compressing attachment file without password");
								ZipUtils.zipFile(attachedFile, archivedFile);
							}
						}catch(IOException e){
							log.error("Unable to create zip file: "+archivedFile, e);
						}
						helper.addAttachment(attachment.getFileName(), archivedFile);
					}else{
						if(attachedFile.isFile()){
							helper.addAttachment(attachment.getFileName(), attachedFile);
						}else{
							log.error("Cannot attach folder, you must compress it before sending.");
						}
					}
				}else{
					log.error("Unable to find attachment: {}", attachedFile);
				}
			}
			log.debug("Attachments were added successfully.");
		}
	}
	
	@Transactional
	@Override
	public void scheduleDelivery(final Long userId, final Long campaignId, Date time) {
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		log.info("Campaign: {} is scheduled to run at: {}",
				new Object[]{campaign, time});
		EmailTimer timer = new EmailTimer(true);
		campaign.setStatus(Campaign.Status.SCHEDULED);
		campaign.setScheduledTime(time);
		campaignDao.save(campaign);
		TimerTask senderTask = new TimerTask(){
			@Override
			public void run() {
				try {
					sendNow(userId, campaignId);
				} catch (Exception e) {
					log.error(e.getMessage() ,e);
				}
				SCHEDULERS.remove(campaignId);
			}
		};
		timer.schedule(senderTask, time);
		SCHEDULERS.put(campaignId, timer);
	}
	
	@Transactional
	@Override
	public boolean cancelDelivery(Long userId, Long campaignId){
		Campaign campaign = campaignDao.get(userId, campaignId);
		Assert.notNull(campaign);
		if(Campaign.Status.IN_PROGRESS.equals(campaign.getStatus())){
			campaign.setStatus(Campaign.Status.CANCELLED);
			STOP_SIGNALS.put(campaign.getId(), true);
		}else if(Campaign.Status.SCHEDULED.equals(campaign.getStatus())){
			campaign.setStatus(Campaign.Status.DRAFT);
			campaign.setScheduledTime(null);
		}
		Timer timer = SCHEDULERS.get(campaignId);
		if(timer!=null){
			timer.cancel();
			SCHEDULERS.remove(campaignId);
			return true;
		}
		return false;
	}
	
	@Override
	public TemplateEmailMessage createEmailMessage(Campaign campaign) {
		log.debug("Creating template email message for campaign: {}", campaign);
		TemplateEmailMessage emailMessage = new TemplateEmailMessage();
		if(campaign.isInlineResource() 
				|| campaign.getMessageType()==MessageType.MIX 
				|| campaign.getAttachments()!=null){
			emailMessage.setMultipart(true);
		}
		String mailUserAgent = configService.getValueAsString(Config.Email.MAIL_USER_AGENT);
		emailMessage.setUserAgent(mailUserAgent);
		log.debug("Fetching template variables into memory.");
		List<TemplateVariable> templateVars = templateVarDao.findAllByStatus(Status.ACTIVE);
		Map<String, String> msgVarMap = new HashMap<String, String>();
		for(TemplateVariable templateVar: templateVars){
			msgVarMap.put(templateVar.getName(), templateVar.getValue());
		}
		log.debug("Total template variables: {}", msgVarMap.size());
		
		final StrSubstitutor msgVarSub = new StrSubstitutor(msgVarMap, "$(", ")");
		StrSubstitutor msgChunkSub = new StrSubstitutor(new StrLookup() {
			@Override
			public String lookup(String key) {
				String value = null;
				try{
					value = templateChunkDao.getValueByNameAndStatus(key, Status.ACTIVE);
					return msgVarSub.replace(value);
				}catch(Exception e){
					log.error("Unable to find key={} in template chunk", key);
					return "";
				}
			}
		}, "$[", "]", StrSubstitutor.DEFAULT_ESCAPE);
		emailMessage.setFromEmail(msgVarSub.replace(campaign.getFromEmail()));
		if(campaign.getFromName()!=null){
			emailMessage.setFromName(msgVarSub.replace(campaign.getFromName()));
		}
		String replyToEmail = campaign.getReplyToEmail();
		if(StringUtils.isBlank(replyToEmail)){
			replyToEmail = emailMessage.getFromEmail();
		}else{
			replyToEmail = msgVarSub.replace(campaign.getReplyToEmail());
		}
		emailMessage.setReplyToEmail(replyToEmail);
		emailMessage.setMailSubject(msgVarSub.replace(campaign.getMailSubject()));
		
		if(campaign.getMessageType()==Campaign.MessageType.MIX
				|| campaign.getMessageType()==Campaign.MessageType.TEXT){
			String value = msgVarSub.replace(campaign.getMessageBodyText());
			value = msgChunkSub.replace(value);
			emailMessage.setMessageBodyText(value);
		}
		if(campaign.getMessageType()==Campaign.MessageType.MIX
				|| campaign.getMessageType()==Campaign.MessageType.HTML){
			String value = msgVarSub.replace(campaign.getMessageBodyHtml());
			value = msgChunkSub.replace(value);
			emailMessage.setMessageBodyHtml(value);
		}
		
		List<Attachment> attachments = campaign.getAttachments();
		if(attachments!=null){
			log.debug("Substituing attachments' variables");
			attachments = new ArrayList<Attachment>(attachments);
			for(Attachment attachment: attachments){
				attachment.setFileName(msgVarSub.replace(attachment.getFileName()));
				attachment.setFilePath(msgVarSub.replace(attachment.getFilePath()));
				attachment.setArchivePasswd(msgVarSub.replace(attachment.getArchivePasswd()));
			}
			emailMessage.setAttachments(attachments);
		}
		
		List<?> blacklist = null;
		if(campaign.isBlacklistEnabled()){
			Long userId = null;
			if(campaign.getUser()!=null){
				userId = campaign.getUser().getId();
			}
			blacklist = blacklistDao.findAllEmails(userId);
			emailMessage.setBlacklist(blacklist);
			if(blacklist!=null){
				log.debug("Total emails in blacklist: {}", blacklist.size());
			}
		}
		
		if(campaign.getMessageType()==MessageType.MIX
				|| campaign.getMessageType()==MessageType.HTML){
			//Intercept html message before filling data
			HtmlMessageInterceptor htmlMsgInterceptor = new HtmlMessageInterceptor();
			htmlMsgInterceptor.setClickstream(campaign.isClickstream());
			htmlMsgInterceptor.setOpenTracking(campaign.isTrackable());
			String htmlMessage = emailMessage.getMessageBodyHtml();
			try {
				htmlMessage = htmlMsgInterceptor.parse(new StringReader(htmlMessage), true).toString();
			} catch (IOException e) {
				log.error("HTML message interceptor error", e);
			}
			emailMessage.setMessageBodyHtml(htmlMessage);
		}
		return emailMessage;
	}
	
	@Override
	public FilledEmailMessage fillUpEmailMessage(Campaign campaign, TemplateEmailMessage emailMessage, Subscriber subscriber) {
		log.debug("Filling up email message for subscriber: {}", subscriber);
		SubscriberList list = campaign.getList();
		FilledEmailMessage filledEmailMessage = new FilledEmailMessage(emailMessage);
		Map<String, String> dataVarMap = filledEmailMessage.getDataVarMap();
		dataVarMap.put(DataVar.EMAIL, subscriber.getEmail());
		if(list.getField1Name()!=null)dataVarMap.put(list.getField1Name(),subscriber.getField1Value());
		if(list.getField2Name()!=null)dataVarMap.put(list.getField2Name(),subscriber.getField2Value());
		if(list.getField3Name()!=null)dataVarMap.put(list.getField3Name(),subscriber.getField3Value());
		if(list.getField4Name()!=null)dataVarMap.put(list.getField4Name(),subscriber.getField4Value());
		if(list.getField5Name()!=null)dataVarMap.put(list.getField5Name(),subscriber.getField5Value());
		if(list.getField6Name()!=null)dataVarMap.put(list.getField6Name(),subscriber.getField6Value());
		if(list.getField7Name()!=null)dataVarMap.put(list.getField7Name(),subscriber.getField7Value());
		if(list.getField8Name()!=null)dataVarMap.put(list.getField8Name(),subscriber.getField8Value());
		if(list.getField9Name()!=null)dataVarMap.put(list.getField9Name(),subscriber.getField9Value());
		if(list.getField10Name()!=null)dataVarMap.put(list.getField10Name(),subscriber.getField10Value());
		if(list.getField11Name()!=null)dataVarMap.put(list.getField11Name(),subscriber.getField11Value());
		if(list.getField12Name()!=null)dataVarMap.put(list.getField12Name(),subscriber.getField12Value());
		if(list.getField13Name()!=null)dataVarMap.put(list.getField13Name(),subscriber.getField13Value());
		if(list.getField14Name()!=null)dataVarMap.put(list.getField14Name(),subscriber.getField14Value());
		if(list.getField15Name()!=null)dataVarMap.put(list.getField15Name(),subscriber.getField15Value());
		if(list.getField16Name()!=null)dataVarMap.put(list.getField16Name(),subscriber.getField16Value());
		if(list.getField17Name()!=null)dataVarMap.put(list.getField17Name(),subscriber.getField17Value());
		if(list.getField18Name()!=null)dataVarMap.put(list.getField18Name(),subscriber.getField18Value());
		if(list.getField19Name()!=null)dataVarMap.put(list.getField19Name(),subscriber.getField19Value());
		if(list.getField20Name()!=null)dataVarMap.put(list.getField20Name(),subscriber.getField20Value());
		
		String serverUrl = configService.getValueAsString(Config.SERVER_URL);
		String securityToken = trackingDao.getSecurityToken(campaign.getId(), subscriber.getId());
		dataVarMap.put(DataVar.SERVER_URL, configService.getValueAsString(Config.SERVER_URL));
		dataVarMap.put(DataVar.CAMPAIGN_ID, String.valueOf(campaign.getId()));
		dataVarMap.put(DataVar.SUBSCRIBER_ID, String.valueOf(subscriber.getId()));
		dataVarMap.put(DataVar.SECURITY_TOKEN, securityToken);
		
		//Modifies html content
		//	* Modifies link to support clickstream
		//	* Adds open email tracking image
		if(campaign.isTrackable() || campaign.isClickstream()){
			String commonTrackingUrlPart = campaign.getId()+"/"+subscriber.getId()+"/"+securityToken;
			if(campaign.isTrackable()){
				String trackingUrl = serverUrl+TrackingsController.PATH+"/"+commonTrackingUrlPart+"/open.png";
				dataVarMap.put("_openTracking_", trackingUrl);
			}
			if(campaign.isClickstream()){
				String clickstreamUrl = serverUrl+TrackingsController.PATH+"/redirect/"+commonTrackingUrlPart+"?url=";
				dataVarMap.put("_clickstream_", clickstreamUrl);
			}
		}
		
		filledEmailMessage.setToEmail(subscriber.getEmail());
		StrSubstitutor dataVarSub = new StrSubstitutor(dataVarMap, "${", "}");
		filledEmailMessage.setFromEmail(dataVarSub.replace(filledEmailMessage.getFromEmail()));
		if(filledEmailMessage.getFromName()!=null){
			filledEmailMessage.setFromName(dataVarSub.replace(filledEmailMessage.getFromName()));
		}
		filledEmailMessage.setReplyToEmail(dataVarSub.replace(filledEmailMessage.getReplyToEmail()));
		filledEmailMessage.setMailSubject(dataVarSub.replace(filledEmailMessage.getMailSubject()));
		if(campaign.isVelocity()){
			log.debug("Interpreting message body by velocity engine");
			String encoding = configService.getValueAsString(Config.Email.Template.VELOCITY_ENCODING);
			if(campaign.getMessageType()==MessageType.MIX
				|| campaign.getMessageType()==MessageType.TEXT){
				String fileName = CampaignService.FILE_PREFIX_TEXT+campaign.getMessageFile();
				String textMessage = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, fileName, encoding, dataVarMap);
				filledEmailMessage.setMessageBodyText(textMessage);
			}
			if(campaign.getMessageType()==MessageType.MIX
				|| campaign.getMessageType()==MessageType.HTML){
				String fileName = CampaignService.FILE_PREFIX_HTML+campaign.getMessageFile();
				String htmlMessage = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, fileName, encoding, dataVarMap);
				filledEmailMessage.setMessageBodyHtml(htmlMessage);
			}
		}else{
			log.debug("Interpreting message body by string substitution");
			if(campaign.getMessageType()==MessageType.MIX
				|| campaign.getMessageType()==MessageType.TEXT){
				filledEmailMessage.setMessageBodyText(dataVarSub.replace(
						filledEmailMessage.getMessageBodyText()));
			}
			if(campaign.getMessageType()==MessageType.MIX
				|| campaign.getMessageType()==MessageType.HTML){
				filledEmailMessage.setMessageBodyHtml(dataVarSub.replace(
						filledEmailMessage.getMessageBodyHtml()));
			}
		}
		List<Attachment> attachments = filledEmailMessage.getAttachments();
		if(attachments!=null){
			log.debug("Filling attachments' data variables");
			for(Attachment attachment: attachments){
				attachment.setFileName(dataVarSub.replace(attachment.getFileName()));
				attachment.setFilePath(dataVarSub.replace(attachment.getFilePath()));
				attachment.setArchivePasswd(dataVarSub.replace(attachment.getArchivePasswd()));
			}
		}
		
		log.debug("Email message was filled for subscriber: {}", subscriber);
		
		return filledEmailMessage;
	}
	
	private static class EmailSendingConfig {
		
		public int numberOfThread;
		public int messagesPerConnection;
		public int numberOfAttempts;
		public int pauseBetweenAttemps;
		public int pauseBetweenMessages;
		public boolean blockOnFail;
		
		public EmailSendingConfig(ConfigurationService configService){
			this.numberOfThread = configService.getValue(
					Config.Email.Sending.NUMBER_OF_THREADS, Integer.class);
			this.messagesPerConnection = configService.getValue(Config.Email.Sending
					.MESSAGES_PER_CONNECTION, Integer.class);
			this.numberOfAttempts = configService.getValue(Config.Email.Sending
					.NUMBER_OF_ATTEMPTS, Integer.class);
			this.pauseBetweenAttemps = configService.getValue(Config.Email.Sending
					.PAUSE_BETWEEN_ATTEMPTS, Integer.class);
			this.pauseBetweenMessages = configService.getValue(Config.Email.Sending
					.PAUSE_BETWEEN_MESSAGES, Integer.class);
			this.blockOnFail = configService.getValue(Config.Email.Sending
					.BLOCK_ON_FAIL, Boolean.class);
		}
	}
	
	private static class Stat {
		public AtomicInteger successCount = new AtomicInteger(0);
		public AtomicInteger failureCount = new AtomicInteger(0);
	}
	
	private static class EmailTimer extends Timer {
		
		private Date time;
		
		public EmailTimer(boolean isDaemon) {
			super(isDaemon);
		}
		
		@Override
		public void schedule(TimerTask task, Date time) {
			this.time = time;
			super.schedule(task, time);
		}
		
		@SuppressWarnings("unused")
		public Date getTime(){
			return time;
		}
	}
	
}
