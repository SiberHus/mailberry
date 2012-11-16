package com.siberhus.mailberry.impexp.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.mailberry.impexp.ExportXmlConfig;

public class XmlDataWriter implements LabeledItemWriter {
	
	private final Logger log = LoggerFactory.getLogger(XmlDataWriter.class);
	
	private BufferedWriter writer = null;
	private ExportXmlConfig config;
	private Document document;
	private Element root;
	private String labels[];
	
	public XmlDataWriter(File file, ExportXmlConfig config) throws IOException{
		this(new OutputStreamWriter(new FileOutputStream(file), config.getCharset()), config);
	}
	
	public XmlDataWriter(OutputStream outputStream, ExportXmlConfig config) throws IOException{
		this(new OutputStreamWriter(outputStream, config.getCharset()), config);
	}
	
	public XmlDataWriter(Writer writer, ExportXmlConfig config){
		if( !(writer instanceof BufferedWriter) ){
			writer = new BufferedWriter(writer);
		}
		this.config = config;
		document = DocumentHelper.createDocument();
		document.setXMLEncoding(config.getCharset());
		root = document.addElement(config.getRootTagName());
	}
	
	@Override
	public void setLabels(String[] values) {
		this.labels = values;
	}
	
	@Override
	public void writeNextItem(String[] values) throws IOException {
		if(values.length!=labels.length){
			throw new RuntimeException("Field names' length should be equal to result values' length");
		}
		Element item = root.addElement(config.getItemTagName());
		for(int i=0;i<values.length;i++){
			String fieldName = labels[i];
			item.addElement(fieldName).addText(values[i]);
		}
	}
	
	@Override
	public void close() {
		try{
			OutputFormat format = null;
			if(config.isPrettyPrint())
				format = OutputFormat.createPrettyPrint();
			else
				format = OutputFormat.createCompactFormat();
			format.setEncoding(config.getCharset());
			format.setLineSeparator(config.getFileFormat().getLineBreak());
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			xmlWriter.write(document);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			IOUtils.closeQuietly(writer);
		}
	}

	
	
	
}
