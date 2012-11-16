package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.ClickstreamDao;
import com.siberhus.mailberry.model.Clickstream;

@Repository
public class ClickstreamDaoImpl implements ClickstreamDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Clickstream save(Clickstream clickstream) {
		clickstream = em.merge(clickstream);
		return clickstream;
	}

	@Override
	public Clickstream get(Long id) {
		Clickstream clickstream = em.find(Clickstream.class, id);
		return clickstream;
	}

	@Override
	public Clickstream findByCampaignIdAndUrl(Long campaignId, String url) {
		try{
			Clickstream clickstream = (Clickstream)em
			.createQuery("from Clickstream c where c.campaign.id=? and c.clickedUrl=?")
			.setParameter(1, campaignId).setParameter(2, url).getSingleResult();
			return clickstream;
		}catch(NoResultException e){
			return null;
		}
	}
	
	
}
