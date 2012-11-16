package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.TemplateChunk;

public interface TemplateChunkDao {
	
	public TemplateChunk save(TemplateChunk templateChunk);
	
	public TemplateChunk get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public String getValueByNameAndStatus(String name, String status);
	
	public List<TemplateChunk> findAllByStatus(String status);
	
	public List<String> getNamesByStatus(String status);
	
}
