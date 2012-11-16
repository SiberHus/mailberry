package com.siberhus.mailberry.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.output.CountingOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.siberhus.mailberry.impexp.ExportBean;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ExportService;
import com.siberhus.mailberry.service.SubscriberListService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(ExportsController.PATH)
public class ExportsController extends BaseController {
	
	public final static String PATH = "/data/exports";
	protected final static String VIEW_PREFIX = "pages/exports";
	
	@Inject
	private SubscriberListService listService;
	
	@Inject
	private ExportService exportService;
	
	@RequestMapping(value = {"/"} , method = {RequestMethod.GET})
	public String showExportPage(){
		return VIEW_PREFIX+"/export";
	}
	
	@RequestMapping(value = {"/"} , method = {RequestMethod.POST})
	public void exportSubscribers(@Valid ExportBean exportBean, 
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(request), 
			exportBean.getList().getId());
		exportBean.setList(list);
		String fileName = list.getListName();
		String ext = null;
		switch(exportBean.getFileType()){
		case CSV: 
			response.setContentType("text/csv");
			ext = ".csv";
			break;
		case XLS:
			response.setContentType("application/vnd.ms-excel");
			ext = ".xls";
			break;
		case XLSX:
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ext = ".xlsx";
			break;
		case XML:
			response.setContentType("text/xml");
			ext = ".xml";
			break;
		}
		
		CountingOutputStream outputStream = new CountingOutputStream(response.getOutputStream());
		exportService.exportData(exportBean, outputStream);
		response.setContentLength(outputStream.getCount());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName+ext+ "\"");
		outputStream.flush();
		outputStream.close();
		
	}
}
