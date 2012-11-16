package com.siberhus.spring.context.support;

import java.util.Locale;

import org.springframework.context.support.AbstractMessageSource;

public abstract class DatabaseMessageSource extends AbstractMessageSource{

	public abstract void setMessage(String code, Locale locale, String text);
	
}
