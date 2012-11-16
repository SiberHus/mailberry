package com.siberhus.mailberry.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.siberhus.mailberry.impexp.ExportBean;
import com.siberhus.mailberry.impexp.ExportCsvConfig;
import com.siberhus.mailberry.impexp.ExportExcelConfig;
import com.siberhus.mailberry.impexp.ExportXmlConfig;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ExportService;

@Service
public class ExportServiceImpl implements ExportService {
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void exportData(ExportBean exportBean, OutputStream outputStream) {
		
		SubscriberList list = exportBean.getList();
		Session session = (Session)em.getDelegate();//Hibernate Session
		String status = exportBean.getStatus();
		List<Integer> fieldNumbers = exportBean.getFieldNumbers();//Field number is one-base index
		List<String> allFields = list.getFieldNames();
		String fieldNames[] = new String[fieldNumbers.size()+1];
		fieldNames[0] = "email";
		String projection = "";
		
		for(int i=0;i<fieldNumbers.size();i++){
			int fieldNumber = fieldNumbers.get(i);
			projection += "field"+fieldNumber+"Value,";
			fieldNames[i+1] = allFields.get(fieldNumber-1);
		}
		projection = StringUtils.chop(projection);
		
		String queryString = "select email,"+projection
			+" from Subscriber s where s.list=:list ";
		if(status!=null){
			queryString += "and s.status=:status";
		}
		
		Query query = session.createQuery(queryString)
			.setParameter("list", list);
		if(status!=null){
			query.setParameter("status", status);
		}
		ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
		try{
			switch(exportBean.getFileType()){
			case CSV:
				exportCsv(results, exportBean.getCsv(), fieldNames, outputStream);
				break;
			case XLS:
				exportExcel(results, exportBean.getExcel(), 
					new HSSFWorkbook(), fieldNames, outputStream);
				break;
			case XLSX:
				exportExcel(results, exportBean.getExcel(), 
					new XSSFWorkbook(), fieldNames, outputStream);
				break;
			case XML:
				exportXml(results, exportBean.getXml(), fieldNames, outputStream);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	private void exportCsv(ScrollableResults results, ExportCsvConfig config, 
			String fieldNames[], OutputStream outputStream) throws IOException{
		
		Writer out = new OutputStreamWriter(outputStream, config.getCharset());
		CSVWriter writer = new CSVWriter(out, config.getSeparator(), config.getQuoteChar()
				, config.getEscapeChar(), config.getFileFormat().getLineBreak() );
		if(config.isLabeled()){
			writer.writeNext(fieldNames);
		}
		while(results.next()){
			Object objs[] = results.get();
			String vals[] = Arrays.copyOf(objs, objs.length, String[].class);
			writer.writeNext(vals);
		}
		writer.close();
	}
	
	private void exportExcel(ScrollableResults results, ExportExcelConfig config, Workbook workbook, 
			String fieldNames[], OutputStream outputStream) throws IOException{
		int itemsPerSheet = config.getItemsPerSheet();
		int rowIdx = 0;
		int sheetNum = 1;
		CellStyle labelStyle = workbook.createCellStyle();
		Font labelFont = workbook.createFont();
		labelFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		labelStyle.setFont(labelFont);
		
		Sheet sheet = workbook.createSheet("List_"+sheetNum);
		Row row = null;
		if(config.isLabeled()){
			row = sheet.createRow(rowIdx++);
			for(int i=0;i<fieldNames.length;i++){
				Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
				cell.setCellStyle(labelStyle);
				cell.setCellValue(fieldNames[i]);			
			}
		}
		int counter = 1;
		while(results.next()){
			if(counter>sheetNum*itemsPerSheet){
				sheetNum++;
				sheet = workbook.createSheet("List_"+sheetNum);
				rowIdx = 0;
				if(config.isLabeled()){
					row = sheet.createRow(rowIdx++);
					for(int i=0;i<fieldNames.length;i++){
						Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
						cell.setCellStyle(labelStyle);
						cell.setCellValue(fieldNames[i]);			
					}
				}
			}
			Object objs[] = results.get();
			row = sheet.createRow(rowIdx++);
			for(int i=0;i<objs.length;i++){
				Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
				cell.setCellValue((String)objs[i]);			
			}
			counter++;
		}
		
		
		workbook.write(outputStream);
	}
	
	private void exportXml(ScrollableResults results, ExportXmlConfig config, 
			String fieldNames[], OutputStream outputStream) throws IOException{
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(config.getCharset());
		Element root = document.addElement(config.getRootTagName());
		while(results.next()){
			Object objs[] = results.get();
			if(objs.length!=fieldNames.length){
				throw new RuntimeException("Field names' length should be equal to result values' length");
			}
			Element item = root.addElement(config.getItemTagName());
			for(int i=0;i<objs.length;i++){
				String fieldName = fieldNames[i];
				String value = (String)objs[i];
				item.addElement(fieldName).addText(value);
			}
		}
		OutputFormat format = null;
		if(config.isPrettyPrint())
			format = OutputFormat.createPrettyPrint();
		else
			format = OutputFormat.createCompactFormat();
		format.setEncoding(config.getCharset());
		format.setLineSeparator(config.getFileFormat().getLineBreak());
		XMLWriter writer = new XMLWriter(outputStream, format);
		writer.write(document);
	}
	
	
}
