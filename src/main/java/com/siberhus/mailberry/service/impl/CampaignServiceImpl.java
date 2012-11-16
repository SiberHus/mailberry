package com.siberhus.mailberry.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.dao.CampaignDao;
import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.exception.MailBerryException;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Campaign.MessageType;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.ConfigurationService;
import com.siberhus.mailberry.service.EmailService;
import com.siberhus.mailberry.service.TrackingService;
import com.siberhus.mailberry.service.pojo.EmailMessage;

@Service
public class CampaignServiceImpl implements CampaignService {
	
	private final Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);
	
	@Inject
	private CampaignDao campaignDao;
	
	@Inject
	private SubscriberDao subscriberDao;
	
	@Inject
	private ConfigurationService configService;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private TrackingService trackingService;
	
	@Transactional
	@Override
	public Campaign save(Campaign campaign) {
		if(campaign.isVelocity()){
			log.debug("Campaign: {} has velocity content", campaign);
			String rand = RandomStringUtils.randomAlphanumeric(4);
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddmmhh");
			String generatedName = sdf.format(new Date())+"_"+rand+".vm";
			String encoding = configService.getValueAsString(Config.Email.Template.VELOCITY_ENCODING);
			File outFile = null;
			campaign.setMessageFile(generatedName);
			try{
				EmailMessage emailMessage = emailService.createEmailMessage(campaign);
				if(campaign.getMessageType()==MessageType.MIX
					|| campaign.getMessageType()==MessageType.TEXT){
					outFile = getTemplateFile(FILE_PREFIX_TEXT+generatedName);
					log.debug("Writing velocity enabled 'TEXT' message to file: {}",outFile);
					FileUtils.write(outFile, emailMessage.getMessageBodyText(), encoding);
				}
				if(campaign.getMessageType()==MessageType.MIX
					|| campaign.getMessageType()==MessageType.HTML){
					outFile = getTemplateFile(FILE_PREFIX_HTML+generatedName);
					log.debug("Writing velocity enabled 'HTML' message to file: {}",outFile);
					FileUtils.write(outFile, emailMessage.getMessageBodyHtml(), encoding);
				}
			} catch (IOException e) {
				throw new MailBerryException("Unable to write template data to "+outFile);
			}
		}
		boolean newCampaign = false;
		if(campaign.isNew()){
			newCampaign = true;
		}else{
			//remove old attachments
			campaignDao.removeAllAttachments(campaign);
		}
		Number emails = subscriberDao.countByStatusFromList(campaign.getList(), Subscriber.Status.ACTIVE);
		log.debug("Number of subscribers in campaign:{} = {}", new Object[]{campaign, emails});
		campaign.setEmails(emails.intValue());
		campaign = campaignDao.save(campaign);
		if(campaign.getAttachments()!=null){
			campaign.getAttachments().size();
		}
		
		if(newCampaign){
			trackingService.createTrackingTable(campaign.getId());
		}
		
		return campaign;
	}
	
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				campaignDao.delete(userId, id);
				trackingService.dropTrackingTable(id);
			}
		}
	}
	
	@Override
	public Campaign get(Long userId, Long id) {
		Campaign campaign =  campaignDao.get(userId, id);
		return campaign;
	}
	
	
	@Override
	public List<Campaign> getCampaignsForCalendar(Long userId, Date start, Date end) {
		
		return campaignDao.getCampaignsForCalendar(userId, start, end);
	}
	
	private File getTemplateFile(String fileName){
		String storePath = configService.getValueAsString(Config.Email.Template.VELOCITY_STORE_PATH);
		if(!storePath.endsWith(File.separator)){
			storePath = storePath + File.separator;
		}
		File templateFile = new File(storePath+fileName);
		return templateFile;
	}

	@Override
	public List<Campaign> findAllByStatus(String status) {
		
		return campaignDao.findAllByStatus(status);
	}
	
	@Override
	public int countByCampaignName(Long userId, String campaignName){
		
		return campaignDao.countByCampaignName(userId, campaignName);
	}
}
