package com.siberhus.mailberry.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.siberhus.mailberry.impexp.FileDataSource;
import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.impexp.ImportBean;
import com.siberhus.mailberry.impexp.ImportTextResult;
import com.siberhus.mailberry.impexp.Progress;
import com.siberhus.mailberry.impexp.TextDataSource;
import com.siberhus.mailberry.model.FileImport;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ImportService;
import com.siberhus.mailberry.service.SubscriberListService;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(ImportsController.PATH)
public class ImportsController extends BaseController {
	
	public final static String PATH = "/data/imports";
	protected final static String VIEW_PREFIX = "pages/imports";
	
	private static final String SESSION_IMPORT_BEAN = "_importBean";
	private static final String SESSION_FILE_DS = "_fileDataSource";
	
	@Inject
	private SubscriberListService listService;
	
	@Inject
	private ImportService importService;
	
	
	@RequestMapping(value = {"/", "/start"} , method = {RequestMethod.GET})
	public String showImportStep1(Model model){
		model.addAttribute(new ImportBean());
		return VIEW_PREFIX+"/start";
	}
	
	@RequestMapping(value = {"/start"} , method = {RequestMethod.POST})
	public String processImportStep1(@Valid ImportBean importBean,  BindingResult result, 
			Model model, HttpSession session){
		if(importBean.getDataSource()==ImportBean.DataSource.manual){
			return "redirect:"+SubscribersController.PATH+"/"+importBean.getList().getId()+"/add-entry";
		}
		if(result.hasErrors()){
			model.addAttribute(importBean);
			return VIEW_PREFIX+"/start";
		}
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(session), 
			importBean.getList().getId());
		importBean.setList(list);
		session.setAttribute(SESSION_IMPORT_BEAN, importBean);
		
		switch(importBean.getDataSource()){
		case database:
			break;
		case file:
			return "redirect:"+PATH+"/file/step1";
		case saas:
			break;
		case text:
			return "redirect:"+PATH+"/text";
		}
		return VIEW_PREFIX+"/start";
	}
	
	//***************************** TEXT DS ************************************//
	
	@RequestMapping(value = {"/text"} , method = {RequestMethod.GET})
	public String showImportTextDataSource(Model model, HttpSession session){
		ImportBean importBean = (ImportBean)session.getAttribute(SESSION_IMPORT_BEAN);
		if(importBean==null){
			return "redirect:"+PATH+"/import";
		}
		model.addAttribute(importBean);
		return VIEW_PREFIX+"/ds_text";
	}
	
	@RequestMapping(value = {"/text"} , method = {RequestMethod.POST})
	public @ResponseBody ImportTextResult processImportTextDataSource(@Valid TextDataSource textDataSource, HttpSession session){
		
		ImportBean importBean = (ImportBean)session.getAttribute(SESSION_IMPORT_BEAN);
		textDataSource.setList(importBean.getList());
		
		return importService.importData(textDataSource);
	}
	
	
	//***************************** FILE DS ************************************//
	
	@RequestMapping(value = {"/file/step1"} , method = {RequestMethod.GET})
	public String showImportFileDataSource1(Model model, HttpSession session){
		ImportBean importBean = (ImportBean)session.getAttribute(SESSION_IMPORT_BEAN);
		if(importBean==null){
			return "redirect:"+PATH+"/";
		}
		FileDataSource fileDataSource = new FileDataSource();
		fileDataSource.setList(importBean.getList());
		model.addAttribute(fileDataSource);
		return VIEW_PREFIX+"/ds_file1";
	}
	
	@RequestMapping(value = {"/file/step1"} , method = {RequestMethod.POST})
	public String processImportFileDataSource1(@RequestParam("uploadFile") MultipartFile uploadFile, 
			@Valid FileDataSource fileDataSource, BindingResult result, 
			Model model, HttpSession session){
		if(uploadFile.isEmpty()){
			result.addError(new ObjectError("fileDataSource", "file is empty"));
		}
		ImportBean importBean = (ImportBean)session.getAttribute(SESSION_IMPORT_BEAN);
		if(fileDataSource==null){
			return "redirect:"+PATH+"/file/step1";
		}
		if(result.hasErrors()){
			model.addAttribute(fileDataSource);
			return VIEW_PREFIX+"/ds_file1";
		}
		FileImport fileImport = importService.storeFile(uploadFile, fileDataSource);
		fileDataSource.setFileImport(fileImport);
		fileDataSource.setList(importBean.getList());
		session.setAttribute(SESSION_FILE_DS, fileDataSource);
		
		return "redirect:"+PATH+"/file/step2";
	}
	
	@RequestMapping(value = {"/file/step2"} , method = {RequestMethod.GET})
	public String showImportFileDataSource2(Model model, HttpSession session){
		FileDataSource fileDataSource = (FileDataSource)session.getAttribute(SESSION_FILE_DS);
		if(fileDataSource==null){
			return "redirect:"+PATH+"/file/step1";
		}
		List<String[]> sampleDataList = importService.getSampleData(fileDataSource, 3);
		//find the max length
		int maxDataLength = 0;
		for(String[] data: sampleDataList){
			if(data.length>maxDataLength) maxDataLength = data.length;
		}
		
		List<String> fieldNames = fileDataSource.getList().getFieldNames();
		int listSize = fieldNames.size()+2;
		int sizeDiff = maxDataLength-listSize;
		for(int i=0;i<sizeDiff;i++){
			fieldNames.add("");
		}
		model.addAttribute("fieldNames", fieldNames);
		model.addAttribute("dataList", sampleDataList);
		model.addAttribute(fileDataSource);
		
		return VIEW_PREFIX+"/ds_file2";
	}
	
	@RequestMapping(value = {"/file/step2"} , method = {RequestMethod.POST})
	public String processImportFileDataSource2(@RequestParam("fieldNames") List<String> fieldNames, 
			Model model, HttpSession session){
		FileDataSource fileDataSource = (FileDataSource)session.getAttribute(SESSION_FILE_DS);
		if(fileDataSource==null){
			return "redirect:"+PATH+"/file/step1";
		}
		
		fileDataSource.setFieldNames(fieldNames);
		String trackingId = importService.importData(fileDataSource);
		
		return "redirect:"+PATH+"/file/track/"+trackingId;
	}
	
	@RequestMapping(value="/file/track/{id}", method={RequestMethod.GET})
	public String showFileImportTracking(@PathVariable("id") String trackingId, Model model){
		
		FileImport fileImport = importService.getFileImport(trackingId);
		if(fileImport==null){
			fileImport = new FileImport();
			fileImport.setFatalError("Fatal error");
			SubscriberList list = new SubscriberList();
			list.setListName("Sample List");
			fileImport.setList(list);
			fileImport.setOriginalName("data2011.csv");
			fileImport.setFileType(FileType.CSV);
			fileImport.setErrorFilePath("somewhere");
			fileImport.setSourceFilePath("somewhere");
		}
		model.addAttribute(fileImport);
		model.addAttribute("trackingId", trackingId);
		
		return VIEW_PREFIX+"/ds_file3";
	}
	
	@RequestMapping(value="/file/progress/{id}", method={RequestMethod.GET})
	public @ResponseBody Progress getFileImportProgress(@PathVariable("id") String trackingId){
		
		return importService.getProgress(trackingId);
		
//		Progress p = new Progress();
//		p.setCreated((int)(Math.random()*100));
//		p.setUpdated((int)(Math.random()*100));
//		p.setError((int)(Math.random()*100));
//		p.setSuccess((int)(Math.random()*100));
//		
//		return p;
	}
	
	
	@RequestMapping(value="/file/download/{id}/{type}", method={RequestMethod.GET})
	public void downloadFiles(@PathVariable("id") String trackingId,
			@PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		FileImport fileImport = importService.getFileImport(trackingId);
		if(fileImport==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, 
				"Object not found with trackingId="+trackingId);
			return;
		}
		File file = null;
		if("source".equals(type)){
			file = fileImport.getSourceFile();
		}else if("error".equals(type)){
			file = fileImport.getErrorFile();
		}else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown file type request.");
			return;
		}
		if(!file.exists()){
			response.sendError(HttpServletResponse.SC_GONE, "Requested file does not exist.");
			return;
		}
		
		ServletOutputStream outputStream = null;
		DataInputStream inputStream = null;
		try{
			int length = 0;
			outputStream = response.getOutputStream();
			response.setContentLength((int) file.length());
	//		String mimetype = context.getMimeType(filename);
	//		response.setContentType((mimetype != null) ? mimetype: "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" 
					+ fileImport.getOriginalName()+ "\"");
			
			byte[] buffer = new byte[1024];
			inputStream = new DataInputStream(new FileInputStream(file));
			while ((inputStream != null) && ((length = inputStream.read(buffer)) != -1)) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		}catch(Exception e){
			log.error(e.getMessage() ,e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}finally{
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}
}
