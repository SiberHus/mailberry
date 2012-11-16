package com.siberhus.mailberry.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.htmlparser.jericho.Source;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.controller.pojo.CampaignStep1FormBean;
import com.siberhus.mailberry.controller.pojo.CampaignStep2FormBean;
import com.siberhus.mailberry.controller.pojo.CampaignStep3FormBean;
import com.siberhus.mailberry.email.DataVar;
import com.siberhus.mailberry.model.Attachment;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Campaign.MessageType;
import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.model.MessageTemplate;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.MailAccountService;
import com.siberhus.mailberry.service.MessageTemplateService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.HtmlUtils;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(CampaignsController.PATH)
public class CampaignsController extends BaseController{
	
	public final static String PATH = "/messages/campaigns";
	protected final static String VIEW_PREFIX = "pages/campaigns";
	
	private static final String SESSION_CAMPAIGN = "campaign";
	private static final String SESSION_CAMPAIGN_EDIT = "campaign.edit";
	private static final String SESSION_STEP1_FORM = "campaign.step1";
	private static final String SESSION_STEP2_FORM = "campaign.step2";
	private static final String SESSION_STEP3_FORM = "campaign.step3";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private CampaignService campaignService;
	
	@Inject
	private ConversionService conversionService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private MailAccountService mailAccountService;
	
	@Inject
	private MessageTemplateService mtService;
	
	
	@RequestMapping({"/", "/list"})
	public String index(Model model, HttpSession session){
		clearSessionVars(session);
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listCampaigns(final HttpServletRequest request, GridParam gridParam){
		dumpParams(request);
		GridDataBuilder<Campaign> builder = new GridDataBuilder<Campaign>(gridParam, 
			new JpaGridDataHandler<Campaign>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from Campaign e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(Campaign o) {
				String format = config.getValueAsString(Config.Format.DISPLAY_TIMESTAMP);
				String status = o.getStatus();
				if(Campaign.Status.DRAFT.equals(o.getStatus())){
					status = "Draft";
				}else if(Campaign.Status.SENT.equals(o.getStatus())){
					status = "Sent: "+DateFormatUtils.format(o.getFinishTime(), format);
				}else if(Campaign.Status.SCHEDULED.equals(o.getStatus())){
					status = "Scheduled: "+DateFormatUtils.format(o.getScheduledTime(), format);
				}else if(Campaign.Status.IN_PROGRESS.equals(o.getStatus())){
					status = "In Progress: "+DateFormatUtils.format(o.getSendTime(), format);;
				}else if(Campaign.Status.CANCELLED.equals(o.getStatus())){
					status = "Cancelled";
				}else{
					status = ":D";
				}
				return new Object[]{"", o.getCampaignName(), o.getList().toString(), 
						o.getEmails(), status, o.getStatus(), o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				if("emails".equals(fieldName)){
					return conversionService.convert(value, Integer.class);
				}else if("createdAt".equals(fieldName)){
					return conversionService.convert(value, Date.class);
				}
				return value;
			}
		});
		return builder.build(request);
	}
	
	
	@RequestMapping(value = "/step1", method = {RequestMethod.GET})
	public String showStep1(Model model, HttpSession session){
		
		model.addAttribute(getStep1FormBean(session, true));
		return VIEW_PREFIX+"/step1";
	}
	
	@RequestMapping(value = "/step1", method = {RequestMethod.POST})
	public String processStep1(@Valid CampaignStep1FormBean formBean, 
			BindingResult result, Model model, HttpSession session){
		
		if("template".equals(formBean.getCreateFrom())){
			if(formBean.getTemplateId()!=null){
				MessageTemplate mt = mtService.get(SpringSecurityUtils.getNotAdminId(session), formBean.getTemplateId());
//				MessageTemplate mt = em.find(MessageTemplate.class, formBean.getTemplateId());
				formBean.setMessageBodyText(mt.getTemplateTextData());
				formBean.setMessageBodyHtml(mt.getTemplateHtmlData());
			}else{
				result.addError(new FieldError("campaignStep1FormBean","createFrom","may not be null "));
			}
		}else if("replicate".equals(formBean.getCreateFrom())){
			if(formBean.getCampaignId()!=null){
				Campaign campaign = campaignService.get(SpringSecurityUtils.getNotAdminId(session)
						, formBean.getCampaignId());
				session.setAttribute(SESSION_CAMPAIGN, campaign);
				if(!isEditMode(session)){
					campaign.setId(null);
					campaign.setCampaignName(campaign.getCampaignName()+"_[REPLICATED]");
					createFormBeansFromExistingCampaign(session);
				}
			}else{
				result.addError(new FieldError("campaignStep1FormBean","createFrom","may not be null "));
			}
		}
		if(result.hasErrors()){
			model.addAttribute(formBean);
			
			return VIEW_PREFIX+"/step1";
		}
		session.setAttribute(SESSION_STEP1_FORM, formBean);
		
		return "redirect:"+PATH+"/step2";
	}
	
	@RequestMapping(value = "/step2", method = {RequestMethod.GET})
	public String showStep2(Model model, HttpSession session){
		
		CampaignStep1FormBean step1FormBean = getStep1FormBean(session, false);
		if(step1FormBean==null){
			return "redirect:"+PATH+"/step1";
		}
		CampaignStep2FormBean step2FormBean = getStep2FormBean(session, true);
		model.addAttribute(step2FormBean);
		List<MailAccount> mailAccounts = mailAccountService.getAllByStatus(
				SpringSecurityUtils.getNotAdminId(session), Status.ACTIVE);
		model.addAttribute("mailAccounts", mailAccounts);
		
		return VIEW_PREFIX+"/step2";
	}
	
	@RequestMapping(value = "/step2", method = {RequestMethod.POST})
	public String processStep2(@Valid CampaignStep2FormBean formBean, 
			BindingResult result, Model model, HttpSession session){
		
		if(!isEditMode(session)){
			String campaignName = formBean.getCampaignName();
			if(campaignName!=null){
				int count = campaignService.countByCampaignName(
					SpringSecurityUtils.getNotAdminId(session), campaignName);
				if(count!=0){
					result.addError(new FieldError("campaignStep2FormBean", 
						"campaignName", "Campaign name is already in use."));
				}
			}
		}
		if(result.hasErrors()){
			model.addAttribute(formBean);
			List<MailAccount> mailAccounts = mailAccountService.getAllByStatus(
					SpringSecurityUtils.getNotAdminId(session), Status.ACTIVE);
			model.addAttribute("mailAccounts", mailAccounts);
			return VIEW_PREFIX+"/step2";
		}
		session.setAttribute(SESSION_STEP2_FORM, formBean);
		
		return "redirect:"+PATH+"/step3";
	}
	
	@RequestMapping(value = "/step3", method = {RequestMethod.GET})
	public String showStep3(Model model, HttpSession session){
		
		CampaignStep1FormBean step1FormBean = getStep1FormBean(session, false);
		if(step1FormBean==null){
			return "redirect:"+PATH+"/step1";
		}
		CampaignStep2FormBean step2FormBean = getStep2FormBean(session, false);
		if(step2FormBean==null){
			return "redirect:"+PATH+"/step2";
		}
		
		
		setupStep3Model(model, session);
		
		CampaignStep3FormBean formBean = getStep3FormBean(session, false);
		if(formBean==null){
			formBean = new CampaignStep3FormBean();
			formBean.setMessageBodyText(step1FormBean.getMessageBodyText());
			formBean.setMessageBodyHtml(step1FormBean.getMessageBodyHtml());
			formBean.setFromEmail(config.getValueAsString(Config.Email.DEFAULT_FROM_EMAIL));
			formBean.setFromName(config.getValueAsString(Config.Email.DEFAULT_FROM_NAME));
			formBean.setReplyToEmail(config.getValueAsString(Config.Email.DEFAULT_REPLY_TO_EMAIL));
		}
		model.addAttribute(formBean);
		return VIEW_PREFIX+"/step3";
	}
	
	@RequestMapping(value = "/step3", method = {RequestMethod.POST})
	public String processStep3(@Valid CampaignStep3FormBean formBean, 
			BindingResult result, Model model, HttpSession session){
		
//		List<Attachment> attachments = formBean.getAttachments();
//		if(attachments!=null){
//			List<Integer> empties = new ArrayList<Integer>(); 
//			for(int i=0;i<attachments.size();i++){
//				Attachment att = attachments.get(i);
//				if(StringUtils.isBlank(att.getFileName())
//					&& StringUtils.isBlank(att.getFilePath())){
//					empties.add(i);
//				}
//			}
//			for(Integer idx: empties){
//				attachments.remove(idx.intValue());
//			}
//		}
		CampaignStep1FormBean step1FormBean = getStep1FormBean(session, false);
		CampaignStep2FormBean step2FormBean = getStep2FormBean(session, false);
		
		if(step1FormBean.getMessageType()==MessageType.MIX
				||step1FormBean.getMessageType()==MessageType.HTML){
			String html = formBean.getMessageBodyHtml();
			boolean htmlEmpty = false;
			if(html!=null){
				html = html.replaceAll("\\s","");
				if(html.equals("<html><head><title></title></head><body></body></html>")){
					htmlEmpty = true;
				}
			}else{ htmlEmpty = true; }
			if(htmlEmpty){
				result.addError(new FieldError("campaignStep3FormBean", "messageBodyHtml", 
						formBean.getMessageBodyHtml(), false, new String[]{"field.required"}, 
						new Object[]{formBean.getMessageBodyHtml()}, "may not be null"));
			}else{
				if(formBean.getMessageBodyText()==null &&
						step1FormBean.getMessageType()==MessageType.MIX){
					Source htmlSrc = null;
					try {
						htmlSrc = new Source(new StringReader(formBean.getMessageBodyHtml()));
						String renderedText = htmlSrc.getRenderer().toString();
						formBean.setMessageBodyText(renderedText);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
		if(step1FormBean.getMessageType()==MessageType.MIX
				||step1FormBean.getMessageType()==MessageType.TEXT){
			if(formBean.getMessageBodyText()==null){
				result.addError(new FieldError("campaignStep3FormBean", "messageBodyText", 
						formBean.getMessageBodyText(), false, new String[]{"field.required"}, 
						new Object[]{formBean.getMessageBodyText()}, "may not be null"));
			}
		}
		if(result.hasErrors()){
			model.addAttribute(formBean);
			setupStep3Model(model, session);
			return VIEW_PREFIX+"/step3";
		}
		Campaign campaign = null;
		if(isEditMode(session)){
			campaign = getCampaign(session, false); //edit existing
		}else{
			campaign = new Campaign();//create new
		}
		campaign.setAttachments(formBean.getAttachments());
		campaign.setList(em.find(SubscriberList.class, step2FormBean.getListId()));
		campaign.setMailAccount(em.find(MailAccount.class, step2FormBean.getMailAccountId()));
		campaign.setCampaignName(step2FormBean.getCampaignName());
		campaign.setCampaignColor(step2FormBean.getCampaignColor());
		campaign.setCampaignImage(step2FormBean.getCampaignImage());
		campaign.setDescription(step2FormBean.getDescription());
		campaign.setStartDate(step2FormBean.getStartDate());
		campaign.setEndDate(step2FormBean.getEndDate());
		campaign.setBlacklistEnabled(step2FormBean.isBlacklistEnabled());
		campaign.setFromEmail(formBean.getFromEmail());
		campaign.setFromName(formBean.getFromName());
		campaign.setReplyToEmail(formBean.getReplyToEmail());
		campaign.setMailSubject(formBean.getMailSubject());
		campaign.setMailPriority(formBean.getMailPriority());
		campaign.setMessageCharset(formBean.getMessageCharset());
		campaign.setMessageType(step1FormBean.getMessageType());
		campaign.setMessageBodyText(formBean.getMessageBodyText());
		campaign.setMessageBodyHtml(formBean.getMessageBodyHtml());
		campaign.setVelocity(step1FormBean.isVelocity());
		//messageFile
		campaign.setAttachments(formBean.getAttachments());
		campaign.setInlineResource(formBean.isInlineResource());
		campaign.setTrackable(step2FormBean.isTrackable());
		campaign.setClickstream(step2FormBean.isClickstream());
		session.setAttribute(SESSION_STEP3_FORM, formBean);
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			campaign.setUser(user);
			campaign = campaignService.save(campaign);
			session.setAttribute(SESSION_CAMPAIGN, campaign);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		if(Campaign.Status.DRAFT.equals(formBean.getStatus())){
			return "redirect:"+PATH+"/";
		}
		clearSessionVars(session);
		return "redirect:"+EmailsController.PATH+"/delivery/"+campaign.getId();
	}
	
	private void setupStep3Model(Model model, HttpSession session){
		model.addAttribute("messageType", getStep1FormBean(session, false).getMessageType());
		final SubscriberList list = em.find(SubscriberList.class, 
				getStep2FormBean(session, false).getListId());
		List<String> fieldNames = list.getFieldNames();
		fieldNames.add(DataVar.EMAIL);//add default field names
		fieldNames.add(DataVar.OPT_OUT);
		fieldNames.add(DataVar.RSVP_ATT);
		fieldNames.add(DataVar.RSVP_MAY);
		fieldNames.add(DataVar.RSVP_DEC);
		fieldNames.add(DataVar.SERVER_URL);
		fieldNames.add(DataVar.CAMPAIGN_ID);
		fieldNames.add(DataVar.SUBSCRIBER_ID);
		fieldNames.add(DataVar.SECURITY_TOKEN);
		model.addAttribute("optOutLink", config.getValueAsString(Config.Subscriber.OptOut.LINK_NAME));
		model.addAttribute("rsvpAttLink", config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_ATT));
		model.addAttribute("rsvpMayLink", config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_MAY));
		model.addAttribute("rsvpDecLink", config.getValueAsString(Config.Subscriber.RSVP.LINK_NAME_DEC));
		
		model.addAttribute("dataVars", fieldNames);
		@SuppressWarnings("unchecked")
		List<String> msgVars = (List<String>)em.createQuery(
			"select name from TemplateVariable where status=?")
				.setParameter(1, Status.ACTIVE).getResultList();
		model.addAttribute("msgVars", msgVars);
		@SuppressWarnings("unchecked")
		List<String> msgChunks = (List<String>)em.createQuery(
			"select name from TemplateChunk where status=?")
				.setParameter(1, Status.ACTIVE).getResultList();
		model.addAttribute("msgChunks", msgChunks);
	}
	
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editCampaign(@PathVariable("id") Long id, HttpSession session){
		Campaign campaign = campaignService.get(SpringSecurityUtils.getNotAdminId(session), id);
		session.setAttribute(SESSION_CAMPAIGN, campaign);
		session.setAttribute(SESSION_CAMPAIGN_EDIT, "true");
		
		createFormBeansFromExistingCampaign(session);
		
		return "redirect:"+PATH+"/step2";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteCampaign(@PathVariable("id") Long id, HttpServletRequest request){
		campaignService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteCampaigns(@RequestParam("id") Long[] ids
			,HttpServletRequest request){
		campaignService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewCampaign(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		Campaign campaign = campaignService.get(SpringSecurityUtils.getNotAdminId(request), id);
		String textContent = campaign.getMessageBodyText();
		if(textContent!=null){
			textContent = HtmlUtils.htmlEscape(textContent);
			textContent = textContent.replaceAll("\\n", "<br/>");
			textContent = HtmlUtils.disableScript(textContent);
		}
		model.addAttribute(campaign);
		model.addAttribute("messageBodyText", textContent);
		return VIEW_PREFIX+"/view";
	}
	
	@RequestMapping(value = "/html/{id}", method = {RequestMethod.GET})
	public String viewCampaignHtmlContent(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		Campaign campaign = campaignService.get(SpringSecurityUtils.getNotAdminId(request), id);
		String htmlContent = campaign.getMessageBodyHtml();
		htmlContent = HtmlUtils.disableScript(htmlContent);
		model.addAttribute("content", htmlContent);
		return VIEW_PREFIX+"/html_body";
	}
	
	private static void clearSessionVars(HttpSession session){
		session.removeAttribute(SESSION_CAMPAIGN);
		session.removeAttribute(SESSION_CAMPAIGN_EDIT);
		session.removeAttribute(SESSION_STEP1_FORM);
		session.removeAttribute(SESSION_STEP2_FORM);
		session.removeAttribute(SESSION_STEP3_FORM);
	}
	
	private static boolean isEditMode(HttpSession session){
		if(session.getAttribute(SESSION_CAMPAIGN_EDIT)!=null) return true;
		return false;
	}
	
	private static void createFormBeansFromExistingCampaign(HttpSession session){
		Campaign campaign = getCampaign(session, false);
		if(campaign==null){
			return;
		}
		CampaignStep1FormBean step1 = new CampaignStep1FormBean();
		step1.setMessageType(campaign.getMessageType());
		step1.setMessageBodyHtml(campaign.getMessageBodyHtml());
		step1.setMessageBodyText(campaign.getMessageBodyText());
		step1.setCampaignId(campaign.getId());
		step1.setCampaignName(campaign.getCampaignName());
		step1.setCreateFrom("replicate");
		step1.setVelocity(campaign.isVelocity());
		session.setAttribute(SESSION_STEP1_FORM, step1);
		
		CampaignStep2FormBean step2 = new CampaignStep2FormBean();
		step2.setCampaignName(campaign.getCampaignName());
		step2.setCampaignImage(campaign.getCampaignImage());
		step2.setCampaignColor(campaign.getCampaignColor());
		step2.setListId(campaign.getList().getId());
		step2.setListName(campaign.getList().getListName());
		step2.setMailAccountId(campaign.getMailAccount().getId());
		step2.setDescription(campaign.getDescription());
		step2.setStartDate(campaign.getStartDate());
		step2.setEndDate(campaign.getEndDate());
		step2.setBlacklistEnabled(campaign.isBlacklistEnabled());
		step2.setTrackable(campaign.isTrackable());
		step2.setClickstream(campaign.isClickstream());
		session.setAttribute(SESSION_STEP2_FORM, step2);
		
		CampaignStep3FormBean step3 = new CampaignStep3FormBean();
		step3.setFromEmail(campaign.getFromEmail());
		step3.setFromName(campaign.getFromName());
		step3.setReplyToEmail(campaign.getReplyToEmail());
		step3.setMailPriority(campaign.getMailPriority());
		step3.setMessageCharset(campaign.getMessageCharset());
		step3.setMailSubject(campaign.getMailSubject());
		step3.setMessageBodyHtml(campaign.getMessageBodyHtml());
		step3.setMessageBodyText(campaign.getMessageBodyText());
		step3.setInlineResource(campaign.isInlineResource());
		if(campaign.getAttachments()!=null){
			List<Attachment> attachments = new ArrayList<Attachment>();
			for(Attachment attachment: campaign.getAttachments()){
				attachments.add(new Attachment(attachment.getFileName(), 
					attachment.getFilePath(), attachment.isCompressed(), attachment.getArchivePasswd()));
			}
			step3.setAttachments(attachments);
		}
		session.setAttribute(SESSION_STEP3_FORM, step3);
	}
	
	private static Campaign getCampaign(HttpSession session, boolean create){
		Campaign campaign = (Campaign)session.getAttribute(SESSION_CAMPAIGN);
		if(campaign==null && create){
			return new Campaign();
		}
		return campaign;
	}
	
	private static CampaignStep1FormBean getStep1FormBean(HttpSession session, boolean create){
		CampaignStep1FormBean formBean = (CampaignStep1FormBean)session.getAttribute(SESSION_STEP1_FORM);
		if(formBean==null && create){
			return new CampaignStep1FormBean();
		}
		return formBean;
	}
	
	private static CampaignStep2FormBean getStep2FormBean(HttpSession session, boolean create){
		CampaignStep2FormBean formBean = (CampaignStep2FormBean)session.getAttribute(SESSION_STEP2_FORM);
		if(formBean==null && create){
			return new CampaignStep2FormBean();
		}
		return formBean;
	}
	
	private static CampaignStep3FormBean getStep3FormBean(HttpSession session, boolean create){
		CampaignStep3FormBean formBean = (CampaignStep3FormBean)session.getAttribute(SESSION_STEP3_FORM);
		if(formBean==null && create){
			return new CampaignStep3FormBean();
		}
		return formBean;
	}
	
	
	
	public static void main(String[] args)throws Exception {
		String a = "<html><head>\n<script>alert('hi');</script></head><body></body></html>";
		a = HtmlUtils.disableScript(a);
		System.out.println(a);
	}
	
}
