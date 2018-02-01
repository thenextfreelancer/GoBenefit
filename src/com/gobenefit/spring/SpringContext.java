package com.gobenefit.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Rahul
 * 
 */
public enum SpringContext {

	INSTANCE;

	private ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

	public ApplicationContext getSpringContextInstance() {
		return appContext;
	}

	/**
	 * Close this application context, releasing all resources and locks that
	 * the implementation might hold. This includes destroying all cached
	 * singleton beans.
	 */
	public void close() {
		appContext = getSpringContextInstance();
		if (appContext instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) appContext).close();
		}
	}

}
