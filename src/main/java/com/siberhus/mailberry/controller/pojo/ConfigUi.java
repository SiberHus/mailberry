package com.siberhus.mailberry.controller.pojo;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;


public class ConfigUi implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Valid
	private DataGrid dataGrid;
	
	@Valid
	private Calendar calendar;
	
	public ConfigUi(){}
	public ConfigUi(ConfigurationService config){
		this.dataGrid = new DataGrid(config);
		this.calendar = new Calendar(config);
	}
	
	public DataGrid getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	public static class DataGrid implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		@Min(5) @Max(200)
		private int rowNum;
		
		@NotNull @Pattern(regexp="^[\\s\\d]+(?:[,][\\s\\d]+)+", message="numberListError")
		private String rowList;
		
		@Min(100) @Max(700)
		private int height;//pixel
		
		public DataGrid(){}
		public DataGrid(ConfigurationService config){
			this.rowNum = config.getValue(Config.UI.DataGrid.ROW_NUM, Integer.class, 15);
			this.rowList = config.getValueAsString(Config.UI.DataGrid.ROW_LIST, "10, 20, 30, 40, 50, 60, 70, 80, 90, 100");
			this.height = config.getValue(Config.UI.DataGrid.HEIGHT, Integer.class, 300);
		}
		
		public int getRowNum() {
			return rowNum;
		}
		public void setRowNum(int rowNum) {
			this.rowNum = rowNum;
		}
		public String getRowList() {
			return rowList;
		}
		public void setRowList(String rowList) {
			this.rowList = rowList;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
	}
	
	public static class Calendar implements Serializable{
		
		private static final long serialVersionUID = 1L;
		private static final String V_COLOR_MESSAGE = "colorRgbTypeMismatch";
		private static final String P_COLOR = "[0-9A-Fa-f]{6}";
		
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignBgColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignBdColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignTxtColor;
		
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignInpBgColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignInpBdColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignInpTxtColor;
		
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSenBgColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSenBdColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSenTxtColor;
		
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignCanBgColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignCanBdColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignCanTxtColor;
		
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSchBgColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSchBdColor;
		@NotNull @Pattern(regexp=P_COLOR, message=V_COLOR_MESSAGE)
		private String campaignSchTxtColor;
		
		public Calendar(){}
		public Calendar(ConfigurationService config){
			this.campaignBgColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_BACKGROUND_COLOR, "#B7F3EC");
			this.campaignBdColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_BORDER_COLOR, "#84D5CC");
			this.campaignTxtColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_TEXT_COLOR, "#2E4A47");
			
			this.campaignInpBgColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_INP_BACKGROUND_COLOR, "#ED220C");
			this.campaignInpBdColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_INP_BORDER_COLOR, "#C9200D");
			this.campaignInpTxtColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_INP_TEXT_COLOR, "#181818");
			
			this.campaignSenBgColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SEN_BACKGROUND_COLOR, "#A1F595");
			this.campaignSenBdColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SEN_BORDER_COLOR, "#7ED972");
			this.campaignSenTxtColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SEN_TEXT_COLOR, "#12300E");
			
			this.campaignCanBgColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_CAN_BACKGROUND_COLOR, "#DFDFD7");
			this.campaignCanBdColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_CAN_BORDER_COLOR, "#B7B7B0");
			this.campaignCanTxtColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_CAN_TEXT_COLOR, "#343432");
			
			this.campaignSchBgColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SCH_BACKGROUND_COLOR, "#EEAFF9");
			this.campaignSchBdColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SCH_BORDER_COLOR, "#CE84DB");
			this.campaignSchTxtColor = config.getValueAsString(Config.UI.Calendar
					.CAMPAIGN_SCH_TEXT_COLOR, "#2C1E2E");
		}
		
		public String getCampaignBgColor() {
			return campaignBgColor;
		}
		public void setCampaignBgColor(String campaignBgColor) {
			this.campaignBgColor = campaignBgColor;
		}
		public String getCampaignBdColor() {
			return campaignBdColor;
		}
		public void setCampaignBdColor(String campaignBdColor) {
			this.campaignBdColor = campaignBdColor;
		}
		public String getCampaignTxtColor() {
			return campaignTxtColor;
		}
		public void setCampaignTxtColor(String campaignTxtColor) {
			this.campaignTxtColor = campaignTxtColor;
		}
		public String getCampaignInpBgColor() {
			return campaignInpBgColor;
		}
		public void setCampaignInpBgColor(String campaignInpBgColor) {
			this.campaignInpBgColor = campaignInpBgColor;
		}
		public String getCampaignInpBdColor() {
			return campaignInpBdColor;
		}
		public void setCampaignInpBdColor(String campaignInpBdColor) {
			this.campaignInpBdColor = campaignInpBdColor;
		}
		public String getCampaignInpTxtColor() {
			return campaignInpTxtColor;
		}
		public void setCampaignInpTxtColor(String campaignInpTxtColor) {
			this.campaignInpTxtColor = campaignInpTxtColor;
		}
		public String getCampaignSenBgColor() {
			return campaignSenBgColor;
		}
		public void setCampaignSenBgColor(String campaignSenBgColor) {
			this.campaignSenBgColor = campaignSenBgColor;
		}
		public String getCampaignSenBdColor() {
			return campaignSenBdColor;
		}
		public void setCampaignSenBdColor(String campaignSenBdColor) {
			this.campaignSenBdColor = campaignSenBdColor;
		}
		public String getCampaignSenTxtColor() {
			return campaignSenTxtColor;
		}
		public void setCampaignSenTxtColor(String campaignSenTxtColor) {
			this.campaignSenTxtColor = campaignSenTxtColor;
		}
		public String getCampaignCanBgColor() {
			return campaignCanBgColor;
		}
		public void setCampaignCanBgColor(String campaignCanBgColor) {
			this.campaignCanBgColor = campaignCanBgColor;
		}
		public String getCampaignCanBdColor() {
			return campaignCanBdColor;
		}
		public void setCampaignCanBdColor(String campaignCanBdColor) {
			this.campaignCanBdColor = campaignCanBdColor;
		}
		public String getCampaignCanTxtColor() {
			return campaignCanTxtColor;
		}
		public void setCampaignCanTxtColor(String campaignCanTxtColor) {
			this.campaignCanTxtColor = campaignCanTxtColor;
		}
		public String getCampaignSchBgColor() {
			return campaignSchBgColor;
		}
		public void setCampaignSchBgColor(String campaignSchBgColor) {
			this.campaignSchBgColor = campaignSchBgColor;
		}
		public String getCampaignSchBdColor() {
			return campaignSchBdColor;
		}
		public void setCampaignSchBdColor(String campaignSchBdColor) {
			this.campaignSchBdColor = campaignSchBdColor;
		}
		public String getCampaignSchTxtColor() {
			return campaignSchTxtColor;
		}
		public void setCampaignSchTxtColor(String campaignSchTxtColor) {
			this.campaignSchTxtColor = campaignSchTxtColor;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(java.util.regex.Pattern.matches("^[\\s\\d]+(?:[,][\\s\\d]+)+", "1,32,33 ,1, 343"));
	}
	
}
