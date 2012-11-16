package com.siberhus.mailberry.service;

import java.io.OutputStream;

import com.siberhus.mailberry.impexp.ExportBean;

public interface ExportService {
	
	public void exportData(ExportBean exportBean, OutputStream outputStream);
	
}
