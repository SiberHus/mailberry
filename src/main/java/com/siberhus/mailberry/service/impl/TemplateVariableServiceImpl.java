package com.siberhus.mailberry.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.TemplateVariableDao;
import com.siberhus.mailberry.model.TemplateVariable;
import com.siberhus.mailberry.service.TemplateVariableService;

@Service
public class TemplateVariableServiceImpl implements TemplateVariableService {
	
	@Inject
	private TemplateVariableDao templateVariableDao;
	
	@Transactional
	@Override
	public TemplateVariable save(TemplateVariable templateVariable) {
		
		return templateVariableDao.save(templateVariable);
	}
	
	@Override
	public TemplateVariable get(Long userId, Long id) {
		return templateVariableDao.get(userId, id);
	}
	
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				templateVariableDao.delete(userId, id);
			}
		}
	}
	
}
