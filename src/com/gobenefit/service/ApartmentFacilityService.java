package com.gobenefit.service;

import java.util.List;
import java.util.Map;

import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacility;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.vo.PagingSearchResult;

public interface ApartmentFacilityService extends Service<ApartmentFacilityRelationship, Long> {

	public void assignFacilities(List<String> facilityList, Apartment apartment);

	public PagingSearchResult<ApartmentFacility> getAllFacilities(Map<String, Object> criteriaMap);

}
