package com.siberhus.mailberry.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ThreadLocalEntityManager {
	
	private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<EntityManager>();
	
	private EntityManagerFactory emf;
	private Set<EntityManager> emSet = Collections.synchronizedSet(new HashSet<EntityManager>());
	
	public ThreadLocalEntityManager(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	public EntityManager get(){
		EntityManager em = THREAD_LOCAL.get();
		if(em==null || !em.isOpen()){
			em = emf.createEntityManager();
			emSet.add(em);
			THREAD_LOCAL.set(em);
		}
		return em;
	}
	
	public void beginTransaction(){
		EntityManager em = get();
		em.getTransaction().begin();
	}
	
	public void commitTransaction(){
		EntityManager em = get();
		em.getTransaction().commit();
	}
	
	public void rollbackTransaction(){
		EntityManager em = get();
		em.getTransaction().rollback();
	}
	
	public void close(){
		for(EntityManager em: emSet){
			if(em.isOpen()){
				em.close();
			}
		}
		emSet.clear();
	}
	
}
