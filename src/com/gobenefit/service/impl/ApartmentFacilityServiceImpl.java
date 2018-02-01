package com.gobenefit.service.impl;

import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.LookupCodeConstants.BASIC_AMINITIES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.ApartmentFacilityDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacility;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.service.ApartmentFacilityService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class ApartmentFacilityServiceImpl implements ApartmentFacilityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentFacilityServiceImpl.class);

	@Autowired
	private ApartmentFacilityDAO apartmentFacilityDAO;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void assignFacilities(List<String> facilityList, Apartment apartment) {
		LOGGER.debug("In Service createRelationship.");
		List<ApartmentFacilityRelationship> enitiyRelationshipList = new ArrayList<>();
		Date date = new Date();
		Map<String, Object> criteriaMap = new HashMap<>();
		criteriaMap.put(APARTMENT_ID, apartment.getId());
		PagingSearchResult<ApartmentFacilityRelationship> assignedFacility = getEntities(criteriaMap);
		List<ApartmentFacilityRelationship> resultList = assignedFacility.getEntityList();
		Comparator<ApartmentFacilityRelationship> comparator = new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((ApartmentFacilityRelationship) o1).getApartmentFacility().getCode()
						.compareTo(((ApartmentFacilityRelationship) o2).getApartmentFacility().getCode());
			}
		};
		Collections.sort(resultList, comparator);
		LOGGER.debug("Polulating facility list for ApartmentFacilitiesRelationship.");
		for (String facilityCode : facilityList) {
			ApartmentFacilityRelationship apartmentFacilitiesRelationship = new ApartmentFacilityRelationship();
			ApartmentFacility facility = new ApartmentFacility();
			facility.setCode(facilityCode);
			apartmentFacilitiesRelationship.setApartmentFacility(facility);
			if (Collections.binarySearch(resultList, apartmentFacilitiesRelationship, comparator) < 0) {
				apartmentFacilitiesRelationship.setFacilityType(BASIC_AMINITIES);
				apartmentFacilitiesRelationship.setApartment(apartment);

				apartmentFacilitiesRelationship.setCreationDate(date);
				apartmentFacilitiesRelationship.setModifiedDate(date);
				enitiyRelationshipList.add(apartmentFacilitiesRelationship);
			}
		}
		List<ApartmentFacilityRelationship> deleteList = new ArrayList<>();
		for (ApartmentFacilityRelationship relationShip : resultList) {

			if (!facilityList.contains(relationShip.getApartmentFacility().getCode())) {
				deleteList.add(relationShip);
			}
		}
		LOGGER.debug("ApartmentFacilitiesRelationship list is populated.");
		if (!deleteList.isEmpty()) {
			apartmentFacilityDAO.deleteEntities(deleteList);
		}
		if (!enitiyRelationshipList.isEmpty()) {
			LOGGER.debug("Calling DAO method to create the relationship.");
			apartmentFacilityDAO.createEntities(enitiyRelationshipList);
			LOGGER.debug("Apartment relationship created successfully.");
		}
	}

	@Override
	public PagingSearchResult<ApartmentFacilityRelationship> getEntities(Map<String, Object> criteriaMap) {
		return apartmentFacilityDAO.getEntities(criteriaMap);
	}

	@Override
	public List<ApartmentFacilityRelationship> getEntities() {
		return apartmentFacilityDAO.getEntities();
	}

	@Override
	public PagingSearchResult<ApartmentFacility> getAllFacilities(Map<String, Object> criteriaMap) {
		return apartmentFacilityDAO.getALLfacilities(criteriaMap);
	}

	@Override
	public ApartmentFacilityRelationship getEntity(Long entityId) {
		return apartmentFacilityDAO.getEntity(entityId, ApartmentFacilityRelationship.class);
	}

	@Override
	public void deleteEntity(ApartmentFacilityRelationship entity) {
		apartmentFacilityDAO.deleteEntity(entity);
	}

}
