package com.siberhus.mailberry.impexp.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.siberhus.mailberry.exception.DataMismatchException;
import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.impexp.ImportExcelConfig;

public abstract class ExcelDataReader implements LabeledItemReader{
	
	private ImportExcelConfig config;
	private InputStream inputStream;
	private Workbook workbook;
	private Sheet sheet; //current sheet
	private int sheetIndex; //current sheet index zero-base
	private Iterator<Row> rowIter;
	private String labels[];
	private boolean trim = true;
	
	
	public ExcelDataReader(File file, ImportExcelConfig config) throws IOException{
		this(new FileInputStream(file), config);
	}
	
	public ExcelDataReader(InputStream inputStream, ImportExcelConfig config) throws IOException{
		this.config = config;
		if( !(inputStream instanceof BufferedInputStream) ){
			this.inputStream = new BufferedInputStream(inputStream);
		}
		FileType fileType = config.getFileType();
		
		if(fileType==FileType.XLS){
			workbook = new HSSFWorkbook(inputStream);
		}else if(fileType==FileType.XLSX){
			workbook = new XSSFWorkbook(inputStream);
		}
		sheetIndex = 0;
		sheet = workbook.getSheetAt(sheetIndex);
		rowIter = sheet.rowIterator();
		if(config.isLabeled()) {
			Row row = rowIter.next();//skip label
			labels = getStringValues(row);
		}
	}
	
	public abstract String formatData(Object source);
	
	public int getCurrentSheet(){
		return sheetIndex;
	}
	
	@Override
	public String[] getLabels(){
		return labels;
	}
	
	@Override
	public String[] readNextItem() {
		
		Row row = null;
		try{
			row = rowIter.next();
		}catch(NoSuchElementException e){
			if(config.isMultipleSheets()){
				sheetIndex++;
				if(sheetIndex>=workbook.getNumberOfSheets()){
					return null;
				}
				sheet = workbook.getSheetAt(sheetIndex);
				rowIter = sheet.rowIterator();
				if(config.isLabeled()){
					Row lblRow = rowIter.next();//skip label
					String lblValues[] = getStringValues(lblRow);
					if(ArrayUtils.isSameLength(labels, lblValues)){
						for(int i=0;i<labels.length;i++){
							if(!StringUtils.trim(lblValues[i]).equals(StringUtils.trim(labels[i]))){
								throw new DataMismatchException("Labels in each sheet must be equal.");
							}
						}
					}else{
						throw new DataMismatchException("Labels size mismatch");
					}
				}
				row = rowIter.next();
			}else{
				 return null;
			}
		}
		return getStringValues(row);
	}
	
	@Override
	public void close(){
		IOUtils.closeQuietly(inputStream);
	}
	
	private String[] getStringValues(Row row){
		String values[] = null;
		values = new String[row.getLastCellNum()];
		for(int i=0;i<row.getLastCellNum();i++){
			String value = getCellValueAsString(row.getCell(i));
			if(trim){
				values[i] = StringUtils.trimToNull(value);
			}else{
				values[i] = value;
			}
		}
		return values;
	}
	
	private String getCellValueAsString(Cell cell) {
		Object value = getCellValue(cell);
		if (value == null) {
			return null;
		} else if (value instanceof Number) {
			return ObjectUtils.toString(value);
		} else if (value instanceof Date) {
			return formatData(value);
		} else {
			return value.toString();
		}
	}
	
	private Object getCellValue(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				RichTextString rts = cell.getRichStringCellValue();
				if (rts != null) {
					return rts.getString();
				}
				return null;
			case Cell.CELL_TYPE_NUMERIC:
				String value = cell.toString();
				/*
				 * In POI we cannot know which cell is date or number because both
				 * cells have numeric type To fix this problem we need to call
				 * toString if it's number cell we can parse it but if it's date
				 * cell we cannot parse the value with number parser
				 */
				try {
					return new BigDecimal(value);
				} catch (Exception e) {
					return cell.getDateCellValue();
				}
			case Cell.CELL_TYPE_BLANK:
				return null;
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
		}
		return null;
	}
}
