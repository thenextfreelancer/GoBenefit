package com.gobenefit.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.TowerDAO;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.service.TowerService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class TowerServiceImpl implements TowerService {
	@Autowired
	private TowerDAO towerDAO;

	@Override
	@Transactional
	public Long createEntity(Tower tower) {
		return towerDAO.createEntity(tower);
	}

	@Override
	@Transactional
	public void updateEntity(Tower tower) {
		towerDAO.updateEntity(tower);
	}

	@Override
	public PagingSearchResult<Tower> getEntities(Map<String, Object> criteriaMap) {
		return towerDAO.getEntities(criteriaMap);
	}

	@Override
	public Tower getEntity(Long entityId) {
		return towerDAO.getEntity(entityId, Tower.class);
	}

	@Override
	public void deleteEntity(Tower entity) {
		towerDAO.deleteEntity(entity);
	}

}
