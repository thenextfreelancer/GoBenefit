package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.GENERATED_ALIAS_0;
import static com.gobenefit.util.AppConstant.LOOKUP_QUERY_PARAM;
import static com.gobenefit.util.LookupCodeConstants.CITY_GROUP_CODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractApartmentDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.LookupCode;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class ApartmentDAOImpl extends AbstractApartmentDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<Apartment> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		Long totalCount = 0l;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Apartment> criteriaQuery = (CriteriaQuery<Apartment>) criteriaBuilder
				.createQuery(Apartment.class);
		Root<Apartment> root = (Root<Apartment>) criteriaQuery.from(Apartment.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		criteriaQuery = criteriaQuery.select(root);
		String apartmentName = (String) criteriaMap.get(LOOKUP_QUERY_PARAM);
		if (StringUtils.isNotBlank(apartmentName)) {
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.like(root.<String> get("name"), "%" + apartmentName + "%"));
			Subquery<LookupCode> subquery = criteriaQuery.subquery(LookupCode.class);
			Root<LookupCode> lookupRoot = subquery.from(LookupCode.class);
			subquery = subquery.select(lookupRoot);
			subquery.where(criteriaBuilder.like(lookupRoot.<String> get("value"), "%" + apartmentName + "%"),
					criteriaBuilder.equal(lookupRoot.<String> get("groupCode"), CITY_GROUP_CODE));
			@SuppressWarnings("rawtypes")
			Join join = root.join("address", JoinType.LEFT);
			join.alias(GENERATED_ALIAS_0);
			predicateList.add(criteriaBuilder.in(join.get("cityCode")).value(subquery));
			Predicate condition = criteriaBuilder.disjunction();
			condition.getExpressions().addAll(predicateList);
			criteriaQuery.where(condition);

		}
		totalCount = getTotalRecordCount(criteriaQuery);

		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		List<Apartment> entityList = (List<Apartment>) query.getResultList();
		totalCount = totalCount == 0l ? entityList.size() : totalCount;
		return (PagingSearchResult<Apartment>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}

	@Override
	public List<Apartment> getEntities() {
		hibernateDAOImpl.setPersistentClass(Apartment.class);
		return hibernateDAOImpl.getEntities();
	}

}
