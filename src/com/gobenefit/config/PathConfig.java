package com.gobenefit.config;

import java.io.File;

public class PathConfig {
	private static String workingDirectory = null;

	public static void setWorkingDirectory(String dir) {
		workingDirectory = dir;
	}

	public static String getWorkingDirectory() {
		return workingDirectory;
	}

	public static String getHibernateConfigPath() {
		return workingDirectory + File.separatorChar
				+ PropertiesConfig.getString(PropertiesConstant.HIBERNATE_CONFIG_FILE);
	}

	public static String getPropertiesConfigPath() {
		return workingDirectory + File.separatorChar + PropertiesConstant.APPLICATION_CONFIG_FILE;
	}
}
