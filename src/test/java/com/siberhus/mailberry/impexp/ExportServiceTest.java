package com.siberhus.mailberry.impexp;

import java.io.FileOutputStream;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.MailBerryTestBootstrap;
import com.siberhus.mailberry.MailBerryTestCase;
import com.siberhus.mailberry.impexp.ExportBean;
import com.siberhus.mailberry.impexp.ExportCsvConfig;
import com.siberhus.mailberry.impexp.ExportExcelConfig;
import com.siberhus.mailberry.impexp.ExportXmlConfig;
import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ConfigurationService;
import com.siberhus.mailberry.service.ExportService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/spring/dataSource-context.xml",
	"/spring/persistence-context.xml",
	"/spring/service-context.xml",
	"/spring/test-context.xml"
})
public class ExportServiceTest extends MailBerryTestCase implements InitializingBean{
	
	@Inject
	private ExportService exportService;
	
	@Inject
	private ConfigurationService configService;
	
	@Inject
	private MailBerryTestBootstrap bootstrap;
	
	private String tmpDirStr = null;
	
	private ExportBean exportBean = new ExportBean();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		tmpDirStr = configService.getValueAsString(Config.TMP_DIR);
		exportBean.setFieldNumbers(Arrays.asList(new Integer[]{1,2,3}));
		SubscriberList list = bootstrap.getDefaultSubscriberList();
		exportBean.setList(list);
	}
	
	@Test
	public void testExportCsv() throws Exception{
		exportBean.setFileType(FileType.CSV);
		exportBean.setCsv(new ExportCsvConfig());
		exportService.exportData(exportBean, new FileOutputStream(tmpDirStr+"/out.csv"));
	}
	
	@Test
	public void testExportXls() throws Exception{
		exportBean.setFileType(FileType.XLS);
		ExportExcelConfig config = new ExportExcelConfig();
		config.setItemsPerSheet(4);
		exportBean.setExcel(config);
		exportService.exportData(exportBean, new FileOutputStream(tmpDirStr+"/out.xls"));
	}
	
	@Test
	public void testExportXlsx() throws Exception{
		exportBean.setFileType(FileType.XLSX);
		ExportExcelConfig config = new ExportExcelConfig();
		config.setLabeled(false);
		config.setItemsPerSheet(500);
		exportBean.setExcel(config);
		exportService.exportData(exportBean, new FileOutputStream(tmpDirStr+"/out.xlsx"));
	}
	
	@Test
	public void testExportXml() throws Exception{
		exportBean.setFileType(FileType.XML);
		ExportXmlConfig config = new ExportXmlConfig();
		config.setRootTagName("list");
		config.setItemTagName("subscriber");
		config.setCharset("UTF-8");
		config.setPrettyPrint(true);
		exportBean.setXml(config);
		exportService.exportData(exportBean, new FileOutputStream(tmpDirStr+"/out.xml"));
	}
}











