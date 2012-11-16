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

import com.siberhus.mailberry.model.MailAccount;
import com.siberhus.mailberry.model.Status;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.MailAccountService;
import com.siberhus.mailberry.service.MailServerService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(MailAccountsController.PATH)
public class MailAccountsController extends BaseController {
	
	public final static String PATH = "/settings/mail-accounts";
	protected final static String VIEW_PREFIX = "pages/mail_accounts";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private MailServerService mailServerService;
	
	@Inject
	private MailAccountService mailAccountService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listMailAccounts(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<MailAccount> builder = new GridDataBuilder<MailAccount>(gridParam, 
			new JpaGridDataHandler<MailAccount>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from MailAccount e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(MailAccount o) {
				return new Object[]{"", o.getDisplayName(), 
					o.getEmail(), o.getMailServer().getServerName(), 
					o.getStatus(),o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createMailAccount(Model model, HttpServletRequest request){
		model.addAttribute(new MailAccount());
		addMailServers(model, request);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveMailAccount(@Valid MailAccount mailAccount, 
			BindingResult result, Model model, HttpServletRequest request){
		addMailServers(model, request);
		if(result.hasErrors()){
			model.addAttribute(mailAccount);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			mailAccount.setUser(user);
			mailAccount = mailAccountService.save(mailAccount);
		}catch(DataAccessException e){
			result.addError(new ObjectError("mailAccount", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editMailAccount(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MailAccount mailAccount = mailAccountService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		mailAccount.setSmtpPassword(null);
		mailAccount.setPop3Password(null);
		model.addAttribute(mailAccount);
		addMailServers(model, request);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteMailAccount(@PathVariable("id") Long id, HttpServletRequest request){
		mailAccountService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteMailAccounts(@RequestParam("id") Long[] ids, HttpServletRequest request){
		mailAccountService.delete(
			SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewMailAccount(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MailAccount mailAccount = mailAccountService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		mailAccount.setSmtpPassword(null);
		mailAccount.setPop3Password(null);
		model.addAttribute(mailAccount);
		return VIEW_PREFIX+"/view";
	}
	
	private void addMailServers(Model model, HttpServletRequest request){
		
		model.addAttribute("mailServers", mailServerService.findAllByStatus(
				SpringSecurityUtils.getNotAdminId(request), Status.ACTIVE));
	}
	
}
