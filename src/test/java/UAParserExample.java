import java.io.IOException;

import cz.mallat.uasparser.CachingOnlineUpdateUASparser;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

public class UAParserExample {

	public static void main(String[] args) throws IOException {
		
		// cache file will be put to tmp dir
		UASparser p = new CachingOnlineUpdateUASparser();
		UserAgentInfo uai = p.parse("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1");
		System.out.println("type:" + uai.getTyp());
		System.out.println("ua_name:" + uai.getUaName());
		System.out.println("ua_family:" + uai.getUaFamily());
		System.out.println("ua_producer:" + uai.getUaCompany());
		System.out.println("os_name:" + uai.getOsName());
		System.out.println("os_family:" + uai.getOsFamily());
		System.out.println("os_icon:" + uai.getOsIcon());
		System.out.println("os_producer:" + uai.getOsCompany());

	}
}
