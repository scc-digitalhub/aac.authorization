package it.smartcommunitylab.aac.authorization;

import java.util.HashMap;
import java.util.Map;

import it.smartcommunitylab.aac.authorization.model.AuthSchema;
import it.smartcommunitylab.aac.authorization.model.Authorization;

public class SimpleAuthStorage implements AuthStorage {

	private Map<Authorization, int[]> storage = new HashMap<>();

	private AuthSchema schema;

	public SimpleAuthStorage(AuthSchema schema) {
		this.schema = schema;
	}

	@Override
	public void insert(Authorization auth) {
		storage.put(auth, new int[0]);

	}

	@Override
	public void remove(Authorization auth) {
		storage.remove(auth);

	}

	@Override
	public boolean search(Authorization auth) {
		boolean checked = storage.containsKey(auth);
		if (checked) {
			return true;
		} else { // check if there is a WILDCARD
			return false;
		}
	}

}
