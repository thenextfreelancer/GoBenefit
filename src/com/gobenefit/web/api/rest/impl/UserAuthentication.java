package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.AUTHENTICATION_SCHEME;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserAuthenticationToken;
import com.gobenefit.service.FlatService;
import com.gobenefit.service.UserService;
import com.gobenefit.util.HttpUtils;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.web.api.rest.impl.mixin.ApartmentMixin;
import com.gobenefit.web.api.rest.impl.mixin.FlatMixin;

@Path("/authentication")
public class UserAuthentication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentFacilityRestProcessor.class);

	@Autowired
	UserService userService;

	@Autowired
	FlatService flatService;

	@PermitAll
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@Context HttpHeaders header, @FormParam("username") String username,
			@FormParam("password") String password) {
		LOGGER.info("Request recieved for user authentication: " + username);
		String response = null;
		try {
			UserAuthenticationToken token = userService.authenticateUser(username, password);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			mixinMap.put(Flat.class, FlatMixin.class);
			response = SerializationUtils.serialize(token, mixinMap, true);
			LOGGER.info("User(" + username + ") authenticated successfully.");
			return Response.ok(response).build();
		} catch (SecurityException se) {
			Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(
					"Error occurred while authenticating the user: " + username + ", Error Message: " + e.getMessage(),
					e);
			throwWebApplicationException(e);
		}
		return Response.status(Response.Status.BAD_REQUEST).build();

	}

	@DELETE
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response logout(@Context HttpHeaders header) {
		LOGGER.info("Request recieved to logout the user");
		try {
			final List<String> authorization = header.getRequestHeaders().get(HttpHeaders.AUTHORIZATION);
			final String accessToken = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			HttpUtils.removeUserToken(accessToken);
			LOGGER.info("User logged out successfully.");
			return Response.noContent().build();
		} catch (SecurityException se) {
			Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error("Error occurred while logout the user. Error Message: " + e.getMessage(), e);
			throwWebApplicationException(e);
		}
		return Response.status(Response.Status.BAD_REQUEST).build();

	}

	@PUT
	@Path("/{id:[0-9]}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response setCurrentUserApartment(@Context HttpHeaders header, @PathParam("id") Long apartmentId) {
		LOGGER.info("Request recieved to set the apartment for the user the user");
		String response = null;
		try {
			final List<String> authorization = header.getRequestHeaders().get(HttpHeaders.AUTHORIZATION);
			final String accessToken = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			UserAuthenticationToken userToken = HttpUtils.getUserAuthenticationToken(accessToken);
			if (userToken != null && apartmentId != null) {
				User user = userToken.getUser();
				List<Apartment> apartments = user.getApartments();
				for (Apartment apartment : apartments) {
					if (apartment.getId().equals(apartmentId)) {
						Map<String, Object> criteriaMap = new HashMap<>();
						user.setCurrentApartment(apartment);
						user.setRole(userService.getUserApartmentRole(user.getId(), apartmentId));
						criteriaMap.put(APARTMENT_ID, apartmentId);
						criteriaMap.put(USER_ID, user.getId());
						user.setFlats(flatService.getUsersApartmentFlats(criteriaMap));
						break;
					}
				}
				Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
				mixinMap.put(Flat.class, FlatMixin.class);
				mixinMap.put(Apartment.class, ApartmentMixin.class);
				response = SerializationUtils.serialize(userToken, mixinMap, true);
				return Response.ok(response).build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while logout the user. Error Message: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}

	}

}
