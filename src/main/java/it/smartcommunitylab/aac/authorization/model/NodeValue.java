package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NodeValue {
	private NodeParameter definition;
	private String value;

	public static final String ALL_VALUE = "*";

	public NodeValue(String qname, String name, String value) {
		definition = new NodeParameter(qname, name);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public final NodeParameter getDefinition() {
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
		NodeValue rhs = (NodeValue) obj;
		return new EqualsBuilder().append(definition, rhs.definition).append(value, rhs.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 13).append(definition).append(value).hashCode();
	}

}
