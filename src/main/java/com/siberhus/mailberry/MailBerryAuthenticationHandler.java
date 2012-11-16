package com.siberhus.mailberry;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

public class MailBerryAuthenticationHandler implements AuthenticationSuccessHandler{

	public static final String USER_SESSION = "_USER";
	public static final String USER_ID_SESSION = "_USER_ID";
	
	@Inject
	private UserService userService;
	
	private String targetUrl;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authenication) throws IOException,
			ServletException {
//		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//        if (roles.contains("ROLE_USER") {
//            response.sendRedirect("/userpage");
//        }
		
		User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
		request.getSession().setAttribute(USER_SESSION, user);
		request.getSession().setAttribute(USER_ID_SESSION, user.getId());
		String url = getTargetUrl();
		if(url.startsWith("/")){
			url = request.getContextPath()+url;
		}
		response.sendRedirect(url);
	}
	
	public String getTargetUrl() {
		return targetUrl;
	}
	
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
}
