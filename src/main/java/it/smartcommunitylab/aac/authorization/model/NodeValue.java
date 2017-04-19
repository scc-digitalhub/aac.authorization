package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NodeValue extends NodeParameter {
	private String value;

	public static final String ALL_VALUE = "*";

	public NodeValue(String qname, String name, String value) {
		super(qname, name);
		this.value = value;
	}

	public String getValue() {
		return value;
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
		return new EqualsBuilder().appendSuper(true).append(value, rhs.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(value).hashCode();
	}

}
