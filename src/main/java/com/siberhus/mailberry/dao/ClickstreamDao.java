package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.Clickstream;

public interface ClickstreamDao {
	
	public Clickstream save(Clickstream clickstream);
	
	public Clickstream get(Long id);
	
	public Clickstream findByCampaignIdAndUrl(Long campaignId, String url);
	
}
