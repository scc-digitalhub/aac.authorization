package it.smartcommunitylab.aac.authorization;

import org.springframework.stereotype.Service;

import it.smartcommunitylab.aac.authorization.model.Authorization;

@Service
public interface AuthorizationHelper {

	Authorization insert(Authorization auth) throws NotValidResourceException;

	void remove(Authorization auth);

	void remove(String authorizationId);

	boolean validate(Authorization auth);

}