package com.siberhus.mailberry.impexp.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.mailberry.impexp.ExportExcelConfig;
import com.siberhus.mailberry.impexp.FileType;

public class ExcelDataWriter implements LabeledItemWriter {
	
	private Logger log = LoggerFactory.getLogger(ExcelDataWriter.class);
	
	private OutputStream outputStream;
	private ExportExcelConfig config;
	private Workbook workbook = null;
	private String labels[] = null;
	private int itemsPerSheet = 0; 
	private int rowIdx = 0;
	private int sheetNum = 1;
	private int counter = 1;
	private Sheet sheet = null; //current sheet
	private Row row = null; //current row
	private CellStyle labelStyle = null;
	
	public ExcelDataWriter(File file, ExportExcelConfig config) throws IOException{
		this(new FileOutputStream(file), config);
	}
	
	public ExcelDataWriter(OutputStream outputStream, ExportExcelConfig config){
		if(outputStream instanceof BufferedOutputStream){
			this.outputStream = outputStream;
		}else{
			this.outputStream = new BufferedOutputStream(outputStream);
		}
		this.config = config;
		FileType fileType = config.getFileType();
		if(fileType==FileType.XLS){
			workbook = new HSSFWorkbook();
		}else if(fileType==FileType.XLSX){
			workbook = new XSSFWorkbook();
		}
		itemsPerSheet = config.getItemsPerSheet();
		
		labelStyle = workbook.createCellStyle();
		Font labelFont = workbook.createFont();
		labelFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		labelStyle.setFont(labelFont);
		
		sheet = workbook.createSheet("List_"+sheetNum);
	}
	
	@Override
	public void setLabels(String[] values) {
		this.labels = values;
		if(config.isLabeled()){
			row = sheet.createRow(rowIdx++);
			for(int i=0;i<labels.length;i++){
				Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
				cell.setCellStyle(labelStyle);
				cell.setCellValue(labels[i]);			
			}
		}
	}
	
	@Override
	public void writeNextItem(String[] values) throws IOException {
		
		if(counter>sheetNum*itemsPerSheet){
			sheetNum++;
			sheet = workbook.createSheet("List_"+sheetNum);
			rowIdx = 0;
			if(config.isLabeled()){
				row = sheet.createRow(rowIdx++);
				for(int i=0;i<labels.length;i++){
					Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
					cell.setCellStyle(labelStyle);
					cell.setCellValue(labels[i]);			
				}
			}
		}
		row = sheet.createRow(rowIdx++);
		for(int i=0;i<values.length;i++){
			Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
			cell.setCellValue(values[i]);			
		}
		counter++;
	}

	@Override
	public void close() {
		try{
			workbook.write(outputStream);
		}catch(IOException e){
			log.error(e.getMessage() ,e);
		}finally{
			IOUtils.closeQuietly(outputStream);
		}
	}



	

}
