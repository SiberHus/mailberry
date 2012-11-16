package com.siberhus.mailberry.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Tracking implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static class RsvpStatus{
		public static final String ATTENDING = "ATT";
		public static final String MAYBE = "MAY";
		public static final String DECLINED = "DEC";
		public static final String VALUES[] = new String[]{ATTENDING, MAYBE, DECLINED};
	}
	
	private Long id = null;
	private Long subscriberId = null;
	private String emailDomain = null;
	private String securityToken = null;
	private boolean success = true;
	private Date sentTime = new Date();
	private boolean hardBounce = false;
	private boolean softBounce = false;
	private String errorMessage = null;
	private Date openTime = null;
	private boolean forwarded = false;
	private boolean optOut = false;
	private String rsvpStatus = null;
	private Date rsvpTime = null;
	private String uaRaw = null;
	private String uaType = null;
	private String uaName = null;
	private String uaVersion = null;
	private String uaIcon = null;
	private String osName = null;
	private String osFamily = null;
	private String osIcon = null;
	private String ipAddress = null;
	
	public Tracking(){}
	
	public Tracking(Subscriber subscriber){
		this.subscriberId = subscriber.getId();
		this.emailDomain = StringUtils.substringAfter(
			subscriber.getEmail(), "@");
	}
	
	public String toStrign(){
		return "Tracking["+id+",subscriber:"+subscriberId+"]";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getEmailDomain() {
		return emailDomain;
	}
	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Date getSentTime() {
		return sentTime;
	}
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	public boolean isHardBounce() {
		return hardBounce;
	}
	public void setHardBounce(boolean hardBounce) {
		this.hardBounce = hardBounce;
	}
	public boolean isSoftBounce() {
		return softBounce;
	}
	public void setSoftBounce(boolean softBounce) {
		this.softBounce = softBounce;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public boolean isForwarded() {
		return forwarded;
	}
	public void setForwarded(boolean forwarded) {
		this.forwarded = forwarded;
	}
	public boolean isOptOut() {
		return optOut;
	}
	public void setOptOut(boolean optOut) {
		this.optOut = optOut;
	}
	public String getRsvpStatus() {
		return rsvpStatus;
	}
	public void setRsvpStatus(String rsvpStatus) {
		if(rsvpStatus!=null){
			if(!ArrayUtils.contains(RsvpStatus.VALUES, rsvpStatus)){
				throw new IllegalArgumentException("rsvpStatus must be any of "
						+Arrays.toString(RsvpStatus.VALUES));
			}
			this.rsvpStatus = rsvpStatus;
		}
	}
	public Date getRsvpTime() {
		return rsvpTime;
	}
	public void setRsvpTime(Date rsvpTime) {
		this.rsvpTime = rsvpTime;
	}
	public String getUaRaw() {
		return uaRaw;
	}
	public void setUaRaw(String uaRaw) {
		this.uaRaw = uaRaw;
	}
	public String getUaType() {
		return uaType;
	}
	public void setUaType(String uaType) {
		this.uaType = uaType;
	}
	public String getUaName() {
		return uaName;
	}
	public void setUaName(String uaName) {
		this.uaName = uaName;
	}
	public String getUaVersion() {
		return uaVersion;
	}
	public void setUaVersion(String uaVersion) {
		this.uaVersion = uaVersion;
	}
	public String getUaIcon() {
		return uaIcon;
	}
	public void setUaIcon(String uaIcon) {
		this.uaIcon = uaIcon;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getOsFamily() {
		return osFamily;
	}
	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
	}
	public String getOsIcon() {
		return osIcon;
	}
	public void setOsIcon(String osIcon) {
		this.osIcon = osIcon;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
}
