package com.gobenefit.vo;

import java.util.List;

public class PagingSearchResult<T> {

	public PagingSearchResult(List<T> entityList, Long totalCount) {
		this.entityList = entityList;
		this.totalCount = totalCount;
	}

	private Long totalCount;

	private List<T> entityList;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

}
