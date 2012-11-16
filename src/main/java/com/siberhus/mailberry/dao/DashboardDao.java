package com.siberhus.mailberry.dao;

import java.util.List;

import com.siberhus.mailberry.model.DashboardWidget;

public interface DashboardDao {

	public DashboardWidget saveWidget(DashboardWidget widget);

	public DashboardWidget getWidget(Long id);
	
	public void deleteWidget(Long id);

	public List<DashboardWidget> findAllActiveWidgetsByIds(List<Long> ids);
	
	public List<DashboardWidget> getAllActiveWidgets();
	
}
