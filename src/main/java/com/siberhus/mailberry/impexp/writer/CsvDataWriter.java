package com.siberhus.mailberry.impexp.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import au.com.bytecode.opencsv.CSVWriter;

import com.siberhus.mailberry.impexp.ExportCsvConfig;

public class CsvDataWriter implements LabeledItemWriter {
	
	private CSVWriter csvWriter;
	
	public CsvDataWriter(File file, ExportCsvConfig config) throws IOException{
		this(new OutputStreamWriter(new FileOutputStream(file, false), config.getCharset()), config);
	}
	
	public CsvDataWriter(OutputStream outputStream, ExportCsvConfig config) throws IOException{
		this(new OutputStreamWriter(outputStream, config.getCharset()), config);
	}
	
	public CsvDataWriter(Writer writer, ExportCsvConfig config) throws IOException{
		if( !(writer instanceof BufferedWriter) ){
			writer = new BufferedWriter(writer);
		}
		csvWriter = new CSVWriter(writer, config.getSeparator(), config.getQuoteChar()
				, config.getEscapeChar(), config.getFileFormat().getLineBreak());
	}
	
	@Override
	public void setLabels(String[] values) {
		csvWriter.writeNext(values);
	}
	
	@Override
	public void writeNextItem(String[] values) throws IOException {
		csvWriter.writeNext(values);
	}
	
	@Override
	public void close() {
		try {
			if(csvWriter!=null) csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
}
