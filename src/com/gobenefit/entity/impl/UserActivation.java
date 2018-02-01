package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.USER_ACTIVATION_TABLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = USER_ACTIVATION_TABLE)
public class UserActivation extends AbstractEntity {

	private static final long serialVersionUID = -5458169442526992024L;

	@Column(name = "USER_TOKEN", nullable = false, length = 128)
	private String userToken;

	@Column(name = "EMAIL_ID", length = 255, nullable = false, unique = true)
	private String emailId;

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
