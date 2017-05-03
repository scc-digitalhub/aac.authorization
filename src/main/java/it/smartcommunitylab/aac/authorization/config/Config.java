package it.smartcommunitylab.aac.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.smartcommunitylab.aac.authorization.AuthHelper;
import it.smartcommunitylab.aac.authorization.AuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.AuthStorage;
import it.smartcommunitylab.aac.authorization.MongoAuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthStorage;

@Configuration
public class Config {

	@Bean
	public AuthSchemaHelper authSchema() {
		return new MongoAuthSchemaHelper();
	}

	@Bean
	public AuthStorage authStorage() {
		return new MongoAuthStorage();
	}

	@Bean
	public AuthHelper authHelper() {
		return new AuthHelper();
	}
}
