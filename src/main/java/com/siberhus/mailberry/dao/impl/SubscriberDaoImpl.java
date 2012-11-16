package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;

@Repository
public class SubscriberDaoImpl implements SubscriberDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Subscriber save(Subscriber subscriber) {
		subscriber = em.merge(subscriber);
		return subscriber;
	}
	
	@Override
	public Subscriber get(Long id) {
		Subscriber subscriber = em.find(Subscriber.class, id);
		return subscriber;
	}
	
	@Override
	public void delete(Long id) {
		Subscriber subscriber = get(id);
		if(subscriber!=null){
			em.remove(subscriber);
		}
	}
	
	@Override
	public List<Subscriber> findAllByEmail(String email) {
		@SuppressWarnings("unchecked")
		List<Subscriber> subscribers = em.createQuery("select count(*) from Subscriber s where s.email=?")
			.setParameter(1, email).getResultList();
		return subscribers;
	}
	
	@Override
	public Subscriber findByEmailFromList(SubscriberList list, String email) {
		try{
			return (Subscriber)em.createQuery("from Subscriber s where s.email=? and s.list=?")
			.setParameter(1, email).setParameter(2, list).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<Subscriber> findAllByStatusFromList(SubscriberList list, String status) {
		@SuppressWarnings("unchecked")
		List<Subscriber> subscribers = em.createQuery("from Subscriber s where s.list=? and s.status=?")
			.setParameter(1, list).setParameter(2, status).getResultList();
		return subscribers;
	}
	
	@Override
	public Number countByStatusFromList(SubscriberList list, String status) {
		Number emails = (Number)em.createQuery("select count(*) from Subscriber s where s.list=? and s.status=?")
			.setParameter(1, list)
			.setParameter(2, status).getSingleResult();
		return emails;
	}
	
	@Override
	public Number countFromList(SubscriberList list) {
		Number emails = (Number)em.createQuery("select count(*) from Subscriber s where s.list=?")
		.setParameter(1, list).getSingleResult();
		return emails;
	}

	@Override
	public List<Long> getAllIdsByStatusFromList(SubscriberList list, String status) {
		@SuppressWarnings("unchecked")
		List<Long> idList = em.createQuery(
			"select id from Subscriber s where s.list=? and s.status=?")
			.setParameter(1, list).setParameter(2, status).getResultList();
		return idList;
	}
	
	
}
