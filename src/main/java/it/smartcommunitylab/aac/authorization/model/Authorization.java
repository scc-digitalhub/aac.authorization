package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Authorization {
	private String subject;
	private String action;
	private Resource resource;
	private AuthUser entity;

	public Authorization(String subject, String action, Resource resource, AuthUser entity) {
		this.subject = subject;
		this.action = action;
		this.resource = resource;
		this.entity = entity;
	}

	public String getSubject() {
		return subject;
	}

	public String getAction() {
		return action;
	}

	public Resource getResource() {
		return resource;
	}

	public AuthUser getEntity() {
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

}
