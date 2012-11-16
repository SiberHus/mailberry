package com.siberhus.mailberry.dao.pojo;

import java.io.Serializable;
import java.util.List;

public class ClientInfoStats implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<UaInfo> uaInfos;
	
	private List<OsInfo> osInfos;

	public List<UaInfo> getUaInfos() {
		return uaInfos;
	}

	public void setUaInfos(List<UaInfo> uaInfos) {
		this.uaInfos = uaInfos;
	}

	public List<OsInfo> getOsInfos() {
		return osInfos;
	}

	public void setOsInfos(List<OsInfo> osInfos) {
		this.osInfos = osInfos;
	}
	
}
