package com.siberhus.mailberry.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.service.CampaignService;
import com.siberhus.mailberry.service.TrackingService;
import com.siberhus.mailberry.service.pojo.CampaignTrackingStats;
import com.siberhus.mailberry.service.pojo.CountingAndRateStats;
import com.siberhus.mailberry.service.pojo.GeneralizedClientInfoStats;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(AnalyticsController.PATH)
public class AnalyticsController extends BaseController {

	public final static String PATH = "/analytics";
	protected final static String VIEW_PREFIX = "pages/analytics";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ConversionService conversionService;
	
	@Inject
	private TrackingService trackingService;
	
	@Inject
	private CampaignService campaignService;
	
	@RequestMapping(value = {"/campaigns/list/data/{status}"}, method = RequestMethod.GET)
	public @ResponseBody GridData listCampaigns(@PathVariable("status") final String status, 
			GridParam gridParam, final HttpServletRequest request){
		
		GridDataBuilder<Campaign> builder = new GridDataBuilder<Campaign>(gridParam, 
			new JpaGridDataHandler<Campaign>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from Campaign e where 1=1 ");
				if("sent".equals(status)){
					query.appendQueryString("and e.status=?");
					query.addParameter(Campaign.Status.SENT);
				}
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(Campaign o) {
				return new Object[]{o.getCampaignName(), o.getList().toString(),
						o.getMailSubject(), o.getStartDate(), o.getEndDate(),
						o.getDescription(), o.getEmails(), o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				if("startDate".equals(fieldName) || "endDate".equals(fieldName)
						|| "createdAt".equals(fieldName)){
					return conversionService.convert(value, Date.class);
				}else if("emails".equals(fieldName)){
					return conversionService.convert(value, Integer.class);
				}
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value={"/campaigns/compare"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String compareCampaigns(@RequestParam(value="campaignIds",required=false) List<Long> campaignIds, 
			Model model, HttpServletRequest request){
		if(campaignIds!=null){
			List<Campaign> campaigns = new ArrayList<Campaign>();
			for(Long campaignId: campaignIds){
				Campaign campaign = campaignService.get(SpringSecurityUtils
					.getNotAdminId(request), campaignId);
				campaigns.add(campaign);
			}
			model.addAttribute("campaigns", campaigns);
		}
		return VIEW_PREFIX+"/campaigns/compare-index";
	}
	
	@RequestMapping(value={"/campaigns/compare/query"}, method=RequestMethod.POST)
	public String queryCampaignsComparison(@RequestParam("campaignIds") List<Long> campaignIds, 
			Model model, HttpServletRequest request){
		List<Campaign> campaigns = new ArrayList<Campaign>();
		List<CountingAndRateStats> statList = new ArrayList<CountingAndRateStats>();
		for(Long campaignId: campaignIds){
			Campaign campaign = campaignService.get(SpringSecurityUtils
					.getNotAdminId(request), campaignId);
			statList.add(trackingService.getCountingAndRateStats(campaignId));
			campaigns.add(campaign);
		}
		model.addAttribute("stats", statList);
		model.addAttribute("campaigns", campaigns);
		return VIEW_PREFIX+"/campaigns/compare-result";
	}
	
	@RequestMapping(value={"/campaigns/track"}, method = {RequestMethod.GET})
	public String trackCampaign(Model model){
		
		return VIEW_PREFIX+"/campaigns/tracking";
	}
	
	@RequestMapping(value={"/campaigns/track/{campaignId}"}, method = {RequestMethod.GET})
	public String trackCampaign(@PathVariable("campaignId") Long campaignId, 
			Model model, HttpServletRequest request){
		Campaign campaign = campaignService.get(SpringSecurityUtils
				.getNotAdminId(request), campaignId);
		model.addAttribute("campaign", campaign);
		model.addAttribute("campaignId", campaignId);
		return VIEW_PREFIX+"/campaigns/tracking";
	}
	
	@RequestMapping(value={"/campaigns/track/query"}, method = {RequestMethod.POST})
	public @ResponseBody CampaignTrackingStats queryCampaignsTracking(@RequestParam("campaignId") Long campaignId, Model model){
		
//		Campaign campaign = campaignService.get(campaignId);
//		CountingStats s = new CountingStats();
//		int total = RandomUtils.nextInt(100)+100;
//		s.setEmails(total);
//		s.setSuccesses(RandomUtils.nextInt(100));
//		s.setClicks(RandomUtils.nextInt(100));
//		s.setOpens(RandomUtils.nextInt(100));
//		s.setHardBounces(RandomUtils.nextInt(100));
//		s.setSoftBounces(RandomUtils.nextInt(100));
//		s.setRsvps(RandomUtils.nextInt(100));
//		s.setOptOuts(RandomUtils.nextInt(100));
//		Clickstream c1 = new Clickstream();
//		c1.setClickedUrl("http://www.siberhus.com");
//		c1.setClickCount(s.getClicks()/(RandomUtils.nextInt(3)+1));
//		campaign.getClickstreams().add(c1);
//		Clickstream c2 = new Clickstream();
//		c2.setClickedUrl("http://www.google.com");
//		c2.setClickCount(s.getClicks()-c1.getClickCount());
//		campaign.getClickstreams().add(c2);
//		CampaignTrackingStats r = new CampaignTrackingStats(s, campaign);
//		return r;
		
		return trackingService.getCampaignTrackingStats(campaignId);
	}
	
	@RequestMapping(value={"/clientinfo"}, method = {RequestMethod.GET})
	public String analizeClientInfo(Model model){
		
		return VIEW_PREFIX+"/clientinfo-index";
	}
	
	@RequestMapping(value={"/clientinfo/{campaignId}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String analizeClientInfo(@PathVariable("campaignId") Long campaignId, 
			Model model, HttpServletRequest request){
		Campaign campaign = campaignService.get(SpringSecurityUtils
				.getNotAdminId(request), campaignId);
		model.addAttribute("campaign", campaign);
		GeneralizedClientInfoStats stats = trackingService
			.getClientTrackingStats(campaignId);
		model.addAttribute("stats", stats);
		return VIEW_PREFIX+"/clientinfo-result";
	}
	
	
}
