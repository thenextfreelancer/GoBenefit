package com.gobenefit.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertiesConfig {

	private static Properties prop = null;

	static {
		prop = new Properties();
		try (InputStream input = new FileInputStream(PathConfig.getPropertiesConfigPath());) {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getString(String propertyName) {
		return prop.getProperty(propertyName);
	}

	@SuppressWarnings("rawtypes")
	public static List getList(String propertyName) {
		String value = getString(propertyName);
		if (StringUtils.isBlank(value)) {
			return Arrays.asList(value.split(","));
		}
		return null;
	}

	public static Integer getInteger(String propertyName) {
		if (!StringUtils.isBlank(getString(propertyName))) {
			return new Integer(getString(propertyName));
		}
		return null;
	}

	public static boolean getBoolean(String propertyName) {
		if (!StringUtils.isBlank(getString(propertyName))) {
			return new Boolean(getString(propertyName));
		}
		return false;
	}

	public static int getInt(String propertyName) {
		if (!StringUtils.isBlank(getString(propertyName))) {
			Integer.parseInt(getString(propertyName));
		}
		return -1;
	}
}
