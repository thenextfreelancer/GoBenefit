package com.gobenefit.dao;

import java.util.Collection;
import java.util.List;

import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.User;

public interface UserDAO extends GenericDAO<User, Long> {

	User getUserByEmailId(String emailId);

	List<Apartment> getUserApartments(Long userId);

	List<User> getUsersByIdList(Collection<Long> userIdList);

}
