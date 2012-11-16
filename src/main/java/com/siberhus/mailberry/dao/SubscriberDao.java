package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;

public interface SubscriberDao {
	
	public Subscriber save(Subscriber subscriber);
	
	public Subscriber get(Long id);
	
	public void delete(Long id);
	
	public List<Subscriber> findAllByEmail(String email);
	
	public Subscriber findByEmailFromList(SubscriberList list, String email);
	
	public List<Subscriber> findAllByStatusFromList(SubscriberList list, String status);
	
	public Number countByStatusFromList(SubscriberList list, String status);
	
	public Number countFromList(SubscriberList list);
	
	public List<Long> getAllIdsByStatusFromList(SubscriberList list, String status);
	
}
