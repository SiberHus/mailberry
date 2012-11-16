package com.siberhus.mailberry.ui.grid;

import java.util.ArrayList;
import java.util.List;

public class GridData {
	
	private int page;
	private int total; //total pages
	private long records; //total records
	private List<Row> rows = new ArrayList<Row>();
	
	public GridData(){}
	public GridData(int page, int total, long records){
		this.page = page;
		this.total = total;
		this.records = records;
	}
	
	public static class Row {
		private Object id;
		private Object[] cell;
		public Row(){};
		public Row(Object id, Object... cell){
			this.id = id;
			this.cell = cell;
		}
		public Object getId() {
			return id;
		}
		public void setId(Object id) {
			this.id = id;
		}
		public Object[] getCell() {
			return cell;
		}
		public void setCell(Object[] cell) {
			this.cell = cell;
		}
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getRecords() {
		return records;
	}
	public void setRecords(long records) {
		this.records = records;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}


	