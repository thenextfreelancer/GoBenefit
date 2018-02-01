package com.gobenefit.web.api.rest.impl.mixin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseMixin {

	@JsonIgnore
	public abstract Date getCreationDate();

	@JsonIgnore
	public abstract Date getModifiedDate();

}
