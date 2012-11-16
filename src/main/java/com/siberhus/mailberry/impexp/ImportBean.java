package com.siberhus.mailberry.impexp;

import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.SubscriberList;

public class ImportBean {
	
	public static enum DataSource {
		database, file, saas, text, manual
	}
	
	@NotNull
	private SubscriberList list;
	
	@NotNull
	private DataSource dataSource = DataSource.file;
	
	public SubscriberList getList() {
		return list;
	}
	
	public void setList(SubscriberList list) {
		this.list = list;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

	
}
