package com.siberhus.mailberry.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.controller.pojo.CalendarEvent;
import com.siberhus.mailberry.controller.pojo.EventColor;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(EventsCalendarController.PATH)
public class EventsCalendarController extends BaseController{
	
	public final static String PATH = "/messages/calendar";
	protected final static String VIEW_PREFIX = "pages/calendar";
	
	@Inject
	private CampaignService campaignService;
	
	@RequestMapping(value = "/", method = {RequestMethod.GET})
	public String index(Model model){
		
		Map<String, EventColor> campaignColors = new HashMap<String, EventColor>();
		campaignColors.put("default", getCampaignEventColor(null));
		campaignColors.put("inp", getCampaignEventColor(Campaign.Status.IN_PROGRESS));
		campaignColors.put("sen", getCampaignEventColor(Campaign.Status.SENT));
		campaignColors.put("can", getCampaignEventColor(Campaign.Status.CANCELLED));
		campaignColors.put("sch", getCampaignEventColor(Campaign.Status.SCHEDULED));
		model.addAttribute("campaignColor", campaignColors);
		return VIEW_PREFIX+"/index";
	}
	
	@RequestMapping(value = "/campaigns/{source}", method = {RequestMethod.GET})
	public @ResponseBody List<CalendarEvent> getCampaignEvents(
			@PathVariable("source") String source,
			@RequestParam("start") Long startTime, 
			@RequestParam("end") Long endTime, HttpServletRequest request){
		
		//multiply time by 1000 because java expects milliseconds
		Date start = new Date(startTime*1000);
		Date end = new Date(endTime*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		log.debug("Get campaigns for calendar from {} to {}", 
				new Object[]{sdf.format(start), sdf.format(end)});
		List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		Long userId = null;
		if(!SpringSecurityUtils.isAdmin()){
			userId = SpringSecurityUtils.getUserId(request);
		}
		List<Campaign> campaigns = campaignService.getCampaignsForCalendar(userId, start, end);
		log.debug("Total campaigns: {} => {} ", new Object[]{campaigns.size(),campaigns});
		for(Campaign campaign: campaigns){
			if("period".equals(source)){
				if(campaign.getStartDate()!=null && campaign.getEndDate()!=null){
					EventColor color = getCampaignEventColor(null);
					CalendarEvent event = new CalendarEvent();
					event.setId(campaign.getId().intValue())
						.setTitle(campaign.getCampaignName())
						.setStart(campaign.getStartDate())
						.setEnd(campaign.getEndDate())
						.setAllDay(true)
						.setDescription(campaign.getDescription())
						.setStatus(campaign.getStatus())
						.setBackgroundColor(color.getBackgroundColor())
						.setBorderColor(color.getBorderColor())
						.setTextColor(color.getTextColor());
					events.add(event);
				}
			}else if("status".equals(source)){
				EventColor color = getCampaignEventColor(campaign.getStatus());
				CalendarEvent event = new CalendarEvent();
				event.setBackgroundColor(color.getBackgroundColor())
				.setBorderColor(color.getBorderColor())
				.setTextColor(color.getTextColor());
				
				event.setId(campaign.getId().intValue())
					.setTitle(campaign.getCampaignName())
					.setDescription(campaign.getDescription())
					.setStatus(campaign.getStatus());
				if(Campaign.Status.SCHEDULED.equals(campaign.getStatus())){
					event.setStartWithTime(campaign.getScheduledTime())
					.setAllDay(true);
				}else{
					event.setStartWithTime(campaign.getSendTime());
					if(campaign.getFinishTime()!=null){
						event.setEndWithTime(campaign.getFinishTime());
					}else{
						event.setAllDay(true);
					}
				}
				events.add(event);
			}
		}
		return events;
	}
	
	private EventColor getCampaignEventColor(String status){
		EventColor color = new EventColor();
		if(status==null){
			color.setBackgroundColor(config.getValueAsString(
					Config.UI.Calendar.CAMPAIGN_BACKGROUND_COLOR, "#B7F3EC"));
			color.setBorderColor(config.getValueAsString(
					Config.UI.Calendar.CAMPAIGN_BORDER_COLOR, "#84D5CC"));
			color.setTextColor(config.getValueAsString(
					Config.UI.Calendar.CAMPAIGN_TEXT_COLOR, "#2E4A47"));
		}else if(Campaign.Status.IN_PROGRESS.equals(status)){
			color.setBackgroundColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_INP_BACKGROUND_COLOR, "#ED220C"));
			color.setBorderColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_INP_BORDER_COLOR, "#C9200D"));
			color.setTextColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_INP_TEXT_COLOR, "#181818"));
		}else if(Campaign.Status.SENT.equals(status)){
			color.setBackgroundColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SEN_BACKGROUND_COLOR, "#A1F595"));
			color.setBorderColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SEN_BORDER_COLOR, "#7ED972"));
			color.setTextColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SEN_TEXT_COLOR, "#12300E"));
		}else if(Campaign.Status.CANCELLED.equals(status)){
			color.setBackgroundColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_CAN_BACKGROUND_COLOR, "#DFDFD7"));
			color.setBorderColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_CAN_BORDER_COLOR, "#B7B7B0"));
			color.setTextColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_CAN_TEXT_COLOR, "#343432"));
		}else if(Campaign.Status.SCHEDULED.equals(status)){
			color.setBackgroundColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SCH_BACKGROUND_COLOR, "#EEAFF9"));
			color.setBorderColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SCH_BORDER_COLOR, "#CE84DB"));
			color.setTextColor(config.getValueAsString(
				Config.UI.Calendar.CAMPAIGN_SCH_TEXT_COLOR, "#2C1E2E"));
		}
		return color;
	}
	
}
