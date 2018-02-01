package com.gobenefit.web.api.rest.impl.mixin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;

public abstract class EventBookingMixin {

	@JsonIgnore
	public abstract Date getApprovalDate();

	@JsonIgnore
	public abstract Date getRejectionDate();

	@JsonIgnore
	public abstract User getRejectedBy();

	@JsonIgnore
	public abstract User getApprovedBy();

	@JsonIgnore
	public abstract Flat getFlat();

}
