package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.SERVICE_REQUEST_COMMENTS_TABLE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = SERVICE_REQUEST_COMMENTS_TABLE)
public class ServiceComment extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_COMMENT", length = 512)
	private String userComment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "SERVICE_REQUEST_ID", nullable = false)
	private ServiceRequest serviceComments;
}
