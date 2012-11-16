package com.siberhus.mailberry.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
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

import com.siberhus.mailberry.model.TemplateVariable;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.TemplateVariableService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(TemplateVariablesController.PATH)
public class TemplateVariablesController extends BaseController {

	public final static String PATH = "/messages/template-variables";
	protected final static String VIEW_PREFIX = "pages/template_variables";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private TemplateVariableService templateVarService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listTemplateVariables(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<TemplateVariable> builder = new GridDataBuilder<TemplateVariable>(gridParam, 
			new JpaGridDataHandler<TemplateVariable>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from TemplateVariable e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				query.addOrder("e.createdAt desc");
				return query;
			}
			@Override
			public Object[] extractResultFields(TemplateVariable o) {
				return new Object[]{"", o.getName(), 
					o.getValue(), o.getStatus(),
					o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createTemplateVariable(Model model){
		model.addAttribute(new TemplateVariable());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveTemplateVariable(@Valid TemplateVariable templateVar, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(templateVar);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			templateVar.setUser(user);
			templateVar = templateVarService.save(templateVar);
		}catch(DataAccessException e){
			result.addError(new ObjectError("templateVariable", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editTemplateVariable(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		TemplateVariable templateVar = templateVarService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( templateVar);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteTemplateVariable(@PathVariable("id") Long id, HttpServletRequest request){
		templateVarService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteTemplateVariables(@RequestParam("id") Long[] ids, HttpServletRequest request){
		templateVarService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewTemplateVariable(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		TemplateVariable templateVar = templateVarService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(templateVar);
		return VIEW_PREFIX+"/view";
	}
}
