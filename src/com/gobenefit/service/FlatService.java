package com.gobenefit.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gobenefit.entity.impl.Flat;

public interface FlatService extends Service<Flat, Long> {

	void createEntities(List<Flat> flats);

	List<Flat> getUsersApartmentFlats(Map<String, Object> criteriaMap);

	List<Flat> getFlatByIdList(Collection<Long> flatIdList);

}