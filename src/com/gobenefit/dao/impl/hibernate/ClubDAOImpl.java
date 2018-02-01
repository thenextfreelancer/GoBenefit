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

import com.gobenefit.dao.impl.AbstractClubDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.entity.impl.Club;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class ClubDAOImpl extends AbstractClubDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<Club> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Club> cq = (CriteriaQuery<Club>) cb.createQuery(Club.class);
		Root<Club> root = (Root<Club>) cq.from(Club.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		cq.select(root);
		Long apartmentId = (Long) criteriaMap.get(APARTMENT_ID);
		Join<ApartmentFacilityRelationship, Apartment> apartmentJoin = root.join(APARTMENT);
		apartmentJoin.alias(GENERATED_ALIAS_0);
		Predicate predicate = cb.equal(apartmentJoin.get(ID), apartmentId);
		String clubName = (String) criteriaMap.get(LOOKUP_QUERY_PARAM);
		if (StringUtils.isNotBlank(clubName)) {
			Predicate condition = cb.conjunction();
			condition.getExpressions().add(predicate);
			condition.getExpressions().add(cb.equal(root.<String> get("name"), "%" + clubName + "%"));
			cq.where(condition);
		} else {
			cq.where(predicate);
		}
		TypedQuery<?> query = entityManager.createQuery(cq);
		Long totalCount = getTotalRecordCount(cq);
		List<Club> entityList = (List<Club>) query.getResultList();
		return (PagingSearchResult<Club>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}
}
