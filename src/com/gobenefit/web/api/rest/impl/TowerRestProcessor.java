package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
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
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.service.TowerService;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.ApartmentMixin;
import com.gobenefit.web.api.rest.impl.mixin.TowerMixin;

@RolesAllowed(RWA_ADMIN_ROLE)
@Path("/towers")
public class TowerRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(TowerRestProcessor.class);

	@Autowired
	TowerService towerService;

	@Override
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		LOGGER.info("Request recieved to create the Apartment.");
		String response = null;
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
			LOGGER.debug("Deserializing the JSON to user apartment.");
			Tower tower = (Tower) SerializationUtils.deserialize(data, Tower.class, mapper);
			tower.setApartment(RequestScope.getCurrentApartment());
			LOGGER.debug("Apartment object deserialized .");
			LOGGER.debug("Calling service method to create apartment.");
			towerService.createEntity(tower);
			LOGGER.debug("Apartment created successfully.");
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			mixinMap.put(Tower.class, TowerMixin.class);
			response = SerializationUtils.serialize(tower, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while create the apartment: " + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@PermitAll
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		PagingSearchResult<Tower> towerList = null;
		Map<String, Object> criteriaMap = new HashMap<>();
		Long apartmentId = null;
		
		Map<String, Object> queryMap = getQueryParameterMap(uriInfo);
		if (!queryMap.isEmpty()) {
			apartmentId = Long.parseLong((String) queryMap.get(APARTMENT_ID));
		} else {
			apartmentId = RequestScope.getCurrentApartmentId();
		}
		
		criteriaMap.put(APARTMENT_ID, apartmentId);
		try {
			LOGGER.debug("Calling service method to get the list of tower for apartment Id: " + apartmentId);
			towerList = towerService.getEntities(criteriaMap);
			LOGGER.debug(towerList.getEntityList().size() + " tower(s) found for apartment Id: " + apartmentId);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Tower.class, TowerMixin.class);
			response = SerializationUtils.serialize(towerList, mixinMap, false);
		} catch (Exception e) {
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response updateEntity(UriInfo uriInfo, HttpHeaders header, Long id, String data)
			throws WebApplicationException {
		Tower tower = null;
		String response = null;
		ObjectMapper mapper = SerializationUtils.getJsonObjectMapper();
		try {
			tower = towerService.getEntity(id);

			ObjectReader updater = mapper.readerForUpdating(tower);
			tower = updater.readValue(data);
			towerService.updateEntity(tower);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Apartment.class, ApartmentMixin.class);
			mixinMap.put(Tower.class, TowerMixin.class);
			response = SerializationUtils.serialize(tower, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while updating the Tower. Error:" + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntity(UriInfo uriInfo, HttpHeaders header, Long id) throws WebApplicationException {
		LOGGER.info("Request recieved to create the Clubs.");
		String response = null;
		try {
			Tower tower = towerService.getEntity(id);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Tower.class, TowerMixin.class);
			response = SerializationUtils.serialize(tower, mixinMap, true);
		} catch (Exception e) {
			LOGGER.error("Error occured while getting the tower: " + e.getMessage(), e);
			throw ApplicationUtils.throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

}
