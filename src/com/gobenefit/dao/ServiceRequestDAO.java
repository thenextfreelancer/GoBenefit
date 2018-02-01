package com.gobenefit.dao;

import com.gobenefit.entity.impl.ServiceRequest;

public interface ServiceRequestDAO extends GenericDAO<ServiceRequest, Long> {
	public void deleteServiceRequest(Long flatId);
}
