package com.siberhus.mailberry.controller.pojo;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.service.ConfigurationService;

public class ConfigBase {

	@NotNull @Size(min=3, max=2048)
	private String tmpDir;

	@NotNull @Size(min=1, max=2048)
	private String serverUrl;
	
	public ConfigBase() {
	}

	public ConfigBase(ConfigurationService config) {
		tmpDir = config.getValueAsString(Config.TMP_DIR,
				System.getProperty("java.io.tmpdir"));
		serverUrl = config.getValueAsString(Config.SERVER_URL,
				"http://localhost:8080");
	}

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public static void main(String[] args) throws Exception {
		URL url = new URL("https://www.google.coms/sda");
	    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	    System.out.println("Response Message is " + httpCon.getResponseCode());
	    httpCon.disconnect();
	}
}
