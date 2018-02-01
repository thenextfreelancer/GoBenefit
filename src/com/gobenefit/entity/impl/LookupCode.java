package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.LOOKUP_CODE_TABLE;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gobenefit.entity.Entity;

@javax.persistence.Entity
@Table(name = LOOKUP_CODE_TABLE, uniqueConstraints = { @UniqueConstraint(columnNames = { "CODE", "GROUP_CODE" }) })
public class LookupCode implements Entity {

	private static final long serialVersionUID = 3081856565667977474L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, updatable = false)
	private long id;

	@Column(name = "CODE", length = 64, nullable = false)
	private String code;

	@Column(name = "VALUE", length = 128, nullable = false)
	private String value;

	@Column(name = "DESCRIPTION", length = 512)
	private String description;

	@Column(name = "GROUP_CODE", length = 64, nullable = false)
	private String groupCode;

	@Column(name = "PARENT_CODE", length = 64)
	private String parentCode;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public String getId() {
		return this.code;
	}

}
