package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorizationNodeParam {
	private FQname fqname;
	private String name;

	public AuthorizationNodeParam(FQname fqname, String name) {
		this.fqname = fqname;
		this.name = name;
	}

	public FQname getFQname() {
		return fqname;
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
		AuthorizationNodeParam rhs = (AuthorizationNodeParam) obj;
		return new EqualsBuilder().append(fqname, rhs.fqname).append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(89, 15).append(fqname).append(name).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("fqname", fqname).append("name", name)
				.build();
	}

}
