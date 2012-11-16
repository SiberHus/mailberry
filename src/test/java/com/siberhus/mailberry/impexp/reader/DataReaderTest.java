package com.siberhus.mailberry.impexp.reader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.ObjectUtils;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;

import com.siberhus.mailberry.impexp.FileFormat;
import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.impexp.ImportCsvConfig;
import com.siberhus.mailberry.impexp.ImportExcelConfig;
import com.siberhus.mailberry.impexp.ImportXmlConfig;
import com.siberhus.mailberry.util.StringArrayUtils;

public class DataReaderTest {
	
	private void testDataSet(ItemReader reader) throws IOException{
		Assert.assertArrayEquals(new String[]{"donald@test.com","Donald","Duck","ACT"}, reader.readNextItem());
		Assert.assertArrayEquals(new String[]{"mickey@test.com","Mickey","Mouse","ACT"}, reader.readNextItem());
		Assert.assertArrayEquals(new String[]{"badger@test.com","Badger",null,"INA"}, reader.readNextItem());
		Assert.assertArrayEquals(new String[]{"minnie@test.com","Minnie","Mouse","ACT"}, reader.readNextItem());
		Assert.assertArrayEquals(new String[]{"pluto@test.com","Pluto","Dog","ACT","Bloodhound"}, reader.readNextItem());
	}
	
	@Test
	public void testCsvFileReading() throws IOException{
		
		File file = new File("src/test/resources/subscribers.csv");
		ImportCsvConfig config = new ImportCsvConfig();
		config.setLabeled(true);
		config.setCharset("UTF-8");
		config.setEscapeChar('\\');
		config.setQuoteChar('"');
		config.setSeparator('|');
		config.setFileFormat(FileFormat.WIN);
		
		CsvDataReader reader = new CsvDataReader(file, config);
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","status"}, reader.getLabels());
		testDataSet(reader);
		Assert.assertTrue(StringArrayUtils.isBlank(reader.readNextItem()));//Blank line
		Assert.assertTrue(StringArrayUtils.isBlank(reader.readNextItem()));//Blank line
		//END FILE
		Assert.assertNull(reader.readNextItem());
		
		reader.close();
	}
	
	@Test
	public void testExcelFileReading() throws IOException {
		File file = new File("src/test/resources/subscribers.xlsx");
		ImportExcelConfig config = new ImportExcelConfig();
		config.setFileType(FileType.XLSX);
		config.setLabeled(true);
		config.setMultipleSheets(false);
		
		ExcelDataReader reader = new ExcelDataReader(file, config) {
			@Override
			public String formatData(Object source) {
				return ObjectUtils.toString(source);
			}
		};
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","status"}, reader.getLabels());
		testDataSet(reader);
		
		file = new File("src/test/resources/subscribers.xls");
		config.setFileType(FileType.XLS);
		reader = new ExcelDataReader(file, config) {
			@Override
			public String formatData(Object source) {
				return ObjectUtils.toString(source);
			}
		};
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","status"}, reader.getLabels());
		testDataSet(reader);
		
		//Test multisheet
		file = new File("src/test/resources/subscribers.xls");
		config.setFileType(FileType.XLS);
		config.setMultipleSheets(true);
		reader = new ExcelDataReader(file, config) {
			@Override
			public String formatData(Object source) {
				return ObjectUtils.toString(source);
			}
		};
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","status"}, reader.getLabels());
		testDataSet(reader);
		//Check until start new sheet
		while(true){
			reader.readNextItem();
			if(reader.getCurrentSheet()==1){
				Assert.assertArrayEquals(new String[]{"mickey@test.com","Mickey","Mouse","ACT"}, reader.readNextItem());
				Assert.assertArrayEquals(new String[]{"badger@test.com","Badger",null,"INA"}, reader.readNextItem());
				Assert.assertArrayEquals(new String[]{"minnie@test.com","Minnie","Mouse","ACT"}, reader.readNextItem());
				Assert.assertArrayEquals(new String[]{"pluto@test.com","Pluto","Dog","ACT","Bloodhound"}, reader.readNextItem());
				break;
			}
		}
		
	}
	
	@Test
	public void testXmlFileReading() throws DocumentException, IOException{
		
		File file = new File("src/test/resources/subscribers.xml");
		ImportXmlConfig config = new ImportXmlConfig();
		config.setCharset("UTF-8");
		config.setFileFormat(FileFormat.WIN);
		config.setRootTagName("list");
		config.setItemTagName("subscriber");
		
		XmlDataReader reader = new XmlDataReader(file, config);
		
		Assert.assertNull(reader.getLabels());
		
		String values[] = reader.readNextItem();
		Assert.assertArrayEquals(new String[]{"donald@test.com","Donald","Duck","ACT"}, values);
		Assert.assertNotNull(reader.getLabels());
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","status"}, reader.getLabels());
		
		Assert.assertEquals("mickey@test.com", reader.readNextItem()[0]);
		Assert.assertEquals("badger@test.com", reader.readNextItem()[0]);
		Assert.assertArrayEquals(new String[]{"email","firstName","status"}, reader.getLabels());
		
		Assert.assertEquals("minnie@test.com", reader.readNextItem()[0]);
		Assert.assertEquals("pluto@test.com", reader.readNextItem()[0]);
		Assert.assertArrayEquals(new String[]{"email","firstName","lastName","breed","status"}, reader.getLabels());
		//END FILE
		Assert.assertNull(reader.readNextItem());
		
		reader.close();
	}
	
	
}
