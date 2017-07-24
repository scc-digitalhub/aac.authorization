package it.smartcommunitylab.aac.authorization.model;

public class RequestedAuthorization {
	private Resource resource;
	private AuthorizationUser entity;
	private String action;

	public RequestedAuthorization(String action, Resource resource, AuthorizationUser entity) {
		super();
		this.resource = resource;
		this.entity = entity;
		this.action = action;
	}

	public Resource getResource() {
		return resource;
	}

	public AuthorizationUser getEntity() {
		return entity;
	}

	public String getAction() {
		return action;
	}

}
