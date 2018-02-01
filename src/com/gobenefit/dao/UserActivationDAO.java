package com.gobenefit.dao;

import com.gobenefit.entity.impl.UserActivation;

public interface UserActivationDAO extends GenericDAO<UserActivation, Long> {

	public UserActivation getUserActivationTokenByEmailId(String emailId);

}
