package it.smartcommunitylab.aac.authorization.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;

@Document(collection = "authorizationGranted")
abstract class AuthorizationGranted {
	private String id;
	private AuthorizationUser subject;
	private String action;
	private AuthorizationUser entity;
	private ResourceDocument resource;

	public AuthorizationGranted(final Authorization auth) {
		if (auth != null) {
			id = auth.getId();
			subject = auth.getSubject();
			action = auth.getAction();
			entity = auth.getEntity();
			resource = new ResourceDocument(auth.getResource());
		}
	}

	/*
	 * Constructor used by Spring data to convert mongo dbobject in class instance
	 */
	protected AuthorizationGranted(String id, AuthorizationUser subject, String action, AuthorizationUser entity, ResourceDocument resource) {
		this.id = id;
		this.subject = subject;
		this.action = action;
		this.entity = entity;
		this.resource = resource;
	}

	public Authorization toAuthorization() {
		return new Authorization(id, subject, action, resource.toResource(), entity);
	}

	public abstract boolean isChildAuth();

	public String getId() {
		return id;
	}

	public AuthorizationUser getSubject() {
		return subject;
	}

	public String getAction() {
		return action;
	}

	public AuthorizationUser getEntity() {
		return entity;
	}

	public ResourceDocument getResource() {
		return resource;
	}

}
