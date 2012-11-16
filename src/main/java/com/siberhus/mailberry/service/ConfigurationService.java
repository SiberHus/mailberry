package com.siberhus.mailberry.service;


public interface ConfigurationService {
	
	public <T>T getValue(String configName, Class<T> targetType);
	
	public <T>T getValue(String configName, Class<T> targetType, T defaultValue);
	
	public String getValueAsString(String configName);
	
	public String getValueAsString(String configName, String defaultValue);
	
	public void setValue(String configName, Class<?> targetType, Object value);
}
