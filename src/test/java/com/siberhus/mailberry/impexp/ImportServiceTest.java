package com.siberhus.mailberry.impexp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siberhus.mailberry.MailBerryTestBootstrap;
import com.siberhus.mailberry.MailBerryTestCase;
import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.model.FileImport;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ImportService;
import com.siberhus.mailberry.service.SubscriberListService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/spring/dataSource-context.xml",
	"/spring/persistence-context.xml",
	"/spring/service-context.xml",
	"/spring/test-context.xml"
})
public class ImportServiceTest extends MailBerryTestCase implements InitializingBean{

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ImportService importService;
	
	@Inject
	private MailBerryTestBootstrap bootstrap;
	
	@Inject
	private SubscriberListService listService;
	
	@Inject
	private SubscriberDao subscriberDao;
	
	private SubscriberList list;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		list = bootstrap.getDefaultSubscriberList();
		list = listService.get(null, list.getId());
	}
	
	@Test
	public void testImportText() throws Exception{
		TextDataSource textDataSource = new TextDataSource();
		textDataSource.setList(list);
		textDataSource.setDelimiter("|");
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add("firstName");
		fieldNames.add("email");
		fieldNames.add("lastName");
		fieldNames.add("status");
		textDataSource.setFieldNames(fieldNames);
		StringBuilder data = new StringBuilder();
		data.append("mickey|mickey@disney.com|mouse|ACT\n");
		data.append("donald||duck|ACT\n");
		data.append("donald|donald@disney.com|duck|ACT\n");
		textDataSource.setText(data.toString());
		
		importService.importData(textDataSource);
		
		Subscriber subscriber = (Subscriber)em.createQuery(
			"from Subscriber s where s.email='mickey@disney.com' and s.list.id="+list.getId())
			.getSingleResult();
		Assert.assertEquals("mickey@disney.com", subscriber.getEmail());
		Assert.assertEquals("ACT", subscriber.getStatus());
		Assert.assertEquals("mickey", subscriber.getField1Value());
		Assert.assertEquals("mouse", subscriber.getField2Value());
		Assert.assertNull(subscriber.getField3Value());
		
		subscriber = (Subscriber)em.createQuery(
			"from Subscriber s where s.email='donald@disney.com' and s.list.id="+list.getId())
			.getSingleResult();
		Assert.assertEquals("donald@disney.com", subscriber.getEmail());
		Assert.assertEquals("ACT", subscriber.getStatus());
		Assert.assertEquals("donald", subscriber.getField1Value());
		Assert.assertEquals("duck", subscriber.getField2Value());
		Assert.assertNull(subscriber.getField3Value());
		
//		try{
//			importService.importData(list, textDataSource);//import again duplicate email error expected
//		}catch(Exception e){ System.out.println(e);}
		
	}
	
	@Test
	public void testImportFile()throws Exception {
		FileDataSource fileDataSource = new FileDataSource();
		ImportCsvConfig config = new ImportCsvConfig();
		config.setSeparator('|');
		fileDataSource.setCsv(config);
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add("email");
		fieldNames.add("field1Value");//firstName
		fieldNames.add("field2Value");//lastName
		fieldNames.add("status");
		fileDataSource.setFieldNames(fieldNames);
		
		FileImport fileImport = new FileImport();
		fileImport.setTrackingId(RandomStringUtils.randomAlphanumeric(8));
		fileImport.setList(list);
		fileImport.setOriginalName("Disney List");
		fileImport.setFileType(FileType.CSV);
		fileImport.setSourceFile(new File("src/test/resources/subscribers.csv"));
		fileImport.setErrorFile(new File("src/test/resources/subscribers.error.csv"));
		fileDataSource.setFileImport(fileImport);
		fileDataSource.setFileType(FileType.CSV);
		fileDataSource.setUpdate(true);
		fileDataSource.setCreateErrorFile(true);
		fileDataSource.setDeleteSourceFile(false);
		fileDataSource.setList(list);
		
		//trackingId is 8 alphanumeric characters 
		String trackingId = importService.importData(fileDataSource);
		Assert.assertTrue(trackingId.length()==8);
		Assert.assertTrue(Pattern.matches("[a-zA-Z0-9]{8}", trackingId));
		
		Thread.sleep(3*1000);//wait the import thread to be done.
		
		
		Assert.assertNotNull(subscriberDao.findByEmailFromList(list, "donald@test.com"));
		Assert.assertNotNull(subscriberDao.findByEmailFromList(list, "mickey@test.com"));
		Subscriber badger = subscriberDao.findByEmailFromList(list, "badger@test.com");
		Assert.assertNotNull(badger);
		Assert.assertEquals("INA", badger.getStatus());
		Assert.assertNotNull(subscriberDao.findByEmailFromList(list, "minnie@test.com"));
		Assert.assertNotNull(subscriberDao.findByEmailFromList(list, "pluto@test.com"));
	}
	
	
}
