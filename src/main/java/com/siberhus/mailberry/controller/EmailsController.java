package com.siberhus.mailberry.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.controller.pojo.ResultResponse;
import com.siberhus.mailberry.controller.pojo.SpamResult;
import com.siberhus.mailberry.email.SpamAssassinInvoker;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.EmailService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(EmailsController.PATH)
public class EmailsController extends BaseController{
	
	public final static String PATH = "/messages/emails";
	protected final static String VIEW_PREFIX = "pages/emails";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private CampaignService campaignService;
	
	@Inject
	private EmailService emailService;
	
	@RequestMapping(value = {"/delivery/{id}"}, method = {RequestMethod.GET})
	public String showDelivery(@PathVariable("id") Long id, Model model, HttpSession session){
		
		Campaign campaign = campaignService.get(SpringSecurityUtils
				.getNotAdminId(session), id);
		if(campaign==null){
			return "redirect:"+PATH+"/";
		}
		String campaignSummary = "&nbsp;&nbsp;&nbsp;&nbsp;You've just created email campaign named ${campaignName} that uses the list ${subscriberListLink}, " +
		"which has ${subscriberCount} active subscribers. The SMTP Server that is used to send all email messages " +
		"at this time is ${mailAccountLink}. You chose to ${blacklistEnabled} blacklist." +
		"You also chose to ${clickstreamEnabled} clickstream and " +
		"${openTrackingEnabled} open tracking. Clickstream and open tracking are active on HTML (Rich text) " +
		"message only. The message email subject that you chose to use is ${mailSubject}. You'd like all " +
		"recipients know you as ${fromEmail} and they may reply to ${replyToEmail} if they want. I recommend you " +
		"to view your ${messageLink} before sending. There are/is ${attachmentCount} attachment(s) included.";
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("campaignName", "<b>"+campaign.getCampaignName()+"</b>");
		valueMap.put("subscriberListLink", "<b>"+campaign.getList().toString()+"</b>");
		Number subscriberCount = (Number)em.createQuery("select count(*) from Subscriber s where s.list=? and s.status=?")
			.setParameter(1, campaign.getList()).setParameter(2, Subscriber.Status.ACTIVE).getSingleResult();
		valueMap.put("subscriberCount", "<b>"+subscriberCount.toString()+"</b>");
		valueMap.put("mailAccountLink", "<b>"+campaign.getMailAccount().toString()+"</b>");
		if(campaign.isBlacklistEnabled()) valueMap.put("blacklistEnabled", "<b>"+"enable"+"</b>");
		else valueMap.put("blacklistEnabled", "<b>"+"disable"+"</b>");
		if(campaign.isClickstream()) valueMap.put("clickstreamEnabled", "<b>"+"enable"+"</b>");
		else valueMap.put("clickstreamEnabled", "<b>"+"disable"+"</b>");
		if(campaign.isTrackable()) valueMap.put("openTrackingEnabled", "<b>"+"enable"+"</b>");
		else valueMap.put("openTrackingEnabled", "<b>"+"disable"+"</b>");
		valueMap.put("mailSubject", "<b>"+campaign.getMailSubject()+"</b>");
		valueMap.put("fromEmail", "<b>"+campaign.getFromEmail()+"</b>");
		valueMap.put("replyToEmail", "<b>"+campaign.getReplyToEmail()+"</b>");
		valueMap.put("messageLink", "<a href='#'>message(s)</a>");
		if(campaign.getAttachments()==null || campaign.getAttachments().size()==0){
			valueMap.put("attachmentCount", "<b>0</b>");
		}else{
			valueMap.put("attachmentCount", "<b>"+String.valueOf(campaign.getAttachments().size())+"</b>");
		}
		StrSubstitutor substitutor = new StrSubstitutor(valueMap, "${", "}");
		campaignSummary = substitutor.replace(campaignSummary);
		model.addAttribute("campaignSummary", campaignSummary);
		model.addAttribute("campaign", campaign);
		model.addAttribute("subscriberCount", subscriberCount);
		Number testSubscriberCount = (Number)em.createQuery("select count(*) from Subscriber s where s.list=? and s.status=?")
		.setParameter(1, campaign.getList()).setParameter(2, Subscriber.Status.TEST).getSingleResult();
		model.addAttribute("testSubscriberCount", testSubscriberCount);
		
		return VIEW_PREFIX+"/delivery";
	}
	
	@RequestMapping(value = "/check-spam", method = {RequestMethod.POST})
	public @ResponseBody SpamResult checkSpamScore(@RequestParam String subject, 
			@RequestParam String html, @RequestParam String text){
		SpamResult spamResult = new SpamResult();
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			if(html!=null && text!=null){
				helper = new MimeMessageHelper(message, true); 
			}else{
				helper = new MimeMessageHelper(message);
			}
			helper.setSubject(subject);
			helper.setFrom("ceo@siberhus.com");
			helper.setReplyTo("ceo@siberhus.com");
			helper.setTo("developer@siberhus.com");
			if(html!=null && text!=null){
				helper.setText(text, html);
			}else if(html!=null){
				helper = new MimeMessageHelper(message);
				helper.setText(html, true);
			}else{
				helper = new MimeMessageHelper(message);
				helper.setText(text);
			}
			String spamdHost = config.getValueAsString(Config.Email.SpamChecker.HOST); 
			Integer spamdPort = config.getValue(Config.Email.SpamChecker.PORT, Integer.class);
			SpamAssassinInvoker spamChecker = new SpamAssassinInvoker(spamdHost, spamdPort);
			
			spamResult.setSpam(spamChecker.scanMail(message));
			spamResult.setScore(spamChecker.getScore());
			spamResult.setRequiredScore(spamChecker.getRequiredScore());
			spamResult.setAnalysisDetails(StringUtils.join(spamChecker.getAnalysisDetails(),'\n'));
		} catch (MessagingException e) {
			spamResult.setErrorDetail(e.getMessage());
		}
		return spamResult;
	}
	
	@RequestMapping(value = "/check-progress/{id}", method = {RequestMethod.GET})
	public @ResponseBody String checkSendingProgress(@PathVariable("id") Long campaignId,
			HttpServletRequest request){
		int progress = 0;
		try{
			progress = emailService.getProgress(SpringSecurityUtils
					.getNotAdminId(request), campaignId);
		}catch(Exception e){
			return e.getMessage();
		}
		return String.valueOf(progress); 
	}

	@RequestMapping(value = "/send-test/{id}", method = {RequestMethod.POST})
	public @ResponseBody ResultResponse sendTest(@PathVariable("id") Long campaignId, @RequestParam(required=false) String email,
			HttpServletRequest request){
		try{
			if(email!=null){
				emailService.sendTest(SpringSecurityUtils
						.getNotAdminId(request), campaignId, email);
			}else{
				emailService.sendTest(SpringSecurityUtils
						.getNotAdminId(request), campaignId);
			}
			return ResultResponse.OK;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return new ResultResponse().setErrorDetail(ExceptionUtils.getRootCauseMessage(e));
		} 
	}
	
	@RequestMapping(value = "/send-now/{id}", method = {RequestMethod.POST})
	public @ResponseBody ResultResponse sendNow(@PathVariable("id") final Long campaignId, 
			final HttpServletRequest request){
		
		final Long userId = SpringSecurityUtils.getNotAdminId(request);
		new Thread(){
			public void run(){
				try {
					emailService.sendNow(userId, campaignId);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}.start();
		return ResultResponse.OK;
	}
	
	@RequestMapping(value = "/schedule-delivery/{id}", method = {RequestMethod.POST})
	public @ResponseBody ResultResponse scheduleDelivery(@PathVariable("id") final Long campaignId, 
			@RequestParam Date date, @RequestParam int hour, @RequestParam int minute,
			HttpServletRequest request){
		Calendar datetime = Calendar.getInstance();
		datetime.setTime(date);
		datetime.set(Calendar.HOUR_OF_DAY, hour);
		datetime.set(Calendar.MINUTE, minute);
		try{
			Date time = datetime.getTime();
			if(time.getTime()-new Date().getTime()<5000){
				throw new IllegalArgumentException("time must be future");
			}
			emailService.scheduleDelivery(SpringSecurityUtils
					.getNotAdminId(request), campaignId, datetime.getTime());
			return ResultResponse.OK;
		}catch(Exception e){
			return new ResultResponse().setErrorDetail(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel-delivery/{id}", method = {RequestMethod.POST})
	public @ResponseBody ResultResponse cancelScheduledDelivery(@PathVariable("id") final Long campaignId,
			HttpServletRequest request){
		try{
			emailService.cancelDelivery(SpringSecurityUtils
					.getNotAdminId(request), campaignId);
			return ResultResponse.OK;
		}catch(Exception e){
			return new ResultResponse().setErrorDetail(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1318971985271L));
	}
}
