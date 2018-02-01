package com.gobenefit.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gobenefit.app.Main;

public class AppServletContextListener implements ServletContextListener {

	Main mainApp = null;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (mainApp != null)
			try {
				mainApp.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
//		try {
//			HttpUtils.setSevletContext(sce.getServletContext());
//			String appWorkingDirPath = null;
//			// fetch via system environment variable
//			appWorkingDirPath = System.getenv(PropertiesConstant.RESOURCE_DIRECTORY_PATH);
//			if (appWorkingDirPath == null) {
//				// fetch via system property
//				appWorkingDirPath = System.getProperty(PropertiesConstant.RESOURCE_DIRECTORY_PATH);
//				if (appWorkingDirPath == null) {
//					appWorkingDirPath = sce.getServletContext()
//							.getInitParameter(PropertiesConstant.RESOURCE_DIRECTORY_PATH);
//				}
//			}
//			if (appWorkingDirPath == null) {
//				throw new ExceptionInInitializerError(
//						"Can't locate application working directory, please set variable \"app.resource.config.path\" either as system environment variable or as a application system property");
//			}
//			mainApp = new Main(appWorkingDirPath);
//			mainApp.boot();
//			HttpUtils.setContextPath(sce.getServletContext().getContextPath());
//		} catch (Exception e) {
//			throw new ExceptionInInitializerError(e);
//		}
	}

}
