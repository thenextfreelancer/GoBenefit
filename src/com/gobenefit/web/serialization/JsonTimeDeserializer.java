package com.gobenefit.web.serialization;

import java.io.IOException;
import java.sql.Time;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonTimeDeserializer extends JsonDeserializer<Time> {

	@Override
	public Time deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext)
			throws IOException, JsonProcessingException {
		return Time.valueOf(jsonparser.getText());
	}

}