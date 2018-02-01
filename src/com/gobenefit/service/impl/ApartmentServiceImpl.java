package com.gobenefit.service.impl;

import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.ApplicationUtils.getUserPasswordMailObject;
import static com.gobenefit.util.ApplicationUtils.setupUserPassword;
import static com.gobenefit.util.LookupCodeConstants.USER_STATUS_ENABLE;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.ApartmentDAO;
import com.gobenefit.dao.TowerDAO;
import com.gobenefit.dao.UserDAO;
import com.gobenefit.dao.UserFlatDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.mail.ApplicationMailSender;
import com.gobenefit.service.ApartmentService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class ApartmentServiceImpl implements ApartmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentServiceImpl.class);

	@Autowired
	private ApartmentDAO apartmentDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserFlatDAO userFlatDAO;

	@Autowired
	private TowerDAO towerDAO;

	@Value("${app.name}")
	private String appName;

	@Autowired
	private ApplicationMailSender applicationMailSender;

	@Override
	@Transactional
	public Long createEntity(Apartment apartment) throws Exception {
		LOGGER.info("In createEntity() - DAO");
		Date date = new Date();
		apartment.setCreationDate(date);
		apartment.setModifiedDate(date);
		Set<Flat> flats = new HashSet<>();
		Tower tower = new Tower();
		tower.setDefaultTower(Boolean.TRUE);
		tower.setName("Default");
		tower.setFlatCount(-1);
		tower.setApartment(apartment);
		tower.setTotalFloor(-1);
		towerDAO.createEntity(tower);
		Flat flat = new Flat();
		flat.setTower(tower);
		flat.setApartment(apartment);
		flat.setCreationDate(date);
		flat.setFlatNumber(-1);
		flat.setFloorNumber(-1);
		flats.add(flat);
		apartment.setFlats(flats);
		Long apartmentId = apartmentDAO.createEntity(apartment);
		try {
			Set<User> users = apartment.getUsers();
			if (users != null && !users.isEmpty()) {
				for (User user : users) {
					boolean isPasswordCreated = false;
					user.setCreationDate(date);
					user.setModifiedDate(date);
					if (isBlank(user.getPassword())) {
						isPasswordCreated = true;
					}
					LOGGER.debug("Setting up the user password for user: " + user.getEmailId());
					setupUserPassword(user);
					LOGGER.debug("User initialized with generated password.");
					user.setStatus(USER_STATUS_ENABLE);
					LOGGER.debug("Calling DAO method to create user: " + user.getEmailId());
					userDAO.createEntity(user);
					LOGGER.debug("DAO- User(" + user.getEmailId() + ") created.");
					UserFlat userFlat = new UserFlat();
					userFlat.setCreationDate(date);
					userFlat.setModifiedDate(date);
					userFlat.setFlatId(flat.getId());
					userFlat.setUserId(user.getId());
					Role role = new Role();
					role.setRole(RWA_ADMIN_ROLE);
					userFlat.setStatus(USER_STATUS_ENABLE);
					userFlat.setRole(role);
					LOGGER.debug("Creating relationship between user and flat.");
					userFlatDAO.createEntity(userFlat);
					LOGGER.debug("relationship between user and flat created successfully.");
					if (isPasswordCreated) {
						LOGGER.debug("Sending password mail to user: " + user.getEmailId());
						sendPasswordMail(user);
						LOGGER.debug("Password mail sent successfully to user: " + user.getEmailId());
					}
				}
			}
		} catch (PersistenceException e) {
			LOGGER.error("Error occured while creating the apartment. Error: " + e.getMessage(), e);
		}

		apartment.setUsers(null);
		return apartmentId;
	}

	@Override
	public PagingSearchResult<Apartment> getEntities(Map<String, Object> criteriaMap) {
		return apartmentDAO.getEntities(criteriaMap);
	}

	@Override
	public List<Apartment> getEntities() {
		return apartmentDAO.getEntities();
	}

	private void sendPasswordMail(User user) {
		if (isNotBlank(user.getOriginalPassword())) {
			SimpleMailMessage message = getUserPasswordMailObject(user, appName);
			applicationMailSender.sendMail(message);
		} else {
			throw new RuntimeException("Empty password cannot be sent to the user.");
		}
	}

	@Override
	public Apartment getEntity(Long id) {
		return apartmentDAO.getEntity(id, Apartment.class);
	}

}
