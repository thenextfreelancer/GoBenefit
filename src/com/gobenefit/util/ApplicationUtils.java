package com.gobenefit.util;

import static com.gobenefit.spring.BeanConstant.LOOKUP_SERVICE_BEAN;
import static com.gobenefit.util.AppConstant.PAGINATION_PAGE_NO;
import static com.gobenefit.util.AppConstant.PAGINATION_RECORD_LIMIT;
import static com.gobenefit.util.EncryptionUtils.getSaltedHash;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import com.gobenefit.entity.impl.LookupCode;
import com.gobenefit.entity.impl.User;
import com.gobenefit.service.LookupCodeService;
import com.gobenefit.service.impl.LookupCodeServiceImpl;
import com.gobenefit.spring.BeanFactory;
import com.gobenefit.vo.PagingSearchResult;;

public class ApplicationUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtils.class);

	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("Access Denied")
			.build();

	private static Map<String, LookupCode> lookupCodeMap = new HashMap<>();

	static final Random random = new SecureRandom();

	static final int startChar = (int) '!';

	static final int endChar = (int) '~';

	public static String getValueText(String code) {
		LookupCode lookupCode = lookupCodeMap.get(code);
		if (lookupCode != null) {
			return lookupCode.getValue();
		}
		return null;
	}

	public static void setupLookupCodes() throws Exception {
		LookupCodeService lookupCodeService = (LookupCodeServiceImpl) BeanFactory
				.getWebContextBean(LOOKUP_SERVICE_BEAN);
		List<LookupCode> lookupCodeList = lookupCodeService.getEntities();
		pupulateLookupMap(lookupCodeList);
	}

	public static void pupulateLookupMap(List<LookupCode> lookupCodeList) {
		if (lookupCodeList != null && !lookupCodeList.isEmpty()) {
			for (LookupCode lookupCode : lookupCodeList) {
				lookupCodeMap.put(lookupCode.getCode(), lookupCode);
			}
		}
	}

	public static WebApplicationException throwWebApplicationException(Exception e) {
		if (!(e instanceof WebApplicationException)) {
			return new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(ExceptionUtils.getRootCauseMessage(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
		} else {
			return (WebApplicationException) e;
		}
	}

	public static WebApplicationException throwWebApplicationException(String message) {
		return new WebApplicationException(
				Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build());
	}

	public static WebApplicationException throwUnauthorizedException() {
		return new WebApplicationException(ACCESS_DENIED);
	}

	public static void addDirOnClassPath(String folderPath) throws Exception {
		File f = new File(folderPath);
		URI u = f.toURI();
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[] { u.toURL() });
	}

	public static Map<String, Object> getQueryParameterMap(UriInfo uriInfo) {
		Map<String, List<String>> map = uriInfo.getQueryParameters();
		Map<String, Object> queryMap = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<String> valueList = entry.getValue();
			if (valueList != null && !valueList.isEmpty()) {
				if (entry.getKey().equals(PAGINATION_PAGE_NO) || entry.getKey().equals(PAGINATION_RECORD_LIMIT)) {
					int value = Integer.parseInt(valueList.get(0));
					if (entry.getKey().equals(PAGINATION_PAGE_NO)) {
						value = value - 1;
					}
					queryMap.put(entry.getKey(), value);
				} else {
					queryMap.put(entry.getKey(), valueList.get(0));
				}
			}
		}
		return queryMap;
	}

	public static PagingSearchResult<?> getPeginationResultObject(List<?> entityList, Long totalCount) {
		return new PagingSearchResult<>(entityList, totalCount);
	}

	public static PagingSearchResult<?> getPeginationResultObject(List<?> entityList) {
		return getPeginationResultObject(entityList, new Long(entityList.size()));
	}

	public static String getUniqueRandomString(int maxLength) {
		return RandomStringUtils.randomAlphanumeric(maxLength).toUpperCase();
	}

	public static SimpleMailMessage getUserPasswordMailObject(User user, String appName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmailId());
		message.setSubject(appName + " - Password");
		message.setText(user.getOriginalPassword());
		return message;
	}

	public static SimpleMailMessage getUserActivationTokenMailObject(User user, String appName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmailId());
		message.setSubject(appName + " - Token");
		message.setText(user.getToken());
		return message;
	}

	public static void setupUserPassword(User user) {
		String password = user.getPassword();
		if (isBlank(password)) {
			LOGGER.debug("Generating the password for user " + user.getEmailId());
			password = getUniqueRandomString(10);
		}
		user.setOriginalPassword(password);
		LOGGER.debug("Password has been generated for user " + user.getEmailId());
		String emailId = user.getEmailId();
		LOGGER.debug("Getting encrpted password for user " + user.getEmailId());
		password = getSaltedHash(emailId.substring(0, emailId.indexOf("@")), password);
		LOGGER.debug("Encrpted password for user " + user.getEmailId() + " : " + password);
		user.setPassword(password);
	}

	public static String getDateFormat() {
		return "yyyy-MM-dd HH:mm:ss";
	}
}