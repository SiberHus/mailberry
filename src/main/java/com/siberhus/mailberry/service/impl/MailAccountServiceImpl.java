package com.siberhus.mailberry.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.MailAccountDao;
import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.service.MailAccountService;

@Service
public class MailAccountServiceImpl implements MailAccountService {

	@Inject
	private MailAccountDao mailAccountDao;
	
	@Inject
	private StringEncryptor encryptor;
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public MailAccount save(MailAccount mailAccount) {
		MailAccount old = mailAccountDao.get(null, mailAccount.getId());
		if(mailAccount.isSmtpAuthen()){
			if(StringUtils.isNotBlank(mailAccount.getSmtpPassword())){
				String passwd = mailAccount.getSmtpPassword();
				passwd = encryptor.encrypt(passwd);
				mailAccount.setSmtpPassword(passwd);
			}else{
				mailAccount.setSmtpPassword(old.getSmtpPassword());
			}
		}
		if(mailAccount.isPop3Authen()){
			if(StringUtils.isNotBlank(mailAccount.getPop3Password())){
				String passwd = mailAccount.getPop3Password();
				passwd = encryptor.encrypt(passwd);
				mailAccount.setPop3Password(passwd);
			}else{
				mailAccount.setPop3Password(old.getPop3Password());
			}
		}
		mailAccount = mailAccountDao.save(mailAccount);
		return mailAccount;
	}
	
	@Override
	public MailAccount get(Long userId, Long id) {
		MailAccount mailAccount = mailAccountDao.get(userId, id);
//		String passwd = smtpProfile.getMailPassword();
//		if(passwd!=null){
//			smtpProfile.setMailPassword(encryptor.decrypt(passwd));
//		}
		return mailAccount;
	}
	
	@Override
	public List<MailAccount> getAllByStatus(Long userId, String status) {
		List<MailAccount> mailAccounts = mailAccountDao.getAllByStatus(userId, status);
		return mailAccounts;
	}
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				mailAccountDao.delete(userId, id);
			}
		}
	}
	
}
