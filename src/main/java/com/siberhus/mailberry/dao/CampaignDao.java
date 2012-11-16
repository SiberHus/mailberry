package com.siberhus.mailberry.dao;

import java.util.Date;
import java.util.List;

import com.siberhus.mailberry.model.Campaign;

public interface CampaignDao {
	
	public Campaign save(Campaign campaign);
	
	public Campaign get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public void removeAllAttachments(Campaign campaign);
	
	public List<Campaign> getCampaignsForCalendar(Long userId, Date start, Date end);
	
	public List<Campaign> findAllByStatus(String status);
	
	public int countByCampaignName(Long userId, String campaignName);
	
}
