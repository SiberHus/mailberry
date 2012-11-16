package com.siberhus.mailberry.service;

import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.Subscriber;

public interface SubscriberService {
	
	public Subscriber save(Subscriber subscriber);
	
	public void validateFieldValue(Subscriber subscriber, FieldValidator validator);
	
	public Subscriber get(Long id);
	
	public void delete(Long... ids);
	
}
