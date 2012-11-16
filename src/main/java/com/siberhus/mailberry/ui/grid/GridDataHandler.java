package com.siberhus.mailberry.ui.grid;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface GridDataHandler <T>{
	
	public GridDataQuery createGridDataQuery(HttpServletRequest request);
	
	public void addAllConditionalExpressions(GridDataQuery query, GridParam gridParam);
	
	public void addQueryOrder(GridDataQuery query, String sidx, String sord);
	
	public long countResult(GridDataQuery query);
	
	public List<T> getResultList(GridDataQuery query, int offset, int limit);	
	
	public Object extractResultId(T o);
	
	public Object[] extractResultFields(T o);
	
}
