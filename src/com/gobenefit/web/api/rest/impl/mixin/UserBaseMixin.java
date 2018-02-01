package com.gobenefit.web.api.rest.impl.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class UserBaseMixin extends BaseMixin {

	@JsonIgnore
	public abstract String getStatus();

	@JsonIgnore
	public abstract String getPassword();

}
