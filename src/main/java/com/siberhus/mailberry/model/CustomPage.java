package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="custom_pages")
public class CustomPage extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static enum Visibility{
		Hidden, 
		Public, //anyone can access 
		//Token, //(unused) Token is required to access
		Secured //Authentication is required to access
	}
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@NotNull
	@Column(name="page_name", length=128, nullable=false, unique=true)
	private String name;
	
	@NotNull
	@Column(name="page_content", nullable=false, columnDefinition="TEXT")
	private String content;
	
	@Column(name="visit_count", nullable=false)
	private int visitCount = 0;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="visibility", length=8, nullable=false)
	private Visibility visibility = Visibility.Public;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	
}
