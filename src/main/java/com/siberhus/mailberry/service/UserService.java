package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.User;

public interface UserService {
	
	public User save(User user, String... authorities);
	
	public User get(Long id);
	
	public void delete(Long... ids);
	
	public User findByUsername(String username);
	
	public void changePassword(Long id, String oldPassword, 
			String newPassword, String confirmPassword);
	
	public String generateApiKey();
	
}
