package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.TemplateVariable;

public interface TemplateVariableService {
	
	public TemplateVariable save(TemplateVariable templateVariable);
	
	public TemplateVariable get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
}
