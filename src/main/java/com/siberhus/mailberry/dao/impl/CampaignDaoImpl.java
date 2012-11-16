package com.siberhus.mailberry.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.CampaignDao;
import com.siberhus.mailberry.model.Campaign;

@Repository
public class CampaignDaoImpl implements CampaignDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Campaign save(Campaign campaign) {
		campaign = em.merge(campaign);
		return campaign;
	}
	
	@Transactional
	@Override
	public Campaign get(Long userId, Long id) {
		String jpql = "from Campaign e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			Campaign campaign = (Campaign)query.getSingleResult();
			if(campaign!=null){
				//load and cache lazy-init collections
				campaign.getAttachments().size();
				campaign.getClickstreams().size();
			}
			return campaign;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		Campaign campaign = get(userId, id);
		if(campaign!=null){
			em.remove(campaign);
		}
	}

	@Override
	public void removeAllAttachments(Campaign campaign) {
		em.createQuery("delete from Attachment a where a.campaign=?")
			.setParameter(1, campaign).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Campaign> getCampaignsForCalendar(Long userId, Date start, Date end) {
		String jpql = "from Campaign c where (c.startDate between :start and :end) or " +
				"(c.endDate between :start and :end) or " +
				"(c.scheduledTime between :start and :end) or " +
				"(c.sendTime between :start and :end) ";
		if(userId!=null){
			jpql += "and c.user.id=:userId";
		}
		Query query = em.createQuery(jpql).setParameter("start", start)
			.setParameter("end", end);
		if(userId!=null){
			query.setParameter("userId", userId);
		}
		List<Campaign> campaigns = query.getResultList();
		return campaigns;
	}
	
	@Override
	public List<Campaign> findAllByStatus(String status) {
		@SuppressWarnings("unchecked")
		List<Campaign> campaigns = (List<Campaign>)em.createQuery("from Campaign c where c.status=?")
			.setParameter(1, status).getResultList();
		return campaigns;
	}
	
	@Override
	public int countByCampaignName(Long userId, String campaignName) {
		String jpql = "select count(*) from Campaign c where c.campaignName=? ";
		if(userId!=null){
			jpql += "and c.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, campaignName);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		Number count = (Number)query.getSingleResult();
		return count.intValue();
	}
	
	
}
