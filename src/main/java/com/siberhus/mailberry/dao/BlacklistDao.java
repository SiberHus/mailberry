package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.Blacklist;

public interface BlacklistDao {
	
	public Blacklist save(Blacklist blacklist);
	
	public Blacklist get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public List<Object> findAllEmails(Long userId);
	
	public Blacklist findByEmail(Long userId, String email);
	
	
}
