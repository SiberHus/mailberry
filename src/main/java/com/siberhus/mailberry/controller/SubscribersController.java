package com.siberhus.mailberry.controller;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.exception.CumulativeException;
import com.siberhus.mailberry.exception.FieldValidationException;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.SubscriberListService;
import com.siberhus.mailberry.service.SubscriberService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(SubscribersController.PATH)
public class SubscribersController extends BaseController {
	
	public final static String PATH = "/data/subscribers";
	protected final static String VIEW_PREFIX = "pages/subscribers";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private SubscriberService subscriberService;
	
	@Inject
	private SubscriberListService listService;
	
	@RequestMapping({"/{listId}", "/{listId}/list"})
	public String index(@PathVariable("listId") Long listId, Model model, HttpServletRequest request){
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(request), listId);
		model.addAttribute(list);
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/{listId}/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listSubscribers(final HttpServletRequest request, 
		GridParam gridParam, @PathVariable("listId") final Long listId){
		
		GridDataBuilder<Subscriber> builder = new GridDataBuilder<Subscriber>(gridParam, 
			new JpaGridDataHandler<Subscriber>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from Subscriber e where e.list.id=? ");
				query.addParameter(listId);
				return query;
			}
			@Override
			public Object[] extractResultFields(Subscriber o) {
				int fieldNamesSize = o.getList().getFieldNames().size();
				Object values[] = new Object[5+fieldNamesSize];
				values[0] = ""; values[1] = o.getEmail();
				int j = 1;
				for(int i=0;i<fieldNamesSize;i++){
					values[++j] = "";
				}
				values[++j] = o.getStatus();
				values[++j] = o.getCreatedBy();
				values[++j] = o.getCreatedAt();
				return values;
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = {"/{listId}/add-entry", "/{listId}/create"} , method = {RequestMethod.GET})
	public String addEntry(@PathVariable("listId") Long listId, Model model, HttpServletRequest request){
		SubscriberList list = listService.get(
			SpringSecurityUtils.getNotAdminId(request), listId);
		Subscriber subscriber = new Subscriber();
		subscriber.setList(list);
		model.addAttribute(subscriber);
		return VIEW_PREFIX+"/entry";
	}
	
	@RequestMapping(value = {"/edit/{id}"} , method = {RequestMethod.GET})
	public String editEntry(@PathVariable("id") Long id, Model model){
		Subscriber subscriber = subscriberService.get(id);
		model.addAttribute(subscriber);
		return VIEW_PREFIX+"/entry";
	}
	
	@RequestMapping(value = {"/save-entry"} , method = {RequestMethod.POST})
	public String saveEntry(@Valid Subscriber subscriber, BindingResult result, 
			Model model, HttpServletRequest request){
		boolean newEntity = subscriber.isNew();
		if(result.hasErrors()){
			SubscriberList list = listService.get(
				SpringSecurityUtils.getNotAdminId(request), 
				subscriber.getList().getId());
			subscriber.setList(list);
			model.addAttribute(subscriber);
			return VIEW_PREFIX+"/entry";
		}
		try{
			subscriber = subscriberService.save(subscriber);
			FlashMap.setInfoMessage("Subscriber:"+subscriber.getEmail()+" was created successfully");
		}catch(CumulativeException e){
			for(FieldValidationException exception: e.getAll(FieldValidationException.class)){
				result.addError(exception.getObjectError());
				return VIEW_PREFIX+"/entry";
			}
		}catch(Exception e){
			result.addError(new ObjectError("subscriber", e.getMessage()));
			return VIEW_PREFIX+"/entry";
		}finally{
			model.addAttribute(subscriber);
		}
		if(newEntity){
			return "redirect:"+PATH+"/"+subscriber.getList().getId()+"/add-entry";
		}else{
			return "redirect:"+PATH+"/"+subscriber.getList().getId()+"/";
		}
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteEntry(@PathVariable("id") Long id){
		Subscriber subscriber = subscriberService.get(id);
		subscriberService.delete(subscriber.getId());
		return "redirect:"+PATH+"/"+subscriber.getList().getId();
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteEntries(@RequestParam("id") Long[] ids){
		subscriberService.delete(ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewEntry(@PathVariable("id") Long id, Model model){
		Subscriber subscriber = subscriberService.get(id);
		model.addAttribute(subscriber);
		return VIEW_PREFIX+"/view";
	}
	
}
