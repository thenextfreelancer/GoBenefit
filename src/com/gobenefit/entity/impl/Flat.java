package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.FLAT_TABLE;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = FLAT_TABLE)
public class Flat extends AbstractEntity {

	private static final long serialVersionUID = 5726402781711982584L;

	@Column(name = "FLAT_NO")
	private Integer flatNumber;

	@Column(name = "FLOOR_NO")
	private Integer floorNumber;

	@Column(name = "PREFIX", length = 32)
	private String prefix;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APARTMENT_ID")
	private Apartment apartment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOWER_ID")
	private Tower tower;

	private transient Set<User> users;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Tower getTower() {
		return tower;
	}

	public void setTower(Tower tower) {
		this.tower = tower;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Integer getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(Integer flatNumber) {
		this.flatNumber = flatNumber;
	}

	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

}
