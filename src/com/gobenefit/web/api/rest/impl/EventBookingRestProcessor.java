package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;
import static com.gobenefit.util.LookupCodeConstants.APPROVED_EVENT_BOOKING;
import static com.gobenefit.util.LookupCodeConstants.NEW_EVENT_BOOKING;
import static com.gobenefit.util.LookupCodeConstants.REJECTED_EVENT_BOOKING;
import static com.gobenefit.util.SerializationUtils.deserialize;
import static com.gobenefit.util.SerializationUtils.getJsonObjectMapper;
import static com.gobenefit.util.SerializationUtils.serialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.EventBooking;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;
import com.gobenefit.service.EventBookingService;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.EventBookingMixin;
import com.gobenefit.web.api.rest.impl.mixin.UserBaseMixin;
import com.gobenefit.web.api.rest.impl.mixin.UserFlatMixin;

@Path("bookings")
public class EventBookingRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventBookingRestProcessor.class);

	@Autowired
	EventBookingService eventBookingService;

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response createEntity(UriInfo ui, HttpHeaders hh, String data) {
		try {
			ObjectMapper mapper = getJsonObjectMapper();
			EventBooking eventBooking = (EventBooking) deserialize(data, EventBooking.class, mapper);
			eventBooking.setStatus(NEW_EVENT_BOOKING);
			eventBooking.setUser(RequestScope.getCurrentUser());
			LOGGER.debug("Calling Service method for event booking");
			eventBookingService.createEntity(eventBooking);
			LOGGER.debug("Event booking completed.");
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.error("Error occured while creating the event booking" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}

	}

	@PUT
	@Path("approve/{id:[0-9]}")
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response approveBooking(@PathParam("id") Long bookingId) {
		try {
			EventBooking eventBooking = eventBookingService.getEntity(bookingId);
			eventBooking.setStatus(APPROVED_EVENT_BOOKING);
			User user = RequestScope.getCurrentUser();
			eventBooking.setApprovedBy(user);
			eventBooking.setApprovalDate(new Date());
			LOGGER.debug("Calling Service method for event booking approval");
			eventBookingService.approveBooking(eventBooking);
			LOGGER.debug("Event booking approved by " + user.getFirstName());
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.error("Error occured while approving the event booking. Error:" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
	}

	@PUT
	@Path("reject/{id:[0-9]}")
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response rejectBooking(@PathParam("id") Long bookingId) {
		try {
			EventBooking eventBooking = eventBookingService.getEntity(bookingId);
			eventBooking.setStatus(REJECTED_EVENT_BOOKING);
			eventBooking.setRejectedBy(RequestScope.getCurrentUser());
			eventBooking.setRejectionDate(new Date());
			LOGGER.debug("Calling Service method for event booking rejection.");
			eventBookingService.rejectBooking(eventBooking);
			LOGGER.debug("Event booking rejected by " + RequestScope.getCurrentUser().getFirstName());
			return Response.ok().build();
		} catch (Exception e) {
			LOGGER.error("Error occured while rejecting the event booking. Error:" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;
		PagingSearchResult<EventBooking> eventBookingList = null;
		Map<String, Object> criteriaMap = getQueryParameterMap(uriInfo);
		criteriaMap.put(APARTMENT_ID, RequestScope.getCurrentApartmentId());
		if (criteriaMap.get("isAdmin") == null) {
			criteriaMap.put(USER_ID, RequestScope.getCurrentUser().getId());
		}
		try {
			LOGGER.debug("Calling service method to get the list of event bookings.");
			eventBookingList = eventBookingService.getEntities(criteriaMap);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(User.class, UserBaseMixin.class);
			mixinMap.put(Flat.class, UserFlatMixin.class);
			mixinMap.put(EventBooking.class, EventBookingMixin.class);
			LOGGER.debug(eventBookingList.getEntityList().size() + " Event Booking(s) found");
			response = serialize(eventBookingList, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the booking list.Error: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	public Response getEntity(UriInfo uriInfo, HttpHeaders header, Long entityId) throws WebApplicationException {
		String response = null;
		try {
			EventBooking eventBooking = eventBookingService.getEntity(entityId);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(User.class, UserBaseMixin.class);
			mixinMap.put(Flat.class, UserFlatMixin.class);
			response = serialize(eventBooking, mixinMap, false);
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the booking details for id " + entityId + ".Error: "
					+ e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}
}
