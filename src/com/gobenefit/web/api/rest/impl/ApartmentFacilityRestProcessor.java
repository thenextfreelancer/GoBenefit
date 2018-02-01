package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;
import static com.gobenefit.util.SerializationUtils.deserializeObject;
import static com.gobenefit.util.SerializationUtils.getJsonObjectMapper;
import static com.gobenefit.util.SerializationUtils.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacility;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.service.ApartmentFacilityService;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.ApartmentFacilityRelationshipMixin;

@Path("facilities")
public class ApartmentFacilityRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentFacilityRestProcessor.class);

	@Autowired
	ApartmentFacilityService apartmentFacilityService;

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		LOGGER.info("Request recieved to get all the facility list");
		PagingSearchResult<ApartmentFacility> apartmentFacilityList = null;
		try {
			LOGGER.debug("Calling the service method to retrieve the facilities list.");
			apartmentFacilityList = apartmentFacilityService.getAllFacilities(null);
			LOGGER.debug("Facilities list retrieved successfully.");
			response = serialize(apartmentFacilityList, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the list of facilities available in application. Error: "
					+ e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@GET
	@Path("/{id: [0-9]+}")
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntities(@PathParam("id") Long apartmentId) throws WebApplicationException {
		String response = null;
		LOGGER.info("Request recieved to get all the facility list");
		PagingSearchResult<ApartmentFacilityRelationship> apartmentFacilityList = null;
		try {
			Map<String, Object> criteraiMap = new HashMap<>();
			criteraiMap.put("apartmentId", apartmentId);
			LOGGER.debug("Calling the service method to retrive the list of apartment facilities.");
			apartmentFacilityList = apartmentFacilityService.getEntities(criteraiMap);
			LOGGER.debug("List of apartment facilities has been retrieved from the service method.");
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(ApartmentFacilityRelationship.class, ApartmentFacilityRelationshipMixin.class);
			response = serialize(apartmentFacilityList, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the list of facilities assigned to the apartment. Error: "
					+ e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		LOGGER.info("Returning the facility list");
		return Response.ok(response).build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/assign")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response assingUnassignFacilities(@PathParam("id") Long id, String data) throws WebApplicationException {
		LOGGER.info("Request recieved to assign the facility to the Apartment.");
		Long apartmentId = RequestScope.getCurrentApartmentId();
		try {
			ObjectMapper mapper = getJsonObjectMapper(true, false);
			LOGGER.debug("Deserializing the JSON to apartment facilty code list.");
			List<String> facilityList = (List<String>) deserializeObject(data, ArrayList.class, mapper);
			LOGGER.debug("Facility Codes: " + facilityList);
			Apartment apartment = new Apartment();
			apartment.setId(apartmentId);
			LOGGER.debug("Calling service method to assign faclity to apartments");
			apartmentFacilityService.assignFacilities(facilityList, apartment);
		} catch (Exception e) {
			LOGGER.error("Error occurred while assigning the Facility to the apartment. Error: " + e.getMessage(), e);
			throw throwWebApplicationException(e);

		}
		LOGGER.info("Faclities assigned to apartments successfully.");
		return Response.ok(Status.CREATED).build();
	}

}
