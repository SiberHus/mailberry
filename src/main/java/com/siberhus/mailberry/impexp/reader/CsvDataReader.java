package com.siberhus.mailberry.impexp.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

import com.siberhus.mailberry.impexp.ImportCsvConfig;
import com.siberhus.mailberry.util.StringArrayUtils;

public class CsvDataReader implements LabeledItemReader {
	
	private CSVReader csvReader;
	private String labels[];
	private boolean trim = true;
	
	public CsvDataReader(File file, ImportCsvConfig config) throws IOException{
		this(new InputStreamReader(new FileInputStream(file), config.getCharset()), config);
	}
	
	public CsvDataReader(Reader reader, ImportCsvConfig config) throws IOException{
		csvReader = new CSVReader(reader, config.getSeparator()
				, config.getQuoteChar(), config.getEscapeChar());
		if(config.isLabeled()){
			if(trim){
				labels = StringArrayUtils.trimToNull(csvReader.readNext());
			}else{
				labels = csvReader.readNext();
			}
			
		}
	}
	
	@Override
	public String[] getLabels() {
		return labels;
	}
	
	@Override
	public String[] readNextItem() throws IOException {
		if(trim){
			String values[] = csvReader.readNext();
			if(values!=null)
				return StringArrayUtils.trimToNull(values);
			else
				return values;
		}
		return csvReader.readNext();
	}
	
	@Override
	public void close() {
		try {
			if(csvReader!=null) csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
