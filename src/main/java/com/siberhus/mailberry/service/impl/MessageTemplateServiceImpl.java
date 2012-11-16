package com.siberhus.mailberry.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.MessageTemplateDao;
import com.siberhus.mailberry.model.MessageTemplate;
import com.siberhus.mailberry.service.MessageTemplateService;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService{
	
	@Inject
	private MessageTemplateDao messageTemplateDao;
	
	@Transactional
	@Override
	public MessageTemplate save(MessageTemplate messageTemplate) {
		
		messageTemplate = messageTemplateDao.save(messageTemplate);
		return messageTemplate;
	}
	
	@Override
	public MessageTemplate get(Long userId, Long id) {
		return messageTemplateDao.get(userId, id);
	}
	
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				messageTemplateDao.delete(userId, id);
			}
		}
	}
	
}
