package com.siberhus.mailberry.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.Roles;
import com.siberhus.mailberry.dao.DashboardDao;
import com.siberhus.mailberry.model.DashboardWidget;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Repository
public class DashboardDaoImpl implements DashboardDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public DashboardWidget saveWidget(DashboardWidget widget) {
		widget = em.merge(widget);
		return widget;
	}
	
	@Override
	public DashboardWidget getWidget(Long id) {
		DashboardWidget widget = em.find(DashboardWidget.class, id);
		return widget;
	}

	@Override
	public void deleteWidget(Long id) {
		DashboardWidget widget = getWidget(id);
		if(widget!=null){
			em.remove(widget);
		}
	}
	
	@Override
	public List<DashboardWidget> findAllActiveWidgetsByIds(List<Long> ids) {
		if(ids==null) return null;
		String param = StringUtils.substringBetween(ids.toString(),"[","]");
		String query = "from DashboardWidget dw where dw.id in ("+param+") and dw.status='"+Status.ACTIVE+"'";
		@SuppressWarnings("unchecked")
		List<DashboardWidget> widgets = em.createQuery(query).getResultList();
		return widgets;
	}
	
	
	@Override
	public List<DashboardWidget> getAllActiveWidgets() {
		String query = "from DashboardWidget dw where dw.status='"+Status.ACTIVE+"'";
		if(!SpringSecurityUtils.hasAuthority(Roles.ADMIN)){
			query += " and dw.adminOnly=false";
		}
		@SuppressWarnings("unchecked")
		List<DashboardWidget> widgets = em.createQuery(query).getResultList();
		return widgets;
	}
	
}
