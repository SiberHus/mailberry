package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.SubscriberListDao;
import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.SubscriberList;

@Repository
public class SubscriberListDaoImpl implements SubscriberListDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public SubscriberList save(SubscriberList subscriberList) {
		subscriberList = em.merge(subscriberList);
		return subscriberList;
	}
	
	@Transactional
	@Override
	public SubscriberList get(Long userId, Long id) {
		
//		SubscriberList subscriberList = em.find(SubscriberList.class, id);
		String jpql = "from SubscriberList e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		
		try{
			SubscriberList subscriberList = (SubscriberList)query.getSingleResult();
			
			if(subscriberList!=null){
				subscriberList.getFieldValidators().size();//load validators 
				Number count = (Number)em.createQuery("select count(*) from Subscriber s where s.list=?")
					.setParameter(1, subscriberList).getSingleResult();
				subscriberList.setSubscriberCount(count.intValue());
			}
			return subscriberList;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		SubscriberList subscriberList = get(userId, id);
		if(subscriberList!=null){
			em.remove(subscriberList);
		}
	}
	
	@Override
	public void setFieldValidators(Long userId, Long id, List<FieldValidator> validators) {
		SubscriberList subscriberList = get(userId, id);
		em.createQuery("delete from FieldValidator fv where fv.list=?")
			.setParameter(1, subscriberList).executeUpdate();
		em.flush();
		if(validators!=null){
			for(FieldValidator validator: validators){
				validator.setList(subscriberList);
				em.persist(validator);
			}
		}
	}
	
}
