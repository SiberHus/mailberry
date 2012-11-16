package com.siberhus.mailberry.controller;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;

public class BaseController implements ServletContextAware{
	
	protected ServletContext servletContext;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected ConfigurationService config;
	
	@RequestMapping(value = "/dump/params", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String dumpParams(HttpServletRequest request){
		StringBuilder dump = new StringBuilder("<html><body><b>Parameters</b><br/>");
		dump.append("<table><tr><th>Parameter Name</th><th>Parameter Value(s)</th></tr>");
		log.info("Parameters===============");
		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String paramName = paramNames.nextElement();
			String paramValues[] = request.getParameterValues(paramName);
			String valuesStr = Arrays.toString(paramValues);
			dump.append("<tr><td>").append(paramName).append("</td>");
			dump.append("<td>")
			.append(StringEscapeUtils.escapeHtml(valuesStr))
			.append("</td></tr>");
			log.info(paramName+" = "+valuesStr);
		}
		dump.append("</table></body></html>");
		return dump.toString();
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleUnexpectedError(Exception exception) {
		
		ModelAndView mav = new ModelAndView("pages/error");
		
		Throwable cause = ExceptionUtils.getRootCause(exception);
		if(cause==null){
			cause = exception;
		}
		String message = cause.getMessage()==null?
				cause.getMessage():cause.toString();
		log.error(message, exception);
		
		String stackTrace = ExceptionUtils.getFullStackTrace(exception);
		
		mav.getModelMap().addAttribute("stackTrace", stackTrace);
		
		FlashMap.setErrorMessage(message);
		
		return mav;
	}
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
		String dateFmtStr = config.getValue(Config.Format.INPUT_DATE, String.class);
		String timestampFmtStr = config.getValue(Config.Format.INPUT_TIMESTAMP, String.class);
		String timeFmtStr = config.getValue(Config.Format.INPUT_TIME, String.class);
		SimpleDateFormat dateFmt = new SimpleDateFormat(dateFmtStr);
		SimpleDateFormat timestampFmt = new SimpleDateFormat(timestampFmtStr);
		SimpleDateFormat timeFmt = new SimpleDateFormat(timeFmtStr);
		
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFmt, true));
        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(timestampFmt, true));
        binder.registerCustomEditor(Time.class, new CustomDateEditor(timeFmt, true));
    }
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
}
