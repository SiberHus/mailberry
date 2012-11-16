package com.siberhus.mailberry.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.dao.SubscriberListDao;
import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.SubscriberListService;

@Service
public class SubscriberListServiceImpl implements SubscriberListService{
	
	@Inject
	private SubscriberDao subscriberDao;
	
	@Inject
	private SubscriberListDao listDao;
	
	@Transactional
	@Override
	public SubscriberList save(SubscriberList list) {
		int fieldCount = list.getFieldNames().size();
		list.setFieldCount(fieldCount);
		
		return listDao.save(list);
	}
	
	@Transactional
	@Override
	public SubscriberList get(Long userId, Long id) {
		SubscriberList list = listDao.get(userId, id);
		if(list==null) return null;
		if(!SubscriberList.Status.LOCKED.equals(list.getStatus())){
			Number number = subscriberDao.countByStatusFromList(
					list, Subscriber.Status.ACTIVE);
			if(number.intValue()>0){
				list.setStatus(SubscriberList.Status.LOCKED);
			}
		}
		list = listDao.save(list);
		return list;
	}
	
	@Transactional
	@Override
	public void delete(Long userId, Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				listDao.delete(userId, id);
			}
		}
	}
	
	@Transactional
	@Override
	public void setFieldValidators(Long userId, Long listId, List<FieldValidator> fieldValidators) {
		listDao.setFieldValidators(userId, listId, fieldValidators);
	}

	@Transactional
	@Override
	public SubscriberList unlock(Long userId, Long id) {
		SubscriberList list = get(userId, id);
//		list.setStatus(SubscriberList.Status.UNLOCKED);
		list.setStatus(SubscriberList.Status.ACTIVE);
		list = listDao.save(list);
		return list;
	}
	
}
