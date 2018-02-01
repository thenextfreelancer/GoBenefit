package com.gobenefit.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gobenefit.dao.FlatDAO;
import com.gobenefit.dao.UserDAO;
import com.gobenefit.dao.UserFlatDAO;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.User;
import com.gobenefit.entity.impl.UserFlat;
import com.gobenefit.mail.ApplicationMailSender;
import com.gobenefit.service.UserFlatService;
import com.gobenefit.util.LookupCodeConstants;

@Service
public class UserFlatServiceImpl implements UserFlatService {

	@Autowired
	UserFlatDAO userFlatDAO;

	@Autowired
	FlatDAO flatDao;

	@Autowired
	UserDAO userDAO;

	@Value("${app.name}")
	private String appName;

	@Autowired
	private ApplicationMailSender applicationMailSender;

	@Override
	@Transactional
	public void updateEntity(UserFlat entity) {
		userFlatDAO.updateEntity(entity);
	}

	@Override
	@Transactional
	public void deleteEntity(UserFlat entity) {
		userFlatDAO.deleteEntity(entity);
	}

	@Override
	@Transactional
	public void deleteEntityById(Long entityId) {
		userFlatDAO.deleteEntityById(entityId, UserFlat.class);
	}

	@Override
	@Transactional
	public Long createEntity(UserFlat entity) {
		Date date = new Date();
		entity.setCreationDate(date);
		entity.setModifiedDate(date);
		return userFlatDAO.createEntity(entity);
	}

	@Override
	public List<UserFlat> getApartmentFlats(Map<String, Object> criteriaMap) {
		return userFlatDAO.getApartmentFlats(criteriaMap);
	}

	@Override
	@Transactional
	public void approve(UserFlat userFlat) {
		UserFlat existingUserFlat = userFlatDAO.getEntity(userFlat.getId(), UserFlat.class);
		existingUserFlat.setStatus(LookupCodeConstants.USER_STATUS_ENABLE);
		userFlatDAO.updateEntity(existingUserFlat);
		Flat flat = flatDao.getEntity(existingUserFlat.getFlatId(), Flat.class);
		SimpleMailMessage message = getApprovalMail(userDAO.getEntity(existingUserFlat.getUserId(), User.class),
				appName, flat.getFlatNumber());
		applicationMailSender.sendMail(message);

	}

	private SimpleMailMessage getApprovalMail(User user, String appName, int flatNumber) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmailId());
		message.setSubject(appName + " - Flat Approval");
		message.setText("Your request has been approved for Flat " + flatNumber);
		return message;
	}

	@Override
	@Transactional
	public void decline(UserFlat userFlat) {
		UserFlat existingUserFlat = userFlatDAO.getEntity(userFlat.getId(), UserFlat.class);
		existingUserFlat.setStatus(LookupCodeConstants.USER_STATUS_ENABLE);
		Flat flat = flatDao.getEntity(existingUserFlat.getFlatId(), Flat.class);
		SimpleMailMessage message = getDeclineMail(userDAO.getEntity(existingUserFlat.getUserId(), User.class), appName,
				flat.getFlatNumber());
		applicationMailSender.sendMail(message);
		userFlatDAO.deleteEntity(existingUserFlat);

	}

	private SimpleMailMessage getDeclineMail(User user, String appName, int flatNumber) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmailId());
		message.setSubject(appName + " - Flat Decline");
		message.setText("Your request has been declined for Flat " + flatNumber);
		return message;
	}

}
