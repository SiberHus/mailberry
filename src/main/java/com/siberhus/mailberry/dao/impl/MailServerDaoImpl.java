package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.MailServerDao;
import com.siberhus.mailberry.model.MailServer;

@Repository
public class MailServerDaoImpl implements MailServerDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public MailServer save(MailServer mailServer) {
		mailServer = em.merge(mailServer);
		return mailServer;
	}
	
	@Override
	public MailServer get(Long userId, Long id) {
		String jpql = "from MailServer ms where ms.id=? ";
		if(userId!=null){
			jpql += "and (ms.user.id=? or ms.publicServer=?)";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId).setParameter(3, true);
		}
		try{
			MailServer mailServer = (MailServer)query.getSingleResult();
			return mailServer;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		MailServer mailServer = get(userId, id);
		if(mailServer!=null){
			em.remove(mailServer);
		}
	}
	
	@Override
	public List<MailServer> findAllByStatus(Long userId, String status) {
		String jpql = "from MailServer ms where ms.status=? ";
		if(userId!=null){
			jpql += "and (ms.user.id=? or ms.publicServer=?)";
		}
		Query query = em.createQuery(jpql).setParameter(1, status);
		if(userId!=null){
			query.setParameter(2, userId).setParameter(3, true);
		}
		@SuppressWarnings("unchecked")
		List<MailServer> mailServers = (List<MailServer>)query.getResultList();
		return mailServers;
	}
	
}
