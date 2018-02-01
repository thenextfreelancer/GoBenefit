package com.gobenefit.service;

import java.util.Collection;
import java.util.List;

import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserActivation;
import com.gobenefit.entity.impl.UserAuthenticationToken;

public interface UserService extends Service<User, Long> {

	User authenticate(String emailId, String password);

	User getUserByUsername(String emailId);

	UserAuthenticationToken authenticateUser(String username, String password);

	List<Apartment> getUserApartments(Long userId);

	Role getUserApartmentRole(Long userId, Long apartmentId);

	void sendActivationToken(User user);

	UserActivation getActivationToken(String emailId);

	void deleteActivationToken(Long userId);

	void approveUser(Long userId);

	List<User> getUsersByIdList(Collection<Long> userIdList);

}