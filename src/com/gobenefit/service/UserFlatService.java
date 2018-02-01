package com.gobenefit.service;

import java.util.List;
import java.util.Map;

import com.gobenefit.entity.impl.UserFlat;

public interface UserFlatService extends Service<UserFlat, Long> {

	List<UserFlat> getApartmentFlats(Map<String, Object> criteriaMap);

	void approve(UserFlat userFlat);

	void decline(UserFlat userFlat);

}