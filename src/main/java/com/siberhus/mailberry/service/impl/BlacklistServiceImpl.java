package com.siberhus.mailberry.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.BlacklistDao;
import com.siberhus.mailberry.model.Blacklist;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.BlacklistService;

@Service
public class BlacklistServiceImpl implements BlacklistService {
	
	@Inject
	private BlacklistDao blacklistDao;
	
	@Transactional
	@Override
	public Blacklist save(Blacklist blacklist, User user) {
		if(blacklist.getEmails()!=null){
			BufferedReader reader = new BufferedReader(
					new StringReader(blacklist.getEmails()));
			String email = null;
			try {
				while((email=reader.readLine())!=null ){
					Blacklist bl = blacklistDao.findByEmail(user.getId(), email);
					if(bl!=null){
						continue;
					}
					bl = new Blacklist();
					bl.setUser(user);
					bl.setEmail(email);
					bl.setFullName(blacklist.getFullName());
					bl.setReason(blacklist.getReason());
					blacklistDao.save(bl);
				}
			} catch (IOException e) {}//StringReader never throw IOException}
		}else{
			blacklist = blacklistDao.save(blacklist);
		}
		return blacklist;
	}
	
	@Override
	public Blacklist get(Long userId, Long id) {
		
		return blacklistDao.get(userId, id);
	}

	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				blacklistDao.delete(userId, id);
			}
		}
	}
	
}
