package com.siberhus.mailberry.velocity;

import javax.servlet.ServletContext;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.context.ServletContextAware;

public class WebappSupportVelocityEngineFactory extends VelocityEngineFactoryBean implements ServletContextAware{
	
	private ServletContext servletContext;
	
	@Override
	protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
		velocityEngine.setApplicationAttribute("javax.servlet.ServletContext", servletContext);
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
}
