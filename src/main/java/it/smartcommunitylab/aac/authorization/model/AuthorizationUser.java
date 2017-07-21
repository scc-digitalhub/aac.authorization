package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorizationUser {
	private AccountAttribute accountAttribute;
	private String type;

	public AuthorizationUser(AccountAttribute accountAttribute, String type) {
		this.accountAttribute = accountAttribute;
		this.type = type;
	}

	public AccountAttribute getAccountAttribute() {
		return accountAttribute;
	}

	public String getType() {
		return type;
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
		AuthorizationUser rhs = (AuthorizationUser) obj;
		return new EqualsBuilder().append(accountAttribute, rhs.accountAttribute).append(type, rhs.type).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 3).append(accountAttribute).append(type).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("accountAttribute", accountAttribute)
				.append("type", type)
				.build();
	}

}
