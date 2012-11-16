package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.ConfigurationDao;
import com.siberhus.mailberry.model.Configuration;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Configuration findByName(String configName) {

		try{
			Configuration config = (Configuration)em
				.createQuery("from Configuration where name=?")
				.setParameter(1, configName).getSingleResult();
			return config;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public void save(Configuration config) {
		em.merge(config);
	}
	
}
