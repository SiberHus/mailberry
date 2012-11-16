package com.siberhus.mailberry.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.exception.CumulativeException;
import com.siberhus.mailberry.exception.FieldValidationException;
import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.SubscriberListService;
import com.siberhus.mailberry.service.SubscriberService;
import com.siberhus.mailberry.util.ClassCache;

@Service
public class SubscriberServiceImpl implements SubscriberService {
	
	@Inject
	private SubscriberDao subscriberDao;
	
	@Inject
	private SubscriberListService listService;
	
	@Inject
	private ConversionService conversionService;
	
	/**
	 * This method is not for large volume data insertion due to its performance.
	 */
	@Transactional
	@Override
	public Subscriber save(Subscriber subscriber) {
		//:TODO Watch out this call, it allows user to edit other users' data
		SubscriberList list = listService.get(null, 
			subscriber.getList().getId());
		subscriber.setList(list);
		List<FieldValidator> validators = list.getFieldValidators();
		CumulativeException exceptions = new CumulativeException(FieldValidationException.class);
		for(FieldValidator validator: validators){
			if(!validator.isEnabled()){
				continue;
			}
			try{
				validateFieldValue(subscriber, validator);
			}catch(FieldValidationException e){
				exceptions.add(e);
			}
		}
		
		if(subscriber.isNew()){ //&& database==Database.HSQL){
			//Hibernate cannot generate constraints correctly for some databases
			//such as HSQLDB. It does not generate unique constraint for multiple columns. 
			if(subscriberDao.findByEmailFromList(list, subscriber.getEmail())!=null){
				exceptions.add(new FieldValidationException( 
						new FieldError("subscriber", "email" , subscriber.getEmail(), 
						false, new String[]{"validator.unique"}, 
						new Object[]{"email"}, "\"{0}\" is duplicate")));
			}
		}
		
		Number subscriberCount = subscriberDao.countFromList(list);
		list.setSubscriberCount(subscriberCount.intValue());
		
		if(exceptions.getSize()>0){
			throw exceptions;
		}
		
		return subscriberDao.save(subscriber);
	}
	
	@Override
	public void validateFieldValue(Subscriber subscriber, FieldValidator validator){
		SubscriberList list = subscriber.getList();
		int fieldNumber = validator.getFieldNumber();
		String fieldName = list.getFieldNames().get(fieldNumber-1);
		String value = getFieldValue(subscriber, fieldNumber);
		value = StringUtils.trimToNull(value);
		if(value==null){
			if(validator.isRequired()){
				throw new FieldValidationException(fieldNumber, 
					new FieldError("subscriber", "field"+fieldNumber+"Value" , value, 
					false, new String[]{"validator.required"}, 
					new Object[]{fieldName}, "\"{0}\" is required"));
			}
			return;
		}
		Object convertedValue = null;
		if("java.lang.String".equals(validator.getDataType())){
			convertedValue = value;
		}else{
			Class<?> targetType = ClassCache.getClass(validator.getDataType());
			try{
				convertedValue = conversionService.convert(value, targetType);
			}catch(ConversionFailedException e){
				throw new FieldValidationException(fieldNumber, 
					new FieldError("subscriber", "field"+fieldNumber+"Value" , value, 
					false, new String[]{"validator.conversionFailed"}, 
					new Object[]{fieldName, value, targetType.getSimpleName()}, 
					"\"{0}\" with value \"{1}\" failed to convert to \"{2}\""));
			}
		}
		if(validator.getMinSize()!=null || validator.getMaxSize()!=null){
			double size = 0;
			if(convertedValue instanceof String){
				size = ((String)convertedValue).length();
			}else if(convertedValue instanceof Number){
				size = ((Number)convertedValue).doubleValue();
			}
			if(validator.getMinSize()!=null && (size < validator.getMinSize()) ){
				throw new FieldValidationException(fieldNumber, 
					new FieldError("subscriber", "field"+fieldNumber+"Value" , value, 
					false, new String[]{"validator.invalid.min"}, 
					new Object[]{fieldName, value, validator.getMinSize()}, 
					"\"{0}\" with value \"{1}\" is less than minimum size of \"{2}\""));
			}
			if(validator.getMaxSize()!=null && (size > validator.getMaxSize()) ){
				throw new FieldValidationException(fieldNumber, 
					new FieldError("subscriber", "field"+fieldNumber+"Value" , value, 
					false, new String[]{"validator.invalid.max"}, 
					new Object[]{fieldName, value, validator.getMaxSize()}, 
					"\"{0}\" with value \"{1}\" exceeds the maximum size of \"{2}\""));
			}
		}
		if(validator.getRegExp()!=null){
			if(!Pattern.matches(validator.getRegExp(), value)){
				throw new FieldValidationException(fieldNumber, 
					new FieldError("subscriber", "field"+fieldNumber+"Value" , value, 
					false, new String[]{"validator.invalid.custom"}, 
					new Object[]{fieldName, value}, 
					"\"{0}\" with value \"{1}\" does not pass custom validation"));
			}
		}
	}
	

	private String getFieldValue(Subscriber subscriber, int fieldNumber){
		if(fieldNumber==1) return subscriber.getField1Value();
		else if(fieldNumber==2) return subscriber.getField2Value();
		else if(fieldNumber==3) return subscriber.getField3Value();
		else if(fieldNumber==4) return subscriber.getField4Value();
		else if(fieldNumber==5) return subscriber.getField5Value();
		else if(fieldNumber==6) return subscriber.getField6Value();
		else if(fieldNumber==7) return subscriber.getField7Value();
		else if(fieldNumber==8) return subscriber.getField8Value();
		else if(fieldNumber==9) return subscriber.getField9Value();
		else if(fieldNumber==10) return subscriber.getField10Value();
		else if(fieldNumber==11) return subscriber.getField11Value();
		else if(fieldNumber==12) return subscriber.getField12Value();
		else if(fieldNumber==13) return subscriber.getField13Value();
		else if(fieldNumber==14) return subscriber.getField14Value();
		else if(fieldNumber==15) return subscriber.getField15Value();
		else if(fieldNumber==16) return subscriber.getField16Value();
		else if(fieldNumber==17) return subscriber.getField17Value();
		else if(fieldNumber==18) return subscriber.getField18Value();
		else if(fieldNumber==19) return subscriber.getField19Value();
		else if(fieldNumber==20) return subscriber.getField20Value();
		throw new IndexOutOfBoundsException("fieldNumber is out of bounds: expect 1-20, value: "+fieldNumber);
	}
	
	@Transactional
	@Override
	public Subscriber get(Long id) {
		Subscriber subscriber = subscriberDao.get(id);
		subscriber.getList().getFieldValidators().size();
		return subscriber;
	}
	
	@Transactional
	@Override
	public void delete(Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				subscriberDao.delete(id);
			}
		}
	}
	
	
}
