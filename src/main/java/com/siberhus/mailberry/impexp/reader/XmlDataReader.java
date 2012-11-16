package com.siberhus.mailberry.impexp.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.siberhus.mailberry.impexp.ImportXmlConfig;

public class XmlDataReader implements LabeledItemReader {

	private Iterator<Element> rootIterator;
	private String labels[];
	private boolean trim = true;
	
	
	public XmlDataReader(File file, ImportXmlConfig config) throws DocumentException, IOException{
		this(new InputStreamReader(new FileInputStream(file), config.getCharset()), config);
	}
	
	@SuppressWarnings("unchecked")
	public XmlDataReader(Reader reader, ImportXmlConfig config) throws DocumentException{
		if( !(reader instanceof BufferedReader)){
			reader = new BufferedReader(reader);
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(reader);
		Element root = doc.getRootElement();
		rootIterator = root.elementIterator();
	}
	
	@Override
	public String[] readNextItem() throws IOException {
		if(!rootIterator.hasNext()) return null;
		List<String> labelList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		Element itemElem = rootIterator.next();
		@SuppressWarnings("unchecked")
		Iterator<Element> valueElem = itemElem.elementIterator();
		while(valueElem.hasNext()){
			Element value = valueElem.next();
			labelList.add(value.getName());
			if(trim){
				valueList.add(StringUtils.trimToNull(value.getText()));
			}else{
				valueList.add(value.getText());
			}
		}
		labels = labelList.toArray(new String[0]);
		return valueList.toArray(new String[0]);
	}
	
	@Override
	public String[] getLabels() {
		return labels;
	}
	
	@Override
	public void close() {}
	
}
