package com.siberhus.mailberry;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.siberhus.mailberry.model.Authority;
import com.siberhus.mailberry.model.Blacklist;
import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.model.TemplateChunk;
import com.siberhus.mailberry.model.TemplateVariable;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.ConfigurationService;

@Service
public class MailBerryTestBootstrap implements InitializingBean{
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	@Inject
	private ConfigurationService configService;
	
	public static final String DEFAULT_LIST_NAME = "us_presidents";
	public static final String DEFAULT_SMTP_PROFILE = "localhost";
	
	private Long defaultAdminUserId;
	private Long defaultMailAccountId;
	private Long defaultListId;
	
	public User getDefaultAdminUser(){
		return em.find(User.class, defaultAdminUserId);
	}
	
	public MailAccount getDefaultMailAccount(){
		return em.find(MailAccount.class, defaultMailAccountId);
	}
	
	public SubscriberList getDefaultSubscriberList(){
		SubscriberList list = em.find(SubscriberList.class, defaultListId);
		return list;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		createDefaultConfigurations();
		
		createDefaultUsers();
		
		createDefaultMailAccount();
		
		createDefaultSubscriberList();
		
		createDefaultTemplateVarsAndChunks();
		
		createDefaultBlacklist();
		
		transactionManager.commit(status);
	}
	
	private void createDefaultConfigurations(){
		Number number = (Number)em.createQuery("select count(*) from Configuration").getSingleResult();
		if(number.intValue()==0){
//			String masterKey = RandomStringUtils.randomAlphanumeric(16);
			String tmpDir = "src/test/tmp/";
			configService.setValue(Config.SERVER_URL, String.class, "http://localhost:8080");
			configService.setValue(Config.TMP_DIR, String.class, tmpDir);
			
			configService.setValue(Config.Email.SpamChecker.HOST, String.class, "localhost");
			configService.setValue(Config.Email.SpamChecker.PORT, Integer.class, 783);
			
			configService.setValue(Config.Format.DISPLAY_TIMESTAMP, String.class, "MMM dd,yyyy h:mm a");
			configService.setValue(Config.Format.DISPLAY_DATE, String.class, "MMM dd,yyyy");
			configService.setValue(Config.Format.DISPLAY_TIME, String.class, "h:mm a");
			configService.setValue(Config.Format.INPUT_TIMESTAMP, String.class, "MM/dd/yyyy h:mm a");
			configService.setValue(Config.Format.INPUT_DATE, String.class, "MM/dd/yyyy");
			configService.setValue(Config.Format.INPUT_TIME, String.class, "h:mm a");
			
			configService.setValue(Config.Email.Template.VELOCITY_ENCODING, String.class, "UTF-8");
			String velocityStorePath = "src/test/templates";
			configService.setValue(Config.Email.Template.VELOCITY_STORE_PATH, String.class, velocityStorePath);
			configService.setValue(Config.Email.MAIL_USER_AGENT, String.class, "MailBerry");
			configService.setValue(Config.Email.DEFAULT_FROM_EMAIL, String.class, "admin@siberhus.com");
			configService.setValue(Config.Email.DEFAULT_REPLY_TO_EMAIL, String.class, "admin@siberhus.com");
			
			configService.setValue(Config.Email.Sending.PAUSE_BETWEEN_MESSAGES, Integer.class, 0);
			configService.setValue(Config.Email.Sending.NUMBER_OF_THREADS, Integer.class, 4);
			configService.setValue(Config.Email.Sending.MESSAGES_PER_CONNECTION, Integer.class, 20);
			configService.setValue(Config.Email.Sending.NUMBER_OF_ATTEMPTS, Integer.class, 2);
			configService.setValue(Config.Email.Sending.PAUSE_BETWEEN_ATTEMPTS, Integer.class, 2);
			configService.setValue(Config.Email.Sending.BLOCK_ON_FAIL, Boolean.class, true);
			configService.setValue(Config.Email.Sending.TIMEOUT, Integer.class, 30);
		}
	}
	
	private void createDefaultUsers(){
		Number number = (Number)em.createQuery("select count(*) from User").getSingleResult();
		if(number.intValue()==0){
			Md5PasswordEncoder pwdEncoder = new Md5PasswordEncoder();
			pwdEncoder.setEncodeHashAsBase64(true);
			User adminUser = new User("admin", pwdEncoder.encodePassword("password", "admin"));
			adminUser.setEmail("admin@siberhus.com");
			adminUser.setFirstName("Hussachai");
			adminUser.setLastName("Puripunpinyo");
			adminUser.addAuthority(new Authority(Roles.ADMIN));
			em.persist(adminUser);
			
			for(int i=1;i<5;i++){
				User user = new User("user"+i, pwdEncoder.encodePassword("password", "user"+i));
				user.setEmail("user"+i+"@siberhus.com");
				Authority auth = null;
				try{
					auth = (Authority)em.createQuery("from Authority where authority=?")
					.setParameter(1, Roles.USER).getSingleResult();
					user.addAuthority(auth);
				}catch(NoResultException e){
					user.addAuthority(new Authority(Roles.USER));
				}
				em.persist(user);
			}
		}
	}
	
	private void createDefaultMailAccount(){
		Number number = (Number)em.createQuery("select count(*) from MailServer").getSingleResult();
		if(number.intValue()==0){
			MailServer mailServer = new MailServer();
			mailServer.setServerName("localhost");
			mailServer.setStatus(Status.ACTIVE);
			mailServer.setSmtpServer("localhost");
			mailServer.setSmtpPort(25);
			
			MailAccount mailAccount = new MailAccount();
			mailAccount.setDisplayName("Hussachai Puripunpinyo");
			mailAccount.setEmail("admin@siberhus.com");
			mailAccount.setStatus(Status.ACTIVE);
			mailAccount.setSmtpAuthen(true);
			mailAccount.setSmtpUsername("admin");
			mailAccount.setSmtpPassword("password");
			
			mailServer.addMailAccount(mailAccount);
			em.persist(mailServer);
			
			this.defaultMailAccountId = mailAccount.getId();
		}
	}
	
	private void createDefaultSubscriberList(){
		Number number = (Number)em.createQuery("select count(*) from SubscriberList").getSingleResult();
		if(number.intValue()==0){
			SubscriberList list = new SubscriberList();
			list.setListName(DEFAULT_LIST_NAME);
			list.setStatus(SubscriberList.Status.ACTIVE);
			list.setFieldCount(3);
			list.setField1Name("firstName");
			list.setField2Name("lastName");
			list.setField3Name("party");
			em.persist(list);
			defaultListId = list.getId();
			String[][] leads = new String[][]{
				new String[]{"washington@siberhus.com","George", "Washington", "", Subscriber.Status.ACTIVE},
				new String[]{"adams@siberhus.com","John", "Adams", "Federalist", Subscriber.Status.ACTIVE},
				new String[]{"jefferson@siberhus.com","Thomas", "Jefferson", "Democratic-Republican", Subscriber.Status.ACTIVE},
				
				//below is in global blocklist
				new String[]{"roosevelt@siberhus.com","Theodore", "Roosevelt", "Republican", Subscriber.Status.ACTIVE},
				new String[]{"wilson@siberhus.com","Woodrow", "Wilson", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"truman@siberhus.com","Harry S.", "Truman", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"eisenhower@siberhus.com","Dwight D.", "Eisenhower", "Republican", Subscriber.Status.ACTIVE},
				//============================
				
				//below is in local blocklist
				new String[]{"kennedy@siberhus.com","John F.", "Kennedy", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"carter@siberhus.com","Jimmy", "Carter", "Democratic", Subscriber.Status.ACTIVE},
				new String[]{"reagan@siberhus.com","Ronald", "Reagan", "Republican", Subscriber.Status.ACTIVE},
				//============================
				
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
		}
	}
	
	private void createDefaultTemplateVarsAndChunks(){
		
		TemplateVariable tv = new TemplateVariable("senderEmail", "admin@siberhus.com");
		em.persist(tv);
		tv = new TemplateVariable("website", "http://www.siberhus.com");
		em.persist(tv);
		tv = new TemplateVariable("fileStore", "src/test/files");
		em.persist(tv);
		
		TemplateChunk tc = new TemplateChunk();
		tc.setName("copyright");
		tc.setValue("SiberHus Copyright 2011");
		em.persist(tc);
	}
	
	private void createDefaultBlacklist(){
		
		String globals[] = new String[]{"roosevelt@siberhus.com","wilson@siberhus.com",
				"truman@siberhus.com","eisenhower@siberhus.com"};
		for(String email: globals){
			Blacklist gbl = new Blacklist();
			gbl.setEmail(email);
			em.persist(gbl);
		}
		
	}
}
