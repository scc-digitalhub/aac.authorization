package it.smartcommunitylab.aac.authorization;

import it.smartcommunitylab.aac.authorization.model.Authorization;

public interface AuthStorage {

	public void insert(Authorization auth);

	public void remove(Authorization auth);

	public boolean search(Authorization auth);
}
