package com.siberhus.mailberry.model.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
public abstract class SpringAuditableModel<ID extends Serializable> extends AbstractAuditableModel<ID> {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	protected String _createdBy;
	@Transient
	protected Date _createdAt;
	
	@PostLoad
	protected void postLoad(){
		this._createdBy = getCreatedBy();
		this._createdAt = getCreatedAt();
	}
	
	@PrePersist
	protected void prePersist(){
		setCreatedBy(getPrincipalName());
		setCreatedAt(new Date());
	}
	
	@PreUpdate
	protected void preUpdate(){
		setCreatedBy(_createdBy);
		setCreatedAt(_createdAt);
		setLastModifiedBy(getPrincipalName());
		setLastModifiedAt(new Date());
	}
	
	
	protected String getPrincipalName(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if(auth!=null){
			return auth.getName();
		}
		return null;
	}
	
}
