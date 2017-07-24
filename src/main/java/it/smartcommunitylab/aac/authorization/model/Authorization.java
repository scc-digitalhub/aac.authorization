package it.smartcommunitylab.aac.authorization.model;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Authorization {
	private String id;
	private AuthorizationUser subject;
	private List<String> actions;
	private Resource resource;
	private AuthorizationUser entity;

	public Authorization(AuthorizationUser subject, List<String> actions, Resource resource, AuthorizationUser entity) {
		this.subject = subject;
		this.actions = actions;
		this.resource = resource;
		this.entity = entity;
	}

	public Authorization(AuthorizationUser subject, String action, Resource resource, AuthorizationUser entity) {
		this.subject = subject;
		this.actions = Arrays.asList(action);
		this.resource = resource;
		this.entity = entity;
	}

	public Authorization(RequestedAuthorization requestedAuthorization) {
		if (requestedAuthorization != null) {
			this.entity = requestedAuthorization.getEntity();
			this.actions = Arrays.asList(requestedAuthorization.getAction());
			this.resource = requestedAuthorization.getResource();
		}
	}

	public Authorization(String id, AuthorizationUser subject, List<String> actions, Resource resource,
			AuthorizationUser entity) {
		this.id = id;
		this.subject = subject;
		this.actions = actions;
		this.resource = resource;
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public AuthorizationUser getSubject() {
		return subject;
	}

	public List<String> getActions() {
		return actions;
	}

	public Resource getResource() {
		return resource;
	}

	public AuthorizationUser getEntity() {
		return entity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Authorization rhs = (Authorization) obj;
		return new EqualsBuilder().append(subject, rhs.subject).append(actions, rhs.actions)
				.append(resource, rhs.resource).append(entity, rhs.entity).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 35).append(subject).append(actions).append(resource).append(entity).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("subject", subject)
				.append("actions", actions).append("resource", resource).append("entity", entity).build();
	}

}
