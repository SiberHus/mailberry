package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.BlacklistDao;
import com.siberhus.mailberry.model.Blacklist;

@Repository
public class BlacklistDaoImpl implements BlacklistDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Blacklist save(Blacklist blacklist) {
		blacklist = em.merge(blacklist);
		return blacklist;
	}
	
	@Override
	public Blacklist get(Long userId, Long id) {
		String jpql = "from Blacklist e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			Blacklist blacklist = (Blacklist)query.getSingleResult();
			return blacklist;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		Blacklist blacklist = get(userId, id);
		if(blacklist!=null){
			em.remove(blacklist);
		}
	}
	
	@Override
	public List<Object> findAllEmails(Long userId) {
		String jpql = "select bl.email from Blacklist bl ";
		if(userId!=null){
			jpql += "where bl.user.id=?";
		}
		Query query = em.createQuery(jpql);
		if(userId!=null){
			query.setParameter(1, userId);
		}
		@SuppressWarnings("unchecked")
		List<Object> emails = query.getResultList();
		return emails;
	}
	
	@Override
	public Blacklist findByEmail(Long userId, String email) {
		String jpql = "from Blacklist bl where bl.email=?";
		try{
			if(userId!=null){
				jpql += "and bl.user.id=?";
			}
			Query query = em.createQuery(jpql);
			query.setParameter(1, email);
			if(userId!=null){
				query.setParameter(2, userId);
			}
			return (Blacklist)query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
}
