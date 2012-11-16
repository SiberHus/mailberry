package com.siberhus.spring.context.support;

import java.text.MessageFormat;
import java.util.Locale;

import javax.persistence.NoResultException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheJpaMessageSource extends JpaMessageSource {
	
	private Ehcache messageCache;
	
	@Override
	public void setMessage(String code, Locale locale, String text) {
		MessageKey key = new MessageKey(code, locale);
		MessageFormat messageFormat = null;
		if(text!=null) {
			messageFormat = new MessageFormat(text, locale);
		}else {
			messageFormat = new MessageFormat(code, locale);
		}
		messageCache.put(new Element(key, messageFormat));
	}
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageKey key = new MessageKey(code, locale);
		Element element = messageCache.get(key);
		MessageFormat messageFormat = null;
		if(element!=null){
			messageFormat = (MessageFormat)element.getValue();
		}
		if(messageFormat==null) {
			LocalizedMessage message = null;
			try{
				message = (LocalizedMessage)getEntityManager()
					.createNamedQuery(LocalizedMessage.FIND_BY_LOCALE_AND_CODE)
					.setParameter("locale", locale.toString())
					.setParameter("code", code).getSingleResult();
			}catch(NoResultException e){
				try{
					message = (LocalizedMessage)getEntityManager()
						.createNamedQuery(LocalizedMessage.FIND_BY_LANG_AND_CODE)
						.setParameter("language", locale.toString())
						.setParameter("code", code).getSingleResult();
				}catch(NoResultException e2){}
			}
			if(message!=null) {
				messageFormat = new MessageFormat(message.getMessageText(), locale);
			}else{
				messageFormat = new MessageFormat(code, locale);
			}
			messageCache.put(new Element(key, messageFormat));
			return messageFormat;
		}
		return messageFormat;
	}

	public Ehcache getMessageCache() {
		return messageCache;
	}

	public void setMessageCache(Ehcache messageCache) {
		this.messageCache = messageCache;
	}
	
}
