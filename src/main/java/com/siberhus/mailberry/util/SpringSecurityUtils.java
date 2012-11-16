package com.siberhus.mailberry.util;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.siberhus.mailberry.MailBerryAuthenticationHandler;
import com.siberhus.mailberry.Roles;
import com.siberhus.mailberry.model.User;

public class SpringSecurityUtils {
	
	public static Long getNotAdminId(HttpSession session){
		if(!SpringSecurityUtils.isAdmin()){
			return getUserId(session);
		}
		return null;
	}
	
	public static Long getNotAdminId(HttpServletRequest request){
		if(!SpringSecurityUtils.isAdmin()){
			return getUserId(request);
		}
		return null;
	}
	
	public static Long getUserId(HttpSession session){
		return getUser(session).getId();
	}
	
	public static Long getUserId(HttpServletRequest request){
		return getUser(request).getId();
	}
	
	public static User getUser(HttpSession session){
		return (User)session.getAttribute(
				MailBerryAuthenticationHandler.USER_SESSION);
	}
	
	public static User getUser(HttpServletRequest request){
		return getUser(request.getSession());
	}
	
	public static boolean hasAuthority(String role){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if(auth==null || !auth.isAuthenticated()){
			throw new AuthenticationServiceException("User not authenticated");
		}
		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		for(GrantedAuthority a: auths){
			if(role.equals(a.getAuthority())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAdmin(){
		return hasAuthority(Roles.ADMIN);
	}
	
	public static String getPrincipalName(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if(auth==null || !auth.isAuthenticated()){
			throw new AuthenticationServiceException("User not authenticated");
		}
		return auth.getName();
	}
	
	public static void checkAdminPermission(String username){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if(auth==null || !auth.isAuthenticated()){
			throw new AuthenticationServiceException("User not authenticated");
		}
		String principalName = auth.getName();
		if(!username.equals(principalName)){
			Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
			for(GrantedAuthority a: auths){
				if(Roles.ADMIN.equals(a.getAuthority())){
					return;
				}
			}
			throw new AccessDeniedException("You lack admin permission");
		}
	}
	
}
