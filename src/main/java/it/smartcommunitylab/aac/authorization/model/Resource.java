package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Resource {
	private String qnameRef;
	private List<AuthorizationNodeValue> values;

	public Resource(String qnameRef, List<AuthorizationNodeValue> values) {
		this.qnameRef = qnameRef;
		this.values = new ArrayList<>(values);
	}

	public boolean isInstanceOf(AuthorizationNode node) {
		return qnameRef.equals(node.getQname()) && checkLogicalEquivalence(node.getParameters(), values);
	}

	private boolean checkLogicalEquivalence(List<AuthorizationNodeParam> s1, List<AuthorizationNodeValue> s2) {
		for (AuthorizationNodeValue nv : s2) {
			if (!s1.contains(nv.getDefinition())) {
				return false;
			}
		}
		return true;
	}

	public String getQnameRef() {
		return qnameRef;
	}

	public List<AuthorizationNodeValue> getValues() {
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("qnameRef", qnameRef)
				.append("values", values).build();
	}

}
