package com.gobenefit.entity.impl;

import static com.gobenefit.config.PropertiesConstant.APP_AMAZONS3_STATIC_BUCKET_ENDPOINT;
import static com.gobenefit.config.PropertiesConstant.APP_AMAZONS3_STATIC_BUCKET_NAME;
import static com.gobenefit.db.DBConstant.USER_TABLE;
import static com.gobenefit.util.AppConstant.AMAZONS3;
import static com.gobenefit.util.AppConstant.DEFAULT_USER_IMAGE;
import static com.gobenefit.util.AppConstant.IMG_PARENT_FOLDER_NAME;
import static com.gobenefit.util.AppConstant.IMG_USER_FOLDER_NAME;
import static com.gobenefit.util.AppConstant.IMG_USER_PROFILE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE;
import static com.gobenefit.util.AppConstant.LOOKUP_CODE_REFERENCE_NULLABLE;
import static com.gobenefit.util.AppConstant.SEPARATOR;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.gobenefit.config.PropertiesConfig;
import com.gobenefit.entity.AbstractEntity;
import com.gobenefit.transport.FileExchangerFactory;
import com.gobenefit.transport.IntermediateFileExchanger;

@Entity
@Table(name = USER_TABLE)
public class User extends AbstractEntity {

	private static final long serialVersionUID = -6252929887221005487L;

	@NotNull
	@Column(name = "FIRST_NAME", length = 32, nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", length = 28)
	private String lastName;

	@NotNull
	@Column(name = "EMAIL_ID", length = 255, nullable = false, unique = true)
	private String emailId;

	@Column(name = "STATUS", columnDefinition = LOOKUP_CODE_REFERENCE)
	private String status;

	@Column(name = "GENDER", columnDefinition = LOOKUP_CODE_REFERENCE_NULLABLE)
	private String gender;

	@NotNull
	@Column(name = "MOBILE_NO", length = 16, nullable = false)
	private String mobileNo;

	@Column(name = "EXTENSION_NO", length = 16)
	private String extensionNo;

	@Column(name = "PASSWORD", length = 1024)
	private String password;

	private transient Role role;

	private transient Apartment currentApartment;

	private transient String originalPassword;

	private transient List<Flat> flats;

	private transient List<Apartment> apartments;

	private transient String token;

	@SuppressWarnings("unused")
	private transient String imagePath;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Flat> getFlats() {
		return flats;
	}

	public void setFlats(List<Flat> flats) {
		this.flats = flats;
	}

	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Apartment getCurrentApartment() {
		return currentApartment;
	}

	public void setCurrentApartment(Apartment currentApartment) {
		this.currentApartment = currentApartment;
	}

	public String getImagePath() {

		String defaultImagePath = IMG_PARENT_FOLDER_NAME + SEPARATOR + IMG_USER_FOLDER_NAME + SEPARATOR
				+ DEFAULT_USER_IMAGE;
		defaultImagePath = defaultImagePath.replaceAll(Matcher.quoteReplacement(File.separator), SEPARATOR);
		String path = IMG_PARENT_FOLDER_NAME + SEPARATOR + IMG_USER_FOLDER_NAME + SEPARATOR + getId() + SEPARATOR
				+ IMG_USER_PROFILE;
		path = path.replaceAll(Matcher.quoteReplacement(File.separator), SEPARATOR);
		String amazonImagePath = PropertiesConfig.getString(APP_AMAZONS3_STATIC_BUCKET_ENDPOINT) + SEPARATOR
				+ PropertiesConfig.getString(APP_AMAZONS3_STATIC_BUCKET_NAME) + SEPARATOR;
		try {
			IntermediateFileExchanger fileExchanger = FileExchangerFactory.getIntermediateFileExchanger(AMAZONS3);
			return fileExchanger.exist(path) ? amazonImagePath + path : amazonImagePath + defaultImagePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}