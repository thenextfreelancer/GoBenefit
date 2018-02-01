package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.GENERATED_ALIAS_0;
import static com.gobenefit.util.AppConstant.ID;
import static com.gobenefit.util.AppConstant.LOOKUP_QUERY_PARAM;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractTowerDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class TowerDAOImpl extends AbstractTowerDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<Tower> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tower> cq = (CriteriaQuery<Tower>) cb.createQuery(Tower.class);
		Root<Tower> root = (Root<Tower>) cq.from(Tower.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		cq.select(root);
		Long apartmentId = (Long) criteriaMap.get(APARTMENT_ID);
		Join<ApartmentFacilityRelationship, Apartment> apartmentJoin = root.join(APARTMENT);
		apartmentJoin.alias(GENERATED_ALIAS_0);
		Predicate predicate = cb.equal(apartmentJoin.get(ID), apartmentId);

		String towerName = (String) criteriaMap.get(LOOKUP_QUERY_PARAM);
		if (StringUtils.isNotBlank(towerName)) {
			Predicate condition = cb.conjunction();
			condition.getExpressions().add(predicate);
			condition.getExpressions().add(cb.equal(root.<String> get("name"), "%" + towerName + "%"));
			cq.where(condition);
		} else {
			cq.where(predicate);
		}
		TypedQuery<?> query = entityManager.createQuery(cq);
		Long totalCount = getTotalRecordCount(cq);
		List<Tower> entityList = (List<Tower>) query.getResultList();
		return (PagingSearchResult<Tower>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}

}
