package com.gobenefit.web.api.rest.impl.mixin;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gobenefit.entity.impl.ServiceComment;

public abstract class ServiceRequestMixin {

	@JsonIgnore
	public abstract Set<ServiceComment> getServiceComments();

}
