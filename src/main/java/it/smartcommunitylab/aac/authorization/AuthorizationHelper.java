package it.smartcommunitylab.aac.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.smartcommunitylab.aac.authorization.model.Authorization;

public class AuthorizationHelper {

	private final static Logger logger = LoggerFactory.getLogger(AuthorizationHelper.class);

	@Autowired
	private AuthorizationSchemaHelper policy;

	@Autowired
	private AuthorizationStorage storage;

	public void insert(Authorization auth) {

		boolean isValidResource = policy.isValid(auth.getResource());
		if (isValidResource) {
			storage.insert(auth);
			logger.info("inserted authorization: {}", auth);
		} else {
			logger.warn("tried to insert a not valid authorization: {}", auth);
		}
	}

	public void remove(Authorization auth) {
		storage.remove(auth);
		logger.info("removed authorization: {}", auth);

	}

	public boolean validate(Authorization auth) {
		boolean isAuthGranted = storage.search(auth);
		if (isAuthGranted) {
			logger.info("authorization is granted: {}");
		} else {
			logger.info("authorization is not granted: {}");
		}
		return isAuthGranted;
	}

}
