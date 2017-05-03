package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Authorization {
	private String id;
	private AuthorizationUser subject;
	private String action;
	private Resource resource;
	private AuthorizationUser entity;

	public Authorization(AuthorizationUser subject, String action, Resource resource, AuthorizationUser entity) {
		this.subject = subject;
		this.action = action;
		this.resource = resource;
		this.entity = entity;
	}

	public Authorization(String id, AuthorizationUser subject, String action, Resource resource, AuthorizationUser entity) {
		this.id = id;
		this.subject = subject;
		this.action = action;
		this.resource = resource;
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public AuthorizationUser getSubject() {
		return subject;
	}

	public String getAction() {
		return action;
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
		return new EqualsBuilder().append(subject, rhs.subject).append(action, rhs.action)
				.append(resource, rhs.resource).append(entity, rhs.entity).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 35).append(subject).append(action).append(resource).append(entity).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("subject", subject)
				.append("action", action).append("resource", resource).append("entity", entity).build();
	}

}
