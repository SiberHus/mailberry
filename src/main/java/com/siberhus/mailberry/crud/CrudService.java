package com.siberhus.mailberry.crud;

import java.io.Serializable;


public interface CrudService<ENTITY> {
	
	public void save(ENTITY entity);
	
	public ENTITY update(ENTITY entity);
	
	public ENTITY get(Serializable id);
	
	public ENTITY delete(Serializable id);
	
}
