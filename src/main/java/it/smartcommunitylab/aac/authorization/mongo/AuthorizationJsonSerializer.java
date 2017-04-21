package it.smartcommunitylab.aac.authorization.mongo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import it.smartcommunitylab.aac.authorization.model.Authorization;

public class AuthorizationJsonSerializer extends JsonSerializer<Authorization> {

	@Override
	public void serialize(Authorization obj, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		generator.writeObject(new AuthGranted(obj));

	}

}
