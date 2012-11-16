package com.siberhus.mailberry.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.siberhus.mailberry.Roles;
import com.siberhus.mailberry.dao.UserDao;
import com.siberhus.mailberry.model.Authority;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private MessageDigestPasswordEncoder passwordEncoder;
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public User save(User user, String... authorities) {
		
		SpringSecurityUtils.checkAdminPermission(user.getUsername());
		
		User old = null;
		if(!user.isNew()){
			old = userDao.get(user.getId());
			user.setUsername(old.getUsername());//cannot change username
			user.setPassword(old.getPassword());
		}else{
			String newApiKey = generateApiKey();
			user.setApiKey(newApiKey);
			if(user.getPassword()!=null){
				//user want to change password
				if(!user.getPassword().equals(user.getPassword2())){
					throw new IllegalArgumentException("Password does not match the confirm password.");
				}
				String encPwd = passwordEncoder
					.encodePassword(user.getPassword(), user.getUsername());
				user.setPassword(encPwd);
			}else{
				throw new IllegalArgumentException("Password is required");
			}
		}
		if(SpringSecurityUtils.hasAuthority(Roles.ADMIN)){
			user = userDao.save(user, authorities);
		}else{
			user = userDao.save(user);
		}
		return user;
	}
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void changePassword(Long id, String oldPassword, String newPassword,
			String confirmPassword) {
		Assert.notNull(oldPassword, "Old password is required");
		Assert.notNull(newPassword, "New password is required");
		if(!newPassword.equals(confirmPassword)){
			throw new IllegalArgumentException("Password does not match the confirm password.");
		}
		
		User user = userDao.get(id);
		
		if(user==null){
			throw new IllegalArgumentException("User not found");
		}
		SpringSecurityUtils.checkAdminPermission(user.getUsername());
		
		String oldEncPwd = passwordEncoder
			.encodePassword(oldPassword, user.getUsername());
		if(!oldEncPwd.equals(user.getPassword())){
			throw new IllegalArgumentException("Incorrect password.");
		}
		String newEncPwd = passwordEncoder
			.encodePassword(newPassword, user.getUsername());
		user.setPassword(newEncPwd);
		userDao.save(user);
		
	}
	
	@Override
	public User get(Long id) {
		User user = userDao.get(id);
		user.setPassword(null);//don't expose this secret.
		return user;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') and !hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void delete(Long... ids) {
		
		String adminUsername = SpringSecurityUtils.getPrincipalName();
		
		if(ids!=null){
			for(Long id: ids){
				User user = userDao.get(id);
				log.info("Deleting user: {} by {}", new Object[]{user, adminUsername});
				if(!user.getUsername().equals(adminUsername)){
					//Admin cannot delete himself
					userDao.delete(id);
				}else{
					log.warn("Admin cannot delete him/herself");
				}
			}
		}
	}
	
	@Override
	public User findByUsername(String username){
		User user = userDao.findByUsername(username);
		user.setPassword(null);
		user.setPassword2(null);
		return user;
	}
	
	public static void main(String[] args) {
		List<Authority> auths = new ArrayList<Authority>();
		Authority auth = new Authority("TEST");
		auth.setId(1L);
		auths.add(auth);
//		System.out.println(auths.contains(new Authority("TEST")));
		System.out.println(new Authority("TEST").equals(auth));
	}

	@Override
	public String generateApiKey() {
		return RandomStringUtils.randomAlphanumeric(32);
	}
}
