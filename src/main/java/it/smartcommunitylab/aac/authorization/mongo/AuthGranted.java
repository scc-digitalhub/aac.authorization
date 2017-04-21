package it.smartcommunitylab.aac.authorization.mongo;

import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;

class AuthGranted {
	private String id;
	private String subject;
	private String action;
	private AuthUser entity;
	private ResourceDocument resource;

	public AuthGranted(final Authorization auth) {
		if (auth != null) {
			id = auth.getId();
			subject = auth.getSubject();
			action = auth.getAction();
			entity = auth.getEntity();
			resource = new ResourceDocument(auth.getResource());
		}
	}

	public Authorization toAuthorization() {
		return new Authorization(id, subject, action, resource.toResource(), entity);
	}

	public String getId() {
		return id;
	}

	public String getSubject() {
		return subject;
	}

	public String getAction() {
		return action;
	}

	public AuthUser getEntity() {
		return entity;
	}

	public ResourceDocument getResource() {
		return resource;
	}

}
