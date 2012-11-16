package com.siberhus.mailberry.ui.grid;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

public class GridDataBuilder<T> {
	
	private final Logger log = LoggerFactory.getLogger(GridDataBuilder.class);
	
	private GridParam gridParam;
	private GridDataHandler<T> gridDataHandler;
	private boolean htmlEscape = true;
	
	public GridDataBuilder(){}
	
	public GridDataBuilder(GridParam gridParam, GridDataHandler<T> gridDataHandler){
		this.gridParam = gridParam;
		this.gridDataHandler = gridDataHandler;
	}
	
	public GridData build(HttpServletRequest request){
		if(gridParam==null || gridDataHandler==null){
			throw new IllegalArgumentException("GridParam and GridDataHandler are required attribute.");
		}
		GridDataQuery gridDataQuery = gridDataHandler.createGridDataQuery(request);
		if("true".equals(request.getParameter("_search"))){
			gridDataHandler.addAllConditionalExpressions(gridDataQuery, gridParam);
		}
		
		int page = gridParam.getPage();
		int limit = gridParam.getRows();
		String sidx = gridParam.getSidx();
		String sord = gridParam.getSord();
		
		long count = gridDataHandler.countResult(gridDataQuery);
		
		int totalPages = (count > 0)?(int)Math.ceil(Double.valueOf(count)/limit):0;
		page = (page>totalPages)?totalPages:page;
	    int offset = limit*page - limit;
	    if(offset<0) offset = 0;
		if(sidx!=null && sidx.length()>1){
			gridDataHandler.addQueryOrder(gridDataQuery, sidx, sord);
		}
		if(log.isDebugEnabled()){
			log.debug("GridParam:[page:{}, rows:{}, sidx:{}, sord:{}]",
					new Object[]{page, limit,sidx,sord});
			log.debug("Query: {}", gridDataQuery.getQueryString());
			log.debug("Params: {}", gridDataQuery.getParameters());
			log.debug("Grid Display Settings: [totalPages:{}, page:{}, offset:{}]",
					new Object[]{totalPages, page, offset});
		}
		List<T> resultList = gridDataHandler.getResultList(gridDataQuery, offset, limit);
		
		GridData gridData = new GridData(page, totalPages, count);
		
		for(T o: resultList){
			GridData.Row row = new GridData.Row();
			row.setId(gridDataHandler.extractResultId(o));
			if(htmlEscape){
				Object vals[] = gridDataHandler.extractResultFields(o);
				row.setCell(new Object[vals.length]);
				for(int i=0;i<vals.length;i++){
					Object val = vals[i];
					if(val instanceof String){
						val = HtmlUtils.htmlEscape(ObjectUtils.toString(val, null));
					}
					row.getCell()[i] = val;
				}
			}else{
				row.setCell(gridDataHandler.extractResultFields(o));
			}
			gridData.getRows().add(row);
		}
		
		return gridData;
	}

	public GridDataBuilder<T> setHtmlEscape(boolean htmlEscape) {
		this.htmlEscape = htmlEscape;
		return this;
	}
	
	
}
