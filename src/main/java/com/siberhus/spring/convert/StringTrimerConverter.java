package com.siberhus.spring.convert;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringTrimerConverter implements Converter<String, String> {
	
	@Override
	public String convert(String source) {
		return StringUtils.trimToNull(source);
	}

}
