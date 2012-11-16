package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.MessageTemplate;

public interface MessageTemplateDao {
	
	public MessageTemplate save(MessageTemplate messageTemplate);
	
	public MessageTemplate get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
}
