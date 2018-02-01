package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.SERVICE_REQUEST_TABLE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = SERVICE_REQUEST_TABLE)
public class ServiceRequest extends AbstractEntity {

	private static final long serialVersionUID = 6443330626789488304L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FACILITY_CODE", nullable = false)
	ApartmentFacility apartmentFacility;

	@Column(name = "SUBJECT", length = 128, nullable = false)
	private String subject;

	@Column(name = "DESCRIPTION", length = 1024)
	private String description;

	@Column(name = "STATUS", columnDefinition = LOOKUP_CODE_REFERENCE)
	private String status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FLAT_ID", nullable = false)
	private Flat flat;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE_PROVIDER_USER_ID")
	private User serviceProvider;

	@OneToMany(mappedBy = "serviceComments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ServiceComment> serviceComments;

	public ApartmentFacility getApartmentFacility() {
		return apartmentFacility;
	}

	public void setApartmentFacility(ApartmentFacility apartmentFacility) {
		this.apartmentFacility = apartmentFacility;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public User getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(User serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Set<ServiceComment> getServiceComments() {
		return serviceComments;
	}

	public void setServiceComments(Set<ServiceComment> serviceComments) {
		this.serviceComments = serviceComments;
	}

}
