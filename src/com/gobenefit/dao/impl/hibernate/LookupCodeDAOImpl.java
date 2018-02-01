package com.gobenefit.dao.impl.hibernate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gobenefit.dao.LookupCodeDAO;
import com.gobenefit.entity.impl.LookupCode;

@Repository
public class LookupCodeDAOImpl implements LookupCodeDAO {

	@Autowired
	HibernateDAOImpl<LookupCode, String> hibernateDAOImpl;

	@Override
	public List<LookupCode> getEntities() {
		hibernateDAOImpl.setPersistentClass(LookupCode.class);
		return hibernateDAOImpl.getEntities();
	}

}
