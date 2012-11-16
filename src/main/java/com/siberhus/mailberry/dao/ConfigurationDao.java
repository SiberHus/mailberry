package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.Configuration;

public interface ConfigurationDao {
	
	Configuration findByName(String configName);
	
	public void save(Configuration config);
	
}
