package it.smartcommunitylab.aac.authorization;

import it.smartcommunitylab.aac.authorization.model.AuthSchema;
import it.smartcommunitylab.aac.authorization.model.Authorization;

public class AuthHelper {

	private final AuthSchema policy = new AuthSchema();

	public void insert(Authorization auth) {

		boolean isValidResource = policy.isValid(auth.getResource());
		if (isValidResource) {

			// struttura supporto chiave ut -> l<auth>
		}

	}

	public void remove(Authorization auth) {

	}

	public boolean validate(Authorization auth) {
		return false;
	}

}
