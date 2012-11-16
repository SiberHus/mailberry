package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.Authority;
import com.siberhus.mailberry.model.User;

public interface UserDao {

	public User save(User user, String... authorities);
	
	public User get(Long id);
	
	public User findByUsername(String username);
	
	public void delete(Long id);
	
	public Authority getAuthority(String authority);
	
}
