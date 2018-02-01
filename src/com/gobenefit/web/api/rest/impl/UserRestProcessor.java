package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.AppConstant.STANDARD_USER_ROLE;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;
import static com.gobenefit.util.LookupCodeConstants.USER_STATUS_PENDING_RWA;
import static com.gobenefit.util.MessageConstant.INVALID_USER_TOKEN;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserActivation;
import com.gobenefit.service.UserService;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.UserMixin;

@Path("users")
@PermitAll
public class UserRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestProcessor.class);

	@Autowired
	UserService userService;

	@Override
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		String response = null;
		try {
			LOGGER.info("Request recieved to create the new user.");
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the request json to user");
			User user = (User) SerializationUtils.deserialize(data, User.class, mapper);
			UserActivation userActivation = userService.getActivationToken(user.getEmailId());
			if (userActivation != null && userActivation.getUserToken().equals(user.getToken())) {
				LOGGER.debug("User retreived after the deserialization.");
				if (user.getRole() == null) {
					Role role = new Role();
					role.setRole(STANDARD_USER_ROLE);
					user.setRole(role);
				}
				user.setStatus(USER_STATUS_PENDING_RWA);
				userService.createEntity(user);
				userService.deleteActivationToken(userActivation.getId());
				user.setToken(null);
				LOGGER.info("User(" + user.getEmailId() + ") created successfully.");
				LOGGER.debug("Getting json string from User(" + user.getEmailId() + ").");

				response = SerializationUtils.serialize(user, mapper);
				LOGGER.debug("User(" + user.getEmailId() + ") serialized successfully.");
			} else {
				throw throwWebApplicationException("You have entered invalid user activation token.");
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while creating the user: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Path("/validateToken")
	@PUT
	public Response validateToken(@Context UriInfo ui, @Context HttpHeaders hh, @QueryParam("token") String token) {
		LOGGER.info("Request recieved to validate the user token: " + token);
		try {
			if (StringUtils.isNotBlank(token)) {
				LOGGER.debug("Getting the username from token: " + token);
				String username = Base64.decodeBase64(token.getBytes()).toString();
				LOGGER.debug("Username(" + username + ") retrived from token: " + token);
				LOGGER.debug("Calling the service method to get the User from username: " + username);
				User user = userService.getUserByUsername(username);
				LOGGER.debug("Calling the service method to get the User from username: " + username);
				if (user != null) {
					LOGGER.debug("User retrieved successfully for user: " + username);
					user.setStatus(USER_STATUS_PENDING_RWA);
					user.setModifiedDate(new Date());
					LOGGER.debug("Updating the user(" + username + ") with status: " + USER_STATUS_PENDING_RWA);
					userService.updateEntity(user);
					LOGGER.debug("User(" + username + ") updated successfully.");
				} else {
					LOGGER.info("Invalid token recived for validation.");
					throw new SecurityException(INVALID_USER_TOKEN);
				}
			} else {
				LOGGER.info("Blank token recieved for validation");
				throw new SecurityException(INVALID_USER_TOKEN);
			}
		} catch (Exception e) {
			if (!(e instanceof SecurityException)) {
				LOGGER.error("Error occurred while validating the user token: " + token + " Error: " + e.getMessage(),
						e);
			}
			throw throwWebApplicationException(e);
		}
		return Response.ok().build();
	}

	@Path("/sendToken")
	@POST
	public Response sendToken(@Context UriInfo ui, @Context HttpHeaders hh, String data) {
		try {
			LOGGER.info("Request recieved to send the user token.");
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the request json to user");
			User user = (User) SerializationUtils.deserialize(data, User.class, mapper);
			if (StringUtils.isNotBlank(user.getEmailId().trim())) {
				User dbUser = userService.getUserByUsername(user.getEmailId().toLowerCase().trim());
				if (dbUser != null) {
					throw throwWebApplicationException("User already exist with email id: " + dbUser.getEmailId() + "");
				}
				LOGGER.debug("Sending the token to user email:" + user.getEmailId());
				userService.sendActivationToken(user);
				LOGGER.debug("Activation token sent to user email:" + user.getEmailId()
						+ " and sucessfully saven to the database.");
			} else {
				throw throwWebApplicationException("Email address cannot be empty.");
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while send the user token to email. Error: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok().build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		PagingSearchResult<User> userList = null;
		Map<String, Object> criteriaMap = getQueryParameterMap(uriInfo);
		Long apartmentId = RequestScope.getCurrentApartmentId();
		criteriaMap.put(APARTMENT_ID, apartmentId);
		try {
			LOGGER.debug("Calling service method to get the list of user for apartment Id: " + apartmentId);
			userList = userService.getEntities(criteriaMap);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(User.class, UserMixin.class);
			LOGGER.debug(userList.getEntityList().size() + " user(s) found for apartment Id: " + apartmentId);
			response = SerializationUtils.serialize(userList, mixinMap, false);
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntity(UriInfo ui, HttpHeaders hh, Long entityId) {
		String response = null;
		try {
			User user = userService.getEntity(entityId);
			user.setPassword(null);
			response = SerializationUtils.serialize(user);
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@PUT
	@Path("approve/{id:[0-9]}")
	public Response updateEntity(@Context UriInfo ui, @Context HttpHeaders hh, @PathParam("id") Long id)
			throws WebApplicationException {
		try {
			User user = userService.getEntity(id);
			// user.
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok().build();
	}
}
