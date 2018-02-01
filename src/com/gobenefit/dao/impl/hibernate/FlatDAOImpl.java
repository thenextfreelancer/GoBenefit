package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.FLAT_ID;
import static com.gobenefit.util.AppConstant.ID;
import static com.gobenefit.util.AppConstant.STATUS;
import static com.gobenefit.util.AppConstant.TOWER_ID;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.LookupCodeConstants.USER_STATUS_ENABLE;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractFlatDAO;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class FlatDAOImpl extends AbstractFlatDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<Flat> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Flat> cq = (CriteriaQuery<Flat>) cb.createQuery(Flat.class);
		Root<Flat> root = (Root<Flat>) cq.from(Flat.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		cq = cq.select(root);
		Predicate condition = cb.conjunction();
		condition.getExpressions().add(cb.notEqual(root.<String>get("flatNumber"), -1));
		condition.getExpressions().add(cb.equal(root.<String>get("tower").get(ID), criteriaMap.get(TOWER_ID)));
		cq.where(condition);
		Long totalCount = getTotalRecordCount(cq);
		// int pageNo = (int) criteriaMap.get(PAGINATION_PAGE_NO);
		// int limit = (int) criteriaMap.get(PAGINATION_RECORD_LIMIT);
		TypedQuery<?> query = entityManager.createQuery(cq);// .setFirstResult(pageNo
															// *
															// limit).setMaxResults(limit);
		List<Flat> entityList = (List<Flat>) query.getResultList();
		return (PagingSearchResult<Flat>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Flat> getUsersApartmentFlats(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Flat> cq = (CriteriaQuery<Flat>) cb.createQuery(Flat.class);
		Root<Flat> root = (Root<Flat>) cq.from(Flat.class);
		cq = cq.select(root);

		Subquery<UserFlat> userFlatSubquery = cq.subquery(UserFlat.class);
		Root<UserFlat> userFlatRoot = userFlatSubquery.from(UserFlat.class);
		userFlatSubquery = userFlatSubquery.select(userFlatRoot.get(FLAT_ID));
		Predicate userFlatCondition = cb.conjunction();
		userFlatCondition.getExpressions().add(cb.equal(userFlatRoot.<Long>get(USER_ID), criteriaMap.get(USER_ID)));
		userFlatCondition.getExpressions()
				.add(cb.equal(userFlatRoot.<Long>get(STATUS), criteriaMap.get(USER_STATUS_ENABLE)));
		userFlatSubquery.where(userFlatCondition);
		Predicate condition = cb.conjunction();
		condition.getExpressions().add(cb.in(root.get(ID)).value(userFlatSubquery));
		condition.getExpressions().add(cb.equal(root.<String>get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
		cq.where(condition);
		TypedQuery<?> query = entityManager.createQuery(cq);
		return (List<Flat>) query.getResultList();
	}

	@Override
	public List<Flat> getFlatByIdList(Collection<Long> flatIdList) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Flat> criteriaQuery = (CriteriaQuery<Flat>) criteriaBuilder.createQuery(Flat.class);
		Root<Flat> root = (Root<Flat>) criteriaQuery.from(Flat.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(root.<Long>get(ID).in(flatIdList));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
}
