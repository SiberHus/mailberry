package com.siberhus.mailberry.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.TemplateChunkDao;
import com.siberhus.mailberry.model.TemplateChunk;
import com.siberhus.mailberry.service.TemplateChunkService;

@Service
public class TemplateChunkServiceImpl implements TemplateChunkService {
	
	@Inject
	private TemplateChunkDao templateChunkDao;
	
	@Transactional
	@Override
	public TemplateChunk save(TemplateChunk templateChunk) {
		
		return templateChunkDao.save(templateChunk);
	}
	
	@Override
	public TemplateChunk get(Long userId, Long id) {
		return templateChunkDao.get(userId, id);
	}

	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				templateChunkDao.delete(userId, id);
			}
		}
	}
	
}
