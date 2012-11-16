package com.siberhus.mailberry.service;

import java.util.List;

import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.SubscriberList;

public interface SubscriberListService {

	public SubscriberList save(SubscriberList list);
	
	public SubscriberList get(Long userId, Long id);
	
	public void delete(Long userId, Long... ids);
	
	public void setFieldValidators(Long userId, Long listId, List<FieldValidator> fieldValidators);
	
	public SubscriberList unlock(Long userId, Long id);
	
}
