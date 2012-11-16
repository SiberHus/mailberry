package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.TemplateChunk;

public interface TemplateChunkService {
	
	public TemplateChunk save(TemplateChunk templateChunk);
	
	public TemplateChunk get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
}
