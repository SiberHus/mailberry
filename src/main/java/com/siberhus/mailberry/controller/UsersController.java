package com.siberhus.mailberry.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mvc.extensions.flash.FlashMap.Message;
import org.springframework.mvc.extensions.flash.FlashMap.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.Roles;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(UsersController.PATH)
public class UsersController extends BaseController {
	
	public final static String PATH = "/settings/users";
	protected final static String VIEW_PREFIX = "pages/users";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		
		if(!SpringSecurityUtils.hasAuthority(Roles.ADMIN)){
			User user = userService.findByUsername(
				SpringSecurityUtils.getPrincipalName());
			return "redirect:"+PATH+"/view/"+user.getId();
		}
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listUsers(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<User> builder = new GridDataBuilder<User>(gridParam, 
			new JpaGridDataHandler<User>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				dumpParams(request);
				GridDataQuery query = new GridDataQuery("from User e");
				return query;
			}
			@Override
			public Object[] extractResultFields(User o) {
				return new Object[]{"", o.getUsername(), 
					o.getEmail(), o.getFirstName(), o.getLastName(),
					o.isEnabled(), o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createUser(Model model){
		model.addAttribute(new User());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveUser(@Valid User user, BindingResult result, 
			@RequestParam(value="authorities", required=false) String[] authorities, Model model){
		if(result.hasErrors()){
			model.addAttribute(user);
			return VIEW_PREFIX+"/form";
		}
		try{
			user = userService.save(user, authorities);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			String msg = e.getMessage()!=null?e.getMessage():e.toString();
			result.addError(new ObjectError("user", msg));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editUser(@PathVariable("id") Long id, Model model){
		User user = userService.get(id);
		model.addAttribute(user);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteUser(@PathVariable("id") Long id){
		userService.delete(id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteUsers(@RequestParam("id") Long[] ids){
		userService.delete(ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewUser(@PathVariable("id") Long id, Model model){
		User user = userService.get(id);
		model.addAttribute(user);
		return VIEW_PREFIX+"/view";
	}
	
	@RequestMapping(value = "/pwd/{id}", method = {RequestMethod.GET})
	public String changePassword(@PathVariable("id") Long id, Model model){
		model.addAttribute("userId", id);
		return VIEW_PREFIX+"/password";
	}
	
	@RequestMapping(value = "/pwd/{id}", method = {RequestMethod.POST})
	public String updatePassword(@PathVariable("id") Long id, 
			@RequestParam("oldPassword") String oldPassword, 
			@RequestParam("newPassword") String newPassword, 
			@RequestParam("confirmPassword") String confirmPassword, Model model){
		model.addAttribute("userId", id);
		try{
			userService.changePassword(id, oldPassword, newPassword, confirmPassword);
			model.addAttribute("message", new Message(MessageType.success, "Your password has been updated"));
		}catch(Exception e){
			String msg = e.getMessage()!=null?e.getMessage():e.toString();
			model.addAttribute("message", new Message(MessageType.error, msg));
			return VIEW_PREFIX+"/password";
		}
		User user = userService.get(id);
		model.addAttribute(user);
		return VIEW_PREFIX+"/view";
	}
	
	@RequestMapping(value = "/generateApiKey", method = {RequestMethod.POST})
	public @ResponseBody String generateApiKey(){
		return userService.generateApiKey();
	}
	
}
