package com.siberhus.mailberry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siberhus.mailberry.controller.pojo.ResultResponse;

@Controller
@RequestMapping(RestfulApiController.PATH)
public class RestfulApiController {

	public final static String PATH = "/api/rest";
	
//	public ResultResponse subscribe(){
//		ResultResponse result = new ResultResponse();
//		
//	}
}
