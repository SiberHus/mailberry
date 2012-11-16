package com.siberhus.mailberry.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.MailServerDao;
import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.service.MailServerService;

@Service
public class MailServerServiceImpl implements MailServerService {

	@Inject
	private MailServerDao mailServerDao;
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public MailServer save(MailServer mailServer) {
		mailServer = mailServerDao.save(mailServer);
		return mailServer;
	}
	
	@Override
	public MailServer get(Long userId, Long id) {
		MailServer mailServer = mailServerDao.get(userId, id);
		return mailServer;
	}
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				mailServerDao.delete(userId, id);
			}
		}
	}

	@Override
	public List<MailServer> findAllByStatus(Long userId, String status) {
		Status.validate(status);
		List<MailServer> mailServers = mailServerDao.findAllByStatus(userId, status);
		return mailServers;
	}
	
}
