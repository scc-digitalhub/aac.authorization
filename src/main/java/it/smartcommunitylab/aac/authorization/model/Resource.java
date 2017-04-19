package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Resource {
	private String qnameRef;
	private List<NodeValue> values;

	public Resource(String qnameRef, List<NodeValue> values) {
		super();
		this.qnameRef = qnameRef;
		this.values = new ArrayList<>(values);
	}

	public boolean isInstanceOf(Node node) {
		return qnameRef.equals(node.getQname()) && checkLogicalEquivalence(node.getParameters(), values);
	}

	private boolean checkLogicalEquivalence(List<NodeParameter> s1, List<? extends NodeParameter> s2) {
		return false;
	}

	public String getQnameRef() {
		return qnameRef;
	}

	public List<NodeValue> getValues() {
		return values;
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
		Resource rhs = (Resource) obj;
		return new EqualsBuilder().append(qnameRef, rhs.qnameRef).append(values, rhs.values).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 21).append(qnameRef).append(values).hashCode();
	}

}
