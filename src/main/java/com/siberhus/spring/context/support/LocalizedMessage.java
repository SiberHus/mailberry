package com.siberhus.spring.context.support;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="localized_messages", uniqueConstraints=
	@UniqueConstraint(columnNames={"user_locale","message_code"}))
@NamedQueries({
	@NamedQuery(name=LocalizedMessage.FIND_BY_LOCALE_AND_CODE, 
		query="from LocalizedMessage e where e.userLocale=:locale and e.messageCode=:code"),
	@NamedQuery(name=LocalizedMessage.FIND_BY_LANG_AND_CODE, 
		query="from LocalizedMessage e where e.userLanguage=:language and e.messageCode=:code"),
})
public class LocalizedMessage implements Serializable {
	
	public static final String FIND_BY_LOCALE_AND_CODE = "LocalizedMessage.findByLocaleAndCode";
	public static final String FIND_BY_LANG_AND_CODE = "LocalizedMessage.findByLangAndCode";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="user_locale", length=16, nullable=false)
	private String userLocale;
	
	@Column(name="user_language", length=16, nullable=false)
	private String userLanguage;
	
	@Column(name="message_code", length=256, nullable=false)
	private String messageCode;
	
	@Column(name="message_text", length=2048, nullable=false)
	private String messageText;
	
	public LocalizedMessage(){}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserLocale() {
		return userLocale;
	}
	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	public String getUserLanguage() {
		return userLanguage;
	}
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}
