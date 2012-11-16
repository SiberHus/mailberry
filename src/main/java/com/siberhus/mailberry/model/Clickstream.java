package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.siberhus.mailberry.model.base.AbstractModel;

@Entity
@Table(name="clickstreams", uniqueConstraints=
	@UniqueConstraint(columnNames = {"campaign_id", "clicked_url"}))
public class Clickstream extends AbstractModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="campaign_id", referencedColumnName="id", nullable=false)
	private Campaign campaign;
	
	@Column(name="clicked_url", length=256, nullable=false)
	private String clickedUrl;
	
	@Column(name="click_count")
	private Integer clickCount = 0;
	
	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public String getClickedUrl() {
		return clickedUrl;
	}

	public void setClickedUrl(String clickedUrl) {
		this.clickedUrl = clickedUrl;
	}
	
	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}
}
