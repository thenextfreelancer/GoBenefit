package com.gobenefit.web.api.rest.jersey.security.filter;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.web.RequestScope.getCurrentUser;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.gobenefit.entity.impl.User;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		Method resourceMethod = resourceInfo.getResourceMethod();
		Class<?> resourceClass = resourceInfo.getResourceClass();
		if (!resourceMethod.isAnnotationPresent(PermitAll.class)) {

			if (resourceClass.isAnnotationPresent(DenyAll.class) || resourceMethod.isAnnotationPresent(DenyAll.class)) {
				requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
			} else if (resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
				Set<String> methodRoles = extractRoles(resourceMethod);
				checkPermissions(methodRoles, requestContext);
			} else if (resourceClass.isAnnotationPresent(RolesAllowed.class)) {
				Set<String> classRoles = extractRoles(resourceClass);
				checkPermissions(classRoles, requestContext);
			}
		}
	}

	private Set<String> extractRoles(AnnotatedElement annotatedElement) {
		RolesAllowed rolesAnnotation = annotatedElement.getAnnotation(RolesAllowed.class);
		return new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
	}

	private void checkPermissions(Set<String> allowedRoles, ContainerRequestContext requestContext) {
		User authenticatedUser = getCurrentUser();
		if (!(allowedRoles.contains(ALL_AUTHETICATED_USER_ROLE) && authenticatedUser != null)
				&& (!allowedRoles.contains(authenticatedUser.getRole().getRole()))) {
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
		}
	}
}