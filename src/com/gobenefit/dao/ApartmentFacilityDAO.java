package com.gobenefit.dao;

import java.util.Map;

import com.gobenefit.entity.impl.ApartmentFacility;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.vo.PagingSearchResult;

public interface ApartmentFacilityDAO extends GenericDAO<ApartmentFacilityRelationship, Long> {

	public PagingSearchResult<ApartmentFacility> getALLfacilities(Map<String, Object> criteriaMap);

}
