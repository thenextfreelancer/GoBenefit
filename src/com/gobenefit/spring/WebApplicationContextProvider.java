package com.gobenefit.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gobenefit.util.HttpUtils;

public class WebApplicationContextProvider {

	private static WebApplicationContext context;

	public static ApplicationContext getApplicationContext() {
		if (context == null) {
			context = WebApplicationContextUtils.getRequiredWebApplicationContext(HttpUtils.getSevletContext());
		}
		return context;
	}
}
