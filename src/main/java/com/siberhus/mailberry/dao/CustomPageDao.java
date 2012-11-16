package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.CustomPage;

public interface CustomPageDao {
	
	public CustomPage save(CustomPage page);
	
	public CustomPage get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public CustomPage findByName(String pageName);
}
