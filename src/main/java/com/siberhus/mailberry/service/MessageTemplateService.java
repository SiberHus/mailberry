package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.MessageTemplate;

public interface MessageTemplateService {
	
	public MessageTemplate save(MessageTemplate messageTemplate);
	
	public MessageTemplate get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
}
