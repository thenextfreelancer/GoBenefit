package com.gobenefit.entity.impl;

import static com.gobenefit.db.DBConstant.EVENT_BOOKING_TABLE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gobenefit.entity.AbstractEntity;
import com.gobenefit.web.serialization.JsonDateDeserializer;
import com.gobenefit.web.serialization.JsonDateSerializer;
import com.gobenefit.web.serialization.JsonTimeDeserializer;

@Entity
@Table(name = EVENT_BOOKING_TABLE)
public class EventBooking extends AbstractEntity {

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

	@Column(name = "EVENT_START_DATE", nullable = false)
	Date eventStartDate;

	@Column(name = "EVENT_END_DATE", nullable = false)
	Date eventEndDate;

	@Column(name = "EVENT_START_TIME")
	Time eventStartTime;

	@Column(name = "EVENT_END_TIME")
	Time eventEndTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
	private User user;

	@Column(name = "APPROVAL_DATE")
	Date approvalDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "APPROVED_BY_USER_ID", referencedColumnName = "ID", nullable = true)
	private User approvedBy;

	@Column(name = "REJECTION_DATE")
	Date rejectionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REJECTED_BY_USER_ID", referencedColumnName = "ID", nullable = true)
	private User rejectedBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FLAT_ID", nullable = false)
	private Flat flat;

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

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getEventStartDate() {
		return eventStartDate;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getEventEndDate() {
		return eventEndDate;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public Time getEventStartTime() {
		return eventStartTime;
	}

	@JsonDeserialize(using = JsonTimeDeserializer.class)
	public void setEventStartTime(Time eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Time getEventEndTime() {
		return eventEndTime;
	}

	@JsonDeserialize(using = JsonTimeDeserializer.class)
	public void setEventEndTime(Time eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getRejectionDate() {
		return rejectionDate;
	}

	public void setRejectionDate(Date rejectionDate) {
		this.rejectionDate = rejectionDate;
	}

	public User getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(User rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

}
