package com.siberhus.mailberry.crud;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.model.base.AuditableModel;
import com.siberhus.mailberry.model.base.Model;

public abstract class BaseCrudService<ENTITY extends Model<? extends Serializable>> implements CrudService<ENTITY>{
	
	@PersistenceContext
	private EntityManager em;
	
	protected abstract Class<ENTITY> getModelClass();
	
	protected EntityManager getEntityManager(){
		return em;
	}
	
	@Transactional
	@Override
	public void save(ENTITY entity) {
		em.persist(entity);
	}
	
	@Transactional
	@Override
	public ENTITY update(ENTITY entity) {
		Model<? extends Serializable> oldEntity = get(entity.getId());
		if(entity instanceof AuditableModel){
			AuditableModel<?> newAuditableEntity = (AuditableModel<?>)entity;
			AuditableModel<?> oldAuditableEntity = (AuditableModel<?>)oldEntity;
			newAuditableEntity.setCreatedAt(oldAuditableEntity.getCreatedAt());
			newAuditableEntity.setCreatedBy(oldAuditableEntity.getCreatedBy());
		}
		return em.merge(entity);
	}
	
	@Override
	public ENTITY get(Serializable id) {
		return em.find(getModelClass(), id);
	}
	
	@Transactional
	@Override
	public ENTITY delete(Serializable id) {
		ENTITY entity = get(id);
		em.remove(entity);
		return entity;
	}
}
