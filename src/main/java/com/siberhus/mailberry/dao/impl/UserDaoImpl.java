package com.siberhus.mailberry.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.UserDao;
import com.siberhus.mailberry.model.Authority;
import com.siberhus.mailberry.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public User save(User user, String... authorities) {
		if(authorities !=null && authorities.length>0){
			user.getAuthorities().clear();
			for(String authName: authorities){
				try{
					Authority authority = (Authority)em
						.createQuery("from Authority auth where auth.authority=?")
						.setParameter(1, authName).getSingleResult();
					user.getAuthorities().add(authority);
				}catch(NoResultException e){}
			}
		}else if(!user.isNew()){
			user.setAuthorities(get(user.getId()).getAuthorities());
		}
		user = em.merge(user);
		return user;
	}
	
	@Override
	public User get(Long id) {
		User user = em.find(User.class, id);
		return user;
	}
	
	
	@Override
	public User findByUsername(String username) {
		User user = null;
		try{
			user = (User)em.createQuery("from User u where u.username=?")
			.setParameter(1, username).getSingleResult();
			return user;
		}catch(NoResultException e){
			return null;
		}
	}

	@Override
	public void delete(Long id) {
		User user = get(id);
		if(user!=null){
			em.remove(user);
		}
	}
	
	@Override
	public Authority getAuthority(String authority) {
		Authority auth = null;
		try{
			auth = (Authority)em.createQuery("from Authority where authority=?")
			.setParameter(1, authority).getSingleResult();
			return auth;
		}catch(NoResultException e){
			return null;
		}
	}
	
}
