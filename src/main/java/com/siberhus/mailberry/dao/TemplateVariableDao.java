package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.TemplateVariable;

public interface TemplateVariableDao {
	
	public TemplateVariable save(TemplateVariable templateVariable);
	
	public TemplateVariable get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public List<TemplateVariable> findAllByStatus(String status);
	
	public List<String> getNamesByStatus(String status);
}
