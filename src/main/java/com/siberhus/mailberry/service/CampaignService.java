package com.siberhus.mailberry.service;

import java.util.Date;
import java.util.List;

import com.siberhus.mailberry.model.Campaign;



public interface CampaignService {
	
	public static final String FILE_PREFIX_TEXT = "text_";
	public static final String FILE_PREFIX_HTML = "html_";
	
	public Campaign save(Campaign campaign);
	
	public Campaign get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
	public List<Campaign> getCampaignsForCalendar(Long userId, Date start, Date end);
	
	public List<Campaign> findAllByStatus(String status);
	
	public int countByCampaignName(Long userId, String campaignName);
	
}
