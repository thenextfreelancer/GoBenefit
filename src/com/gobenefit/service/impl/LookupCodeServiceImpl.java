package com.gobenefit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gobenefit.dao.LookupCodeDAO;
import com.gobenefit.entity.impl.LookupCode;
import com.gobenefit.service.LookupCodeService;

@Service("lookupCodeServiceImpl")
public class LookupCodeServiceImpl implements LookupCodeService {

	@Autowired
	LookupCodeDAO lookupCodeDAO;

	@Override
	public List<LookupCode> getEntities() {
		return lookupCodeDAO.getEntities();
	}

}
