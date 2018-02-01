package com.gobenefit.web.api.rest.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class Application extends ResourceConfig {
	/**
	 * Register JAX-RS application components.
	 */
	public Application() {
		register(RequestContextFilter.class);
		packages("com.gobenefit.web.api.rest");
	}
}