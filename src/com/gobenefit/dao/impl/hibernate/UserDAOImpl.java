package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.CUSTOM_ENTITY_ALIAS;
import static com.gobenefit.util.AppConstant.FLAT_ID;
import static com.gobenefit.util.AppConstant.ID;
import static com.gobenefit.util.AppConstant.LOOKUP_QUERY_PARAM;
import static com.gobenefit.util.AppConstant.PAGINATION_PAGE_NO;
import static com.gobenefit.util.AppConstant.PAGINATION_RECORD_LIMIT;
import static com.gobenefit.util.AppConstant.STATUS;
import static com.gobenefit.util.AppConstant.USER_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractUserDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.util.ApplicationUtils;
import com.gobenefit.vo.PagingSearchResult;

@Repository
public class UserDAOImpl extends AbstractUserDAO {

	@SuppressWarnings("unchecked")
	@Override
	public PagingSearchResult<User> getEntities(Map<String, Object> criteriaMap) {
		Long apartmentId = (Long) criteriaMap.get(APARTMENT_ID);
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);
		Root<User> root = (Root<User>) criteriaQuery.from(User.class);
		root.alias(CUSTOM_ENTITY_ALIAS);
		criteriaQuery = criteriaQuery.select(root);

		Subquery<Flat> flatSubquery = criteriaQuery.subquery(Flat.class);
		Root<Flat> flatIdRoot = flatSubquery.from(Flat.class);
		flatSubquery = flatSubquery.select(flatIdRoot.get(ID));
		flatSubquery.where(criteriaBuilder.equal(flatIdRoot.get(APARTMENT).get(ID), apartmentId));

		Subquery<UserFlat> userFLatSubquery = criteriaQuery.subquery(UserFlat.class);
		Root<UserFlat> userFLatIdRoot = userFLatSubquery.from(UserFlat.class);
		userFLatSubquery = userFLatSubquery.select(userFLatIdRoot.get(USER_ID));
		Expression<Object> exp = userFLatIdRoot.get(FLAT_ID);
		userFLatSubquery.where(criteriaBuilder.in(exp).value(flatSubquery));

		List<Predicate> predicateList = new ArrayList<>();
		if (criteriaMap.get(LOOKUP_QUERY_PARAM) != null) {
			String lookup = (String) criteriaMap.get(LOOKUP_QUERY_PARAM);
			Predicate condition = criteriaBuilder.disjunction();
			condition.getExpressions().add(criteriaBuilder.like(root.<String>get("name"), "%" + lookup + "%"));
			condition.getExpressions().add(criteriaBuilder.like(root.<String>get("emailId"), "%" + lookup + "%"));
			if (StringUtils.isNotBlank((String) criteriaMap.get(STATUS))) {
				condition.getExpressions()
						.add(criteriaBuilder.equal(root.<String>get(STATUS), (String) criteriaMap.get(STATUS)));
			}
			predicateList.add(condition);
		}
		predicateList.add(criteriaBuilder.in(root.get(ID)).value(userFLatSubquery));

		Predicate mainCondition = criteriaBuilder.conjunction();
		mainCondition.getExpressions().addAll(predicateList);
		criteriaQuery.where(mainCondition);

		int pageNo = criteriaMap.get(PAGINATION_PAGE_NO) != null ? (int) criteriaMap.get(PAGINATION_PAGE_NO) : 0;
		int limit = criteriaMap.get(PAGINATION_RECORD_LIMIT) != null ? (int) criteriaMap.get(PAGINATION_RECORD_LIMIT)
				: 0;
		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		Long totalCount = 0l;
		if (limit > 0) {
			totalCount = getTotalRecordCount(criteriaQuery);
			query.setFirstResult(pageNo * limit).setMaxResults(limit);
		}
		List<User> userList = (List<User>) query.getResultList();
		return (PagingSearchResult<User>) ApplicationUtils.getPeginationResultObject(userList,
				totalCount == 0 ? userList.size() : totalCount);
	}

	@Override
	public List<User> getEntities() {
		hibernateDAOImpl.setPersistentClass(User.class);
		return hibernateDAOImpl.getEntities();
	}

	@Override
	public User getUserByEmailId(String emailId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);
		Root<User> root = (Root<User>) criteriaQuery.from(User.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.<String>get("emailId"), emailId));
		try {
			return entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> getUserApartments(Long userId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Apartment> criteriaQuery = (CriteriaQuery<Apartment>) criteriaBuilder
				.createQuery(Apartment.class);
		Root<Apartment> root = (Root<Apartment>) criteriaQuery.from(Apartment.class);
		criteriaQuery = criteriaQuery.select(root);

		Subquery<UserFlat> userFLatSubquery = criteriaQuery.subquery(UserFlat.class);
		Root<UserFlat> userFLatIdRoot = userFLatSubquery.from(UserFlat.class);
		userFLatSubquery = userFLatSubquery.select(userFLatIdRoot.get("flatId"));
		userFLatSubquery.where(criteriaBuilder.equal(userFLatIdRoot.<String>get(USER_ID), userId));

		Subquery<Flat> flatSubquery = criteriaQuery.subquery(Flat.class);
		Root<Flat> flatIdRoot = flatSubquery.from(Flat.class);
		flatSubquery = flatSubquery.select(flatIdRoot.get(APARTMENT).get(ID));
		Expression<Object> flatExp = flatIdRoot.get(ID);
		flatSubquery.where(criteriaBuilder.in(flatExp).value(userFLatSubquery));

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(criteriaBuilder.in(root.get(ID)).value(flatSubquery));
		Predicate condition = criteriaBuilder.disjunction();
		condition.getExpressions().addAll(predicateList);
		criteriaQuery.where(condition);
		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		return (List<Apartment>) query.getResultList();
	}

	@Override
	public List<User> getUsersByIdList(Collection<Long> userIdList) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);
		Root<User> root = (Root<User>) criteriaQuery.from(User.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(root.<Long>get(ID).in(userIdList));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

}
