package com.siberhus.mailberry.ui.grid.handler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.siberhus.mailberry.model.base.Model;
import com.siberhus.mailberry.ui.grid.GridDataQuery;

public abstract class JpaGridDataHandler<T> extends AbstractSqlGridDataHandler<T> {
	
	private EntityManager entityManager;
	
	public JpaGridDataHandler(){}
	
	public JpaGridDataHandler(EntityManager em){
		this.entityManager = em;
	}
	
	@Override
	public void addQueryOrder(GridDataQuery query, String sidx, String sord) {
		if(query.getAlias()!=null){
			query.appendQueryString(" ORDER BY "+query.getAlias()+"."+sidx+" "+sord);
		}else{
			query.appendQueryString(" ORDER BY "+sidx+" "+sord);
		}
//		for(String order: query.getOrders()){
//			String orderParts[] = order.split("\\s");
//			String orderStr = "ASC"; 
//			if(orderParts.length>1){
//				orderStr = orderParts[1];
//			}
//			System.out.println(">>>>>>>>>>>>>>>"+orderParts[0]+" "+orderStr);
//		}
	}
	
	@Override
	public Object extractResultId(T o) {
		if(o instanceof Model){
			return ((Model<?>)o).getId();
		}
		throw new IllegalArgumentException("Object "+o+" is not an instance of Model, " +
			"you have to override extractResultId metohd");
	}
	
	@Override
	public long countResult(GridDataQuery gridDataQuery) {
		Query query = entityManager.createQuery("select count(*) "
				+gridDataQuery.getQueryString());
		int pos = 1;
		for(Object param: gridDataQuery.getParameters()){
			query.setParameter(pos, param);
			pos++;
		}
		return ((Number)query.getSingleResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getResultList(GridDataQuery gridDataQuery, int offset, int limit) {
		Query query =  entityManager.createQuery(gridDataQuery.getQueryString());
		int pos = 1;
		for(Object param: gridDataQuery.getParameters()){
			query.setParameter(pos, param);
			pos++;
		}
		query.setFirstResult(offset).setMaxResults(limit);
		return query.getResultList();
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
