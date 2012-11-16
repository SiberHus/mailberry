package com.siberhus.mailberry.service.impl;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.CustomPageDao;
import com.siberhus.mailberry.model.CustomPage;
import com.siberhus.mailberry.model.CustomPage.Visibility;
import com.siberhus.mailberry.service.CustomPageService;

@Service
public class CustomPageServiceImpl implements CustomPageService {

	@Inject
	private CustomPageDao customPageDao;
	
	@Transactional
	@Override
	public CustomPage save(CustomPage page) {
		
		page = customPageDao.save(page);
		return page;
	}
	
	@Override
	public CustomPage get(Long userId, Long id) {
		CustomPage page = customPageDao.get(userId, id);
		return page;
	}
	
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				customPageDao.delete(userId, id);
			}
		}
	}
	
	@Transactional
	@Override
	public String getContent(String pageName) {
		
		CustomPage page = customPageDao.findByName(pageName);
		if(page==null) return null;
		if(page.getVisibility()!=Visibility.Public){
			if(page.getVisibility()==Visibility.Secured){
				SecurityContext securityContext = SecurityContextHolder.getContext();
				Authentication auth = securityContext.getAuthentication();
				if(auth==null || !auth.isAuthenticated()){
					return null;
				}
			}
		}
		page.setVisitCount(page.getVisitCount()+1);
		customPageDao.save(page);
		
		return page.getContent();
	}
	
	
	
	
}
