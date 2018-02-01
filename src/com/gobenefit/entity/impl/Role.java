package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.USER_ROLE_TABLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = USER_ROLE_TABLE)
public class Role implements com.gobenefit.entity.Entity {

	private static final long serialVersionUID = -308573824968931490L;
	@Id
	@Column(name = "ROLE", length = 32, nullable = false, updatable = false, unique = true)
	private String role;

	@Column(name = "NAME", length = 32, nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", length = 512)
	private String description;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active = false;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String getId() {
		return role;
	}

}
