package com.gobenefit.service.impl;

import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.ApplicationUtils.getUniqueRandomString;
import static com.gobenefit.util.ApplicationUtils.getUserActivationTokenMailObject;
import static com.gobenefit.util.ApplicationUtils.getUserPasswordMailObject;
import static com.gobenefit.util.ApplicationUtils.setupUserPassword;
import static com.gobenefit.util.EncryptionUtils.getSaltedHash;
import static com.gobenefit.util.HttpUtils.addNewUserToken;
import static com.gobenefit.util.HttpUtils.isTokenExist;
import static com.gobenefit.util.LookupCodeConstants.USER_STATUS_ENABLE;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.FlatDAO;
import com.gobenefit.dao.UserActivationDAO;
import com.gobenefit.dao.UserDAO;
import com.gobenefit.dao.UserFlatDAO;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Role;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserActivation;
import com.gobenefit.entity.impl.UserAuthenticationToken;
import com.gobenefit.mail.ApplicationMailSender;
import com.gobenefit.service.UserService;
import com.gobenefit.vo.PagingSearchResult;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserFlatDAO userFlatDAO;

	@Autowired
	private UserActivationDAO userActivationDAO;

	@Autowired
	private FlatDAO flatDAO;

	@Value("${app.user.creation.token.validation.enable}")
	private Boolean enableTokenValidation;

	@Value("${app.security.authentication.token.length}")
	private Integer tokenLength;

	@Value("${app.security.authentication.token.expiry.second}")
	private Long tokenExpirySecond;

	@Value("${app.name}")
	private String appName;

	@Autowired
	private ApplicationMailSender applicationMailSender;

	@Override
	@Transactional
	public void sendActivationToken(User user) {
		UserActivation activationToken = null;
		String token = null;
		activationToken = getActivationToken(user.getEmailId());
		if (activationToken == null) {
			token = getUniqueRandomString(6);
		} else {
			token = activationToken.getUserToken();
		}
		user.setToken(token);
		SimpleMailMessage message = getUserActivationTokenMailObject(user, appName);
		applicationMailSender.sendMail(message);
		Date date = new Date();
		if (activationToken == null) {
			activationToken = new UserActivation();
			activationToken.setCreationDate(date);
			activationToken.setModifiedDate(date);
			activationToken.setEmailId(user.getEmailId());
			activationToken.setUserToken(token);
			userActivationDAO.createEntity(activationToken);
		} else {
			activationToken.setCreationDate(date);
			activationToken.setModifiedDate(date);
			userActivationDAO.updateEntity(activationToken);

		}
	}

	@Override
	@Transactional
	public Long createEntity(User user) {
		Date currentDate = new Date();
		user.setCreationDate(currentDate);
		user.setModifiedDate(currentDate);
		LOGGER.debug("Calling the DAO method for user " + user.getEmailId() + " creation.");
		user.setEmailId(user.getEmailId().toLowerCase().trim());
		setupUserPassword(user);
		Long userId = userDAO.createEntity(user);
		sendPasswordMail(user);
		LOGGER.debug("User " + user.getEmailId() + " created successfully.");
		return userId;
	}

	@Override
	public void updateEntity(User entity) {
		userDAO.updateEntity(entity);
	}

	@Override
	public PagingSearchResult<User> getEntities(Map<String, Object> criteriaMap) {
		return userDAO.getEntities(criteriaMap);
	}

	@Override
	public User getEntity(Long entityId) {
		return userDAO.getEntity(entityId, User.class);
	}

	@Override
	public void deleteEntity(User entity) {
	}

	@Override
	public User authenticate(String emailId, String password) {
		LOGGER.debug("In authenticate()");
		User user = getUserByUsername(emailId);
		if (user != null) {
			String hashPassword = getSaltedHash(emailId.substring(0, emailId.indexOf("@")), password);
			if (hashPassword.equals(user.getPassword()) && USER_STATUS_ENABLE.equals(user.getStatus())) {
				LOGGER.info("User(" + emailId + ") authenticated successfully.");
				user.setPassword(null);
				return user;
			}
		}
		LOGGER.info("Invalid user(" + emailId + ")credential for authentication.");
		throw new SecurityException();

	}

	@Override
	public User getUserByUsername(String emailId) {
		return userDAO.getUserByEmailId(emailId);
	}

	private void sendPasswordMail(User user) {
		if (isNotBlank(user.getOriginalPassword())) {
			SimpleMailMessage message = getUserPasswordMailObject(user, appName);
			applicationMailSender.sendMail(message);
		} else {
			throw new RuntimeException("Empty password cannot be sent to the user.");
		}
	}

	public UserAuthenticationToken authenticateUser(String username, String password) {
		User user = authenticate(username, password);
		String token = getUniqueToken();
		return getUserAuthenticationToken(user, token);
	}

	private UserAuthenticationToken getUserAuthenticationToken(User user, String token) {
		UserAuthenticationToken authToken = new UserAuthenticationToken();
		Date date = new Date();
		Date expiryDate = new Date();
		expiryDate.setTime(expiryDate.getTime() + tokenExpirySecond * 1000);
		authToken.setIssueDate(date);
		authToken.setExpiryDate(expiryDate);
		authToken.setToken(token);
		authToken.setUser(user);
		user.setApartments(getUserApartments(user.getId()));
		if (user.getApartments() != null && user.getApartments().size() == 1) {
			user.setCurrentApartment(user.getApartments().get(0));
			user.setRole(userFlatDAO.getUserApartmentRole(user.getId(), user.getApartments().get(0).getId()));
			Map<String, Object> criteriaMap = new HashMap<>();
			criteriaMap.put(APARTMENT_ID, user.getCurrentApartment().getId());
			criteriaMap.put(USER_ID, user.getId());
			user.setFlats(flatDAO.getUsersApartmentFlats(criteriaMap));
		}
		addNewUserToken(authToken);
		return authToken;
	}

	@Override
	public Role getUserApartmentRole(Long userId, Long apartmentId) {
		return userFlatDAO.getUserApartmentRole(userId, apartmentId);
	}

	private String getUniqueToken() {
		String token = getUniqueRandomString(tokenLength);
		if (isTokenExist(token)) {
			getUniqueToken();
		}
		return token;
	}

	@Override
	public List<Apartment> getUserApartments(Long userId) {
		return userDAO.getUserApartments(userId);
	}

	@Override
	public UserActivation getActivationToken(String emailId) {
		return userActivationDAO.getUserActivationTokenByEmailId(emailId.toLowerCase().trim());
	}

	@Override
	@Transactional
	public void deleteActivationToken(Long entityId) {
		userActivationDAO.deleteEntityById(entityId, UserActivation.class);
	}

	@Override
	@Transactional
	public void approveUser(Long userId) {

	}

	@Override
	public List<User> getUsersByIdList(Collection<Long> userIdList) {
		return userDAO.getUsersByIdList(userIdList);
	}

}
