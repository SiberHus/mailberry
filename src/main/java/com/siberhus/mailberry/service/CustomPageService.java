package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.CustomPage;

public interface CustomPageService {
	
	public CustomPage save(CustomPage page);
	
	public CustomPage get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
	public String getContent(String pageName);
	
}
