package it.smartcommunitylab.aac.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.smartcommunitylab.aac.authorization.AuthStorage;
import it.smartcommunitylab.aac.authorization.IAuthSchema;
import it.smartcommunitylab.aac.authorization.model.AuthSchema;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthStorage;

@Configuration
public class Config {

	@Bean
	public IAuthSchema authSchema() {
		return new AuthSchema();
	}

	@Bean
	public AuthStorage authStorage() {
		return new MongoAuthStorage();
	}
}
