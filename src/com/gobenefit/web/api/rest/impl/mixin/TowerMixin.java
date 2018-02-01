package com.gobenefit.web.api.rest.impl.mixin;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Flat;

public abstract class TowerMixin {

	@JsonIgnore
	public abstract Apartment getApartment();

	@JsonIgnore
	public abstract Set<Flat> getFlats();

}
