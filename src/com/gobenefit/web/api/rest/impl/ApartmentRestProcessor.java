package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.ApplicationUtils.getPeginationResultObject;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.service.ApartmentService;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.ApartmentMixin;

@Path("apartments")
@PermitAll
public class ApartmentRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentRestProcessor.class);

	@Autowired
	ApartmentService apartmentService;

	@Override
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		LOGGER.info("Request recieved to create the Apartment.");
		String response = null;
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the JSON to user apartment.");
			Apartment apartment = (Apartment) SerializationUtils.deserialize(data, Apartment.class, mapper);
			LOGGER.debug("Apartment object deserialized .");
			LOGGER.debug("Calling service method to create apartment.");
			LOGGER.debug(apartment.toString());
			apartmentService.createEntity(apartment);
			LOGGER.debug("Apartment created successfully.");
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			response = SerializationUtils.serialize(apartment, mixinMap, true);
		} catch (Exception e) {
			LOGGER.error("Error occured while create the apartment: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		PagingSearchResult<Apartment> apartmentList = null;
		try {
			Map<String, Object> queryMap = getQueryParameterMap(uriInfo);
			if (!queryMap.isEmpty()) {
				apartmentList = apartmentService.getEntities(queryMap);
			} else {
				List<Apartment> resultList = apartmentService.getEntities();
				apartmentList = (PagingSearchResult<Apartment>) getPeginationResultObject(resultList);
			}
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			response = SerializationUtils.serialize(apartmentList, mixinMap, false);
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	public Response getEntity(UriInfo uriInfo, HttpHeaders header, Long id) throws WebApplicationException {
		String response = null;
		Apartment apartment = null;
		try {
			apartment = apartmentService.getEntity(id);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			response = SerializationUtils.serialize(apartment, mixinMap, true);
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}
}
