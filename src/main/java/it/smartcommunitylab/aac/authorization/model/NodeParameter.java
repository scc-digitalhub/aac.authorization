package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NodeParameter {
	private String qname;
	private String name;

	public NodeParameter(String qname, String name) {
		this.qname = qname;
		this.name = name;
	}

	public String getQname() {
		return qname;
	}

	public String getName() {
		return name;
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
		NodeParameter rhs = (NodeParameter) obj;
		return new EqualsBuilder().append(qname, rhs.qname).append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(89, 15).append(qname).append(name).hashCode();
	}

}
