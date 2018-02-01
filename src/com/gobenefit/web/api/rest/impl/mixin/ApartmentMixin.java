package com.gobenefit.web.api.rest.impl.mixin;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.Club;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.entity.impl.User;

public abstract class ApartmentMixin {

	@JsonIgnore
	public abstract Set<Tower> getTowers();

	@JsonIgnore
	public abstract Set<Club> getClubs();

	@JsonIgnore
	public abstract Set<User> getUsers();

	@JsonIgnore
	public abstract Set<Flat> getFlats();

}
