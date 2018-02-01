package com.gobenefit.dao.impl.hibernate;

import static com.gobenefit.util.AppConstant.APARTMENT;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.FLAT_ID;
import static com.gobenefit.util.AppConstant.ID;
import static com.gobenefit.util.AppConstant.STATUS;
import static com.gobenefit.util.AppConstant.USER_ID;

import java.util.ArrayList;
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

import com.gobenefit.dao.impl.AbstractUserFlatDAO;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.UserFlat;

@Repository
public class UserFlatDAOImpl extends AbstractUserFlatDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Role getUserApartmentRole(Long userId, Long apartmentId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserFlat> criteriaQuery = (CriteriaQuery<UserFlat>) criteriaBuilder.createQuery(UserFlat.class);
		Root<UserFlat> root = (Root<UserFlat>) criteriaQuery.from(UserFlat.class);
		criteriaQuery = criteriaQuery.select(root);

		Subquery<Flat> flatSubquery = criteriaQuery.subquery(Flat.class);
		Root<Flat> flatIdRoot = flatSubquery.from(Flat.class);
		flatSubquery = flatSubquery.select(flatIdRoot.get(ID));
		flatSubquery.where(criteriaBuilder.equal(flatIdRoot.<Long>get(APARTMENT).get(ID), apartmentId));

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(criteriaBuilder.equal(root.<String>get(USER_ID), userId));
		predicateList.add(criteriaBuilder.in(root.get(FLAT_ID)).value(flatSubquery));
		Predicate condition = criteriaBuilder.conjunction();
		condition.getExpressions().addAll(predicateList);
		criteriaQuery.where(condition);

		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		List<UserFlat> userFlatList = ((List<UserFlat>) query.getResultList());
		return (userFlatList == null || userFlatList.isEmpty()) ? null : userFlatList.get(0).getRole();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFlat> getFlatUsers(Long flatId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserFlat> criteriaQuery = (CriteriaQuery<UserFlat>) criteriaBuilder.createQuery(UserFlat.class);
		Root<UserFlat> root = (Root<UserFlat>) criteriaQuery.from(UserFlat.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.<String>get(FLAT_ID), flatId));
		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		return ((List<UserFlat>) query.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFlat> getUsersFlat(List<Long> userIds) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserFlat> criteriaQuery = (CriteriaQuery<UserFlat>) criteriaBuilder.createQuery(UserFlat.class);
		Root<UserFlat> root = (Root<UserFlat>) criteriaQuery.from(UserFlat.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.in(root.get(USER_ID)).value(userIds));
		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		return ((List<UserFlat>) query.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserFlat> getApartmentFlats(Map<String, Object> criteriaMap) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserFlat> criteriaQuery = (CriteriaQuery<UserFlat>) criteriaBuilder.createQuery(UserFlat.class);
		Root<UserFlat> root = (Root<UserFlat>) criteriaQuery.from(UserFlat.class);
		criteriaQuery = criteriaQuery.select(root);

		Subquery<Flat> flatSubquery = criteriaQuery.subquery(Flat.class);
		Root<Flat> flatIdRoot = flatSubquery.from(Flat.class);
		flatSubquery = flatSubquery.select(flatIdRoot.get(ID));
		flatSubquery
				.where(criteriaBuilder.equal(flatIdRoot.<Long>get(APARTMENT).get(ID), criteriaMap.get(APARTMENT_ID)));

		List<Predicate> predicateList = new ArrayList<>();
		if (criteriaMap.get(USER_ID) != null) {
			predicateList.add(criteriaBuilder.equal(root.<Long>get(USER_ID), criteriaMap.get(USER_ID)));
			predicateList.add(criteriaBuilder.in(root.get(FLAT_ID)).value(flatSubquery));
			predicateList.add(criteriaBuilder.equal(root.<String>get(STATUS), criteriaMap.get(STATUS)));
			Predicate condition = criteriaBuilder.conjunction();
			condition.getExpressions().addAll(predicateList);
			criteriaQuery.where(condition);
		} else {
			if (criteriaMap.containsKey(STATUS)) {
				predicateList.add(criteriaBuilder.in(root.get(FLAT_ID)).value(flatSubquery));
				predicateList.add(criteriaBuilder.equal(root.<String>get(STATUS), criteriaMap.get(STATUS)));
				Predicate condition = criteriaBuilder.conjunction();
				condition.getExpressions().addAll(predicateList);
				criteriaQuery.where(condition);
			} else {
				criteriaQuery.where(criteriaBuilder.in(root.get(FLAT_ID)).value(flatSubquery));
			}
		}

		TypedQuery<?> query = entityManager.createQuery(criteriaQuery);
		return ((List<UserFlat>) query.getResultList());
	}

}
