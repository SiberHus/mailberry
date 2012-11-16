package com.siberhus.mailberry;


public class Config {
	
	public static final String TMP_DIR = "app.tmpDir"; //chop File.separator ending
	public static final String SERVER_URL = "app.serverUrl";//server base url (must end with /)
	
	public static class UI {
		
		public static class DataGrid {
			
			public static final String ROW_NUM = "ui.datagrid.rowNum"; //default number of display rows
			public static final String ROW_LIST = "ui.datagrid.rowList"; //comma separated values 20,30,40
			public static final String HEIGHT = "ui.datagrid.height";
		}
		
		public static class Calendar {
			public static final String CAMPAIGN_BACKGROUND_COLOR = "ui.calendar.campaign.backgroundColor";
			public static final String CAMPAIGN_BORDER_COLOR = "ui.calendar.campaign.borderColor";
			public static final String CAMPAIGN_TEXT_COLOR = "ui.calendar.campaign.textColor";
			
			//IN PROGRESS
			public static final String CAMPAIGN_INP_BACKGROUND_COLOR = "ui.calendar.campaign.inp.backgroundColor";
			public static final String CAMPAIGN_INP_BORDER_COLOR = "ui.calendar.campaign.inp.borderColor";
			public static final String CAMPAIGN_INP_TEXT_COLOR = "ui.calendar.campaign.inp.textColor";
			//SENT
			public static final String CAMPAIGN_SEN_BACKGROUND_COLOR = "ui.calendar.campaign.sen.backgroundColor";
			public static final String CAMPAIGN_SEN_BORDER_COLOR = "ui.calendar.campaign.sen.borderColor";
			public static final String CAMPAIGN_SEN_TEXT_COLOR = "ui.calendar.campaign.sen.textColor";
			//CANCELLED
			public static final String CAMPAIGN_CAN_BACKGROUND_COLOR = "ui.calendar.campaign.can.backgroundColor";
			public static final String CAMPAIGN_CAN_BORDER_COLOR = "ui.calendar.campaign.can.borderColor";
			public static final String CAMPAIGN_CAN_TEXT_COLOR = "ui.calendar.campaign.can.textColor";
			//SCHEDULED
			public static final String CAMPAIGN_SCH_BACKGROUND_COLOR = "ui.calendar.campaign.sch.backgroundColor";
			public static final String CAMPAIGN_SCH_BORDER_COLOR = "ui.calendar.campaign.sch.borderColor";
			public static final String CAMPAIGN_SCH_TEXT_COLOR = "ui.calendar.campaign.sch.textColor";
		}
	}
	
	public static class Subscriber {
		
		public static class Import {
			public static final String SOURCE_DIR = "import.source.dir"; //chop File.separator ending
			public static final String ERROR_DIR = "import.error.dir"; //chop File.separator ending
		}
		
		public static class OptOut {
			public static final String PAGE_SUCCESS = "optOut.page.success";
			public static final String PAGE_CONFIG = "optOut.page.config";
			public static final String LINK_NAME = "optOut.link.name";
		}
		
		public static class RSVP {
			public static final String PAGE_SUCCESS = "rsvp.page.success";
			public static final String PAGE_CONFIG = "rsvp.page.config";
			public static final String LINK_NAME_ATT = "rsvp.link.name.att";
			public static final String LINK_NAME_MAY = "rsvp.link.name.may";
			public static final String LINK_NAME_DEC = "rsvp.link.name.dec";
		}
		
	}
	
	public static class Format {
		
		public static final String DISPLAY_TIMESTAMP = "format.display.timestamp";
		public static final String DISPLAY_DATE = "format.display.date";
		public static final String DISPLAY_TIME = "format.display.time";
		
		public static final String INPUT_TIMESTAMP = "format.input.timestamp";
		public static final String INPUT_DATE = "format.input.date";
		public static final String INPUT_TIME = "format.input.time";
	}
	
	public static class Email {
		
		public static final String MAIL_USER_AGENT = "email.header.userAgent";//"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.20) Gecko/20110804 Thunderbird/3.1.12"
		public static final String DEFAULT_FROM_EMAIL = "email.fromEmail";
		public static final String DEFAULT_FROM_NAME = "email.fromName";
		public static final String DEFAULT_REPLY_TO_EMAIL = "email.replyToEmail";
		
		public static class Sending {
			public static final String PAUSE_BETWEEN_MESSAGES = "email.sending.pauseBetweenMessages";
			public static final String NUMBER_OF_THREADS = "email.sending.numberOfThreads";
			public static final String MESSAGES_PER_CONNECTION = "email.sending.messagesPerConnection";
			public static final String NUMBER_OF_ATTEMPTS = "email.sending.numberOfAttempts";
			public static final String PAUSE_BETWEEN_ATTEMPTS= "email.sending.pauseBetweenAttempts";
			public static final String BLOCK_ON_FAIL = "email.sending.blockOnFail";
			public static final String TIMEOUT = "email.sending.timeout";
		}
		
		public static class Template {
			public static final String VELOCITY_STORE_PATH = "email.body.velocity.storePath";
			public static final String VELOCITY_ENCODING = "email.body.velocity.encoding";//UTF-8
		}
		
		public static class SpamChecker {
			public static final String HOST = "spamChecker.host";
			public static final String PORT = "spamChecker.port";
		}
		
	}
	
}
