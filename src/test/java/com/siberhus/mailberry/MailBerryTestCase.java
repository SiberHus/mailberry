package com.siberhus.mailberry;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public abstract class MailBerryTestCase {
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	protected boolean autoStartTransaction(){
		return false;
	}
	
	@Before
	public void _startTransaction(){
		if(autoStartTransaction()){
			startTransaction();
		}
	}
	
	@After
	public void _endTransaction(){
		if(autoStartTransaction()){
			if(!status.isRollbackOnly()){
				commitTransaction();
			}
		}
	}
	
	protected void startTransaction(){
		TransactionDefinition def = new DefaultTransactionDefinition();
		status = transactionManager.getTransaction(def);
	}
	
	protected void commitTransaction(){
		transactionManager.commit(status);
	}
	
	protected void rollbackTransaction(){
		transactionManager.rollback(status);
	}
	
}
