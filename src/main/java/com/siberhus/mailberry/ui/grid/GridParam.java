package com.siberhus.mailberry.ui.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.siberhus.mailberry.exception.MailBerryException;

public class GridParam {
	
	public static final List<String> SEARCH_OPS = Collections.unmodifiableList(
		new ArrayList<String>(Arrays.asList(new String[]{
			"eq","ne","lt","le","gt","ge","bw","bn","in","ni","ew","en","cn","nc"})));
	
	private int page;
	private int rows;
	private String sidx; //sort index 
	private String sord; //sort order
	
	/*=== Single search field ====*/
	private String searchField;
	private String searchOper;
	private String searchString;
	/*=== Multiple search fields ====*/
	private String filters;
	private SearchFilter searchFilters; //JSON Object
	
	public static class SearchFilter {
		private String groupOp;
		private List<SearchRule> rules;
		private SearchGroup groups;
		public String getGroupOp() {return groupOp;}
		public void setGroupOp(String groupOp) {this.groupOp = groupOp;}
		public List<SearchRule> getRules() {return rules;}
		public void setRules(List<SearchRule> rules) {this.rules = rules;}
		public SearchGroup getGroups() {return groups;}
		public void setGroups(SearchGroup groups) {this.groups = groups;}
	}
	public static class SearchGroup {
		private String groupOp;
		private List<SearchRule> rules;
		private SearchGroup groups;
		public String getGroupOp() {return groupOp;}
		public void setGroupOp(String groupOp) {this.groupOp = groupOp;}
		public List<SearchRule> getRules() {return rules;}
		public void setRules(List<SearchRule> rules) {this.rules = rules;}
		public SearchGroup getGroups() {return groups;}
		public void setGroups(SearchGroup groups) {this.groups = groups;}
	}
	public static class SearchRule {
		private String field;
		private String op;
		private String data;
		public String getField() {return field;}
		public void setField(String field) {this.field = field;}
		public String getOp() {return op;}
		public void setOp(String op) {this.op = op;}
		public String getData() {return data;}
		public void setData(String data) {this.data = data;}
	}
	
	public String toString(){
		return "GridParam[page="+page+",rows="+rows
			+",sidx="+sidx+",sord="+sord+"]";
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		if(filters==null){
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		try{
			this.searchFilters = mapper.readValue(filters, SearchFilter.class);
		}catch(Exception e){
			throw new MailBerryException(e);
		}
		this.filters = filters;
	}

	public SearchFilter getSearchFilters() {
		return searchFilters;
	}
	
}
