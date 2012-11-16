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

import com.siberhus.mailberry.model.MailServer;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.MailServerService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(MailServersController.PATH)
public class MailServersController extends BaseController {
	
	public final static String PATH = "/settings/mail-servers";
	protected final static String VIEW_PREFIX = "pages/mail_servers";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private MailServerService mailServerService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listMailServers(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<MailServer> builder = new GridDataBuilder<MailServer>(gridParam, 
			new JpaGridDataHandler<MailServer>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from MailServer e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? or e.publicServer=?");
					query.addParameter(SpringSecurityUtils.getUserId(request));
					query.addParameter(true);
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(MailServer o) {
				return new Object[]{"", o.getServerName(), 
					o.getSmtpServer(), o.getPop3Server(), o.getStatus(),
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
	public String createMailServer(Model model){
		model.addAttribute(new MailServer());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveMailServer(@Valid MailServer mailServer, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(mailServer);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			mailServer.setUser(user);
			mailServer = mailServerService.save(mailServer);
		}catch(DataAccessException e){
			result.addError(new ObjectError("mailServer", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editMailServer(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MailServer mailServer = mailServerService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( mailServer);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteMailServer(@PathVariable("id") Long id, HttpServletRequest request){
		mailServerService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteMailServers(@RequestParam("id") Long[] ids, HttpServletRequest request){
		mailServerService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewMailServer(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MailServer mailServer = mailServerService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(mailServer);
		return VIEW_PREFIX+"/view";
	}
	
}
