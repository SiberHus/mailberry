package com.siberhus.mailberry.controller;

import java.util.HashSet;
import java.util.List;

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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.controller.pojo.FieldValidatorFormBean;
import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.SubscriberListService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(SubscriberListsController.PATH)
public class SubscriberListsController extends BaseController {
	
	public final static String PATH = "/data/subscriber-lists";
	protected final static String VIEW_PREFIX = "pages/subscriber_lists";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private SubscriberListService listService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listSubscriberLists(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<SubscriberList> builder = new GridDataBuilder<SubscriberList>(gridParam, 
			new JpaGridDataHandler<SubscriberList>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from SubscriberList e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(SubscriberList o) {
				return new Object[]{"", o.getListName(), o.getSubscriberCount(),
					o.getStatus() ,o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = {"/create"} , method = {RequestMethod.GET})
	public String createSubscriberList(Model model){
		model.addAttribute(new SubscriberList());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = {"/save"} , method = {RequestMethod.POST})
	public String saveSubscriberList(@Valid SubscriberList list, BindingResult result, Model model){
		int uniqueNamesSize = new HashSet<String>(list.getFieldNames()).size();
		int namesSize = list.getFieldNames().size();
		if(uniqueNamesSize!=namesSize){
			result.addError(new FieldError("subscriberList", "fieldNames", "Field name must be unique"));
		}
		if(result.hasErrors()){
			model.addAttribute(list);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			list.setUser(user);
			list = listService.save(list);
		}catch(DataAccessException e){
			result.addError(new ObjectError("subscriberList", "SQL error: "+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/add-validators/"+list.getId();
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editSubscriberList(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		SubscriberList subscriberList = listService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(subscriberList);
		if(SubscriberList.Status.LOCKED.equals(subscriberList.getStatus())){
			return VIEW_PREFIX+"/locked";
		}
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteSubscriberList(@PathVariable("id") Long id, HttpServletRequest request){
		listService.delete(SpringSecurityUtils.getNotAdminId(request), id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteMessagetTemplates(@RequestParam("id") Long[] ids, HttpServletRequest request){
		listService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewSubscriberList(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(list);
		return VIEW_PREFIX+"/view";
	}
	
	@RequestMapping(value = "/unlock/{id}", method = {RequestMethod.POST})
	public String unlockSubscriberList(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		
		SubscriberList list = listService.unlock(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute(list);
		
		return VIEW_PREFIX+"/form";
	}
	
	/*================================================================*/
	
	@RequestMapping(value = "/add-validators/{id}" , method = {RequestMethod.GET})
	public String addValidators(@PathVariable("id") Long listId, Model model, HttpServletRequest request){
		SubscriberList list = listService.get(
				SpringSecurityUtils.getNotAdminId(request), listId);
		model.addAttribute("list", list);
		@SuppressWarnings("unchecked")
		List<FieldValidator> validators = (List<FieldValidator>)em
			.createQuery("from FieldValidator fv where fv.list=?")
			.setParameter(1, list).getResultList();
		FieldValidatorFormBean formBean = new FieldValidatorFormBean();
		formBean.setListId(list.getId());
		formBean.setFieldValidators(validators);
		model.addAttribute(formBean);
		return VIEW_PREFIX+"/validators";
	}
	
	@RequestMapping(value = "/save-validators" , method = {RequestMethod.POST})
	public String saveValidators(@Valid FieldValidatorFormBean formBean, BindingResult result, 
			Model model, HttpServletRequest request){
		if(result.hasErrors()){
			SubscriberList list = listService.get(
				SpringSecurityUtils.getNotAdminId(request), formBean.getListId());
			model.addAttribute("list", list);
			model.addAttribute(formBean);
			return VIEW_PREFIX+"/validators";
		}
		try{
			listService.setFieldValidators(
					SpringSecurityUtils.getNotAdminId(request), 
					formBean.getListId(), 
					formBean.getFieldValidators());
		}catch(DataAccessException e){
			result.addError(new ObjectError("fieldValidatorFormBean", 
					"SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/validators";
		}
		return "redirect:"+SubscribersController.PATH+"/"+formBean.getListId()+"/add-entry/";
	}
	
	@RequestMapping(value = {"/fieldnames/{id}"} , method = {RequestMethod.GET})
	public @ResponseBody List<String> getFieldNamesFilter(@PathVariable("id") Long id, 
			Model model, HttpServletRequest request){
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		return list.getFieldNames();
	}
	
}
