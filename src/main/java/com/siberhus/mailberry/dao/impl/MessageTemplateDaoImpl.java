package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.MessageTemplateDao;
import com.siberhus.mailberry.model.MessageTemplate;

@Repository
public class MessageTemplateDaoImpl implements MessageTemplateDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public MessageTemplate save(MessageTemplate messageTemplate) {
		messageTemplate = em.merge(messageTemplate);
		return messageTemplate;
	}
	
	@Override
	public MessageTemplate get(Long userId, Long id) {
		String jpql = "from MessageTemplate e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			MessageTemplate messageTemplate = (MessageTemplate)query.getSingleResult();
			return messageTemplate;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		MessageTemplate messageTemplate = get(userId, id);
		if(messageTemplate!=null){
			em.remove(messageTemplate);
		}
	}
	
	
}
