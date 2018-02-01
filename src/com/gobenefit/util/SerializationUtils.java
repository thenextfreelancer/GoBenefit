package com.gobenefit.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gobenefit.entity.Entity;

public class SerializationUtils {

	public static ObjectMapper getJsonObjectMapper(boolean wrapRootValue) {
		return getJsonObjectMapper(wrapRootValue, true);
	}

	public static ObjectMapper getJsonObjectMapper(boolean wrapRootValue, boolean unwrapRootValue) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, unwrapRootValue);
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, wrapRootValue);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

	public static ObjectMapper getJsonObjectMapper() {
		return getJsonObjectMapper(true);
	}

	public static String serialize(Object obj) throws Exception {
		return serialize(obj, getJsonObjectMapper());
	}

	public static String serialize(Object obj, boolean wrapRootValue) throws Exception {
		return serialize(obj, getJsonObjectMapper(wrapRootValue));
	}

	public static String serialize(Object obj, ObjectMapper mapper) throws Exception {
		return serialize(obj, null, mapper);
	}

	public static String serialize(Object obj, Map<Class<?>, Class<?>> mixinConfig) throws Exception {
		return serialize(obj, mixinConfig, getJsonObjectMapper());
	}

	public static String serialize(Object obj, Map<Class<?>, Class<?>> mixinConfig, boolean wrapRootValue)
			throws Exception {
		return serialize(obj, mixinConfig, getJsonObjectMapper(wrapRootValue));
	}

	public static String serialize(Object obj, Map<Class<?>, Class<?>> mixinConfig, ObjectMapper mapper)
			throws Exception {
		StringWriter writer = new StringWriter();
		if (null != mixinConfig && !mixinConfig.isEmpty()) {
			mapper.setMixIns(mixinConfig);
		}
		mapper.writeValue(writer, obj);
		return writer.toString();
	}

	public static Entity deserialize(String data, Class<?> klass)
			throws JsonParseException, JsonMappingException, IOException {
		return deserialize(data, klass, getJsonObjectMapper());
	}

	public static Entity deserialize(String data, Class<?> klass, ObjectMapper mapper)
			throws JsonParseException, JsonMappingException, IOException {
		return (Entity) mapper.readValue(data, klass);
	}

	public static Object deserializeObject(String data, Class<?> klass, ObjectMapper mapper)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(data, klass);
	}

}
