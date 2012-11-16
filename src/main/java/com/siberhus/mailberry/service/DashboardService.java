package com.siberhus.mailberry.service;

import java.util.List;

import com.siberhus.mailberry.model.DashboardWidget;

public interface DashboardService {

	public DashboardWidget saveWidget(DashboardWidget widget);
	
	public DashboardWidget getWidget(Long id);
	
	public void deleteWidgets(Long... ids);
	
	public List<DashboardWidget> findAllActiveWidgetsByIds(List<Long> ids);
	
	public List<DashboardWidget> getAllActiveWidgets();
}
