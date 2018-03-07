package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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

	public Resource(FQname fqnameRef) {
		this.fqnameRef = fqnameRef;
		this.values = new ArrayList<>();
	}

	public void addNodeValue(AuthorizationNodeValue value) {
		values.add(value);
	}

	public boolean isInstanceOf(AuthorizationNode node) {
		return fqnameRef.equals(node.getFqname()) && checkLogicalEquivalence(node.getParameters(), values);

	}

	private boolean checkLogicalEquivalence(List<AuthorizationNodeParam> s1, List<AuthorizationNodeValue> s2) {
		Collection<AuthorizationNodeParam> definitions = s2.stream().map(nodeValue -> nodeValue.getDefinition())
				.collect(Collectors.toList());
		return CollectionUtils.isEqualCollection(s1, definitions);
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
