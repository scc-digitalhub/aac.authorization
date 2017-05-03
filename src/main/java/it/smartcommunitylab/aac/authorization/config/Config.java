package it.smartcommunitylab.aac.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.smartcommunitylab.aac.authorization.AuthorizationHelper;
import it.smartcommunitylab.aac.authorization.AuthorizationSchemaHelper;
import it.smartcommunitylab.aac.authorization.AuthorizationStorage;
import it.smartcommunitylab.aac.authorization.MongoAuthorizationSchemaHelper;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthorizationStorage;

@Configuration
public class Config {

	@Bean
	public AuthorizationSchemaHelper authSchema() {
		return new MongoAuthorizationSchemaHelper();
	}

	@Bean
	public AuthorizationStorage authStorage() {
		return new MongoAuthorizationStorage();
	}

	@Bean
	public AuthorizationHelper authHelper() {
		return new AuthorizationHelper();
	}
}
