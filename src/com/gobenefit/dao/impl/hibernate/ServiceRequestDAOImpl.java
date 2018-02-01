package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.FLAT;
import static com.gobenefit.util.AppConstant.ID;
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

import com.gobenefit.dao.impl.AbstractServiceRequestDAO;
import com.gobenefit.entity.impl.ServiceRequest;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class ServiceRequestDAOImpl extends AbstractServiceRequestDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<ServiceRequest> getEntities(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServiceRequest> cq = (CriteriaQuery<ServiceRequest>) cb.createQuery(ServiceRequest.class);
		Root<ServiceRequest> root = (Root<ServiceRequest>) cq.from(ServiceRequest.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		cq = cq.select(root);
		if (criteriaMap.get(USER_ID) != null) {
			Predicate condition = cb.conjunction();
			condition.getExpressions()
					.add(cb.equal(root.get(FLAT).get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
			condition.getExpressions().add(cb.equal(root.<String>get(USER).get(ID), criteriaMap.get(USER_ID)));
			cq.where(condition);
		} else {
			cq.where(cb.equal(root.get(FLAT).get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));
		}

		Long totalCount = getTotalRecordCount(cq);
		// int pageNo = (int) criteriaMap.get(PAGINATION_PAGE_NO);
		// int limit = (int) criteriaMap.get(PAGINATION_RECORD_LIMIT);
		TypedQuery<?> query = entityManager.createQuery(cq);// .setFirstResult(pageNo
															// *
															// limit).setMaxResults(limit);
		List<ServiceRequest> entityList = (List<ServiceRequest>) query.getResultList();
		return (PagingSearchResult<ServiceRequest>) ApplicationUtils.getPeginationResultObject(entityList, totalCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteServiceRequest(Long flatId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServiceRequest> cq = (CriteriaQuery<ServiceRequest>) cb.createQuery(ServiceRequest.class);
		Root<ServiceRequest> root = (Root<ServiceRequest>) cq.from(ServiceRequest.class);
		cq.where(cb.equal(root.get(FLAT).get(ID), flatId));
		TypedQuery<?> query = entityManager.createQuery(cq);
		List<ServiceRequest> serviceRequestList = (List<ServiceRequest>) query.getResultList();
		for (ServiceRequest serviceRequest : serviceRequestList) {
			entityManager.remove(serviceRequest);
		}
	}

	@Override
	public ServiceRequest getEntity(Long entityId, Class<ServiceRequest> klass) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServiceRequest> cq = (CriteriaQuery<ServiceRequest>) cb.createQuery(ServiceRequest.class);
		Root<ServiceRequest> root = (Root<ServiceRequest>) cq.from(ServiceRequest.class);
		root.fetch("serviceComments", JoinType.LEFT);
		cq.where(cb.equal(root.get(ID), entityId));
		TypedQuery<?> query = entityManager.createQuery(cq);
		return (ServiceRequest) query.getSingleResult();
	}

}
