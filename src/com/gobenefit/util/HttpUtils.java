package com.gobenefit.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import com.gobenefit.entity.impl.UserAuthenticationToken;

public class HttpUtils {

	private static Map<String, UserAuthenticationToken> userTokenMap = new ConcurrentHashMap<>();

	private static ServletContext sevletContext;

	private static String applicationContextPath = null;

	public static ServletContext getSevletContext() {
		return sevletContext;
	}

	public static void setSevletContext(ServletContext context) {
		sevletContext = context;
	}

	public static String getContextPath() {
		return applicationContextPath;
	}

	public static void setContextPath(String contextPath) {
		applicationContextPath = contextPath;
	}

	public static boolean isTokenExist(String accessToken) {
		return userTokenMap.containsKey(accessToken);
	}

	public static UserAuthenticationToken getUserAuthenticationToken(String accessToken) {
		return userTokenMap.get(accessToken);
	}

	public static void removeUserToken(String accessToken) {
		userTokenMap.remove(accessToken);
	}

	public static void addNewUserToken(UserAuthenticationToken userAuthenticationToken) {
		userTokenMap.put(userAuthenticationToken.getToken(), userAuthenticationToken);
	}

}
