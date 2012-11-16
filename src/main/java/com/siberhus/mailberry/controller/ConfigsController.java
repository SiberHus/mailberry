package com.siberhus.mailberry.controller;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.controller.pojo.ConfigBase;
import com.siberhus.mailberry.controller.pojo.ConfigEmail;
import com.siberhus.mailberry.controller.pojo.ConfigFormat;
import com.siberhus.mailberry.controller.pojo.ConfigSubscriber;
import com.siberhus.mailberry.controller.pojo.ConfigUi;
import com.siberhus.mailberry.controller.pojo.ResultResponse;

@Controller
@RequestMapping(ConfigsController.PATH)
public class ConfigsController extends BaseController {
	
	public final static String PATH = "/settings/configs";
	protected final static String VIEW_PREFIX = "pages/configs";
	
	@RequestMapping(value={"/"},method=RequestMethod.GET)
	public String index(Model model){
		
		ConfigBase baseConfig = new ConfigBase(config);
		model.addAttribute("base", baseConfig);
		
		ConfigUi uiConfig = new ConfigUi(config);
		model.addAttribute("ui", uiConfig);
		
		ConfigSubscriber subConfig = new ConfigSubscriber(config);
		model.addAttribute("subscriber", subConfig);
		
		ConfigFormat fmtConfig = new ConfigFormat(config);
		model.addAttribute("format", fmtConfig);
		
		ConfigEmail emailConfig = new ConfigEmail(config);
		model.addAttribute("email", emailConfig);
		
		return VIEW_PREFIX+"/index";
	}
	
	@RequestMapping(value={"/base"},method=RequestMethod.POST)
	public @ResponseBody ResultResponse saveBaseConfig(@ModelAttribute("base") @Valid ConfigBase baseConfig, 
			BindingResult result, Model model){
		
		ResultResponse response = new ResultResponse();
		String tmpDir = baseConfig.getTmpDir();
		String serverUrl = baseConfig.getServerUrl();
		if(!isAccessibleFile(tmpDir)){
			result.addError(new FieldError("base", "tmpDir", tmpDir, false, 
					new String[]{"error.fileDoesNotExist"}, new String[]{tmpDir}, "File does not exist"));
		}
		if(!isAccessibleUrl(serverUrl)){
			result.addError(new FieldError("base", "serverUrl", serverUrl, false, 
					new String[]{"error.inaccessibleUrl"}, new String[]{serverUrl}, "URL cannot be accessed"));
		}
		if(result.hasErrors()){
			setErrors(result, response);
			return response;
		}
		
		config.setValue(Config.TMP_DIR, String.class, tmpDir);
		config.setValue(Config.SERVER_URL, String.class, serverUrl);
		servletContext.setAttribute("serverUrl", serverUrl);
		
		return response.setValue("OK");
	}
	
	
	@RequestMapping(value={"/ui"},method=RequestMethod.POST)
	public @ResponseBody ResultResponse saveUiConfig(@ModelAttribute("ui") @Valid ConfigUi uiConfig, 
			BindingResult result, Model model){
		
		ResultResponse response = new ResultResponse();
		
		if(result.hasErrors()){
			setErrors(result, response);
			return response;
		}
		
		config.setValue(Config.UI.DataGrid.ROW_NUM, 
				Integer.class, uiConfig.getDataGrid().getRowNum());
		config.setValue(Config.UI.DataGrid.ROW_LIST, 
				String.class, uiConfig.getDataGrid().getRowList());
		config.setValue(Config.UI.DataGrid.HEIGHT, 
				Integer.class, uiConfig.getDataGrid().getHeight());
		
		servletContext.setAttribute("gridHeight", uiConfig.getDataGrid().getHeight());
		servletContext.setAttribute("gridRowNum", uiConfig.getDataGrid().getRowNum());
		servletContext.setAttribute("gridRowList", uiConfig.getDataGrid().getRowList());
		
		config.setValue(Config.UI.Calendar.CAMPAIGN_BACKGROUND_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignBgColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_BORDER_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignBdColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_TEXT_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignTxtColor());
		
		config.setValue(Config.UI.Calendar.CAMPAIGN_INP_BACKGROUND_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignInpBgColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_INP_BORDER_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignInpBdColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_INP_TEXT_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignInpTxtColor());
		
		config.setValue(Config.UI.Calendar.CAMPAIGN_SEN_BACKGROUND_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSenBgColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_SEN_BORDER_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSenBdColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_SEN_TEXT_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSenTxtColor());
		
		config.setValue(Config.UI.Calendar.CAMPAIGN_CAN_BACKGROUND_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignCanBgColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_CAN_BORDER_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignCanBdColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_CAN_TEXT_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignCanTxtColor());
		
		config.setValue(Config.UI.Calendar.CAMPAIGN_SCH_BACKGROUND_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSchBgColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_SCH_BORDER_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSchBdColor());
		config.setValue(Config.UI.Calendar.CAMPAIGN_SCH_TEXT_COLOR, 
				String.class, uiConfig.getCalendar().getCampaignSchTxtColor());
		
		
		return response.setValue("OK");
	}
	
	@RequestMapping(value={"/subscriber"},method=RequestMethod.POST)
	public @ResponseBody ResultResponse saveSubscriberConfig(@ModelAttribute("subscriber") @Valid ConfigSubscriber subConfig, 
			BindingResult result, Model model){
		
		ResultResponse response = new ResultResponse();
		if(result.hasErrors()){
			setErrors(result, response);
			return response;
		}
		
		config.setValue(Config.Subscriber.Import.SOURCE_DIR, 
				String.class, subConfig.getFileImport().getSourceDir());
		config.setValue(Config.Subscriber.Import.ERROR_DIR, 
				String.class, subConfig.getFileImport().getErrorDir());
		
		config.setValue(Config.Subscriber.OptOut.PAGE_SUCCESS, 
				String.class, subConfig.getOptOut().getPageSuccess());
		config.setValue(Config.Subscriber.OptOut.PAGE_CONFIG, 
				String.class, subConfig.getOptOut().getPageConfig());
		config.setValue(Config.Subscriber.OptOut.LINK_NAME, 
				String.class, subConfig.getOptOut().getLinkName());
		
		config.setValue(Config.Subscriber.RSVP.PAGE_SUCCESS, 
				String.class, subConfig.getRsvp().getPageSuccess());
		config.setValue(Config.Subscriber.RSVP.PAGE_CONFIG, 
				String.class, subConfig.getRsvp().getPageConfig());
		config.setValue(Config.Subscriber.RSVP.LINK_NAME_ATT, 
				String.class, subConfig.getRsvp().getLinkNameAtt());
		config.setValue(Config.Subscriber.RSVP.LINK_NAME_MAY, 
				String.class, subConfig.getRsvp().getLinkNameMay());
		config.setValue(Config.Subscriber.RSVP.LINK_NAME_DEC, 
				String.class, subConfig.getRsvp().getLinkNameDec());
		
		return response.setValue("OK");
	}
	
	@RequestMapping(value={"/format"},method=RequestMethod.POST)
	public @ResponseBody ResultResponse saveFormatConfig(@ModelAttribute("format") @Valid ConfigFormat fmtConfig, 
			BindingResult result, Model model){
		
		ResultResponse response = new ResultResponse();
		if(result.hasErrors()){
			setErrors(result, response);
			return response;
		}
		
		config.setValue(Config.Format.DISPLAY_TIMESTAMP, String.class, 
				fmtConfig.getDisplayTimestamp());
		config.setValue(Config.Format.DISPLAY_DATE, String.class, 
				fmtConfig.getDisplayDate());
		config.setValue(Config.Format.DISPLAY_TIME, String.class, 
				fmtConfig.getDisplayTime());
		
		config.setValue(Config.Format.INPUT_TIMESTAMP, String.class, 
				fmtConfig.getInputTimestamp());
		config.setValue(Config.Format.INPUT_DATE, String.class, 
				fmtConfig.getInputDate());
		config.setValue(Config.Format.INPUT_TIME, String.class, 
				fmtConfig.getInputTime());
		
		return response.setValue("OK");
	}
	
	@RequestMapping(value={"/email"},method=RequestMethod.POST)
	public @ResponseBody ResultResponse saveEmailConfig(@ModelAttribute("email") @Valid ConfigEmail emailConfig, 
			BindingResult result, Model model){
		
		ResultResponse response = new ResultResponse();
		
		String spamdHost = emailConfig.getSpamChecker().getHost();
		int spamdPort = emailConfig.getSpamChecker().getPort();
		
		Socket spamcSocket = null;
		try{
			spamcSocket = new Socket(spamdHost, spamdPort);
		}catch(Exception e){
			result.addError(new FieldError("email", "spamChecker.host", spamdHost, false, 
				new String[]{"error.socketFailure"}, new Object[]{spamdHost, spamdPort}, 
				"Socket Error - Service is not available"));
		}finally{
			if(spamcSocket!=null){
				try{ spamcSocket.close(); }catch(Exception e){}
			}
		}
		
		String velocityTemplatePath = emailConfig.getTemplate().getVelocityStorePath();
		if(!isAccessibleFile(velocityTemplatePath)){
			result.addError(new FieldError("email", "template.velocityStorePath", velocityTemplatePath, false, 
					new String[]{"error.fileDoesNotExist"}, new String[]{velocityTemplatePath}, 
					"File does not exist"));
		}
		if(result.hasErrors()){
			setErrors(result, response);
			return response;
		}
		
		config.setValue(Config.Email.MAIL_USER_AGENT, String.class, 
				emailConfig.getMailUserAgent());
		config.setValue(Config.Email.DEFAULT_FROM_EMAIL, String.class, 
				emailConfig.getDefaultFromEmail());
		config.setValue(Config.Email.DEFAULT_FROM_NAME, String.class, 
				emailConfig.getDefaultFromName());
		config.setValue(Config.Email.DEFAULT_REPLY_TO_EMAIL, String.class, 
				emailConfig.getDefaultReplyToEmail());
		
		config.setValue(Config.Email.Sending.PAUSE_BETWEEN_MESSAGES, Integer.class, 
				emailConfig.getSending().getPauseBetweenMessages());
		config.setValue(Config.Email.Sending.NUMBER_OF_THREADS, Integer.class, 
				emailConfig.getSending().getNumberOfThreads());
		config.setValue(Config.Email.Sending.MESSAGES_PER_CONNECTION, Integer.class, 
				emailConfig.getSending().getMessagesPerConnection());
		config.setValue(Config.Email.Sending.NUMBER_OF_ATTEMPTS, Integer.class, 
				emailConfig.getSending().getNumberOfAttempts());
		config.setValue(Config.Email.Sending.PAUSE_BETWEEN_ATTEMPTS, Integer.class, 
				emailConfig.getSending().getPauseBetweenAttempts());
		config.setValue(Config.Email.Sending.BLOCK_ON_FAIL, Boolean.class, 
				emailConfig.getSending().isBlockOnFail());
		config.setValue(Config.Email.Sending.TIMEOUT, Integer.class, 
				emailConfig.getSending().getTimeout());
		
		config.setValue(Config.Email.SpamChecker.HOST, String.class, spamdHost);
		config.setValue(Config.Email.SpamChecker.PORT, Integer.class, spamdPort);
		
		config.setValue(Config.Email.Template.VELOCITY_STORE_PATH, String.class, velocityTemplatePath);
		config.setValue(Config.Email.Template.VELOCITY_ENCODING, String.class, 
				emailConfig.getTemplate().getVelocityEncoding());
				
		return response.setValue("OK");
	}
	
	private void setErrors(BindingResult result, ResultResponse response){
		response.setError(true);
		for(FieldError fieldError: result.getFieldErrors()){
			response.addFieldError(fieldError.getField(), 
					fieldError.getDefaultMessage());
		}
	}
	
	private boolean isAccessibleFile(String file){
		return new File(file).exists();
	}
	
	private boolean isAccessibleUrl(String urlStr){
		URL url = null;
		HttpURLConnection httpCon = null;
		try{
			url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.disconnect();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
