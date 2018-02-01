package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.TOWER_TABLE;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = TOWER_TABLE)
public class Tower extends AbstractEntity {

	private static final long serialVersionUID = 6443330626789488304L;

	@Column(name = "NAME", length = 256, nullable = false)
	@Size(min = 1, max = 256)
	private String name;

	@Column(name = "FLAT_PREFIX", length = 4)
	private String flatPrefix;

	@Column(name = "FLAT_COUNT")
	Integer flatCount;

	@OneToMany(mappedBy = "tower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Flat> flats;

	@Column(name = "TOTAL_FLOOR")
	Integer totalFloor;

	@Column(name = "DEFAULT_TOWER")
	Boolean defaultTower = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APARTMENT_ID", updatable = false)
	private Apartment apartment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFlatCount() {
		return flatCount;
	}

	public void setFlatCount(Integer flatCount) {
		this.flatCount = flatCount;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Set<Flat> getFlats() {
		return flats;
	}

	public void setFlats(Set<Flat> flats) {
		this.flats = flats;
	}

	public Integer getTotalFloor() {
		return totalFloor;
	}

	public void setTotalFloor(Integer totalFloor) {
		this.totalFloor = totalFloor;
	}

	public String getFlatPrefix() {
		return flatPrefix;
	}

	public void setFlatPrefix(String flatPrefix) {
		this.flatPrefix = flatPrefix;
	}

	public Boolean getDefaultTower() {
		return defaultTower;
	}

	public void setDefaultTower(Boolean defaultTower) {
		this.defaultTower = defaultTower;
	}

}
