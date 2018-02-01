package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.GENERATED_ALIAS_0;
import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.ID;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractApartmentFacilityDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.ApartmentFacility;
import com.gobenefit.entity.impl.ApartmentFacilityRelationship;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class ApartmentFacilityDAOImpl extends AbstractApartmentFacilityDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<ApartmentFacilityRelationship> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		Long totalCount = 0l;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ApartmentFacilityRelationship> criteriaQuery = (CriteriaQuery<ApartmentFacilityRelationship>) criteriaBuilder
				.createQuery(ApartmentFacilityRelationship.class);
		Root<ApartmentFacilityRelationship> root = (Root<ApartmentFacilityRelationship>) criteriaQuery
				.from(ApartmentFacilityRelationship.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		criteriaQuery = criteriaQuery.select(root);
		Long apartmentId = (Long) criteriaMap.get(APARTMENT_ID);

		Join<ApartmentFacilityRelationship, Apartment> relationshipJoin = root.join(APARTMENT);
		relationshipJoin.alias(GENERATED_ALIAS_0);
		criteriaQuery.where(criteriaBuilder.equal(relationshipJoin.get(ID), apartmentId));

		totalCount = getTotalRecordCount(criteriaQuery);

		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		List<ApartmentFacility> entityList = (List<ApartmentFacility>) query.getResultList();
		totalCount = totalCount == 0l ? entityList.size() : totalCount;
		return (PagingSearchResult<ApartmentFacilityRelationship>) ApplicationUtils
				.getPeginationResultObject(entityList, totalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<ApartmentFacility> getALLfacilities(Map<String, Object> criteriaMap) {
		hibernateDAOImpl.setPersistentClass(ApartmentFacility.class);
		return (PagingSearchResult<ApartmentFacility>) ApplicationUtils
				.getPeginationResultObject(hibernateDAOImpl.getEntities());
	}

}
