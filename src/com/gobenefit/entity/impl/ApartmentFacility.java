package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.APARTMENT_FACILITY_TABLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = APARTMENT_FACILITY_TABLE)
public class ApartmentFacility implements com.gobenefit.entity.Entity {

	private static final long serialVersionUID = -6252929887221005487L;

	@Id
	@Column(name = "CODE", length = 8)
	private String code;

	@Column(name = "NAME", length = 64, nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", length = 256)
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

}