package com.gobenefit.dao;

import static com.gobenefit.util.MessageConstant.METHOD_NOT_IMPLEMENTED;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import com.gobenefit.entity.Entity;
import com.gobenefit.vo.PagingSearchResult;

public interface GenericDAO<E extends Entity, PK extends Serializable> {

	default PK createEntity(E entity) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void createEntities(List<E> entity) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void updateEntity(E entity) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void updateEntities(List<E> entities) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default E getEntity(PK entityId, Class<E> klass) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default PagingSearchResult<E> getEntities(Map<String, Object> criteriaMap) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default List<E> getEntities() {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void deleteEntity(E entity) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void deleteEntityById(PK entityId, Class<E> klass) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void setPersistentClass(Class<?> persistentClass) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default Class<?> getPersistentClass() {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default Long getTotalRecordCount(CriteriaQuery<?> criteriaQuery) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default void deleteEntities(List<E> entitiesList) {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

}
