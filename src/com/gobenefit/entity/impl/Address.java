package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.ADDRESS_TABLE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;
import static com.gobenefit.util.ApplicationUtils.getValueText;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = ADDRESS_TABLE)
public class Address extends AbstractEntity {

	private static final long serialVersionUID = 7958416653408847614L;

	@Column(name = "ADDRESS", length = 512, nullable = false)
	@Size(min = 1, max = 256)
	private String address;

	@Column(name = "CITY_CODE", columnDefinition = LOOKUP_CODE_REFERENCE)
	@Size(min = 1, max = 32)
	private String cityCode;

	@Column(name = "STATE_CODE", columnDefinition = LOOKUP_CODE_REFERENCE)
	@Size(min = 1, max = 32)
	private String stateCode;

	@Column(name = "PINCODE", nullable = false)
	private Integer pinCode;

	@Column(name = "COUNTRY_CODE", columnDefinition = LOOKUP_CODE_REFERENCE)
	private String countryCode;

	@Column(name = "LAND_MARK", length = 64)
	private String landMark;

	@Column(name = "LATITUDE")
	private BigDecimal latitude;

	@Column(name = "LONGITUDE")
	private BigDecimal longitude;

	@SuppressWarnings("unused")
	private transient String cityName;

	@SuppressWarnings("unused")
	private transient String stateName;

	@SuppressWarnings("unused")
	private transient String countryName;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCityName() {
		return getValueText(getCityCode());
	}

	public String getStateName() {
		return getValueText(getStateCode());
	}

	public String getCountryName() {
		return getValueText(getCountryCode());
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

}
