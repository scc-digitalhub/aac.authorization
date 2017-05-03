package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorizationUser {
	private String id;
	private String type;

	public AuthorizationUser(String id, String type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
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
		AuthorizationUser rhs = (AuthorizationUser) obj;
		return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 3).append(id).append(type).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id).append("type", type)
				.build();
	}

}
