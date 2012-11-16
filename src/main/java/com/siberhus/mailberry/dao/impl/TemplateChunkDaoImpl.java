package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.TemplateChunkDao;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.TemplateChunk;

@Repository
public class TemplateChunkDaoImpl implements TemplateChunkDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public TemplateChunk save(TemplateChunk templateChunk) {
		templateChunk = em.merge(templateChunk);
		return templateChunk;
	}

	@Override
	public TemplateChunk get(Long userId, Long id) {
		String jpql = "from TemplateChunk e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			TemplateChunk templateChunk = (TemplateChunk)query.getSingleResult();
			return templateChunk;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void delete(Long userId, Long id) {
		TemplateChunk templateChunk = get(userId, id);
		if(templateChunk!=null){
			em.remove(templateChunk);
		}
	}
	
	@Override
	public List<TemplateChunk> findAllByStatus(String status) {
		@SuppressWarnings("unchecked")
		List<TemplateChunk> templateChunks = em.createQuery("from TemplateChunk where status=?")
			.setParameter(1, status).getResultList();
		return templateChunks;
	}
	
	@Override
	public String getValueByNameAndStatus(String name, String status) {
		String value = (String)em.createQuery("select value from TemplateChunk where name=? and status=?")
		.setParameter(1, name).setParameter(2, status).getSingleResult();
		return value;
	}

	@Override
	public List<String> getNamesByStatus(String status) {
		@SuppressWarnings("unchecked")
		List<String> msgChunks = (List<String>)em.createQuery(
			"select name from TemplateChunk where status=?")
				.setParameter(1, Status.ACTIVE).getResultList();
		return msgChunks;
	}
	
}
