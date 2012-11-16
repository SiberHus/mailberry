package com.siberhus.mailberry.service;

import java.util.List;

import com.siberhus.mailberry.model.MailServer;

public interface MailServerService {
	
	public MailServer save(MailServer mailServer);
	
	public MailServer get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
	public List<MailServer> findAllByStatus(Long userId, String status);
	
}
