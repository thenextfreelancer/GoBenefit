package com.gobenefit.entity.impl;

import static com.gobenefit.config.PropertiesConstant.APP_AMAZONS3_STATIC_BUCKET_ENDPOINT;
import static com.gobenefit.config.PropertiesConstant.APP_AMAZONS3_STATIC_BUCKET_NAME;
import static com.gobenefit.db.DBConstant.APARTMENT_TABLE;
import static com.gobenefit.util.AppConstant.DEFUALT_APARTMENT_IMAGE;
import static com.gobenefit.util.AppConstant.IMG_APARTMENT_FOLDER_NAME;
import static com.gobenefit.util.AppConstant.IMG_PARENT_FOLDER_NAME;
import static com.gobenefit.util.AppConstant.SEPARATOR;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.gobenefit.config.PropertiesConfig;
import com.gobenefit.entity.AbstractEntity;

@Entity
@Table(name = APARTMENT_TABLE)
public class Apartment extends AbstractEntity {

	private static final long serialVersionUID = 6443330626789488304L;

	@Column(name = "NAME", length = 256, nullable = false)
	@Size(min = 1, max = 256)
	private String name;

	@Column(name = "FLAT_COUNT")
	Integer flatCount;

	@OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Tower> towers;

	@OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Club> clubs;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
	private Address address;

	@OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Flat> flats;

	private transient Set<User> users;

	@SuppressWarnings("unused")
	private transient String imagePath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFlatCount() {
		return flatCount;
	}

	public void setFlatCount(Integer flatCount) {
		this.flatCount = flatCount;
	}

	public Set<Tower> getTowers() {
		return towers;
	}

	public void setTowers(Set<Tower> towers) {
		this.towers = towers;
	}

	public Set<Club> getClubs() {
		return clubs;
	}

	public void setClubs(Set<Club> clubs) {
		this.clubs = clubs;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Flat> getFlats() {
		return flats;
	}

	public void setFlats(Set<Flat> flats) {
		this.flats = flats;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getImagePath() {

		String userImagePath = IMG_PARENT_FOLDER_NAME + SEPARATOR + IMG_APARTMENT_FOLDER_NAME + SEPARATOR
				+ DEFUALT_APARTMENT_IMAGE;
		String imagePath = userImagePath.replaceAll(Matcher.quoteReplacement(File.separator), SEPARATOR);
		// fileExchngerType = LOCAL;
		// if (!ApplicationUtils.isApplicationDeployedLocally()) {
		// userImagePath = imagePath;
		return PropertiesConfig.getString(APP_AMAZONS3_STATIC_BUCKET_ENDPOINT) + SEPARATOR
				+ PropertiesConfig.getString(APP_AMAZONS3_STATIC_BUCKET_NAME) + SEPARATOR + imagePath;
		// fileExchngerType = AMAZONS3;
		// // }
		// try {
		// IntermediateFileExchanger fileExchanger = FileExchangerFactory
		// .getIntermediateFileExchanger(fileExchngerType);
		// return fileExchanger.exist(userImagePath) ? imagePath : "";
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return "";
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
