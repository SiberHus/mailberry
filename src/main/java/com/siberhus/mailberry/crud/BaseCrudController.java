package com.siberhus.mailberry.crud;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridParam;

public abstract class BaseCrudController <ENTITY extends com.siberhus.mailberry.model.base.Model<? extends Serializable>> {
	
	private static final Logger log = LoggerFactory.getLogger(BaseCrudController.class);
	
	protected abstract ENTITY createEntity();
	
	protected abstract String getViewPrefix();
	
	protected abstract GridData getEntities(final HttpServletRequest request, GridParam gridParam);
	
	protected abstract CrudService<ENTITY> getCrudService();
	
	@RequestMapping({"/", "/list"})
	public String index(){
		return getViewPrefix()+"list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData _getEntities(final HttpServletRequest request, GridParam gridParam){
		return getEntities(request, gridParam);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createEntity(Model model){
		model.addAttribute("command", createEntity());
		model.addAttribute("page", "create");
		return getViewPrefix()+"form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveEntity( @ModelAttribute("command") @Valid ENTITY entity, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			if(log.isDebugEnabled()){
				for(FieldError error: result.getFieldErrors()){
					log.debug("{} = {}", new Object[]{error.getField(),error.getDefaultMessage()});
				}
			}
			model.addAttribute("command", entity);
			return getViewPrefix()+"form";
		}
		getCrudService().save(entity);
		model.addAttribute("command", entity);
		return getViewPrefix()+"view";
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editEntity(@PathVariable("id") Long id, Model model){
		ENTITY entity = getCrudService().get(id);
		model.addAttribute("command", entity);
		model.addAttribute("page", "edit");
		return getViewPrefix()+"form";
	}
	
	@RequestMapping(value = "/update", method = {RequestMethod.POST})
	public String updateEntity(@ModelAttribute("command") @Valid ENTITY entity, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute("command", entity);
			return getViewPrefix()+"form";
		}
		entity = getCrudService().update(entity);
		model.addAttribute("command",entity);
		return getViewPrefix()+"view";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public @ResponseBody String deleteEntity(@PathVariable("id") Long id){
		ENTITY entity = getCrudService().delete(id);
		return entity+" deleted";
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewEntity(@PathVariable("id") Long id, Model model){
		ENTITY mv = getCrudService().get(id);
		model.addAttribute("command", mv);
		model.addAttribute("page", "view");
		return getViewPrefix()+"view";
	}
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	
}
