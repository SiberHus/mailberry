package com.siberhus.mailberry.ui.grid;

import java.util.ArrayList;
import java.util.List;


public class GridDataQuery {
	
	private StringBuilder queryString;
	
	private List<Object> parameters = new ArrayList<Object>();
	
	private List<String> orders = new ArrayList<String>();
	
	private String alias;
	
	public GridDataQuery(){
		this.queryString = new StringBuilder(32);
	}
	
	public GridDataQuery(String queryString){
		this.queryString = new StringBuilder(queryString);
	}
	
	public String getQueryString() {
		return queryString.toString();
	}
	
	public void setQueryString(String queryString) {
		this.queryString = new StringBuilder(queryString);
	}
	
	public void appendQueryString(String queryString){
		this.queryString.append(queryString);
	}
	
	public List<Object> getParameters() {
		return parameters;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(Object parameter){
		this.parameters.add(parameter);
	}
	
	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}

	public void addOrder(String order){
		this.orders.add(order);
	}
	
	public String getOrdersString(){
		String ordersStr = "";
		for(String order: orders){
			ordersStr += order+", ";
		}
		return ordersStr;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	
	
}
