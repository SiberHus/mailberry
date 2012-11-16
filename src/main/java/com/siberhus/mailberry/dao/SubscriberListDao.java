package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.SubscriberList;

public interface SubscriberListDao {
	
	public SubscriberList save(SubscriberList subscriberList);
	
	public SubscriberList get(Long userId, Long id);
	
	public void delete(Long userId, Long id);
	
	public void setFieldValidators(Long userId, Long id, List<FieldValidator> validators);
	
}
