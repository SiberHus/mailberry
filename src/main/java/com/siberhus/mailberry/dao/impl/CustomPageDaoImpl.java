package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.CustomPageDao;
import com.siberhus.mailberry.model.CustomPage;

@Repository
public class CustomPageDaoImpl implements CustomPageDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public CustomPage save(CustomPage page) {
		page = em.merge(page);
		return page;
	}
	
	@Override
	public CustomPage get(Long userId, Long id) {
		String jpql = "from CustomPage e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			CustomPage page = (CustomPage)query.getSingleResult();
			return page;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		CustomPage page = get(userId, id);
		if(page!=null){
			em.remove(page);
		}
	}
	
	@Override
	public CustomPage findByName(String pageName) {
		CustomPage page = null;
		try{
			page = (CustomPage)em.createQuery("from CustomPage cp where cp.name=?")
			.setParameter(1, pageName).getSingleResult();
			return page;
		}catch(NoResultException e){
			return null;
		}
	}
	
	

	
	
	
}
