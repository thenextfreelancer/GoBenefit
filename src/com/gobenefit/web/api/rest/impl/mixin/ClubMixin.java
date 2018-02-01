package com.gobenefit.web.api.rest.impl.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Apartment;

public abstract class ClubMixin {

	@JsonIgnore
	public abstract Apartment getApartment();

}
