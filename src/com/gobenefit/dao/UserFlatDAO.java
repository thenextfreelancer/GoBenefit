package com.gobenefit.dao;

import java.util.List;
import java.util.Map;

import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.UserFlat;

public interface UserFlatDAO extends GenericDAO<UserFlat, Long> {

	public Role getUserApartmentRole(Long userId, Long apartmentId);

	public List<UserFlat> getFlatUsers(Long flatId);

	public List<UserFlat> getUsersFlat(List<Long> userIds);

	public List<UserFlat> getApartmentFlats(Map<String, Object> criteriaMap);
}
