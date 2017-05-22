package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Resource {
	private FQname fqnameRef;
	private List<AuthorizationNodeValue> values;

	public Resource(FQname fqnameRef, List<AuthorizationNodeValue> values) {
		this.fqnameRef = fqnameRef;
		this.values = new ArrayList<>(values);
	}

	public boolean isInstanceOf(AuthorizationNode node) {
		return fqnameRef.equals(node.getFqname()) && checkLogicalEquivalence(node.getParameters(), values);
	}

	private boolean checkLogicalEquivalence(List<AuthorizationNodeParam> s1, List<AuthorizationNodeValue> s2) {
		for (AuthorizationNodeValue nv : s2) {
			if (!s1.contains(nv.getDefinition())) {
				return false;
			}
		}
		return true;
	}

	public FQname getFqnameRef() {
		return fqnameRef;
	}

	public final List<AuthorizationNodeValue> getValues() {
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
		return new EqualsBuilder().append(fqnameRef, rhs.fqnameRef).append(values, rhs.values).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 21).append(fqnameRef).append(values).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("fqnameRef", fqnameRef)
				.append("values", values).build();
	}

}
