package com.siberhus.mailberry.service.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.siberhus.mailberry.model.Attachment;

public class FilledEmailMessage extends EmailMessage {
	
	private Map<String, String> dataVarMap = new HashMap<String, String>();
	
	public FilledEmailMessage(TemplateEmailMessage emailMessage){
		this.toEmail = emailMessage.getToEmail();
		this.fromEmail = emailMessage.getFromEmail();
		this.fromName = emailMessage.getFromName();
		this.replyToEmail = emailMessage.getReplyToEmail();
		this.mailSubject = emailMessage.getMailSubject();
		this.messageBodyText = emailMessage.getMessageBodyText();
		this.messageBodyHtml = emailMessage.getMessageBodyHtml();
		this.attachments = new ArrayList<Attachment>();
		for(Attachment att: emailMessage.getAttachments()){
			this.attachments.add(new Attachment(att.getFileName(), att.getFilePath()
				, att.isCompressed(), att.getArchivePasswd()));
		}
	}
	
	public Map<String, String> getDataVarMap() {
		return dataVarMap;
	}
	
}
