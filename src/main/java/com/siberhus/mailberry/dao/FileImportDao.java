package com.siberhus.mailberry.dao;

import com.siberhus.mailberry.model.FileImport;

public interface FileImportDao {

	public FileImport save(FileImport fileImport);
	
	public FileImport get(Long id);
	
	public FileImport findByTrackingId(String trackingId);
	
}
