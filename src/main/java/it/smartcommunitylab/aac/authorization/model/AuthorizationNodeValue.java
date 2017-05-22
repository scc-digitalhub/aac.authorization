package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorizationNodeValue {
	private AuthorizationNodeParam definition;
	private String value;

	public static final String ALL_VALUE = "*";

	public AuthorizationNodeValue(String qname, String name, String value) {
		definition = new AuthorizationNodeParam(qname, name);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public final AuthorizationNodeParam getDefinition() {
		return definition;
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
		AuthorizationNodeValue rhs = (AuthorizationNodeValue) obj;
		return new EqualsBuilder().append(definition, rhs.definition).append(value, rhs.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 13).append(definition).append(value).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("definition", definition)
				.append("value", value).build();
	}

}
