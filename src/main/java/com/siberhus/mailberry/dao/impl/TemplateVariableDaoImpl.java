package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.TemplateVariableDao;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.TemplateVariable;

@Repository
public class TemplateVariableDaoImpl implements TemplateVariableDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public TemplateVariable save(TemplateVariable templateVariable) {
		templateVariable = em.merge(templateVariable);
		return templateVariable;
	}

	@Override
	public TemplateVariable get(Long userId, Long id) {
		String jpql = "from TemplateVariable e where e.id=? ";
		if(userId!=null){
			jpql += "and e.user.id=?";
		}
		Query query = em.createQuery(jpql).setParameter(1, id);
		if(userId!=null){
			query.setParameter(2, userId);
		}
		try{
			TemplateVariable templateVariable = (TemplateVariable)query.getSingleResult();
			return templateVariable;
		}catch(NoResultException e){
			return null;
		}
	}

	@Override
	public void delete(Long userId, Long id) {
		TemplateVariable templateVariable = get(userId, id);
		if(templateVariable!=null){
			em.remove(templateVariable);
		}
	}
	
	@Override
	public List<TemplateVariable> findAllByStatus(String status) {
		@SuppressWarnings("unchecked")
		List<TemplateVariable> templateVars = em.createQuery("from TemplateVariable where status=?")
			.setParameter(1, status).getResultList();
		return templateVars;
	}
	
	@Override
	public List<String> getNamesByStatus(String status) {
		@SuppressWarnings("unchecked")
		List<String> msgVars = (List<String>)em.createQuery(
			"select name from TemplateVariable where status=?")
				.setParameter(1, Status.ACTIVE).getResultList();
		return msgVars;
	}

	
	
}
