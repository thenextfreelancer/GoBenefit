package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Club;
import com.gobenefit.service.ClubService;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.ApartmentMixin;
import com.gobenefit.web.api.rest.impl.mixin.ClubMixin;

@Path("/clubs")
public class ClubRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClubRestProcessor.class);

	@Autowired
	ClubService clubService;

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		LOGGER.info("Request recieved to create the Clubs.");
		String response = null;
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the JSON to club.");
			Club club = (Club) SerializationUtils.deserialize(data, Club.class, mapper);
			club.setApartment(RequestScope.getCurrentApartment());
			LOGGER.debug("Apartment object deserialized .");
			LOGGER.debug("Calling service method to create club.");
			clubService.createEntity(club);
			LOGGER.debug("Club created successfully.");
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			mixinMap.put(Club.class, ClubMixin.class);
			response = SerializationUtils.serialize(club, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while create the club: " + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;

		PagingSearchResult<Club> clubList = null;
		Map<String, Object> criteriaMap = new HashMap<>();
		Long apartmentId = RequestScope.getCurrentApartmentId();
		criteriaMap.put(APARTMENT_ID, apartmentId);
		try {
			clubList = clubService.getEntities(criteriaMap);
			LOGGER.debug(clubList.getEntityList().size() + " club(s) found for apartment Id: " + apartmentId);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Club.class, ClubMixin.class);
			response = SerializationUtils.serialize(clubList, mixinMap, false);
		} catch (Exception e) {
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response deleteEntity(UriInfo uriInfo, HttpHeaders header, Long id) throws WebApplicationException {
		try {
			clubService.deleteEntityById(id);
			return Response.status(Response.Status.ACCEPTED).build();
		} catch (Exception e) {
			LOGGER.error("Error occured while deleting the club. Error:" + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntity(UriInfo uriInfo, HttpHeaders header, Long id) throws WebApplicationException {
		LOGGER.info("Request recieved to create the Clubs.");
		String response = null;
		try {
			Club club = clubService.getEntity(id);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Club.class, ClubMixin.class);
			response = SerializationUtils.serialize(club, mixinMap, true);
		} catch (Exception e) {
			LOGGER.error("Error occured while getting the club: " + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response updateEntity(UriInfo uriInfo, HttpHeaders header, Long id, String data)
			throws WebApplicationException {
		Club club = null;
		ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
		try {
			club = clubService.getEntity(id);

			ObjectReader updater = mapper.readerForUpdating(club);
			club = updater.readValue(data);
			clubService.updateEntity(club);
		} catch (Exception e) {
			LOGGER.error("Error occured while updating the club. Error:" + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(id).build();
	}

}
