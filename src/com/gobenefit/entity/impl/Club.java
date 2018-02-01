package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.CLUB_TABLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = CLUB_TABLE)
public class Club extends AbstractEntity {

	private static final long serialVersionUID = 6443330626789488304L;

	@Column(name = "NAME", length = 128, nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", length = 512)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APARTMENT_ID", updatable = false)
	private Apartment apartment;

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

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

}
