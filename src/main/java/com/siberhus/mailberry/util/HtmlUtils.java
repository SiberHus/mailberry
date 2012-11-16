package com.siberhus.mailberry.util;

public class HtmlUtils extends org.springframework.web.util.HtmlUtils {
	
	public static String disableScript(String html){
		if(html==null) return null;
		html = html.replaceAll("<[sS][cC][rR][iI][pP][tT][^>]*?>[\\s\\S]*?<\\/[sS][cC][rR][iI][pP][tT]>", "[SCRIPT]");
		return html;
	}
	
}
