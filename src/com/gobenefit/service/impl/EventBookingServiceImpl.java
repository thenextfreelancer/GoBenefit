package com.gobenefit.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.EventBookingDAO;
import com.gobenefit.entity.impl.EventBooking;
import com.gobenefit.service.EventBookingService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class EventBookingServiceImpl implements EventBookingService {

	@Autowired
	EventBookingDAO eventBookingDAO;

	@Override
	@Transactional
	public Long createEntity(EventBooking eventBooking) throws Exception {
		Date date = new Date();
		eventBooking.setCreationDate(date);
		eventBooking.setModifiedDate(date);
		Long eventBookingId = eventBookingDAO.createEntity(eventBooking);
		return eventBookingId;

	}

	@Override
	public PagingSearchResult<EventBooking> getEntities(Map<String, Object> criteriaMap) {
		return eventBookingDAO.getEntities(criteriaMap);
	}

	@Override
	public EventBooking getEntity(Long entityId) {
		return eventBookingDAO.getEntity(entityId, EventBooking.class);
	}

	@Override
	@Transactional
	public void approveBooking(EventBooking booking) {
		Date date = new Date();
		booking.setModifiedDate(date);
		eventBookingDAO.updateEntity(booking);

	}

	@Override
	@Transactional
	public void rejectBooking(EventBooking booking) {
		Date date = new Date();
		booking.setModifiedDate(date);
		eventBookingDAO.updateEntity(booking);
	}

}
