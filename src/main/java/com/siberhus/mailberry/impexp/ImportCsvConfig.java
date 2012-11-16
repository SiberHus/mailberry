package com.siberhus.mailberry.impexp;

import au.com.bytecode.opencsv.CSVParser;

public class ImportCsvConfig extends ExportCsvConfig {
	
	public ImportCsvConfig(){
		setEscapeChar(CSVParser.DEFAULT_ESCAPE_CHARACTER);
		setQuoteChar(CSVParser.DEFAULT_QUOTE_CHARACTER);
		setSeparator(CSVParser.DEFAULT_SEPARATOR);
	}
	
}
