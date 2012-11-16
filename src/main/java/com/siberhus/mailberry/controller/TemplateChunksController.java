package com.siberhus.mailberry.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.siberhus.mailberry.model.TemplateChunk;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.TemplateChunkService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(TemplateChunksController.PATH)
public class TemplateChunksController  extends BaseController {
	
	public final static String PATH = "/messages/template-chunks";
	protected final static String VIEW_PREFIX = "pages/template_chunks";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private TemplateChunkService templateChunkService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listTemplateChunks(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<TemplateChunk> builder = new GridDataBuilder<TemplateChunk>(gridParam, 
			new JpaGridDataHandler<TemplateChunk>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from TemplateChunk e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(TemplateChunk o) {
				return new Object[]{"", o.getName(), 
					StringUtils.abbreviate(o.getValue(), 30), o.getStatus(),
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
	public String createTemplateChunk(Model model){
		model.addAttribute(new TemplateChunk());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveTemplateChunk(@Valid TemplateChunk templateChunk, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(templateChunk);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			templateChunk.setUser(user);
			templateChunk = templateChunkService.save(templateChunk);
		}catch(DataAccessException e){
			result.addError(new ObjectError("templateChunkiable", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editTemplateChunk(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		TemplateChunk templateChunk = templateChunkService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( templateChunk);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteTemplateChunk(@PathVariable("id") Long id, HttpServletRequest request){
		templateChunkService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteTemplateChunks(@RequestParam("id") Long[] ids, HttpServletRequest request){
		templateChunkService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewTemplateChunk(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		TemplateChunk templateChunk = templateChunkService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(templateChunk);
		return VIEW_PREFIX+"/view";
	}
}
