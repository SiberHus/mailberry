package com.siberhus.mailberry.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.siberhus.mailberry.MailBerryTestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/spring/dataSource-context.xml",
	"/spring/persistence-context.xml",
	"/spring/service-context.xml",
	"/spring/test-context.xml"
})
public class TrackingServiceTest extends MailBerryTestCase {
	
	@Inject
	private TrackingService trackingService;
	
	@Test
	public void test()throws Exception{
	
		
	}
}
