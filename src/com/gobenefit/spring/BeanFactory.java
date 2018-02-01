package com.gobenefit.spring;

public class BeanFactory {

	public static Object getBean(String beanName) throws Exception {
		return SpringContext.INSTANCE.getSpringContextInstance().getBean(beanName);
	}

	public static Object getWebContextBean(String beanName) throws Exception {
		return WebApplicationContextProvider.getApplicationContext().getBean(beanName);
	}
	
	public static Object getWebContextBean(Class<?> beanClass) throws Exception {
		return WebApplicationContextProvider.getApplicationContext().getBean(beanClass);
	}
}
