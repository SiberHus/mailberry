package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.Blacklist;
import com.siberhus.mailberry.model.User;

public interface BlacklistService {
	
	public Blacklist save(Blacklist blacklist, User user);
	
	public Blacklist get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
}
