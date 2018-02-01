package com.gobenefit.dao.impl.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.gobenefit.dao.impl.AbstractUserActivationDAO;
import com.gobenefit.entity.impl.UserActivation;

@Repository
public class UserActivationDAOImpl extends AbstractUserActivationDAO {

	@Override
	public UserActivation getUserActivationTokenByEmailId(String emailId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserActivation> criteriaQuery = (CriteriaQuery<UserActivation>) criteriaBuilder
				.createQuery(UserActivation.class);
		Root<UserActivation> root = (Root<UserActivation>) criteriaQuery.from(UserActivation.class);
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.<String>get("emailId"), emailId));
		try {
			return entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
