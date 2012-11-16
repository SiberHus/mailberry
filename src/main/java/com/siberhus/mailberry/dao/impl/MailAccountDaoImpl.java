package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.MailAccountDao;
import com.siberhus.mailberry.model.MailAccount;

@Repository
public class MailAccountDaoImpl implements MailAccountDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public MailAccount save(MailAccount mailAccount) {
		mailAccount = em.merge(mailAccount);
		return mailAccount;
	}
	
	@Override
	public MailAccount get(Long userId, Long id) {
		String jpql = "from MailAccount e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			MailAccount mailAccount = (MailAccount)query.getSingleResult();
			return mailAccount;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<MailAccount> getAllByStatus(Long userId, String status) {
		String jpql = "from MailAccount e where e.status=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, status);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		@SuppressWarnings("unchecked")
		List<MailAccount> mailAccounts = query.getResultList();
		return mailAccounts;
	}
	
	@Override
	public void delete(Long userId, Long id) {
		MailAccount mailAccount = get(userId, id);
		if(mailAccount!=null){
			em.remove(mailAccount);
		}
	}

	
	
}
