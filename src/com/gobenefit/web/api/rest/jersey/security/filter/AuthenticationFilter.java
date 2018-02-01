package com.gobenefit.web.api.rest.jersey.security.filter;

import static com.gobenefit.util.AppConstant.AUTHENTICATION_SCHEME;
import static com.gobenefit.util.ApplicationUtils.throwUnauthorizedException;
import static com.gobenefit.util.HttpUtils.getUserAuthenticationToken;
import static com.gobenefit.util.HttpUtils.removeUserToken;
import static com.gobenefit.web.RequestScope.setCurrentUser;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gobenefit.entity.impl.UserAuthenticationToken;
import com.gobenefit.service.UserService;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Autowired
	UserService userService;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method resourceMethod = resourceInfo.getResourceMethod();
		Class<?> resourceClass = resourceInfo.getResourceClass();

		if (!resourceMethod.isAnnotationPresent(PermitAll.class)) {

			if (resourceMethod.isAnnotationPresent(DenyAll.class)) {
				throw throwUnauthorizedException();
			}

			if (resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
				authenticate(requestContext);
			} else if (!resourceClass.isAnnotationPresent(PermitAll.class)) {

				if (resourceClass.isAnnotationPresent(DenyAll.class)) {
					throw throwUnauthorizedException();
				}

				if (resourceClass.isAnnotationPresent(RolesAllowed.class)) {
					authenticate(requestContext);
				}

			} else {
				setupUserInfo(requestContext);
			}
		} else {
			setupUserInfo(requestContext);
		}
	}

	private void setupUserInfo(ContainerRequestContext requestContext) {

		final MultivaluedMap<String, String> headers = requestContext.getHeaders();

		final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);
		if (authorization != null && !authorization.isEmpty()) {
			final String accessToken = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			if (StringUtils.isNotBlank(accessToken)) {
				UserAuthenticationToken authenticationToken = getUserAuthenticationToken(accessToken);
				if (authenticationToken != null) {
					setCurrentUser(authenticationToken.getUser());
				}
			}
		}

	}

	private void authenticate(ContainerRequestContext requestContext) {
		final MultivaluedMap<String, String> headers = requestContext.getHeaders();

		final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);

		if (authorization == null || authorization.isEmpty()) {
			throw throwUnauthorizedException();
		}

		final String accessToken = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		UserAuthenticationToken authenticationToken = getUserAuthenticationToken(accessToken);
		Date currentDate = new Date();
		if (authenticationToken == null || currentDate.compareTo(authenticationToken.getExpiryDate()) > 0) {
			removeUserToken(accessToken);
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
			return;
		}
		setCurrentUser(authenticationToken.getUser());
	}

}