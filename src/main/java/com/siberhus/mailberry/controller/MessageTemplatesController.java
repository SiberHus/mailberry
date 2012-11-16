package com.siberhus.mailberry.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
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

import com.siberhus.mailberry.model.MessageTemplate;
import com.siberhus.mailberry.model.User;
import com.siberhus.mailberry.service.MessageTemplateService;
import com.siberhus.mailberry.service.UserService;
import com.siberhus.mailberry.ui.grid.GridData;
import com.siberhus.mailberry.ui.grid.GridDataBuilder;
import com.siberhus.mailberry.ui.grid.GridDataQuery;
import com.siberhus.mailberry.ui.grid.GridParam;
import com.siberhus.mailberry.ui.grid.handler.JpaGridDataHandler;
import com.siberhus.mailberry.util.HtmlUtils;
import com.siberhus.mailberry.util.SpringSecurityUtils;

@Controller
@RequestMapping(MessageTemplatesController.PATH)
public class MessageTemplatesController extends BaseController{
	
	public final static String PATH = "/messages/message-templates";
	protected final static String VIEW_PREFIX = "pages/message_templates";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private MessageTemplateService mtService;
	
	@Inject
	private ConversionService conversionService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping({"/", "/list"})
	public String index(Model model){
		return VIEW_PREFIX+"/list";
	}
	
	@RequestMapping(value = {"/list/data"}, method = RequestMethod.GET)
	public @ResponseBody GridData listMessageTemplates(final HttpServletRequest request, GridParam gridParam){
		GridDataBuilder<MessageTemplate> builder = new GridDataBuilder<MessageTemplate>(gridParam, 
			new JpaGridDataHandler<MessageTemplate>(em) {
			@Override
			public GridDataQuery createGridDataQuery(HttpServletRequest request) {
				GridDataQuery query = new GridDataQuery("from MessageTemplate e where 1=1 ");
				if(!SpringSecurityUtils.isAdmin()){
					query.appendQueryString("and e.user.id=? ");
					query.addParameter(SpringSecurityUtils.getUserId(request));
				}
				return query;
			}
			@Override
			public Object[] extractResultFields(MessageTemplate o) {
				return new Object[]{"", o.getTemplateName(), 
					StringUtils.abbreviate(o.getDescription(), 40)
					,o.getCreatedBy(), o.getCreatedAt()};
			}
			@Override
			public Object convertFieldValue(String fieldName, String value) {
				if("createdAt".equals(fieldName)){
					return conversionService.convert(value, Date.class);
				}
				return value;
			}
		});
		return builder.build(request);
	}
	
	@RequestMapping(value = "/create", method = {RequestMethod.GET})
	public String createMessageTemplate(Model model){
		model.addAttribute(new MessageTemplate());
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public String saveMessageTemplate(@Valid MessageTemplate messageTemplate, 
			BindingResult result, Model model){
		if(result.hasErrors()){
			model.addAttribute(messageTemplate);
			return VIEW_PREFIX+"/form";
		}
		try{
			User user = userService.findByUsername(SpringSecurityUtils.getPrincipalName());
			messageTemplate.setUser(user);
			messageTemplate = mtService.save(messageTemplate);
		}catch(DataAccessException e){
			result.addError(new ObjectError("messageTemplate", "SQL error:"+e.getMessage()));
			return VIEW_PREFIX+"/form";
		}
		return "redirect:"+PATH+"/view/"+messageTemplate.getId();
	}
	
	@RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
	public String editMessageTemplate(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MessageTemplate messageTemplate = mtService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		model.addAttribute( messageTemplate);
		return VIEW_PREFIX+"/form";
	}
	
	@RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
	public String deleteMessageTemplate(@PathVariable("id") Long id){
		mtService.delete(id);
		return "redirect:"+PATH+"/";
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ResponseEntity<String> deleteMessagetTemplates(@RequestParam("id") Long[] ids, HttpServletRequest request){
		mtService.delete(SpringSecurityUtils.getNotAdminId(request), ids);
		return new ResponseEntity<String>("Deleted successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/view/{id}", method = {RequestMethod.GET})
	public String viewMessageTemplate(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MessageTemplate messageTemplate = mtService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		String textContent = messageTemplate.getTemplateTextData();
		if(textContent!=null){
			textContent = HtmlUtils.htmlEscape(textContent);
//			textContent = textContent.replaceAll("\\n", "<br/>"); //use html <pre> tag instead
			textContent = HtmlUtils.disableScript(textContent);
		}
		model.addAttribute(messageTemplate);
		model.addAttribute("templateTextData", textContent);
		return VIEW_PREFIX+"/view";
	}
	
	@RequestMapping(value = "/html/{id}", method = {RequestMethod.GET})
	public String viewMessageTemplateHtmlContent(@PathVariable("id") Long id, Model model, HttpServletRequest request){
		MessageTemplate messageTemplate = mtService.get(
			SpringSecurityUtils.getNotAdminId(request), id);
		String htmlContent = messageTemplate.getTemplateHtmlData();
		htmlContent = HtmlUtils.disableScript(htmlContent);
		model.addAttribute("content", htmlContent);
		return VIEW_PREFIX+"/html_body";
	}
	
}
