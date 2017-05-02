package it.smartcommunitylab.aac.authorization;

import org.springframework.beans.factory.annotation.Autowired;

import it.smartcommunitylab.aac.authorization.model.Authorization;

public class AuthHelper {

	@Autowired
	private IAuthSchema policy;

	@Autowired
	private AuthStorage storage;

	public void insert(Authorization auth) {

		boolean isValidResource = policy.isValid(auth.getResource());
		if (isValidResource) {
			storage.insert(auth);
		}
	}

	public void remove(Authorization auth) {
		storage.remove(auth);

	}

	public boolean validate(Authorization auth) {
		return storage.search(auth);
	}

}
