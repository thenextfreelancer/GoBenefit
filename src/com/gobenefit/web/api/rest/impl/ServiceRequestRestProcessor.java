package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;
import static com.gobenefit.util.LookupCodeConstants.SERVICE_OPEN_STATUS;
import static com.gobenefit.util.SerializationUtils.deserialize;
import static com.gobenefit.util.SerializationUtils.getJsonObjectMapper;
import static com.gobenefit.util.SerializationUtils.serialize;

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
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.ServiceRequest;
import com.gobenefit.entity.impl.User;
import com.gobenefit.service.ServiceRequestService;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.UserFlatMixin;
import com.gobenefit.web.api.rest.impl.mixin.ServiceRequestMixin;
import com.gobenefit.web.api.rest.impl.mixin.UserMixin;

@Path("serviceRequests")
public class ServiceRequestRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestRestProcessor.class);

	@Autowired
	ServiceRequestService serviceRequestService;

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		try {
			ObjectMapper mapper = getJsonObjectMapper();
			ServiceRequest serviceRequest = (ServiceRequest) deserialize(data, ServiceRequest.class, mapper);
			serviceRequest.setStatus(SERVICE_OPEN_STATUS);
			serviceRequest.setUser(RequestScope.getCurrentUser());
			LOGGER.debug("Calling Service method to create service request");
			serviceRequestService.createEntity(serviceRequest);
			LOGGER.debug("Service Request created successfuly");
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.error("Error occured while creating the service request" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}

	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		PagingSearchResult<ServiceRequest> serviceRequestList = null;
		Map<String, Object> criteriaMap = getQueryParameterMap(uriInfo);
		criteriaMap.put(APARTMENT_ID, RequestScope.getCurrentApartmentId());
		if (criteriaMap.get("isAdmin") == null) {
			criteriaMap.put(USER_ID, RequestScope.getCurrentUser().getId());
		}
		try {
			LOGGER.debug("Calling service method to get the list of service request.");
			serviceRequestList = serviceRequestService.getEntities(criteriaMap);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(User.class, UserMixin.class);
			mixinMap.put(Flat.class, UserFlatMixin.class);
			mixinMap.put(ServiceRequest.class, ServiceRequestMixin.class);
			LOGGER.debug(serviceRequestList.getEntityList().size() + " Service Request(s) found");
			response = serialize(serviceRequestList, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the service requests.Error: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntity(UriInfo uriInfo, HttpHeaders header, Long entityId) throws WebApplicationException {
		String response = null;
		try {
			ServiceRequest request = serviceRequestService.getEntity(entityId);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(User.class, UserMixin.class);
			mixinMap.put(Flat.class, UserFlatMixin.class);
			response = serialize(request, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the service request for id " + entityId + ".Error: "
					+ e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}
}
