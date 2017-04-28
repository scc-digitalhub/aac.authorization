package it.smartcommunitylab.aac.authorization.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;

@Document(collection = "authGranted")
abstract class AuthGranted {
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

	/*
	 * Constructor used by Spring data to convert mongo dbobject in class instance
	 */
	protected AuthGranted(String id, String subject, String action, AuthUser entity, ResourceDocument resource) {
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
