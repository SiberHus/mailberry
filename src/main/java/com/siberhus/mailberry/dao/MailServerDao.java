package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.MailServer;

public interface MailServerDao {
	
	public MailServer save(MailServer mailServer);
	
	public MailServer get(Long userId, Long id);
	
	public List<MailServer> findAllByStatus(Long userId, String status);
	
	public void delete(Long userId, Long id);
	
}
