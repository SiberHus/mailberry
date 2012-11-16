package com.siberhus.mailberry.controller.widget;

import java.text.DecimalFormat;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.fakesmtp.FakeSmtpServer;
import com.siberhus.mailberry.controller.pojo.FakeSmtpParam;
import com.siberhus.mailberry.controller.pojo.FakeSmtpStatus;
import com.siberhus.mailberry.controller.pojo.ResultResponse;


@Controller
@RequestMapping(AdminWidgetsController.PATH)
public class AdminWidgetsController {
	
	private final Logger log = LoggerFactory.getLogger(AdminWidgetsController.class);
	
	public final static String PATH = "/widgets/admin";
	protected final static String VIEW_PREFIX = "pages/widgets/admin";
	
	private FakeSmtpServer fakeSmtpServer = new FakeSmtpServer();
	
	@RequestMapping({"/jvm-memory"})
	public String showJvmMemory(Model model){
		log.debug("Getting JVM memory info");
		Runtime runtime = Runtime.getRuntime();
		DecimalFormat fmt = new DecimalFormat("###,###");
		double free = runtime.freeMemory()/1048576;
		double total = runtime.totalMemory()/1048576;
		double used = total - free;
		double max = runtime.maxMemory()/1048576;
		
		//Returns the amount of free memory in the Java Virtual Machine.
		model.addAttribute("free", fmt.format(free));
		model.addAttribute("freePercent", (int)(free*100/max) );
		//Returns the total amount of memory in the Java virtual machine.
		model.addAttribute("total", fmt.format(total));
		model.addAttribute("totalPercent", (int)(total*100/max) );
		model.addAttribute("used", fmt.format(used));
		model.addAttribute("usedPercent", (int)(used*100/max) );
		//Returns the maximum amount of memory that the Java virtual machine will attempt to use.
		model.addAttribute("max", fmt.format(max));
		return VIEW_PREFIX+"/jvm_memory";
	}
	
	@RequestMapping(value={"/gc"}, method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> executeGc(Model model){
		log.debug("Calling Garbage Collector");
		System.gc();
		showJvmMemory(model);
		return model.asMap();
	}
	
	@RequestMapping({"/app-info"})
	public String showAppInfo(Model model){
		return VIEW_PREFIX+"/app_info";
	}
	
	@RequestMapping(value={"/fake-smtp-server"}, method=RequestMethod.GET)
	public String showFakeSmtpServerCtl(Model model){
		model.addAttribute("port", fakeSmtpServer.getPort());
		model.addAttribute("chanceOfError", fakeSmtpServer.getChanceOfError());
		model.addAttribute("stopped", fakeSmtpServer.isStopped());
		model.addAttribute("successes", fakeSmtpServer.getReceivedEmails());
		model.addAttribute("errors", fakeSmtpServer.getErrorEmails());
		return VIEW_PREFIX+"/fake_smtp_ctl";
	}
	
	@RequestMapping(value={"/fake-smtp-server"}, method=RequestMethod.POST)
	public @ResponseBody ResultResponse changeFakeSmtpServerSettings(@Valid FakeSmtpParam param, BindingResult result, Model model){
		log.debug("Change Fake SMTP Server Setting: {}", ToStringBuilder.reflectionToString(param));
		ResultResponse response = new ResultResponse();
		if(result.hasErrors()){
			response.setErrorDetail(result.getFieldError().getDefaultMessage());
			return response;
		}
		if(param.getCommand()==FakeSmtpParam.Command.Start){
			if(!fakeSmtpServer.isStopped()){
				response.setErrorDetail("Server is running");
				return response;
			}else{
				fakeSmtpServer.setPort(param.getPort());
				fakeSmtpServer.setChanceOfError(param.getChanceOfError());
				fakeSmtpServer.start();
			}
		}else{
			fakeSmtpServer.stop();
		}
		response.setValue("OK");
		return response;
	}
	
	@RequestMapping(value={"/fake-smtp-server/status"}, method=RequestMethod.GET)
	public @ResponseBody FakeSmtpStatus getFakeSmtpServerStatus(){
		FakeSmtpStatus status = new FakeSmtpStatus();
		status.setStopped(fakeSmtpServer.isStopped());
		status.setSuccesses(fakeSmtpServer.getReceivedEmails()-fakeSmtpServer.getErrorEmails());
		status.setErrors(fakeSmtpServer.getErrorEmails());
		return status;
	}
	
	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleUnexpectedError(Exception exception) {
		return "ERROR: "+exception.getMessage();
	}
}
