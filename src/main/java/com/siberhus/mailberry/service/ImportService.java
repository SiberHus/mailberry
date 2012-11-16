package com.siberhus.mailberry.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.siberhus.mailberry.impexp.FileDataSource;
import com.siberhus.mailberry.impexp.ImportTextResult;
import com.siberhus.mailberry.impexp.Progress;
import com.siberhus.mailberry.impexp.TextDataSource;
import com.siberhus.mailberry.model.FileImport;


public interface ImportService {

	
	public ImportTextResult importData(TextDataSource textDataSource);
	
	public Progress getProgress(String trackingId);
	
	public FileImport getFileImport(String trackingId);
	
	public FileImport storeFile(MultipartFile uploadFile, FileDataSource fileDataSource);
	
	public List<String[]> getSampleData(FileDataSource fileDataSource, int records);
	
	/**
	 * 
	 * @param fileDataSource
	 * @return trackingId (8 alphanumeric characters)
	 */
	public String importData(FileDataSource fileDataSource);
	
}
