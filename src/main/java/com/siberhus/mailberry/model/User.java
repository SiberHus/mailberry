package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="users")
public class User extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;

	@NotNull @Size(min=3, max=32) @Pattern(regexp="^[a-zA-Z][a-zA-Z0-9_]*")
	@Column(name="username", length=32, unique=true, nullable=false)
	private String username;
	
	@Size(min=6, max=64)
	@Column(name="password", length=128, nullable=false)
	private String password;
	
	@Size(min=6, max=64)
	@Transient
	private String password2;//confirm password
	
	@Size(min=32)
	@Column(name="api_key", length=64, nullable=false)
	private String apiKey;
	
	@Column(name="api_client_ip", length=64)
	private String apiClientIp;
	
	@NotNull @Email
	@Column(name="email", length=64, nullable=false, unique=true)
	private String email;
	
	@Size(min=3, max=32)
	@Column(name="first_name", length=32, nullable=false)
	private String firstName;
	
	@Size(min=3, max=32)
	@Column(name="last_name", length=32, nullable=false)
	private String lastName;
	
	@Column(name="enabled", nullable=false)
	private boolean enabled = true;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinTable(name="users_authorities", joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="auth_id", referencedColumnName="id"),
			uniqueConstraints={@UniqueConstraint(columnNames={"user_id","auth_id"})})
	private List<Authority> authorities = new ArrayList<Authority>();
	
	public User(){}
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public void addAuthority(Authority auth){
		auth.addUser(this);
		authorities.add(auth);
	}
	
	@Override
	public String toString(){
		return username;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public String getApiClientIp() {
		return apiClientIp;
	}

	public void setApiClientIp(String apiClientIp) {
		this.apiClientIp = apiClientIp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
}
