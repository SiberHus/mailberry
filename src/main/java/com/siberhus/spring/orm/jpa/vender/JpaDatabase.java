package com.siberhus.spring.orm.jpa.vender;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.vendor.Database;

public class JpaDatabase implements FactoryBean<Database>{
	
	private Database database;
	
	public JpaDatabase(String name){
		database = Database.valueOf(name);
	}
	
	@Override
	public Database getObject() throws Exception {
		return database;
	}
	
	@Override
	public Class<?> getObjectType() {
		return Database.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	
}
