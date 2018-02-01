package com.gobenefit.service;

import static com.gobenefit.util.MessageConstant.METHOD_NOT_IMPLEMENTED;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gobenefit.entity.Entity;
import com.gobenefit.vo.PagingSearchResult;

public interface Service<T extends Entity, PK extends Serializable> {

	default public PK createEntity(T entity) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public void updateEntity(T entity) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public PagingSearchResult<T> getEntities(Map<String, Object> criteriaMap) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public T getEntity(PK entityId) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public void deleteEntity(T entity) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public List<T> getEntities() throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

	default public void deleteEntityById(PK entityId) throws Exception {
		throw new RuntimeException(METHOD_NOT_IMPLEMENTED);
	}

}
