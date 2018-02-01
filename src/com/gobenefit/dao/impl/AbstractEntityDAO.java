package com.gobenefit.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.gobenefit.dao.GenericDAO;
import com.gobenefit.dao.impl.hibernate.HibernateDAOImpl;
import com.gobenefit.entity.Entity;

public abstract class AbstractEntityDAO<E extends Entity, PK extends Serializable> implements GenericDAO<E, PK> {

	@Autowired
	public HibernateDAOImpl<E, PK> hibernateDAOImpl;

	protected EntityManager getEntityManager() {
		return hibernateDAOImpl.getEntityManager();
	}

	@Override
	public PK createEntity(E entity) {
		return hibernateDAOImpl.createEntity(entity);
	}

	@Override
	public void createEntities(List<E> entityList) {
		hibernateDAOImpl.createEntities(entityList);
	}

	@Override
	public void deleteEntity(E entity) {
		hibernateDAOImpl.deleteEntity(entity);
	}

	@Override
	public void deleteEntities(List<E> entitieList) {
		hibernateDAOImpl.deleteEntities(entitieList);
	}

	@Override
	public void deleteEntityById(PK entityId, Class<E> klass) {
		hibernateDAOImpl.deleteEntityById(entityId, klass);
	}

	@Override
	public void updateEntity(E entity) {
		hibernateDAOImpl.updateEntity(entity);
	}

	@Override
	public void updateEntities(List<E> entities) {
		hibernateDAOImpl.updateEntities(entities);
	}

	@Override
	public E getEntity(PK entityId, Class<E> klass) {
		return hibernateDAOImpl.getEntity(entityId, klass);
	}

	@Override
	public Long getTotalRecordCount(CriteriaQuery<?> criteriaQuery) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
		Set<Root<?>> roots = criteriaQuery.getRoots();
		Root<?> root = roots.iterator().next();
		Root<?> countRoot = countCriteria.from(root.getJavaType());
		countRoot.alias(root.getAlias());
		doJoins(root.getJoins(), countRoot);
		countCriteria.select(criteriaBuilder.count(countRoot));
		if (criteriaQuery.getRestriction() != null) {
			countCriteria.where(criteriaQuery.getRestriction());
		}
		return entityManager.createQuery(countCriteria).getSingleResult();
	}

	private void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root_) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root_.join(join.getAttribute().getName(), join.getJoinType());
			doJoins(join.getJoins(), joined);
		}
	}

	private void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root_) {
		for (Join<?, ?> join : joins) {
			Join<?, ?> joined = root_.join(join.getAttribute().getName(), join.getJoinType());
			doJoins(join.getJoins(), joined);
		}
	}

}