package com.gobenefit.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gobenefit.entity.impl.Flat;

public interface FlatDAO extends GenericDAO<Flat, Long> {

	List<Flat> getUsersApartmentFlats(Map<String, Object> criteriaMap);

	List<Flat> getFlatByIdList(Collection<Long> flatIdList);

}
