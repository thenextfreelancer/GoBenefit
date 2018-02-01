package com.gobenefit.web.api.rest.impl.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class UserMixin {

	@JsonIgnore
	public abstract String getPassword();

}
