package com.siberhus.spring.context.support;

import javax.persistence.EntityManager;

public abstract class JpaMessageSource extends DatabaseMessageSource {
	
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
