package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.MailAccount;

public interface MailAccountDao {

	public MailAccount save(MailAccount mailAccount);

	public MailAccount get(Long userId, Long id);

	public List<MailAccount> getAllByStatus(Long userId, String status);
	
	public void delete(Long userId, Long id);
	
}
