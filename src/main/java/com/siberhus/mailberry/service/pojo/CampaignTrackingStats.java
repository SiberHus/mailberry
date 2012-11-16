package com.siberhus.mailberry.service.pojo;

import java.util.ArrayList;
import java.util.List;

import com.siberhus.mailberry.dao.pojo.CountingStats;
import com.siberhus.mailberry.model.Campaign;
import com.siberhus.mailberry.model.Clickstream;

public class CampaignTrackingStats extends CountingAndRateStats {
	
	private static final long serialVersionUID = 1L;
	
	private List<ClickInfo> clickstreams;
	
	public CampaignTrackingStats(CountingStats countingStat, Campaign campaign) {
		super(countingStat);
		clickstreams = new ArrayList<ClickInfo>();
		for(Clickstream clickstream: campaign.getClickstreams()){
			ClickInfo click = new ClickInfo();
			click.setUrl(clickstream.getClickedUrl());
			click.setCount(clickstream.getClickCount());
			if(countingStat.getClicks()>0){
				click.setRate(click.getCount()*100/countingStat.getClicks());
			}
			clickstreams.add(click);
		}
	}
	
	public List<ClickInfo> getClickstreams() {
		return clickstreams;
	}

	public void setClickstreams(List<ClickInfo> clickstreams) {
		this.clickstreams = clickstreams;
	}
	
}
