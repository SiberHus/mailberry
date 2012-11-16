package com.siberhus.mailberry.impexp;

import au.com.bytecode.opencsv.CSVWriter;




public class ExportCsvConfig {
	
	private boolean labeled = true;
	private String charset = "UTF-8";
	private char separator = CSVWriter.DEFAULT_SEPARATOR;
	private char quoteChar = CSVWriter.DEFAULT_QUOTE_CHARACTER;
	private char escapeChar = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
	private FileFormat fileFormat = FileFormat.WIN;
	
	public boolean isLabeled() {
		return labeled;
	}
	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public char getSeparator() {
		return separator;
	}
	public void setSeparator(char separator) {
		this.separator = separator;
	}
	public char getQuoteChar() {
		return quoteChar;
	}
	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}
	public char getEscapeChar() {
		return escapeChar;
	}
	public void setEscapeChar(char escapeChar) {
		this.escapeChar = escapeChar;
	}
	public FileFormat getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	
}
