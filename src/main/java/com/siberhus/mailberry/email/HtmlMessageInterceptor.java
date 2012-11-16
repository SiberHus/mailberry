package com.siberhus.mailberry.email;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Alternative parser (looks better)
 * http://jericho.htmlparser.net/docs/index.html
 * 
 * @author hussachai
 *
 */
public class HtmlMessageInterceptor extends ParserCallback{

	private Logger log = LoggerFactory.getLogger(HtmlMessageInterceptor.class);
	
	private ParserDelegator parserDelegator = new ParserDelegator();
	private StringBuilder html = new StringBuilder();
	private boolean clickstream;
	private boolean openTracking;
	
	public HtmlMessageInterceptor(){}

	public boolean isClickstream() {
		return clickstream;
	}

	public void setClickstream(boolean clickstream) {
		this.clickstream = clickstream;
	}

	public boolean isOpenTracking() {
		return openTracking;
	}
	
	public void setOpenTracking(boolean openTracking) {
		this.openTracking = openTracking;
	}

	public StringBuilder parse(Reader reader, boolean ignoreCharSet) throws IOException{
		parserDelegator.parse(reader, this, ignoreCharSet);
		return html;
	}
	
	@Override
	public void handleText(final char[] data, final int pos) {
		html.append((StringUtils.join(ArrayUtils.toObject(data))));
	}
	
	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attribute,
			int pos) {
		html.append("<"+tag);
		Enumeration<?> attribNames = attribute.getAttributeNames();
		while(attribNames.hasMoreElements()){
			Object attribKeyObj = attribNames.nextElement();
			HTML.Attribute attribKey = null;
			Object attrib = attribute.getAttribute(attribKeyObj);
			if(clickstream){
				if(attribKeyObj instanceof HTML.Attribute){
					attribKey = (HTML.Attribute)attribKeyObj;
					if(HTML.Attribute.HREF.equals(attribKey)){
						if(!skipClickstream(attrib.toString())){
							try {
								html.append(" "+attribKeyObj+"=\"${_clickstream_}"
									+URLEncoder.encode(attrib.toString(),"UTF-8")+"\"");
								continue;
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						
					}
				}
			}
			html.append(" "+attribKeyObj+"=\""+attrib+"\"");
		}
		html.append(">");
		if(tag==Tag.BODY){
			if(openTracking){
				html.append("<img src=\"${_openTracking_}\" width=\"0\" height=\"0\"/>");
			}
		}
	}
	
	@Override
	public void handleEndTag(Tag t, final int pos) {
		html.append("</"+t+">");
	}

	@Override
	public void handleSimpleTag(Tag tag, MutableAttributeSet attribute,
			final int pos) {
		html.append("<"+tag);
		Enumeration<?> attribNames = attribute.getAttributeNames();
		while(attribNames.hasMoreElements()){
			Object attribKeyObj = attribNames.nextElement();
			HTML.Attribute attribKey = null;
			if(attribKey instanceof HTML.Attribute){
				attribKey = (HTML.Attribute)attribKeyObj;
			}
			Object attrib = attribute.getAttribute(attribKeyObj);
			html.append(" "+attribKeyObj+"=\""+attrib+"\"");
		}
		html.append("/>");
	}

	@Override
	public void handleComment(final char[] data, final int pos) {
		html.append("<!--"+StringUtils.join(ArrayUtils.toObject(data))+"-->");
	}
	
	@Override
	public void handleError(final java.lang.String errMsg, final int pos) {
		log.trace("html parsing error: {}", errMsg);
	}
	
	private final String IGNORED_HREF_KEYS[] = new String[]{
			"/redirect/", "/open.png", "/unsubscribe/", "/rsvp/"
		};
	
	private boolean skipClickstream(String href){
		for(String key: IGNORED_HREF_KEYS){
			if(href.contains(key)){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		String html = FileUtils.readFileToString(new File("resources/demo.html"));
		System.out.println(html);
		for(int i=0;i<10000;i++){
			HtmlMessageInterceptor li = new HtmlMessageInterceptor();
			li.setClickstream(true);
			li.setOpenTracking(true);
			StringReader reader = new StringReader(html);
			StringBuilder output = li.parse(reader, true);
			System.out.println(output!=null);
		}
	}
	
}
