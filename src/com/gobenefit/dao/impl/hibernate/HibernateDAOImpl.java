package com.gobenefit.dao.impl.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.gobenefit.dao.GenericDAO;
import com.gobenefit.entity.Entity;

@Repository
public class HibernateDAOImpl<E extends Entity, PK extends Serializable> implements GenericDAO<E, PK> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateDAOImpl.class);

	private Class<?> persistentClass;

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public PK createEntity(E entity) {
		LOGGER.debug("In createEntity()");
		String className = entity.getClass().getSimpleName();
		LOGGER.debug("Calling persit method");
		entityManager.persist(entity);
		LOGGER.debug("Entity saved to session");
		LOGGER.info(className + " Entity with id =\"" + entity.getId() + "\" created successfully");
		return (PK) entity.getId();
	}

	@Override
	public void createEntities(List<E> entityList) {
		LOGGER.debug("In createEntities()");
		String className = entityList.get(0).getClass().getSimpleName();
		for (Entity entity : entityList) {
			LOGGER.debug("Calling persit method");
			entityManager.persist(entity);
			LOGGER.debug("Entity saved to session");
			LOGGER.info(className + " Entity with id =\"" + entity.getId() + "\" created successfully");
		}
	}

	@Override
	public void updateEntity(E entity) {
		entityManager.merge(entity);
		LOGGER.info(entity.getClass().getSimpleName() + " Entity with id =\"" + entity.getId()
				+ "\" updated successfully.");
	}

	@Override
	public E getEntity(PK entityId, Class<E> klass) {
		try {
			return (E) entityManager.find(klass, entityId);
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getEntities() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = (CriteriaQuery<E>) cb.createQuery(getPersistentClass());
		Root<E> root = (Root<E>) cq.from(getPersistentClass());
		cq.select(root);
		TypedQuery<?> query = entityManager.createQuery(cq);
		return (List<E>) query.getResultList();
	}

	@Override
	public void deleteEntity(E entity) {
		entityManager.remove(entity);
	}

	@Override
	public void deleteEntities(List<E> entitiesList) {
		for (E entity : entitiesList) {
			entityManager.remove(entity);
		}
	}

	@Override
	public void deleteEntityById(PK entityId, Class<E> klass) {
		E entity = getEntity(entityId, klass);
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	@Override
	public void setPersistentClass(Class<?> persistentClass) {
		this.persistentClass = persistentClass;

	}

	@Override
	public Class<?> getPersistentClass() {
		return persistentClass;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void updateEntities(List<E> entities) {
		for (E entity : entities) {
			entityManager.merge(entity);
		}
	}

}
