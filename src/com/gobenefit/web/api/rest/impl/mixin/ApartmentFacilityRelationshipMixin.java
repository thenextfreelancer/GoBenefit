package com.gobenefit.web.api.rest.impl.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Apartment;

public abstract class ApartmentFacilityRelationshipMixin {

	@JsonIgnore
	public abstract Apartment getApartment();

}
