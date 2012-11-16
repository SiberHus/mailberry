package com.siberhus.mailberry.ui.grid.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siberhus.mailberry.ui.grid.GridDataHandler;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.GridParam.SearchFilter;
import com.siberhus.mailberry.ui.grid.GridParam.SearchRule;

public abstract class AbstractSqlGridDataHandler<T> implements GridDataHandler<T> {
	
	
	public AbstractSqlGridDataHandler(){}
	
	public abstract Object convertFieldValue(String fieldName, String value);
	
	@Override
	public void addAllConditionalExpressions(GridDataQuery query, GridParam gridParam){
		boolean omitFirstOp = false;
		if(!query.getQueryString().toUpperCase().contains(" WHERE ")){
			omitFirstOp =  true;
			query.appendQueryString(" WHERE ");
		}
		if(gridParam.getSearchField()!=null){
			//single search field
			String fieldName = gridParam.getSearchField();
			String op = gridParam.getSearchOper();
			String value = gridParam.getSearchString();
			String conditionalExp = translateConditionalExp(query, fieldName, op, value);
			if(omitFirstOp){
				query.appendQueryString(conditionalExp);
				omitFirstOp = false;
			}else{
				query.appendQueryString(" AND "+conditionalExp);
			}
			return;
		}
		SearchFilter searchFilter = gridParam.getSearchFilters();
		String groupOp = searchFilter.getGroupOp();
		for(SearchRule rule : searchFilter.getRules()){
			String fieldName = rule.getField();
			String op = rule.getOp();
			String value = rule.getData();
			String conditionalExp = translateConditionalExp(query, fieldName, op, value);
			if(omitFirstOp){
				query.appendQueryString(conditionalExp);
				omitFirstOp = false;
			}else{
				query.appendQueryString(" "+groupOp+" "+conditionalExp);
			}
		}
		//TODO: implement multigroup search
	}
	
	private static final Map<String, String> BASIC_SEARCH_OPS = Collections.unmodifiableMap(
		new HashMap<String, String>(){
			private static final long serialVersionUID = 1L;
			{
				put("eq", "=");put("ne", "<>");
				put("lt", "<");put("le", "<=");
				put("gt", ">");put("ge", ">=");
			}
	});
	
	private String translateConditionalExp(GridDataQuery query, 
			String fieldName, String op, String value){
		List<Object> params = query.getParameters();
		String fieldNameWithAlias = fieldName;
		if(query.getAlias()!=null){
			fieldNameWithAlias = query.getAlias()+"."+fieldName;
		}
		int pos = params.size()+1;
		String translatedOp = BASIC_SEARCH_OPS.get(op);
		if(translatedOp!=null){
			params.add(convertFieldValue(fieldName, value));
			return fieldNameWithAlias+translatedOp+"?"+pos;
		}
		if("cn".equals(op)){
			params.add("%"+value+"%");
			return fieldNameWithAlias+" LIKE ?"+pos;
		}else if("nc".equals(op)){
			params.add("%"+value+"%");
			return fieldNameWithAlias+" NOT LIKE ?"+pos;
		}else if("bw".equals(op)){
			params.add("%"+value);
			return fieldNameWithAlias+" LIKE ?"+pos;
		}else if("bn".equals(op)){
			params.add("%"+value);
			return fieldNameWithAlias+" NOT LIKE ?"+pos;
		}else if("ew".equals(op)){
			params.add(value+"%");
			return fieldNameWithAlias+" LIKE ?"+pos;
		}else if("en".equals(op)){
			params.add(value+"%");
			return fieldNameWithAlias+" NOT LIKE ?"+pos;
		}else if("in".equals(op)){
			params.add(convertFieldValue(fieldName, value));
			return fieldNameWithAlias+" IN ?"+pos;
		}else if("ni".equals(op)){
			params.add(convertFieldValue(fieldName, value));
			return fieldNameWithAlias+" NOT IN ?"+pos;
		}
		throw new IllegalArgumentException("Unkow operator: "+op);
	}
	
}
