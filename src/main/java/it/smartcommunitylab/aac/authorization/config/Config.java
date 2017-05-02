package it.smartcommunitylab.aac.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.smartcommunitylab.aac.authorization.AuthStorage;
import it.smartcommunitylab.aac.authorization.SimpleAuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.AuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthStorage;

@Configuration
public class Config {

	@Bean
	public AuthSchemaHelper authSchema() {
		return new SimpleAuthSchemaHelper();
	}

	@Bean
	public AuthStorage authStorage() {
		return new MongoAuthStorage();
	}
}
