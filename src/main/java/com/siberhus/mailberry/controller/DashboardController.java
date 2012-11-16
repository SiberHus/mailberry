package com.siberhus.mailberry.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.model.DashboardWidget;
import com.siberhus.mailberry.service.DashboardService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;

@Controller
@RequestMapping(DashboardController.PATH)
public class DashboardController extends BaseController {
	
	public final static String PATH = "/dashboard";
	protected final static String VIEW_PREFIX = "pages/dashboard";
	
	private static final String WIDGETS_SESSION = "_WIDGETS";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private DashboardService dashboardService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String dashboard(Model model, HttpSession session){
		
		List<DashboardWidget> userWidgets = null;
		List<Long> userWidgetIds = null;
		
		userWidgetIds = (List<Long>)session.getAttribute(WIDGETS_SESSION);
		if(userWidgetIds!=null){
			userWidgets = dashboardService.findAllActiveWidgetsByIds(userWidgetIds);
		}else{
			userWidgets = dashboardService.getAllActiveWidgets();
			if(userWidgets!=null && userWidgets.size()>0){
				userWidgetIds = new ArrayList<Long>();
				for(DashboardWidget widget: userWidgets){
					userWidgetIds.add(widget.getId());
				}
				session.setAttribute(WIDGETS_SESSION, userWidgetIds);
			}
		}
		
		model.addAttribute("userWidgets", userWidgets);
		
		return VIEW_PREFIX+"/index";
	}
	
	@RequestMapping({"/widgets", "/widgets/list"})
	public String showWidgetList(Model model){
		return VIEW_PREFIX+"/widgets/list";
	}
	
	@RequestMapping(value = {"/widgets/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listDashboardWidgets(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<DashboardWidget> builder = new GridDataBuilder<DashboardWidget>(gridParam, 
			new JpaGridDataHandler<DashboardWidget>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from DashboardWidget e");
				return query;
			}
			@Override
			public Object[] extractResultFields(DashboardWidget o) {
				return new Object[]{"", o.getName(), 
					o.getContentUri(), o.getStatus(),
					o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/widgets/create", method = {RequestMethod.GET})
	public String createDashboardWidget(Model model){
		model.addAttribute(new DashboardWidget());
		return VIEW_PREFIX+"/widgets/form";
	}
	
	@RequestMapping(value = "/widgets/save", method = {RequestMethod.POST})
	public String saveDashboardWidget(@Valid DashboardWidget widget, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(widget);
			return VIEW_PREFIX+"/widgets/form";
		}
		try{
			widget = dashboardService.saveWidget(widget);
		}catch(DataAccessException e){
			result.addError(new ObjectError("widgetiable", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/widgets/form";
		}
		return "redirect:"+PATH+"/widgets";
	}
	
	@RequestMapping(value = "/widgets/edit/{id}", method = {RequestMethod.GET})
	public String editDashboardWidget(@PathVariable("id") Long id, Model model){
		DashboardWidget widget = dashboardService.getWidget(id);
		model.addAttribute( widget);
		return VIEW_PREFIX+"/widgets/form";
	}
	
	@RequestMapping(value = "/widgets/delete/{id}", method = {RequestMethod.POST})
	public String deleteDashboardWidget(@PathVariable("id") Long id){
		dashboardService.deleteWidgets(id);
		return "redirect:"+PATH+"/widgets";
	}
	
	@RequestMapping(value = "/widgets/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteDashboardWidgets(@RequestParam("id") Long[] ids){
		dashboardService.deleteWidgets(ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/widgets/view/{id}", method = {RequestMethod.GET})
	public String viewDashboardWidget(@PathVariable("id") Long id, Model model){
		DashboardWidget widget = dashboardService.getWidget(id);
		model.addAttribute(widget);
		return VIEW_PREFIX+"/widgets/view";
	}
}
