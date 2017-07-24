package it.smartcommunitylab.aac.authorization.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;

@Document(collection = "authorizationGranted")
abstract class AuthorizationGranted {
	private String id;
	private AuthorizationUser subject;

	// to maintain compatibility with previous field name
	@Field(value = "action")
	private List<String> actions;
	private AuthorizationUser entity;
	private ResourceDocument resource;

	public AuthorizationGranted(final Authorization auth) {
		if (auth != null) {
			id = auth.getId();
			subject = auth.getSubject();
			actions = auth.getActions();
			entity = auth.getEntity();
			resource = new ResourceDocument(auth.getResource());
		}
	}

	/*
	 * Constructor used by Spring data to convert mongo dbobject in class instance
	 */
	protected AuthorizationGranted(String id, AuthorizationUser subject, List<String> actions, AuthorizationUser entity,
			ResourceDocument resource) {
		this.id = id;
		this.subject = subject;
		this.actions = actions;
		this.entity = entity;
		this.resource = resource;
	}

	public Authorization toAuthorization() {
		return new Authorization(id, subject, actions, resource.toResource(), entity);
	}

	public abstract boolean isChildAuth();

	public String getId() {
		return id;
	}

	public AuthorizationUser getSubject() {
		return subject;
	}

	public List<String> getActions() {
		return actions;
	}

	public AuthorizationUser getEntity() {
		return entity;
	}

	public ResourceDocument getResource() {
		return resource;
	}

}
