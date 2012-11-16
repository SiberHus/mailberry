package com.siberhus.mailberry.service.pojo;

import java.util.HashMap;
import java.util.Map;

import com.siberhus.mailberry.dao.pojo.ClientInfoStats;
import com.siberhus.mailberry.dao.pojo.OsInfo;
import com.siberhus.mailberry.dao.pojo.UaInfo;

public class GeneralizedClientInfoStats extends ClientInfoStats {
	
	private static final long serialVersionUID = 1L;

	private static final String UNKNOWN_VALUE = "Unknown";
	
	private Map<String, Integer> uaTypes;
	
	private Map<String, Integer> uaNames;
	
	private Map<String, Integer> osFamilies;
	
	private Map<String, Integer> osNames;
	
	public GeneralizedClientInfoStats(){}
	
	public GeneralizedClientInfoStats(ClientInfoStats infoStats){
		
		setUaInfos(infoStats.getUaInfos());
		setOsInfos(infoStats.getOsInfos());
		
		uaTypes = new HashMap<String, Integer>();
		uaNames = new HashMap<String, Integer>();
		osFamilies = new HashMap<String, Integer>();
		osNames = new HashMap<String, Integer>();
		
		if(infoStats.getUaInfos()!=null)
		for(UaInfo uaInfo : infoStats.getUaInfos()){
			String uaType = uaInfo.getType();
			if(uaType==null) uaType = UNKNOWN_VALUE;
			Integer tCounter = uaTypes.get(uaType);
			if(tCounter!=null){
				uaTypes.put(uaType, tCounter+uaInfo.getCount());
			}else{
				uaTypes.put(uaType, uaInfo.getCount());
			}
			String uaName = uaInfo.getName();
			if(uaName==null) uaName = UNKNOWN_VALUE;
			Integer nCounter = uaNames.get(uaName);
			if(nCounter!=null){
				uaNames.put(uaName, nCounter+uaInfo.getCount());
			}else{
				uaNames.put(uaName, uaInfo.getCount());
			}
		}
		
		if(infoStats.getOsInfos()!=null)
		for(OsInfo osInfo : infoStats.getOsInfos()){
			String osFamily = osInfo.getFamily();
			if(osFamily==null) osFamily = UNKNOWN_VALUE;
			Integer fCounter = osFamilies.get(osFamily);
			if(fCounter!=null){
				osFamilies.put(osFamily, fCounter+osInfo.getCount());
			}else{
				osFamilies.put(osFamily, osInfo.getCount());
			}
			String osName = osInfo.getName();
			if(osName==null) osName = UNKNOWN_VALUE;
			Integer nCounter = osNames.get(osName);
			if(nCounter!=null){
				osNames.put(osName, nCounter+osInfo.getCount());
			}else{
				osNames.put(osName, osInfo.getCount());
			}
		}
	}

	public Map<String, Integer> getUaTypes() {
		return uaTypes;
	}

	public void setUaTypes(Map<String, Integer> uaTypes) {
		this.uaTypes = uaTypes;
	}
	
	public Map<String, Integer> getUaNames() {
		return uaNames;
	}

	public void setUaNames(Map<String, Integer> uaNames) {
		this.uaNames = uaNames;
	}

	public Map<String, Integer> getOsFamilies() {
		return osFamilies;
	}

	public void setOsFamilies(Map<String, Integer> osFamilies) {
		this.osFamilies = osFamilies;
	}

	public Map<String, Integer> getOsNames() {
		return osNames;
	}

	public void setOsNames(Map<String, Integer> osNames) {
		this.osNames = osNames;
	}
	
}
