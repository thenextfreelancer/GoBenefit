package com.gobenefit.entity.impl;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gobenefit.web.serialization.JsonDateSerializer;

@SuppressWarnings("serial")
public class UserAuthenticationToken implements Serializable {

	private String token;

	private Date issueDate;

	private Date expiryDate;

	private User user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
