package com.siberhus.mailberry.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.service.TrackingService;

@Controller
@RequestMapping(TrackingsController.PATH)
public class TrackingsController extends BaseController {
	
	public final static String PATH = "/t";
//	protected final static String VIEW_PREFIX = "pages/email";
	
	@Inject
	private TrackingService trackingService; 
	
	/**
	 * 
	 * Example: http://localhost:8080/mailberry/tracking/123/24566/aBsk34dp/open.png
	 * 
	 * @param campaignId
	 * @param subscriberId
	 * @param token
	 * @param request
	 */
	@RequestMapping(value="/{campaignId}/{subscriberId}/{token}/open.png", method=RequestMethod.GET)
	public ResponseEntity<String> trackOpen(@PathVariable("campaignId") Long campaignId, @PathVariable("subscriberId") Long subscriberId,
			@PathVariable("token") String token, HttpServletRequest request){
		
		trackingService.setOpenTime(campaignId, subscriberId, token, request);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * Note: IE8's maximum URL length is 2083 chars
	 * Example: http://localhost:8080/mailberry/tracking/click/123/aBsk34dp/http%3A%2F%2Fwww.siberhus.com%2Fmailberry%2Fpromotion
	 * 
	 * @param campaignId
	 * @param token
	 * @param url
	 * @param request
	 * @throws IOException 
	 */
	@RequestMapping(value="/redirect/{campaignId}/{subscriberId}/{token}", method=RequestMethod.GET)
	public void trackClickstream(@PathVariable("campaignId") Long campaignId, @PathVariable("subscriberId") Long subscriberId,
			@PathVariable("token") String token, @RequestParam("url") String url, 
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		url = trackingService.updateClickstream(campaignId, subscriberId, token, request, url);
		
		response.sendRedirect(url);
		
	}
	
	@RequestMapping(value="/unsubscribe/{campaignId}/{subscriberId}/{token}", method=RequestMethod.GET)
	public ResponseEntity<String> setOptOut(@PathVariable("campaignId") Long campaignId, @PathVariable("subscriberId") Long subscriberId,
			@PathVariable("token") String token, 
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		Subscriber subscriber = null;
		try{
			subscriber = trackingService.setOptOut(campaignId, subscriberId, token, request);
			if(subscriber==null){
				return new ResponseEntity<String>("Wrong security code", HttpStatus.UNAUTHORIZED);
			}
			String optOutConfigPage = config.getValueAsString(Config.Subscriber.OptOut.PAGE_CONFIG);
			if(optOutConfigPage!=null){
				//go to config page
			}
			String optOutSuccessPage = config.getValueAsString(Config.Subscriber.OptOut.PAGE_SUCCESS);
			if(optOutSuccessPage!=null){
				log.debug("Redirecting to: {}", optOutSuccessPage);
				response.sendRedirect(optOutSuccessPage);
			}
			return new ResponseEntity<String>("Unsubscribed '"+subscriber.getEmail()
				+"' from list '"+subscriber.getList()+"'", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/rsvp/{campaignId}/{subscriberId}/{token}/{status}", method=RequestMethod.GET)
	public ResponseEntity<String> setRsvpStatus(@PathVariable("campaignId") Long campaignId, @PathVariable("subscriberId") Long subscriberId,
			@PathVariable("token") String token, @PathVariable("status") String status,  
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		try{
			trackingService.setRsvpStatus(campaignId, subscriberId, token, request, status);
			String rsvpSuccessPage = config.getValueAsString(Config.Subscriber.RSVP.PAGE_SUCCESS);
			if(rsvpSuccessPage!=null){
				log.debug("Redirecting to: {}", rsvpSuccessPage);
				response.sendRedirect(rsvpSuccessPage);
			}
			return new ResponseEntity<String>("Thank you for your response", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
