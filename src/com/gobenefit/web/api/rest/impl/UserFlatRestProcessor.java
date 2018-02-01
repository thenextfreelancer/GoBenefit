package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;
import static com.gobenefit.util.LookupCodeConstants.USER_STATUS_PENDING_RWA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.service.FlatService;
import com.gobenefit.service.UserFlatService;
import com.gobenefit.service.UserService;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.UserFlatMixin;

@Path("userflats")
@PermitAll
public class UserFlatRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserFlatRestProcessor.class);

	@Autowired
	UserFlatService userFlatService;

	@Autowired
	UserService userService;

	@Autowired
	FlatService flatService;

	@PUT
	@Path("approve")
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response approveRegistration(@Context UriInfo ui, @Context HttpHeaders hh, String data)
			throws WebApplicationException {
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			UserFlat userFlat = (UserFlat) SerializationUtils.deserialize(data, UserFlat.class, mapper);
			LOGGER.debug("Request recieved to approve the flat to the user:" + userFlat.getUserId());
			userFlatService.approve(userFlat);
		} catch (Exception e) {
			LOGGER.error("Error occurred while approve the flat to the user: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(null).build();
	}

	@PUT
	@Path("decline")
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response declineRegistration(@Context UriInfo ui, @Context HttpHeaders hh, String data)
			throws WebApplicationException {
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			UserFlat userFlat = (UserFlat) SerializationUtils.deserialize(data, UserFlat.class, mapper);
			LOGGER.debug("Request recieved to decline the flat to the user:" + userFlat.getUserId());
			userFlatService.decline(userFlat);
		} catch (Exception e) {
			LOGGER.error("Error occurred while decline the flat to the user: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(null).build();
	}

	@Override
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		String response = null;
		try {
			LOGGER.info("Request recieved to create the new user.");
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the request json to user");
			UserFlat userFlat = (UserFlat) SerializationUtils.deserialize(data, UserFlat.class, mapper);
			User user = userService.getUserByUsername(userFlat.getEmailId());
			userFlat.setStatus(USER_STATUS_PENDING_RWA);
			userFlat.setUserId(user.getId());
			userFlatService.createEntity(userFlat);
			response = SerializationUtils.serialize(userFlat, mapper);
		} catch (Exception e) {
			LOGGER.error("Error occurred while creating the user: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response getEntities(UriInfo ui, HttpHeaders hh) throws WebApplicationException {
		String response = null;
		try {
			Map<String, Object> criteriaMap = getQueryParameterMap(ui);
			List<UserFlat> userFlatList = userFlatService.getApartmentFlats(criteriaMap);
			if (!userFlatList.isEmpty()) {
				Map<Long, List<UserFlat>> userIdMappedMap = new HashMap<>();
				Map<Long, List<UserFlat>> flatIdMappedMap = new HashMap<>();
				for (UserFlat userFlat : userFlatList) {
					List<UserFlat> userIdFlatList = userIdMappedMap.get(userFlat.getUserId());
					if (userIdFlatList == null) {
						userIdFlatList = new ArrayList<>();
						userIdMappedMap.put(userFlat.getUserId(), userIdFlatList);
					}
					userIdFlatList.add(userFlat);

					List<UserFlat> flatsIdList = userIdMappedMap.get(userFlat.getFlatId());
					if (flatsIdList == null) {
						flatsIdList = new ArrayList<>();
						flatIdMappedMap.put(userFlat.getFlatId(), flatsIdList);
					}
					userIdFlatList.add(userFlat);
				}
				List<User> userList = userService.getUsersByIdList(userIdMappedMap.keySet());
				List<Flat> flatList = flatService.getFlatByIdList(flatIdMappedMap.keySet());
				for (User user : userList) {
					List<UserFlat> userFlats = userIdMappedMap.get(user.getId());
					String imagePath = user.getImagePath();
					for (UserFlat userFlat : userFlats) {
						userFlat.setEmailId(user.getEmailId());
						userFlat.setImagePath(imagePath);
						userFlat.setMobileNo(user.getMobileNo());
						userFlat.setFirstName(user.getFirstName());
						userFlat.setLastName(user.getLastName());
					}
				}
				for (Flat flat : flatList) {
					List<UserFlat> userFlats = flatIdMappedMap.get(flat.getId());
					for (UserFlat userFlat : userFlats) {
						userFlat.setFlatNumber(flat.getFlatNumber());
					}
				}
				Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
				mixinMap.put(UserFlat.class, UserFlatMixin.class);
				response = SerializationUtils.serialize(userFlatList, mixinMap, false);
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while retriving the flat to the user: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}
}
