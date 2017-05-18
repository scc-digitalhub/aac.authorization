package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FQname {
	private String domain;
	private String qname;

	public FQname(String domain, String qname) {
		super();
		this.domain = domain;
		this.qname = qname;
	}

	public String getDomain() {
		return domain;
	}

	public String getQname() {
		return qname;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("domain", domain)
				.append("qname", qname).build();
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
		FQname rhs = (FQname) obj;
		return new EqualsBuilder().append(domain, rhs.domain).append(qname, rhs.qname).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(35, 1).append(domain).append(qname).hashCode();
	}

}
