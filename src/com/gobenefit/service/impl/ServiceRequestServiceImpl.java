package com.gobenefit.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.ServiceRequestDAO;
import com.gobenefit.entity.impl.ServiceRequest;
import com.gobenefit.mail.ApplicationMailSender;
import com.gobenefit.service.ServiceRequestService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

	@Autowired
	ServiceRequestDAO serviceRequestDAO;

	@Autowired
	ApplicationMailSender mailSender;

	@Override
	@Transactional
	public Long createEntity(ServiceRequest serviceRequest) throws Exception {
		Date date = new Date();
		serviceRequest.setCreationDate(date);
		serviceRequest.setModifiedDate(date);
		Long serviceRequestId = serviceRequestDAO.createEntity(serviceRequest);
		// mailSender.sendMail(null);
		return serviceRequestId;

	}

	@Override
	public PagingSearchResult<ServiceRequest> getEntities(Map<String, Object> criteriaMap) {
		return serviceRequestDAO.getEntities(criteriaMap);
	}

	@Override
	public List<ServiceRequest> getEntities() {
		return serviceRequestDAO.getEntities();
	}

	@Override
	public ServiceRequest getEntity(Long entityId) {
		return serviceRequestDAO.getEntity(entityId, ServiceRequest.class);
	}

}
