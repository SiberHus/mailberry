package com.siberhus.mailberry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.siberhus.mailberry.model.Blacklist;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/spring/dataSource-context.xml",
	"/spring/persistence-context.xml"
})
public class EntityManagerThreadSafetyTest {
	
	//Injected em is shared EntityManager, thread-safety.
	@PersistenceContext
	private EntityManager em;
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@Inject
	private PlatformTransactionManager txManager;
	
	@Ignore
	@Test
	public void testSpringSharedEntityManager()throws Exception{
		int nthread = 200;
		final CountDownLatch countDown = new CountDownLatch(nthread);
		for(int i=0;i<nthread;i++){
			Thread t = new Thread(){
				public void run(){
					TransactionDefinition txDef = new DefaultTransactionDefinition();
					TransactionStatus txStatus = txManager.getTransaction(txDef);
					Blacklist bl = new Blacklist();
					String name = RandomStringUtils.randomAlphanumeric(8);
					bl.setEmail(name+"@test.com");
					em.persist(bl);
					Assert.assertTrue(txStatus.isNewTransaction());
					txManager.commit(txStatus);
					countDown.countDown();
				}
			};
			t.start();
		}
		countDown.await();
		Number n = (Number)em.createQuery("select count(*) from Blacklist").getSingleResult();
		Assert.assertEquals(nthread, n.intValue());
		
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		Assert.assertTrue(txManager.getTransaction(txDef).isNewTransaction());
		Assert.assertFalse(txManager.getTransaction(txDef).isNewTransaction());
	}
	
	@Ignore
	@Test
	public void testEntityManager()throws Exception{
		
		final EntityManager em = emf.createEntityManager();
		final CountDownLatch countDown = new CountDownLatch(20);
		for(int i=0;i<20;i++){
			Thread t = new Thread(){
				public void run(){
					em.getTransaction().begin();
					Blacklist bl = new Blacklist();
					String name = RandomStringUtils.randomAlphanumeric(8);
					bl.setEmail(name+"@test.com");
					em.persist(bl);
					em.getTransaction().commit();
					countDown.countDown();
				}
			};
			t.start();
		}
		countDown.await();
		try{
			Number n = (Number)em.createQuery("select count(*) from Blacklist").getSingleResult();
			Assert.assertTrue(20 != n.intValue());
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	@Ignore
	@Test
	public void testEntityManager2()throws Exception{
		final AtomicInteger counter = new AtomicInteger();
		int nthread = 200;
		final CountDownLatch countDown = new CountDownLatch(nthread);
		for(int i=0;i<nthread;i++){
			Thread t = new Thread(){
				public void run(){
					counter.incrementAndGet();
					EntityManager em = emf.createEntityManager();
					em.getTransaction().begin();
					Blacklist bl = new Blacklist();
					String name = RandomStringUtils.randomAlphanumeric(20);
					bl.setEmail(name+"@test.com");
					em.persist(bl);
					em.getTransaction().commit();
					countDown.countDown();
				}
			};
			t.start();
		}
		countDown.await();
		Number n = (Number)em.createQuery("select count(*) from Blacklist").getSingleResult();
		Assert.assertEquals(nthread, n.intValue());
	}
}
