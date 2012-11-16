package com.siberhus.mailberry.exception;

import java.util.ArrayList;
import java.util.List;

public class CumulativeException extends MailBerryException {
	
	private static final long serialVersionUID = 1L;
	
	private final List<Exception> exceptions = new ArrayList<Exception>();
	
	private Class<? extends Exception> type;
	
	public CumulativeException(Class<? extends Exception> type){
		this.type = type;
	}
	
	public void add(Exception exception){
		if(type.isAssignableFrom(exception.getClass())){
			exceptions.add(exception);
		}else{
			throw new IllegalArgumentException("Exception must be type of "+type);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T>List<T> getAll(Class<T> type){
		return (List<T>)exceptions;
	}
	
	public List<Exception> getAll(){
		return exceptions;
	}
	
	public int getSize(){
		return exceptions.size();
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(Exception e: exceptions){
			str.append("+").append(e.toString()).append("\n");
		}
		return str.toString();
	}
	
	public String getMessage(){
		StringBuilder str = new StringBuilder();
		for(Exception e: exceptions){
			str.append("+").append(e.getMessage()).append("\n");
		}
		return str.toString();
	}
}
