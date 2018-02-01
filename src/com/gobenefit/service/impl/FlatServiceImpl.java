package com.gobenefit.service.impl;

import static com.gobenefit.util.AppConstant.DEFAULT_FLAT_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.FlatDAO;
import com.gobenefit.dao.ServiceRequestDAO;
import com.gobenefit.dao.UserFlatDAO;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.service.FlatService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class FlatServiceImpl implements FlatService {

	@Autowired
	private FlatDAO flatDAO;

	@Autowired
	UserFlatDAO userFlatDAO;

	@Autowired
	ServiceRequestDAO serviceRequestDAO;

	@Override
	@Transactional
	public Long createEntity(Flat flat) {
		return flatDAO.createEntity(flat);
	}

	@Override
	@Transactional
	public void createEntities(List<Flat> flats) {
		flatDAO.createEntities(flats);
	}

	@Override
	@Transactional
	public void updateEntity(Flat flat) {
		flatDAO.updateEntity(flat);
	}

	@Override
	public PagingSearchResult<Flat> getEntities(Map<String, Object> criteriaMap) {
		return flatDAO.getEntities(criteriaMap);
	}

	@Override
	public Flat getEntity(Long entityId) {
		return flatDAO.getEntity(entityId, Flat.class);
	}

	@Override
	@Transactional
	public void deleteEntity(Flat entity) {
		flatDAO.deleteEntity(entity);
	}

	@Override
	@Transactional
	public void deleteEntityById(Long entityId) {

		List<UserFlat> flatUsers = userFlatDAO.getFlatUsers(entityId);
		if (flatUsers != null && !flatUsers.isEmpty()) {
			List<Long> userIdList = new ArrayList<>();
			for (UserFlat flatUser : flatUsers) {
				userIdList.add(flatUser.getUserId());
			}
			List<UserFlat> userFlats = userFlatDAO.getUsersFlat(userIdList);
			if (userFlats != null && !userFlats.isEmpty()) {
				Map<Long, List<UserFlat>> map = groupUserFlatByUserId(userFlats);
				Map<String, List<UserFlat>> userFlatMap = getDeleteUpdateMap(map, flatUsers);
				List<UserFlat> deleteEntities = userFlatMap.get("deleteEntities");
				if (deleteEntities != null) {
					userFlatDAO.deleteEntities(deleteEntities);
				}
				List<UserFlat> updateEntities = userFlatMap.get("updateEntities");
				if (updateEntities != null) {
					userFlatDAO.updateEntities(updateEntities);
				}
			}
		}
		serviceRequestDAO.deleteServiceRequest(entityId);
		flatDAO.deleteEntityById(entityId, Flat.class);
	}

	private Map<Long, List<UserFlat>> groupUserFlatByUserId(List<UserFlat> userFlats) {
		Map<Long, List<UserFlat>> map = new HashMap<>();
		for (UserFlat userFlat : userFlats) {
			List<UserFlat> flatList = map.get(userFlat.getUserId());
			if (flatList == null) {
				flatList = new ArrayList<>();
				map.put(userFlat.getUserId(), flatList);
			}
			flatList.add(userFlat);
		}
		return map;
	}

	private Map<String, List<UserFlat>> getDeleteUpdateMap(Map<Long, List<UserFlat>> map, List<UserFlat> flatUsers) {
		Map<String, List<UserFlat>> userFlatMap = new HashMap<>();
		List<UserFlat> deleteList = new ArrayList<>();
		List<UserFlat> updateList = new ArrayList<>();
		for (UserFlat flatUser : flatUsers) {
			if (map.get(flatUser.getUserId()).size() > 1) {
				deleteList.add(flatUser);
			} else {
				flatUser.setFlatId(DEFAULT_FLAT_ID);
				updateList.add(flatUser);
			}
		}
		if (!deleteList.isEmpty()) {
			userFlatMap.put("deleteEntities", deleteList);
		}
		if (!updateList.isEmpty()) {
			userFlatMap.put("updateEntities", updateList);
		}
		return userFlatMap;

	}

	@Override
	public List<Flat> getUsersApartmentFlats(Map<String, Object> criteriaMap) {
		return flatDAO.getUsersApartmentFlats(criteriaMap);
	}

	@Override
	public List<Flat> getFlatByIdList(Collection<Long> flatIdList) {
		return flatDAO.getFlatByIdList(flatIdList);
	}
}
