package com.siberhus.mailberry.service;

import java.util.Date;

import javax.mail.MessagingException;

import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.service.pojo.FilledEmailMessage;
import com.siberhus.mailberry.service.pojo.TemplateEmailMessage;

public interface EmailService {
	
	public int getProgress(Long userId, Long campaignId);
	
	public void sendTest(Long userId, Long campaignId, String email) throws MessagingException;
	
	public void sendTest(Long userId, Long campaignId) throws MessagingException;
	
	public void sendNow(Long userId, Long campaignId) throws MessagingException;
	
	public void scheduleDelivery(Long userId, Long campaignId, Date time);
	
	public boolean cancelDelivery(Long userId, Long campaignId);
	
	public TemplateEmailMessage createEmailMessage(Campaign campaign);
	
	public FilledEmailMessage fillUpEmailMessage(Campaign campaign, TemplateEmailMessage emailMessage, Subscriber subscriber);
	
	
}
