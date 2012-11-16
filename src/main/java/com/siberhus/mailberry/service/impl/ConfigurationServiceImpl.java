package com.siberhus.mailberry.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.siberhus.mailberry.dao.ConfigurationDao;
import com.siberhus.mailberry.model.Configuration;
import com.siberhus.mailberry.service.ConfigurationService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Map<String, Object> CACHE = new ConcurrentHashMap<String, Object>();
	
	private final static Null NULL = new Null();
	
	private static class Null {
		@Override
		public String toString(){
			return null;
		}
	}
	
	@Inject
	private ConfigurationDao configDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String configName, Class<T> targetType) {
		Assert.notNull(configName, "configName cannot be null");
		Assert.notNull(targetType, "targetType cannot be null");
		if(CACHE.containsKey(configName)){
			return (T)CACHE.get(configName);
		}
		Configuration config = configDao.findByName(configName);
		if(config==null) return null;
		String valueStr = config.getValue();
		Class<?> type = null;
		try {
			type = Class.forName(config.getType());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class not found", e);
		}
		T value = (T)ConvertUtils.convert(valueStr, type);
		if(value!=null){
			CACHE.put(configName, value);
		}else{
			CACHE.put(configName, NULL);
		}
		return value;
	}
	
	@Override
	public <T> T getValue(String configName, Class<T> targetType, T defaultValue) {
		T value = getValue(configName, targetType);
		if(value==null){
			value = defaultValue;
		}
		return value;
	}
	
	@Override
	public String getValueAsString(String configName) {
		Assert.notNull(configName, "configName cannot be null");
		Object value = CACHE.get(configName);
		if(value!=null){
			return value.toString();
		}
		Configuration config = configDao.findByName(configName);
		if(config==null) return null;
		if(String.class.getName().equals(config.getType())){
			if(config.getValue()!=null){
				CACHE.put(configName, config.getValue());
			}else{
				CACHE.put(configName, NULL);
			}
		}
		return config.getValue();
	}
	
	@Override
	public String getValueAsString(String configName, String defaultValue) {
		String value = getValueAsString(configName);
		if(value==null){
			value = defaultValue;
		}
		return value;
	}
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void setValue(String configName, Class<?> targetType, Object value) {
		Assert.notNull(configName, "configName cannot be null");
		Assert.notNull(targetType, "targetType cannot be null");
		Configuration config = null;
		String valueStr = null;
		String typeStr = targetType.getName();
		if(value!=null){
			valueStr = ObjectUtils.toString(value);
		}
		
		config = configDao.findByName(configName);
		if(config!=null){
			config.setValue(valueStr);
			config.setType(typeStr);
			configDao.save(config);
		}else{
			config = new Configuration(configName, valueStr, typeStr);
			configDao.save(config);
		}
		if(value!=null){
			CACHE.put(configName, ConvertUtils.convert(valueStr, targetType));
		}else{
			CACHE.put(configName, NULL);
		}
	}
	
}
