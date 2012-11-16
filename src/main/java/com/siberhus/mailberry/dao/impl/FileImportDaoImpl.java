package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.FileImportDao;
import com.siberhus.mailberry.model.FileImport;

@Repository
public class FileImportDaoImpl implements FileImportDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public FileImport save(FileImport fileImport) {
		fileImport = em.merge(fileImport);
		return fileImport;
	}

	@Override
	public FileImport get(Long id) {
		FileImport fileImport = em.find(FileImport.class, id);
		return fileImport;
	}

	@Override
	public FileImport findByTrackingId(String trackingId) {
		try{
			FileImport fileImport = (FileImport)em
			.createQuery("from FileImport fi where fi.trackingId=?")
			.setParameter(1, trackingId).getSingleResult();
			return fileImport;
		}catch(NoResultException e){
			return null;
		}
	}
	
}
