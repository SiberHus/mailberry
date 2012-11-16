package com.siberhus.mailberry.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siberhus.mailberry.service.CustomPageService;

@Controller
public class CustomPageViewController extends BaseController {
	
	@Inject
	private CustomPageService customPageService;
	
	@RequestMapping({"/pages/{name}"})
	public String index(@PathVariable("name") String pageName, Model model){
		
		String content = customPageService.getContent(pageName);
		model.addAttribute("pageContent", content);
		model.addAttribute("pageName", pageName);
		return CustomPagesController.VIEW_PREFIX+"/content";
	}
	
	
}
