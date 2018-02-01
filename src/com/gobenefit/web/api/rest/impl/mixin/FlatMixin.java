package com.gobenefit.web.api.rest.impl.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Tower;

public abstract class FlatMixin {

	@JsonIgnore
	public abstract Apartment getApartment();

	@JsonIgnore
	public abstract Tower getTower();

}
