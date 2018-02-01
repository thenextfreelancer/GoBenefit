package com.gobenefit.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.ClubDAO;
import com.gobenefit.entity.impl.Club;
import com.gobenefit.service.ClubService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class ClubServiceImpl implements ClubService {

	@Autowired
	private ClubDAO clubDAO;

	@Override
	@Transactional
	public Long createEntity(Club club) {
		return clubDAO.createEntity(club);
	}

	@Override
	@Transactional
	public void updateEntity(Club club) {
		clubDAO.updateEntity(club);
	}

	@Override
	public PagingSearchResult<Club> getEntities(Map<String, Object> criteriaMap) {
		return clubDAO.getEntities(criteriaMap);
	}

	@Override
	public Club getEntity(Long entityId) {
		return clubDAO.getEntity(entityId, Club.class);
	}

	@Override
	@Transactional
	public void deleteEntity(Club entity) {
		clubDAO.deleteEntity(entity);
	}

	@Override
	@Transactional
	public void deleteEntityById(Long entityId) {
		clubDAO.deleteEntityById(entityId, Club.class);
	}

}
