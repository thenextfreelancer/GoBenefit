package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.APARTMENT_FACILITY_RELATIONSHIP_TABLE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;
import static com.gobenefit.util.ApplicationUtils.getValueText;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = APARTMENT_FACILITY_RELATIONSHIP_TABLE, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "APARTMENT_ID", "FACILITY_ID" }) })
public class ApartmentFacilityRelationship extends AbstractEntity {

	private static final long serialVersionUID = -6252929887221005487L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APARTMENT_ID")
	private Apartment apartment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FACILITY_ID")
	private ApartmentFacility apartmentFacility;

	@Column(name = "FACILITY_TYPE", columnDefinition = LOOKUP_CODE_REFERENCE)
	private String facilityType;

	@SuppressWarnings("unused")
	private transient String facilityTypeDescription;

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public ApartmentFacility getApartmentFacility() {
		return apartmentFacility;
	}

	public void setApartmentFacility(ApartmentFacility apartmentFacility) {
		this.apartmentFacility = apartmentFacility;
	}

	public String getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}

	public String getFacilityTypeDescription() {
		return getValueText(getFacilityType());
	}

	public void setFacilityTypeDescription(String facilityTypeDescription) {
		this.facilityTypeDescription = facilityTypeDescription;
	}
}