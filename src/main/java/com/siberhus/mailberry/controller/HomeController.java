package com.siberhus.mailberry.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.siberhus.mailberry.exception.MailBerryException;

@Controller
public class HomeController {
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String showLoginPage(Map<String, Object> model, @RequestParam(required=false) boolean error) {
		model.put("error", error);
		return "pages/login";
	}
	
	@RequestMapping(value = {"/home"}, method = RequestMethod.GET)
	public String showHomePage() {
		return "pages/home";
	}
	
	@RequestMapping(value = {"/bug-report"}, method = RequestMethod.POST)
	public String showBugReportPage() {
		return "pages/bug_report";
	}
	
	@RequestMapping(value = {"/submit-bug"}, method = RequestMethod.POST)
	public String submitBug() {
		return "pages/bug_report";
	}
	
	@RequestMapping(value = {"/error"}, method = RequestMethod.GET)
	public void showErrorPage() {
		throw new MailBerryException("Trivial exception");
	}
}

