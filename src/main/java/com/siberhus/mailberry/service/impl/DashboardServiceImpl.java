package com.siberhus.mailberry.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siberhus.mailberry.dao.DashboardDao;
import com.siberhus.mailberry.model.DashboardWidget;
import com.siberhus.mailberry.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Inject
	private DashboardDao dashboardDao;
	
	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public DashboardWidget saveWidget(DashboardWidget widget) {
		widget.setCode(DigestUtils.md5Hex(widget.getName()));
		String contentUri = widget.getContentUri();
		if(contentUri!=null && !contentUri.startsWith("/")){
			widget.setContentUri("/"+contentUri);
		}
		widget = dashboardDao.saveWidget(widget);
		return widget;
	}
	
	@Override
	public DashboardWidget getWidget(Long id) {
		DashboardWidget widget = dashboardDao.getWidget(id);
		return widget;
	}

	@PreAuthorize("!hasRole('ROLE_DEMO')")
	@Transactional
	@Override
	public void deleteWidgets(Long... ids) {
		if(ids!=null){
			for(Long id: ids){
				dashboardDao.deleteWidget(id);
			}
		}
	}
	
	@Override
	public List<DashboardWidget> findAllActiveWidgetsByIds(List<Long> ids) {
		List<DashboardWidget> widgets = dashboardDao.findAllActiveWidgetsByIds(ids);
		return widgets;
	}
	
	@Override
	public List<DashboardWidget> getAllActiveWidgets() {
		List<DashboardWidget> widgets = dashboardDao.getAllActiveWidgets();
		return widgets;
	}
	
}
