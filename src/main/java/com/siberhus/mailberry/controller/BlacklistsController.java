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

import com.siberhus.mailberry.model.Blacklist;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.BlacklistService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(BlacklistsController.PATH)
public class BlacklistsController extends BaseController {
	
	public final static String PATH = "/data/blacklists";
	protected final static String VIEW_PREFIX = "pages/blacklists";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private BlacklistService blacklistService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listBlacklist(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<Blacklist> builder = new GridDataBuilder<Blacklist>(gridParam, 
			new JpaGridDataHandler<Blacklist>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from Blacklist e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(Blacklist o) {
				return new Object[]{"", o.getEmail(), 
					o.getFullName(), o.getReason(),
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
	public String createBlacklist(Model model){
		model.addAttribute(new Blacklist());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveBlacklist(@Valid Blacklist blacklist, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(blacklist);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			blacklist = blacklistService.save(blacklist, user);
		}catch(DataAccessException e){
			result.addError(new ObjectError("blacklist", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editBlacklist(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		Blacklist blacklist = blacklistService.get(SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( blacklist);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteBlacklist(@PathVariable("id") Long id, HttpServletRequest request){
		blacklistService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteBlacklists(@RequestParam("id") Long[] ids
			, HttpServletRequest request){
		blacklistService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewBlacklist(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		Blacklist blacklist = blacklistService.get(SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(blacklist);
		return VIEW_PREFIX+"/view";
	}
	
}
