package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.FLAT_USER_RELATIONSHIP;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = FLAT_USER_RELATIONSHIP, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "FLAT_ID", "USER_ID", "ROLE" }) })
public class UserFlat extends AbstractEntity {

	private static final long serialVersionUID = 6751017798230522803L;

	@Column(name = "FLAT_ID", nullable = false)
	private Long flatId;

	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE")
	private Role role;

	@Column(name = "IS_DEFAULT_APARTMENT", nullable = false)
	private Boolean defaultApartment = false;

	@Column(name = "STATUS", columnDefinition = LOOKUP_CODE_REFERENCE)
	private String status;

	private transient String emailId;

	private transient String firstName;

	private transient String lastName;

	private transient String mobileNo;

	private transient Integer flatNumber;

	private transient String imagePath;

	public Long getFlatId() {
		return flatId;
	}

	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean getDefaultApartment() {
		return defaultApartment;
	}

	public void setDefaultApartment(Boolean defaultApartment) {
		this.defaultApartment = defaultApartment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(Integer flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
