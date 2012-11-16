package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="authorities")
public class Authority extends SpringAuditableModel<Long> {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull @Size(max=32)
	@Column(name="authority", length=32, unique=true, nullable=false)
	private String authority;
	
	@ManyToMany(mappedBy="authorities", fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	private List<User> users = new ArrayList<User>();
	
	public Authority(){}
	
	public Authority(String authority){
		this.authority = authority;
	}
	
	public void addUser(User user){
		users.add(user);
	}
	
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
//		if (!super.equals(obj))
//			return false;
		if (getClass() != obj.getClass())
			return false;
		Authority other = (Authority) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}
	
}
