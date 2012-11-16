package com.siberhus.mailberry.service;

import java.util.List;

import com.siberhus.mailberry.model.MailAccount;

public interface MailAccountService {
	
	public MailAccount save(MailAccount mailAccount);
	
	public MailAccount get(Long userId, Long id);
	
	public List<MailAccount> getAllByStatus(Long userId, String status);
	
	public void delete(Long userId, Long... ids);
	
}
