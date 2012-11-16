package com.siberhus.mailberry.controller.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.siberhus.mailberry.MailBerryAuthenticationHandler;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(DefaultWidgetsController.PATH)
public class DefaultWidgetsController implements ServletContextAware{
	
	private ServletContext servletContext;
	
	private final Logger log = LoggerFactory.getLogger(DefaultWidgetsController.class);
	
	public final static String PATH = "/widgets/default";
	protected final static String VIEW_PREFIX = "pages/widgets/default";
	
	@PersistenceContext
	private EntityManager em;
	
	@RequestMapping({"/listCampaigns/{n}"})
	public String listCampaigns(@PathVariable("n") Integer n, Model model){
		log.debug("Getting the last {} campaigns", n);
		if(n>20){
			throw new IllegalArgumentException("May not over 20");
		}
		Object campaigns = em.createQuery("from Campaign c order by c.createdAt desc")
			.setMaxResults(n).getResultList();
		model.addAttribute("campaigns", campaigns);
		model.addAttribute("count", n);
		return VIEW_PREFIX+"/campaigns";
	}
	
	@RequestMapping({"/listMyCampaigns/{n}"})
	public String listMyCampaigns(@PathVariable("n") Integer n, Model model){
		log.debug("Getting the last {} my campaigns", n);
		if(n>20){
			throw new IllegalArgumentException("May not over 20");
		}
		Object campaigns = em.createQuery("from Campaign c where c.createdBy=? order by c.createdAt desc")
			.setParameter(1, SpringSecurityUtils.getPrincipalName())
			.setMaxResults(n).getResultList();
		model.addAttribute("campaigns", campaigns);
		model.addAttribute("count", n);
		return VIEW_PREFIX+"/campaigns";
	}
	
	//This is resource intensive operation. Cache the resource in servletContext for specified updateInterval
	@SuppressWarnings("unchecked")
	@RequestMapping({"/summary"})
	public String getSummary(@RequestParam(value="updateInterval", required=false) Long updateInterval, Model model){
		
		Date lastUpdate = (Date)servletContext.getAttribute("_WIDGET_SUMMARY");
		Map<String, Object> lastModel = (Map<String, Object>)servletContext.getAttribute("_WIDGET_SUMMARY_MODEL");
		if(lastUpdate!=null){
			long diff = new Date().getTime()-lastUpdate.getTime();
			diff = diff/6000;//convert to minute
			if(updateInterval==null) updateInterval = 10L;//minutes
			if(diff<=updateInterval && lastModel!=null){
				model.addAllAttributes(lastModel);
				return VIEW_PREFIX+"/summary";
			}
		}
		//Campaign summary
		List<Object[]> campaignStatuseResults = (List<Object[]>)em.createQuery(
			"select c.status, count(*) from Campaign c group by c.status").getResultList();
		Map<String, Object> campaignStatus = new HashMap<String, Object>();
		String campaignStatuses[] = new String[]{"DRA", "SEN", "SCH", "CAN", "INP", "PAU", "ALL"};
		for(String status: campaignStatuses){
			campaignStatus.put(status, 0);
		}
		int totalCampaigns = 0;
		for(Object[] result: campaignStatuseResults){
			if(result[0].equals(Campaign.Status.DRAFT)) campaignStatus.put("DRA", result[1]);
			else if(result[0].equals(Campaign.Status.SENT)) campaignStatus.put("SEN", result[1]);
			else if(result[0].equals(Campaign.Status.SCHEDULED)) campaignStatus.put("SCH", result[1]);
			else if(result[0].equals(Campaign.Status.CANCELLED)) campaignStatus.put("CAN", result[1]);
			else if(result[0].equals(Campaign.Status.IN_PROGRESS)) campaignStatus.put("INP", result[1]);
			else campaignStatus.put("PAU", result[1]);
			totalCampaigns += ((Number)result[1]).intValue();
		}
		campaignStatus.put("ALL", totalCampaigns);
		
		Number totalLists = (Number)em.createQuery("select count(*) from SubscriberList").getSingleResult();
		Number totalBlacklist = (Number)em.createQuery("select count(*) from Blacklist").getSingleResult();
		Number uniqueSubscribers = (Number)em.createQuery("select count(distinct email) from Subscriber").getSingleResult();
		int totalSubscribers = 0;
		List<Object[]> subscriberStatusResults = (List<Object[]>)em.createQuery(
			"select s.status, count(*) from Subscriber s group by s.status").getResultList();
		Map<String, Object> subscriberStatus = new HashMap<String, Object>();
		String subscriberStatuses[] = new String[]{"ACT", "INA", "BLO", "UNS", "TES", "ALL"};
		for(String status: subscriberStatuses){
			subscriberStatus.put(status, 0);
		}
		for(Object[] result: subscriberStatusResults){
			if(result[0].equals(Subscriber.Status.ACTIVE)) subscriberStatus.put("ACT", result[1]);
			else if(result[0].equals(Subscriber.Status.INACTIVE)) subscriberStatus.put("INA", result[1]);
			else if(result[0].equals(Subscriber.Status.BLOCKED)) subscriberStatus.put("BLO", result[1]);
			else if(result[0].equals(Subscriber.Status.UNSUBSCRIBED)) subscriberStatus.put("UNS", result[1]);
			else subscriberStatus.put("TES", result[1]);
			totalSubscribers += ((Number)result[1]).intValue();
		}
		subscriberStatus.put("ALL", totalSubscribers);
		
		Number successMails = (Number)em.createQuery("select sum(successfulMails) from MailAccount").getSingleResult();
		Number errorMails = (Number)em.createQuery("select sum(failedMails) from MailAccount").getSingleResult();
		
		model.addAttribute("campaignStatus", campaignStatus);
		model.addAttribute("totalLists", totalLists);
		model.addAttribute("totalBlacklists", totalBlacklist);
		model.addAttribute("uniqueSubscribers", uniqueSubscribers);
		model.addAttribute("subscriberStatus", subscriberStatus);
		model.addAttribute("successMails", successMails);
		model.addAttribute("errorMails", errorMails);
		
		servletContext.setAttribute("_WIDGET_SUMMARY_MODEL", model.asMap());
		servletContext.setAttribute("_WIDGET_SUMMARY", new Date());
		
		return VIEW_PREFIX+"/summary";
	}

	//This is resource intensive operation. Cache the resource in session for specified updateInterval
	@RequestMapping({"/user-info"})
	public String getUserInfo(@RequestParam(value="updateInterval", required=false) Long updateInterval, 
			Model model, HttpSession session){
		
		Date lastUpdate = (Date)session.getAttribute("_WIDGET_USER_INFO");
		@SuppressWarnings("unchecked")
		Map<String, Object> lastModel = (Map<String, Object>)session.getAttribute("_WIDGET_USER_INFO_MODEL");
		if(lastUpdate!=null){
			long diff = new Date().getTime()-lastUpdate.getTime();
			diff = diff/6000;//convert to minute
			if(updateInterval==null) updateInterval = 10L;//minutes
			if(diff<=updateInterval && lastModel!=null){
				model.addAllAttributes(lastModel);
				return VIEW_PREFIX+"/user_info";
			}
		}
		
		User user = (User)session.getAttribute(MailBerryAuthenticationHandler.USER_SESSION);
		
		Number myCampaigns = (Number)em.createQuery("select count(*) from Campaign c where c.createdBy=?")
			.setParameter(1, user.getUsername()).getSingleResult();
		Number myLists = (Number)em.createQuery("select count(*) from SubscriberList s where s.createdBy=?")
			.setParameter(1, user.getUsername()).getSingleResult();
		//unique subscribers
		Number mySubscribers = (Number)em.createQuery("select count(distinct email) from Subscriber s where s.createdBy=?")
			.setParameter(1, user.getUsername()).getSingleResult();
		Number myMailAccounts = (Number)em.createQuery("select count(*) from MailAccount m where m.createdBy=?")
			.setParameter(1, user.getUsername()).getSingleResult();
		
		model.addAttribute("user", user);
		model.addAttribute("myCampaigns", myCampaigns);
		model.addAttribute("myLists", myLists);
		model.addAttribute("mySubscribers", mySubscribers);
		model.addAttribute("myMailAccounts", myMailAccounts);
		
		session.setAttribute("_WIDGET_USER_INFO_MODEL", model.asMap());
		session.setAttribute("_WIDGET_USER_INFO", new Date());
		
		return VIEW_PREFIX+"/user_info";
	}
	
	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleUnexpectedError(Exception exception) {
		return "ERROR: "+exception.getMessage();
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
	public static void main(String[] args) throws Exception{
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		Date date = fmt.parse("03/11/2011");
		long diff = new Date().getTime()-date.getTime();
		System.out.println(diff/1000/60);
	}
}
