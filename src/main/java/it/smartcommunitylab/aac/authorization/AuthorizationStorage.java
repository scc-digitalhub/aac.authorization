package it.smartcommunitylab.aac.authorization;

import org.springframework.stereotype.Service;

import it.smartcommunitylab.aac.authorization.model.Authorization;

@Service
public interface AuthorizationStorage {

	public Authorization insert(Authorization auth);

	public void remove(Authorization auth);

	public void remove(String authorizationId);

	public boolean search(Authorization auth);
}
