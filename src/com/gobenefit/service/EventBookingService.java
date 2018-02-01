package com.gobenefit.service;

import com.gobenefit.entity.impl.EventBooking;

public interface EventBookingService extends Service<EventBooking, Long> {

	public void approveBooking(EventBooking booking);

	public void rejectBooking(EventBooking booking);

}