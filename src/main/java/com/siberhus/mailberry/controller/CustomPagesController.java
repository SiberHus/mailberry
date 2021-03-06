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

import com.siberhus.mailberry.model.CustomPage;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.CustomPageService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(CustomPagesController.PATH)
public class CustomPagesController extends BaseController {

	public final static String PATH = "/tools/custom-pages";
	protected final static String VIEW_PREFIX = "pages/custom_pages";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private CustomPageService customPageService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listCustomPages(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<CustomPage> builder = new GridDataBuilder<CustomPage>(gridParam, 
			new JpaGridDataHandler<CustomPage>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from CustomPage e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(CustomPage o) {
				return new Object[]{"", o.getName(), o.getVisitCount(), 
					o.getVisibility(), o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createCustomPage(Model model){
		model.addAttribute(new CustomPage());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveCustomPage(@Valid CustomPage customPage, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(customPage);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			customPage.setUser(user);
			customPage = customPageService.save(customPage);
		}catch(DataAccessException e){
			result.addError(new ObjectError("customPageiable", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editCustomPage(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		CustomPage customPage = customPageService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( customPage);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteCustomPage(@PathVariable("id") Long id, HttpServletRequest request){
		customPageService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteCustomPages(@RequestParam("id") Long[] ids, HttpServletRequest request){
		customPageService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewCustomPage(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		CustomPage customPage = customPageService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(customPage);
		return VIEW_PREFIX+"/view";
	}
	
}
