package com.siberhus.mailberry;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ServletContextAware;

import com.siberhus.mailberry.controller.AnalyticsController;
import com.siberhus.mailberry.controller.BlacklistsController;
import com.siberhus.mailberry.controller.CampaignsController;
import com.siberhus.mailberry.controller.ConfigsController;
import com.siberhus.mailberry.controller.CustomPagesController;
import com.siberhus.mailberry.controller.DashboardController;
import com.siberhus.mailberry.controller.EmailsController;
import com.siberhus.mailberry.controller.EventsCalendarController;
import com.siberhus.mailberry.controller.ExportsController;
import com.siberhus.mailberry.controller.ImportsController;
import com.siberhus.mailberry.controller.MailAccountsController;
import com.siberhus.mailberry.controller.MailServersController;
import com.siberhus.mailberry.controller.MessageTemplatesController;
import com.siberhus.mailberry.controller.SubscriberListsController;
import com.siberhus.mailberry.controller.SubscribersController;
import com.siberhus.mailberry.controller.TemplateChunksController;
import com.siberhus.mailberry.controller.TemplateVariablesController;
import com.siberhus.mailberry.controller.UsersController;
import com.siberhus.mailberry.model.Authority;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.DashboardWidget;
import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.MailServer.ConnectionSecurity;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.ConfigurationService;
import com.siberhus.mailberry.service.DashboardService;
import com.siberhus.mailberry.service.EmailService;
import com.siberhus.web.ui.js.format.JQueryDateFormatTranslator;

public class MailBerryBootstrap implements InitializingBean, ServletContextAware{
	
	private final Logger log = LoggerFactory.getLogger(MailBerryBootstrap.class);
	
	@PersistenceContext
	private EntityManager em;
	
	private ServletContext servletContext;
	
	/*
	 * Known issue.
	 * An Authentication object was not found in SecurityContext issue
	 * If we declare @PreAuthorize and there is no user (at the first install),
	 * we will get that exception.
	 */
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	@Inject
	private ConfigurationService configService;
	
	@Inject
	private Database database;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private CampaignService campaignService;
	
	@Inject
	private DashboardService dashboardService;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		servletContext.setAttribute("ctx", servletContext.getContextPath());
		
		servletContext.setAttribute("Charsets", Charset.availableCharsets().keySet());
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		
		TransactionStatus status = null;
		
		try{
			status = transactionManager.getTransaction(def);
			createDefaultUsers();
			transactionManager.commit(status);
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken("admin", "password"));
		}catch(Exception e){
			if(!status.isCompleted()){
				transactionManager.rollback(status);
			}
			throw e;
		}
		
		try{
			
			status = transactionManager.getTransaction(def);
			
			createDefaultConfigurations();
			
			createDefaultMailAccount();
			
			createDefaultWidgets();
			
			recoverEmailSchedules();
			
			createControllerPathMap();
			
			transactionManager.commit(status);
			
		}catch(Exception e){
			if(!status.isCompleted()){
				transactionManager.rollback(status);
			}
			throw e;
		}
	}
	
	private void createControllerPathMap(){
		
		log.info("Creating the PATH map");
		Map<String, String> pathMap = new HashMap<String, String>();
		String ctx = servletContext.getContextPath();
		//*********** MESSAGES ****************//
		pathMap.put("campaigns", ctx+CampaignsController.PATH);
		pathMap.put("emails", ctx+EmailsController.PATH);
		pathMap.put("calendar", ctx+EventsCalendarController.PATH);
		pathMap.put("messageTemplates", ctx+MessageTemplatesController.PATH);
		pathMap.put("templateVariables", ctx+TemplateVariablesController.PATH);
		pathMap.put("templateChunks", ctx+TemplateChunksController.PATH);
		//************ DATA *******************//
		pathMap.put("subscriberLists", ctx+SubscriberListsController.PATH);
		pathMap.put("blacklists", ctx+BlacklistsController.PATH);
		pathMap.put("subscribers", ctx+SubscribersController.PATH);
		pathMap.put("imports", ctx+ImportsController.PATH);
		pathMap.put("exports", ctx+ExportsController.PATH);
		//************ ANALYTICS **************//
		pathMap.put("analytics", ctx+AnalyticsController.PATH);
		//************ TOOLS **************//
		pathMap.put("customPages", ctx+CustomPagesController.PATH);
		//************ SETTINGS **************//
		pathMap.put("dashboard", ctx+DashboardController.PATH);
		pathMap.put("mailServers", ctx+MailServersController.PATH);
		pathMap.put("mailAccounts", ctx+MailAccountsController.PATH);
		pathMap.put("users", ctx+UsersController.PATH);
		pathMap.put("configs", ctx+ConfigsController.PATH);
		servletContext.setAttribute("PATH", pathMap);
		
		log.info("Finish setting up PATH map");
	}
	
	private void createDefaultConfigurations(){
		
		String baseUrl = "http://localhost:8080"+servletContext.getContextPath();
		
		Number number = (Number)em.createQuery("select count(*) from Configuration").getSingleResult();
		if(number.intValue()==0){
			
			log.info("No configurations. Creating default configurations");
			String tmpDir = servletContext.getRealPath("/tmp");
			configService.setValue(Config.SERVER_URL, String.class, baseUrl);
			configService.setValue(Config.TMP_DIR, String.class, tmpDir);
			
			configService.setValue(Config.Subscriber.Import.SOURCE_DIR, String.class, servletContext.getRealPath("/files/sources"));
			configService.setValue(Config.Subscriber.Import.ERROR_DIR, String.class, servletContext.getRealPath("/files/errors"));
			
			configService.setValue(Config.Email.SpamChecker.HOST, String.class, "localhost");
			configService.setValue(Config.Email.SpamChecker.PORT, Integer.class, 783);
			
			configService.setValue(Config.Format.DISPLAY_TIMESTAMP, String.class, "dd MMM yyyy HH:mm:ss");
			configService.setValue(Config.Format.DISPLAY_DATE, String.class, "dd MMM yyyy");
			configService.setValue(Config.Format.DISPLAY_TIME, String.class, "HH:mm:ss");
			configService.setValue(Config.Format.INPUT_TIMESTAMP, String.class, "dd/MM/yyyy HH:mm:ss");
			configService.setValue(Config.Format.INPUT_DATE, String.class, "dd/MM/yyyy");
			configService.setValue(Config.Format.INPUT_TIME, String.class, "HH:mm:ss");
			
			configService.setValue(Config.Email.Template.VELOCITY_ENCODING, String.class, "UTF-8");
			String velocityStorePath = servletContext.getRealPath("/templates");
			configService.setValue(Config.Email.Template.VELOCITY_STORE_PATH, String.class, velocityStorePath);
			configService.setValue(Config.Email.MAIL_USER_AGENT, String.class, null);
			configService.setValue(Config.Email.DEFAULT_FROM_EMAIL, String.class, "admin@siberhus.org");
			configService.setValue(Config.Email.DEFAULT_REPLY_TO_EMAIL, String.class, "admin@siberhus.org");
			
			configService.setValue(Config.Email.Sending.PAUSE_BETWEEN_MESSAGES, Integer.class, 1);
			configService.setValue(Config.Email.Sending.NUMBER_OF_THREADS, Integer.class, 2);
			configService.setValue(Config.Email.Sending.MESSAGES_PER_CONNECTION, Integer.class, 1);
			configService.setValue(Config.Email.Sending.NUMBER_OF_ATTEMPTS, Integer.class, 2);
			configService.setValue(Config.Email.Sending.PAUSE_BETWEEN_ATTEMPTS, Integer.class, 2);
			configService.setValue(Config.Email.Sending.BLOCK_ON_FAIL, Boolean.class, false);
			configService.setValue(Config.Email.Sending.TIMEOUT, Integer.class, 30);
			
		}
		
		log.info("Setting up default configurations");
		servletContext.setAttribute("serverUrl", configService.getValue(Config.SERVER_URL, String.class));
		servletContext.setAttribute("database", database.toString());
		servletContext.setAttribute("gridHeight", configService.getValue(Config.UI.DataGrid.HEIGHT, Integer.class, 350));
		servletContext.setAttribute("gridRowNum", configService.getValue(Config.UI.DataGrid.ROW_NUM, Integer.class, 20));
		servletContext.setAttribute("gridRowList", configService.getValueAsString(Config.UI.DataGrid.ROW_LIST, "10,20,30,40,50,60,70,80,90,100"));
		
		Map<String, String> FMT_MAP = new HashMap<String, String>();
		FMT_MAP.put("displayDate", configService.getValueAsString(Config.Format.DISPLAY_DATE));
		FMT_MAP.put("displayTimestamp", configService.getValueAsString(Config.Format.DISPLAY_TIMESTAMP));
		FMT_MAP.put("displayTime", configService.getValueAsString(Config.Format.DISPLAY_TIME));
		String inputDateFmtStr = configService.getValueAsString(Config.Format.INPUT_DATE);
		FMT_MAP.put("inputDate", inputDateFmtStr);
		String jsDateFmtStr = JQueryDateFormatTranslator.INSTANCE.translate(inputDateFmtStr);
		FMT_MAP.put("jsDate", jsDateFmtStr);
		FMT_MAP.put("inputTimestamp", configService.getValueAsString(Config.Format.INPUT_TIMESTAMP));
		FMT_MAP.put("inputTime", configService.getValueAsString(Config.Format.INPUT_TIME));
		servletContext.setAttribute("FMT", FMT_MAP);
		log.info("Finish setting up default configurations");
	}
	
	private void createDefaultUsers(){
		
		Number number = (Number)em.createQuery("select count(*) from User").getSingleResult();
		if(number.intValue()>0){
			return;
		}
		
		log.info("Creating default users");
		
		Authority adminAuth = new Authority(Roles.ADMIN);
		Authority userAuth = new Authority(Roles.USER);
		Authority demoAuth = new Authority(Roles.DEMO);
		
		Md5PasswordEncoder pwdEncoder = new Md5PasswordEncoder();
		pwdEncoder.setEncodeHashAsBase64(true);
		User adminUser = new User("admin", pwdEncoder.encodePassword("password", "admin"));
		adminUser.setEmail("admin@siberhus.com");
		adminUser.setFirstName("Administrator");
		adminUser.setLastName("System");
		adminUser.addAuthority(adminAuth);
		adminUser.setApiKey(RandomStringUtils.randomAlphanumeric(32));
		em.persist(adminUser);
		
		User adminDemoUser = new User("admin_demo", pwdEncoder.encodePassword("admin_demo", "admin_demo"));
		adminDemoUser.setEmail("admin.demo@siberhus.com");
		adminDemoUser.setFirstName("Administrator");
		adminDemoUser.setLastName("System");
		adminDemoUser.addAuthority(adminAuth);
		adminDemoUser.addAuthority(demoAuth);
		adminDemoUser.setApiKey(RandomStringUtils.randomAlphanumeric(32));
		em.persist(adminDemoUser);
		
		for(int i=1;i<5;i++){
			User user = new User("user"+i, pwdEncoder.encodePassword("password", "user"+i));
			user.setEmail("user"+i+"@siberhus.com");
			user.setFirstName("demo-user-"+i);
			user.setLastName("demo-user-"+i);
			user.setEnabled(false);
			user.addAuthority(userAuth);
			user.setApiKey(RandomStringUtils.randomAlphanumeric(32));
			em.persist(user);
		}
		
		User demoUser = new User("user_demo", pwdEncoder.encodePassword("user_demo", "user_demo"));
		demoUser.setEmail("user.demo@siberhus.com");
		demoUser.setFirstName("demo-user");
		demoUser.setLastName("demo-user");
		demoUser.addAuthority(userAuth);
		demoUser.addAuthority(demoAuth);
		demoUser.setApiKey(RandomStringUtils.randomAlphanumeric(32));
		em.persist(demoUser);
		
		log.info("Finish creating default users");
	}
	
	private void createDefaultMailAccount(){
		Number number = (Number)em.createQuery("select count(*) from MailServer").getSingleResult();
		if(number.intValue()>0){
			return;
		}
		
		log.info("Creating default mail account");
		User adminUser = (User)em.createQuery("from User u where u.username='admin'").getSingleResult();
		MailServer localServer = new MailServer();
		localServer.setServerName("localhost");
		localServer.setUser(adminUser);
		localServer.setDescription("Local SMTP/POP3 Server");
		localServer.setStatus(Status.ACTIVE);
		localServer.setSmtpServer("localhost");
		localServer.setSmtpPort(25);
		localServer.setPop3Server("localhost");
		localServer.setPop3Port(110);
		em.persist(localServer);
			
		MailServer gmailSslServer = new MailServer();
		gmailSslServer.setServerName("Gmail (SSL)");
		gmailSslServer.setUser(adminUser);
		gmailSslServer.setPublicServer(true);
		gmailSslServer.setDescription("Google Gmail Server");
		gmailSslServer.setStatus(Status.ACTIVE);
		gmailSslServer.setSmtpServer("smtp.gmail.com");
		gmailSslServer.setSmtpPort(465);
		gmailSslServer.setSmtpConnectionSecurity(ConnectionSecurity.SSL);
		gmailSslServer.setPop3Server("pop.gmail.com");
		gmailSslServer.setPop3Port(995);
		gmailSslServer.setPop3ConnectionSecurity(ConnectionSecurity.SSL);
		em.persist(gmailSslServer);
			
		MailServer gmailTlsServer = new MailServer();
		gmailTlsServer.setServerName("Gmail (TLS)");
		gmailTlsServer.setUser(adminUser);
		gmailTlsServer.setPublicServer(true);
		gmailTlsServer.setDescription("Google Gmail Server");
		gmailTlsServer.setStatus(Status.ACTIVE);
		gmailTlsServer.setSmtpServer("smtp.gmail.com");
		gmailTlsServer.setSmtpPort(587);
		gmailTlsServer.setSmtpConnectionSecurity(ConnectionSecurity.STARTTLS);
		gmailTlsServer.setPop3Server("pop.gmail.com");
		gmailTlsServer.setPop3Port(995);
		gmailSslServer.setPop3ConnectionSecurity(ConnectionSecurity.SSL);
		em.persist(gmailTlsServer);
		
		log.info("Finish creating default mail account");
	}
	
	private void createDefaultWidgets(){
		
		Number number = (Number)em.createQuery("select count(*) from DashboardWidget").getSingleResult();
		if(number.intValue()>0){
			return;
		}
		
		log.info("Creating default widgets");
		
		DashboardWidget widget = new DashboardWidget();
		widget.setName("JVM Memory");
		widget.setContentUri("/widgets/admin/jvm-memory");
		widget.setDefaultPosition(DashboardWidget.Position.Left);
		widget.setAdminOnly(true);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("Application Info");
		widget.setContentUri("/widgets/admin/app-info");
		widget.setDefaultPosition(DashboardWidget.Position.Left);
		widget.setAdminOnly(true);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("Fake SMTP Server");
		widget.setContentUri("/widgets/admin/fake-smtp-server");
		widget.setDefaultPosition(DashboardWidget.Position.Left);
		widget.setAdminOnly(true);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("Summary");
		widget.setContentUri("/widgets/default/summary");
		widget.setDefaultPosition(DashboardWidget.Position.Right);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("User Info");
		widget.setContentUri("/widgets/default/user-info");
		widget.setDefaultPosition(DashboardWidget.Position.Right);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("Last 10 Campaigns");
		widget.setContentUri("/widgets/default/listCampaigns/10");
		widget.setDefaultPosition(DashboardWidget.Position.Center);
		dashboardService.saveWidget(widget);
		
		widget = new DashboardWidget();
		widget.setName("My Last 10 Campaigns");
		widget.setContentUri("/widgets/default/listMyCampaigns/10");
		widget.setDefaultPosition(DashboardWidget.Position.Center);
		dashboardService.saveWidget(widget);
		
		log.info("Finish creating default mail account");
	}
	
	private void recoverEmailSchedules(){
		List<Campaign> campaigns = campaignService.findAllByStatus(Campaign.Status.SCHEDULED);
		if(campaigns!=null){
			log.info("Scheduling email scheduled campaigns");
			for(Campaign campaign: campaigns){
				emailService.scheduleDelivery(null, campaign.getId(), campaign.getScheduledTime());
			}
			log.info("Finish scheduling email scheduled campaigns");
		}
	}
	
	
}
