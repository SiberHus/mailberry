package com.siberhus.mailberry.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassCache {

	public static final Map<String, Class<?>> CACHE = new ConcurrentHashMap<String, Class<?>>();
	
	public static Class<?> getClass(String className){
		Class<?> clazz = CACHE.get(className);
		if(clazz!=null){
			return clazz;
		}
		try{
			clazz = Class.forName(className);
			CACHE.put(className, clazz);
			return clazz;
		}catch(Exception e){
			return null;
		}
	}
	
}
