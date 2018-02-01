package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.FLAT;
import static com.gobenefit.util.AppConstant.ID;
import static com.gobenefit.util.AppConstant.STATUS;
import static com.gobenefit.util.AppConstant.USER;
import static com.gobenefit.util.AppConstant.USER_ID;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractEventBookingDAO;
import com.gobenefit.entity.impl.EventBooking;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class EventBookingDAOImpl extends AbstractEventBookingDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<EventBooking> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EventBooking> cq = (CriteriaQuery<EventBooking>) cb.createQuery(EventBooking.class);
		Root<EventBooking> root = (Root<EventBooking>) cq.from(EventBooking.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		cq = cq.select(root);
		if (criteriaMap.get(USER_ID) != null) {
			Predicate condition = cb.conjunction();
			condition.getExpressions()
					.add(cb.equal(root.get(FLAT).get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
			condition.getExpressions().add(cb.equal(root.<String>get(USER).get(ID), criteriaMap.get(USER_ID)));
			if (criteriaMap.get(STATUS) != null) {
				condition.getExpressions().add(cb.equal(root.<String>get(STATUS), criteriaMap.get(STATUS)));
			}
			cq.where(condition);
		} else {
			if (criteriaMap.get(STATUS) != null) {
				Predicate condition = cb.conjunction();
				condition.getExpressions()
						.add(cb.equal(root.get(FLAT).get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
				condition.getExpressions().add(cb.equal(root.<String>get(STATUS), criteriaMap.get(STATUS)));
				cq.where(condition);
			} else {
				cq.where(cb.equal(root.get(FLAT).get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
			}
		}

		Long totalCount = getTotalRecordCount(cq);
		// int pageNo = (int) criteriaMap.get(PAGINATION_PAGE_NO);
		// int limit = (int) criteriaMap.get(PAGINATION_RECORD_LIMIT);
		TypedQuery<?> query = entityManager.createQuery(cq);// .setFirstResult(pageNo
															// *
															// limit).setMaxResults(limit);
		List<EventBooking> entityList = (List<EventBooking>) query.getResultList();
		return (PagingSearchResult<EventBooking>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}

	@Override
	public EventBooking getEntity(Long entityId, Class<EventBooking> klass) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EventBooking> cq = (CriteriaQuery<EventBooking>) cb.createQuery(EventBooking.class);
		Root<EventBooking> root = (Root<EventBooking>) cq.from(EventBooking.class);
		root.fetch("approvedBy", JoinType.LEFT);
		root.fetch("rejectedBy", JoinType.LEFT);
		cq.where(cb.equal(root.get(ID), entityId));
		TypedQuery<?> query = entityManager.createQuery(cq);
		return (EventBooking) query.getSingleResult();
	}

}
